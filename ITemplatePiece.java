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

/** Class to store template pieces.
 * @author Itamar Almeida de Carvalho
 * @version 1.0
 * @see ITemplate
 */
public class ITemplatePiece {
	private String texto;
	/** 1=Text, 2=Substitute, 3=Advanced
	 */
	private int tipo;
	public ITemplatePiece (String texto, int tipo) throws ParameterException {
		this.texto = texto;
		this.tipo = tipo;
		if (tipo < 1 || tipo > 3) {
			throw new ParameterException("Parameter 'tipo' must have values between 1 e 3.");
		}
	}
	public String getTexto() {
		return texto;
	}
	public int getTipo() {
		return tipo;
	}
	public String toString() {
		String tipostr = "";
		if (tipo == 1) {
			tipostr="text";
		} else if (tipo == 2) {
			tipostr="substitute";
		} else if (tipo == 3) {
			tipostr="advanced";
		} else {
			tipostr="unknown";
		}
		return "Type "+tipo+" ("+tipostr+") - Text:\n"+texto;
	}
}

