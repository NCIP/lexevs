
package org.lexgrid.loader.umls.processor.support;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexgrid.loader.processor.support.AbstractBasicPropertyResolver;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class UmlsPropertyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsPropertyResolver extends AbstractBasicPropertyResolver<Mrconso>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getDegreeOfFidelity(java.lang.Object)
	 */
	public String getDegreeOfFidelity(Mrconso item) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getFormat(java.lang.Object)
	 */
	public String getFormat(Mrconso item) {
		return SQLTableConstants.TBLCOLVAL_FORMAT_TXT_PLAIN;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getIsActive(java.lang.Object)
	 */
	public boolean getIsActive(Mrconso item) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getLanguage(java.lang.Object)
	 */
	public String getLanguage(Mrconso item) {
		return item.getLat();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getMatchIfNoContext(java.lang.Object)
	 */
	public boolean getMatchIfNoContext(Mrconso item) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyName(java.lang.Object)
	 */
	public String getPropertyName(Mrconso item) {
		return SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyType(java.lang.Object)
	 */
	public String getPropertyType(Mrconso item) {
		return SQLTableConstants.TBLCOLVAL_PRESENTATION;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyValue(java.lang.Object)
	 */
	public String getPropertyValue(Mrconso item) {
		return item.getStr();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getRepresentationalForm(java.lang.Object)
	 */
	public String getRepresentationalForm(Mrconso item) {
		return item.getTty();
	}

	public String getEntityCodeNamespace(Mrconso item) {
		return null;
	}
}