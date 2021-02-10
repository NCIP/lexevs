
package org.lexgrid.loader.processor.support;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;

/**
 * The Class SameAsCodingSchemeNameEntityNamespaceResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SameAsCodingSchemeNameEntityNamespaceResolver implements EntityNamespaceResolver<Object> {
	
	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeIdSetter;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityNamespaceResolver#getEntityNamespace(java.lang.Object)
	 */
	public String getEntityNamespace(Object item) {
		return codingSchemeIdSetter.getCodingSchemeName();
	}

	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}

	public void setCodingSchemeIdSetter(CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}
}