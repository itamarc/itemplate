/*
 * ITemplate - fill in text templates with variable content.
 *
 * Copyright (C) 2001 Itamar Carvalho <itamarc@gmail.com>s
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
	private String text;
	/** 1=Text, 2=Key
	 */
	private int type;
	public ITemplatePiece (String text, int type) throws ParameterException {
		this.text = text;
		this.type = type;
		if (type < 1 || type > 2) {
			throw new ParameterException("Parameter 'type' must have values between 1 and 2.");
		}
	}
	public String getText() {
		return text;
	}
	public int getType() {
		return type;
	}
	public String toString() {
		String typestr = "unknown";
		if (type == ITemplate.TEXT) {
			typestr="text";
		} else if (type == ITemplate.KEY) {
			typestr="key";
		}
		return "Type '"+typestr+"' - Text: \u00AB"+text+"\u00BB";
	}
}

