
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