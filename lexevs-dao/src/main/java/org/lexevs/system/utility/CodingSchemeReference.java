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
package org.lexevs.system.utility;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;

public class CodingSchemeReference extends AbsoluteCodingSchemeVersionReference {

	private static final long serialVersionUID = -7847077319061238240L;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getCodingSchemeURN() == null) ? 0 : getCodingSchemeURN().hashCode());
		result = prime
				* result
				+ ((getCodingSchemeVersion() == null) ? 0 : getCodingSchemeVersion()
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodingSchemeReference other = (CodingSchemeReference) obj;
		if (getCodingSchemeURN() == null) {
			if (other.getCodingSchemeURN() != null)
				return false;
		} else if (!getCodingSchemeURN().equals(other.getCodingSchemeURN()))
			return false;
		if (getCodingSchemeVersion() == null) {
			if (other.getCodingSchemeVersion() != null)
				return false;
		} else if (!getCodingSchemeVersion().equals(other.getCodingSchemeVersion()))
			return false;
		return true;
	}
}