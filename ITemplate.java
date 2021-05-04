/*
 * ITemplate - fill in text templates with variable content.
 *
 * Copyright (C) 2001 Itamar Almeida de Carvalho <itamarc@gmail.com>
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

import java.io.*;
import java.util.*;

/** Class to fill in text templates with variable content.<br>
 * Note: Advanced mode is planned, but not yet implemented.
 *
 * @author Itamar Almeida de Carvalho
 * @version 1.0
 */
public class ITemplate {
	/* Class attributes */
	private static String openTkn = "[#";
	private static String closeTkn = "#]";
	/* Instance attributes */
	private boolean advanced = false;
	private Vector<ITemplatePiece> parsed = new Vector<ITemplatePiece>();

	// SET TO "true" TO PRODUCE DEBUG MSGS (DON'T COMMIT WITH debug=true !)
	private boolean debug = false;
	
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
			if (debug) {
				System.out.println("TEMPLATE:\n"+tmpl);
			}
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
		if (debug) {
			System.out.println("TEMPLATE:\n"+tmpl);
		}
		if (tmpl.length() == 0) {
			throw new EmptyTemplateException();
		} else {
			parse(tmpl);
		}
	}
	public ITemplate (File file, boolean advanced) 
		throws EmptyTemplateException,TokensDontMatchException {
		this.advanced = advanced;
		String tmpl="";
		tmpl = readFile(file);
		if (debug) {
			System.out.println("TEMPLATE:\n"+tmpl);
		}
		if (tmpl.length() == 0) {
			throw new EmptyTemplateException();
		} else {
			parse(tmpl);
		}
	}
	private void parse(String tmpl) throws TokensDontMatchException {
		boolean open=false;
		int type=1; // 1-text 2-key 3-advanced
		int subst = (advanced ? 3 : 2);
		StringBuffer str = new StringBuffer();
		try {
			// Fills the 'parsed' Vector
			for (int i=0; i<tmpl.length(); i++) {
				char c = tmpl.charAt(i);
				if (!open && c == openTkn.charAt(0)) {
					if (tmpl.substring(i,i+openTkn.length()).compareTo(openTkn) == 0) {
						if (str.length() > 0) {
							type = (open ? subst : 1);
							parsed.add(new ITemplatePiece(str.toString(),type));
							str = new StringBuffer();
						}
						open = true;
						i += openTkn.length()-1;
					} else {
						str.append(c);
					}
				} else if (open && c == closeTkn.charAt(0)) {
					if (tmpl.substring(i,i+closeTkn.length()).compareTo(closeTkn) == 0) {
						if (str.length() > 0) {
							type = (open ? subst : 1);
							parsed.add(new ITemplatePiece(str.toString(),type));
							str = new StringBuffer();
						}
						i += openTkn.length()-1;
						open = false;
					} else {
						str.append(c);
					}
				} else if (open && c == openTkn.charAt(0)) {
					if (tmpl.substring(i,i+openTkn.length()).compareTo(openTkn) == 0) {
						throw new TokensDontMatchException("Two opening tokens without a closing one.");
					}
				} else if (!open && c == closeTkn.charAt(0)) {
					if (tmpl.substring(i,i+closeTkn.length()).compareTo(closeTkn) == 0) {
						throw new TokensDontMatchException("Closing token without an opening one.");
					}
				} else {
					str.append(c);
				}
			}
			if (str.length() > 0) {
				if (open) {
					throw new TokensDontMatchException();
				} else {
					type = (open ? subst : 1);
					parsed.add(new ITemplatePiece(str.toString(),type));
				}
			}
		} catch (ParameterException e) {
			System.out.println("Parameter error: "+e.getMessage());
		}
		if (debug) {
			printParsed();
		}
	}
	private void printParsed() {
		for (int i=0; i<parsed.size(); i++) {
			String s = ((ITemplatePiece)parsed.get(i)).toString();
			System.out.println("##### PIECE "+i+":\n"+s+"\n");
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
	public String fill(Hashtable<String, String> h) {
		StringBuffer s = new StringBuffer();
		for (int i=0; i<parsed.size(); i++) {
			ITemplatePiece p = (ITemplatePiece)parsed.get(i);
			if (p.getTipo() == 1) {
				s.append(p.getTexto());
			} else if (p.getTipo() == 2) {
				s.append((String)h.get(p.getTexto().trim()));
			}
		}
		return s.toString();
	}
	/** Ativate the advanced mode.
	 */
	public void setAdvancedMode() {
		advanced = true;
	}
	/** Turn the advanced mode off.
	 */
	public void unsetAdvancedMode() {
		advanced = false;
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
