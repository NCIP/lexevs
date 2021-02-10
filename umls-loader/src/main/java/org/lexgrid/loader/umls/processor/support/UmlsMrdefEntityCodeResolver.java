
package org.lexgrid.loader.umls.processor.support;

import org.lexgrid.loader.processor.support.EntityCodeResolver;
import org.lexgrid.loader.rrf.model.Mrdef;
import org.lexgrid.loader.rrf.staging.MrconsoStagingDao;

/**
 * The Class UmlsMrdefEntityCodeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsMrdefEntityCodeResolver implements EntityCodeResolver<Mrdef>{

	/** The mrconso staging dao. */
	private MrconsoStagingDao mrconsoStagingDao;		

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityCodeResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(Mrdef item){
		return mrconsoStagingDao.getCodeAndSab(item.getCui(), item.getAui()).getCode();
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