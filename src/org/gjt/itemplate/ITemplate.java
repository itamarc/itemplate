/*
 * ITemplate - fill in text templates with variable content.
 *
 * Copyright (C) 2001 Itamar Carvalho <itamarc@gmail.com>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 *
 * See the COPYING file located in the "doc" directory of
 * the archive of this library for complete text of license.
 */
package org.gjt.itemplate;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/** Class to fill in text templates with variable content.
 *
 * @author Itamar Almeida de Carvalho
 * @version 1.0
 */
public class ITemplate {
	/* Constants */
	public final static int TEXT = 1;
	public final static int KEY = 2;
	/* Class attributes */
	private static String openTkn = "[#";
	private static String closeTkn = "#]";
	/* Instance attributes */
	private ArrayList<ITemplatePiece> parsed = new ArrayList<ITemplatePiece>();

	/* Constructors */
    /** @param text Path to the file containing the template or the text of the template.
      * @param type Valid types: path, string. */
	public ITemplate (String text, String type) 
		throws ParameterException,
			EmptyTemplateException,
			TokensDontMatchException {
		String tmpl="";
		if (type.equalsIgnoreCase("path")) {
			File arq = new File(text);
			tmpl = readFile(arq);
		} else if (type.equalsIgnoreCase("string")) {
			tmpl = new String(text);
		} else {
			throw new ParameterException("Unknown type: "+type);
		}
		if (tmpl.length() == 0) {
			throw new EmptyTemplateException();
		} else {
			parse(tmpl);
		}
	}
	public ITemplate (File file) 
		throws EmptyTemplateException,TokensDontMatchException {
		String tmpl="";
		tmpl = readFile(file);
		if (tmpl.length() == 0) {
			throw new EmptyTemplateException();
		} else {
			parse(tmpl);
		}
	}
	private void parse(String tmpl) throws TokensDontMatchException {
		boolean open=false;
		int type=TEXT; // 1-text 2-key
		StringBuffer str = new StringBuffer();
		try {
			// Fills the 'parsed' Vector
			for (int i=0; i<tmpl.length(); i++) {
				char c = tmpl.charAt(i);
				// If the char is the first in the open token and it was not found yet (!open)
				if (!open && c == openTkn.charAt(0)) {
					// Check if the token is complete in the current position
					if (tmpl.substring(i,i+openTkn.length()).compareTo(openTkn) == 0) {
						if (str.length() > 0) {
							type = (open ? KEY : TEXT);
							parsed.add(new ITemplatePiece(str.toString(),type));
							str = new StringBuffer();
						}
						open = true;
						i += openTkn.length()-1;
					} else { // If fails to find the full token, just append the char
						str.append(c);
					}
				// If the open token has already been found and the current
				// char is the first in the close token
				} else if (open && c == closeTkn.charAt(0)) {
					if (tmpl.substring(i,i+closeTkn.length()).compareTo(closeTkn) == 0) {
						if (str.length() > 0) {
							type = (open ? KEY : TEXT);
							parsed.add(new ITemplatePiece(str.toString(),type));
							str = new StringBuffer();
						}
						i += openTkn.length()-1;
						open = false;
					} else { // If fails to find the full token, just append the char
						str.append(c);
					}
				} else if (open && c == openTkn.charAt(0)) {
					if (tmpl.substring(i,i+openTkn.length()).compareTo(openTkn) == 0) {
						throw new TokensDontMatchException("Two opening tokens without a closing one at position "+i+".");
					}
				} else if (!open && c == closeTkn.charAt(0)) {
					if (tmpl.substring(i,i+closeTkn.length()).compareTo(closeTkn) == 0) {
						throw new TokensDontMatchException("Closing token without an opening one at position "+i+".");
					}
				} else {
					str.append(c);
				}
			}
			if (str.length() > 0) {
				if (open) {
					throw new TokensDontMatchException();
				} else {
					parsed.add(new ITemplatePiece(str.toString(),TEXT));
				}
			}
		} catch (ParameterException e) {
			System.out.println("Parameter error: "+e.getMessage());
		}
	}
	private String readFile(File file) {
		int c;
		StringBuffer tmpl = new StringBuffer();
		try {
			FileReader in = new FileReader(file);
			while ((c = in.read()) != -1) {
				tmpl.append((char)c);
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found: "+file.getAbsolutePath()+" ("+e.getMessage()+")");
		} catch (IOException e) {
			System.out.println("I/O error: "+e.getMessage());
		}
		return tmpl.toString();
	}
	/** Fill in the template making substitutions.
	 */
	public String fill(HashMap<String, String> h) {
		StringBuffer s = new StringBuffer();
		for (ITemplatePiece p : parsed) {
			if (p.getType() == 1) { // text
				s.append(p.getText());
			} else if (p.getType() == 2) { // key
				String key = p.getText().trim();
				s.append((h.containsKey(key)) ? (String)h.get(key) : "");
			}
		}
		return s.toString();
	}
	/** Define the tokens that opens and closes the pieces to be substituted.<br>
	 * The default values are &quot;[#&quot; and &quot;#]&quot;.
	 */
	public static void setTokens (String open, String close) {
		openTkn = open;
		closeTkn = close;
	}
}

/*

General algorithm:

On create (new):
- Obtain template
- Broke the template in pieces
- Put the pieces in a vector

On fill in:
- Obtain substitution hash
- For each substitutle piece, do the change
- Mount the final text

*/
