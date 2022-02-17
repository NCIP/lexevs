
package org.lexgrid.loader.umls.processor.support;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexgrid.loader.data.DataUtils;
import org.lexgrid.loader.processor.support.AbstractBasicPropertyResolver;
import org.lexgrid.loader.rrf.data.property.MrsatUtility;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * The Class MrsatPropertyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsatPropertyResolver extends AbstractBasicPropertyResolver<Mrsat>{

	/** The mrsat utility. */
	private MrsatUtility mrsatUtility;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getDegreeOfFidelity(java.lang.Object)
	 */
	public String getDegreeOfFidelity(Mrsat item) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getFormat(java.lang.Object)
	 */
	public String getFormat(Mrsat item) {
		return SQLTableConstants.TBLCOLVAL_FORMAT_TXT_PLAIN;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getIsActive(java.lang.Object)
	 */
	public boolean getIsActive(Mrsat item) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getLanguage(java.lang.Object)
	 */
	public String getLanguage(Mrsat item) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getMatchIfNoContext(java.lang.Object)
	 */
	public boolean getMatchIfNoContext(Mrsat item) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyName(java.lang.Object)
	 */
	public String getPropertyName(Mrsat item) {
		return mrsatUtility.getPropertyName(item);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyType(java.lang.Object)
	 */
	public String getPropertyType(Mrsat item) {
		return mrsatUtility.getPropertyType(item);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyValue(java.lang.Object)
	 */
	public String getPropertyValue(Mrsat item) {
		return DataUtils.adjustNonNullValue(item.getAtv());
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getRepresentationalForm(java.lang.Object)
	 */
	public String getRepresentationalForm(Mrsat item) {
		return null;
	}

	/**
	 * Gets the mrsat utility.
	 * 
	 * @return the mrsat utility
	 */
	public MrsatUtility getMrsatUtility() {
		return mrsatUtility;
	}

	/**
	 * Sets the mrsat utility.
	 * 
	 * @param mrsatUtility the new mrsat utility
	 */
	public void setMrsatUtility(MrsatUtility mrsatUtility) {
		this.mrsatUtility = mrsatUtility;
	}

	public String getEntityCodeNamespace(Mrsat item) {
		// TODO Auto-generated method stub
		return null;
	}
}