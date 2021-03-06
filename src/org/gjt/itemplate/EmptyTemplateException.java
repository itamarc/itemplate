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

public class EmptyTemplateException extends Exception {
	EmptyTemplateException() {}
	EmptyTemplateException(String msg) {
		super(msg);
	}
}

