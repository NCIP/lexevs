/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.gui.restrictions;

/**
 * Holder to store the Code System restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodeSystem extends Restriction {
	public String codeSystem;
	public int type = 0;

	public static final int CODE_SYSTEM = 0;
	public static final int SOURCE_CODE_SYSTEM = 1;
	public static final int TARGET_CODE_SYSTEM = 2;

	public CodeSystem(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		if (type == CODE_SYSTEM) {
			return "Restrict to code system '" + codeSystem + "'";
		} else if (type == SOURCE_CODE_SYSTEM) {
			return "Restrict to source code system '" + codeSystem + "'";
		} else if (type == TARGET_CODE_SYSTEM) {
			return "Restrict to target system '" + codeSystem + "'";
		} else {
			return "Error - invalid type";
		}
	}
}