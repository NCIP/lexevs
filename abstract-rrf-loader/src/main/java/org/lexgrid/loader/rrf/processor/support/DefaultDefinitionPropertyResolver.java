
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexgrid.loader.processor.support.AbstractBasicPropertyResolver;
import org.lexgrid.loader.rrf.model.Mrdef;

/**
 * The Class DefaultDefinitionPropertyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultDefinitionPropertyResolver extends AbstractBasicPropertyResolver<Mrdef>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getDegreeOfFidelity(java.lang.Object)
	 */
	public String getDegreeOfFidelity(Mrdef item) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getFormat(java.lang.Object)
	 */
	public String getFormat(Mrdef item) {
		return SQLTableConstants.TBLCOLVAL_FORMAT_TXT_PLAIN;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getIsActive(java.lang.Object)
	 */
	public boolean getIsActive(Mrdef item) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getLanguage(java.lang.Object)
	 */
	public String getLanguage(Mrdef item) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getMatchIfNoContext(java.lang.Object)
	 */
	public boolean getMatchIfNoContext(Mrdef item) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyName(java.lang.Object)
	 */
	public String getPropertyName(Mrdef item) {
		return SQLTableConstants.TBLCOLVAL_DEFINITION;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyType(java.lang.Object)
	 */
	public String getPropertyType(Mrdef item) {
		return SQLTableConstants.TBLCOLVAL_DEFINITION;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyValue(java.lang.Object)
	 */
	public String getPropertyValue(Mrdef item) {
		return item.getDef();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getRepresentationalForm(java.lang.Object)
	 */
	public String getRepresentationalForm(Mrdef item) {
		return null;
	}
}