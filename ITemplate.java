/*
 * ITemplate - fill in text templates with variable content.
 *
 * Copyright (C) 2001 Itamar Almeida de Carvalho <itamar@gjt.org>
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

import org.gjt.itemplate.*;
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
	private static String abre = "[#";
	private static String fecha = "#]";
	/* Instance attributes */
	private boolean advanced = false;
	private Vector parsed = new Vector();

	// SET TO "true" TO PRODUCE DEBUG MSGS (DON'T COMMIT WITH debug=true !)
	private boolean debug = false;
	
	/* Constructors */
	public ITemplate (String texto, String tipo) 
		throws ParameterException,
			EmptyTemplateException,
			TokensDontMatchException {
		String tmpl="";
		if (tipo.equalsIgnoreCase("path")) {
			File arq = new File(texto);
			tmpl = leArquivo(arq);
			if (debug) {
				System.out.println("TEMPLATE:\n"+tmpl);
			}
		} else if (tipo.equalsIgnoreCase("string")) {
			tmpl = new String(texto);
		} else {
			throw new ParameterException("Unknown type: "+tipo);
		}
		if (tmpl.length() == 0) {
			throw new EmptyTemplateException();
		} else {
			parse(tmpl);
		}
	}
	/* I don't know if it is a good constructor...
	public ITemplate (String path, boolean advanced) {
		System.out.println("Construtor ainda nao implementado.");
	}
	*/
	public ITemplate (File arq) 
		throws EmptyTemplateException,TokensDontMatchException {
		String tmpl="";
		tmpl = leArquivo(arq);
		if (debug) {
			System.out.println("TEMPLATE:\n"+tmpl);
		}
		if (tmpl.length() == 0) {
			throw new EmptyTemplateException();
		} else {
			parse(tmpl);
		}
	}
	public ITemplate (File arq, boolean advanced) 
		throws EmptyTemplateException,TokensDontMatchException {
		this.advanced = advanced;
		String tmpl="";
		tmpl = leArquivo(arq);
		if (debug) {
			System.out.println("TEMPLATE:\n"+tmpl);
		}
		if (tmpl.length() == 0) {
			throw new EmptyTemplateException();
		} else {
			parse(tmpl);
		}
	}
	private void parse (String tmpl) throws TokensDontMatchException {
		boolean open=false;
		int tipo=1; // 1-texto 2-subst. 3-avancado
		int subst = (advanced ? 3 : 2);
		StringBuffer str = new StringBuffer();
		try {
			// Preenche Vector parsed
			for (int i=0; i<tmpl.length(); i++) {
				char c = tmpl.charAt(i);
				if (!open && c == abre.charAt(0)) {
					if (tmpl.substring(i,i+abre.length()).compareTo(abre) == 0) {
						if (str.length() > 0) {
							tipo = (open ? subst : 1);
							parsed.add(new ITemplatePiece(str.toString(),tipo));
							str = new StringBuffer();
						}
						open = true;
						i += abre.length()-1;
					} else {
						str.append(c);
					}
				} else if (open && c == fecha.charAt(0)) {
					if (tmpl.substring(i,i+fecha.length()).compareTo(fecha) == 0) {
						if (str.length() > 0) {
							tipo = (open ? subst : 1);
							parsed.add(new ITemplatePiece(str.toString(),tipo));
							str = new StringBuffer();
						}
						i += abre.length()-1;
						open = false;
					} else {
						str.append(c);
					}
				} else {
					str.append(c);
				}
			}
			if (str.length() > 0) {
				if (open) {
					throw new TokensDontMatchException();
				} else {
					tipo = (open ? subst : 1);
					parsed.add(new ITemplatePiece(str.toString(),tipo));
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
	private String leArquivo (File arq) {
		int c;
		StringBuffer tmpl = new StringBuffer();
		try {
			FileReader in = new FileReader(arq);
			while ((c = in.read()) != -1) {
				tmpl.append((char)c);
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found: "+arq.getAbsolutePath()+" ("+e.getMessage()+")");
		} catch (IOException e) {
			System.out.println("I/O error: "+e.getMessage());
		}
		return tmpl.toString();
	}
	/** Fill in the template making substitutions.
	 */
	public String fill(Hashtable h) {
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
		abre = open;
		fecha = close;
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

---------------

Algoritmo geral:

Ao criar:
- Obter template
- Quebrar template em pedaços
- Guardar pedaços em vetor

Ao preencher:
- Obter hash de substituição
- Para cada pedaço substituível, efetuar substituição
- Montar texto final

*/

