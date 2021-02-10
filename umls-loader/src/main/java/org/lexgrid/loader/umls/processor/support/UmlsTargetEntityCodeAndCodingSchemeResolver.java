
package org.lexgrid.loader.umls.processor.support;

import org.lexgrid.loader.processor.support.EntityCodeAndCodingSchemeResolver;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.lexgrid.loader.rrf.staging.MrconsoStagingDao;
import org.lexgrid.loader.wrappers.CodeCodingSchemePair;

/**
 * The Class UmlsTargetEntityCodeAndCodingSchemeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsTargetEntityCodeAndCodingSchemeResolver implements EntityCodeAndCodingSchemeResolver<Mrrel> {
	
	/** The mrconso staging dao. */
	private MrconsoStagingDao mrconsoStagingDao;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityCodeAndCodingSchemeResolver#getEntityCodeAndCodingScheme(java.lang.Object)
	 */
	public CodeCodingSchemePair getEntityCodeAndCodingScheme(Mrrel item) {
		return mrconsoStagingDao.getCodeAndCodingScheme(item.getCui2(), item.getAui2());
	}

	/**
	 * Gets the mrconso staging dao.
	 * 
	 * @return the mrconso staging dao
	 */
	public MrconsoStagingDao getMrconsoStagingDao() {
		return mrconsoStagingDao;
	}

	/**
	 * Sets the mrconso staging dao.
	 * 
	 * @param mrconsoStagingDao the new mrconso staging dao
	 */
	public void setMrconsoStagingDao(MrconsoStagingDao mrconsoStagingDao) {
		this.mrconsoStagingDao = mrconsoStagingDao;
	}
}