
package org.lexgrid.loader.umls.processor.support;

import org.lexevs.logging.LoggerFactory;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver;
import org.lexgrid.loader.rrf.staging.MrconsoStagingDao;

/**
 * The Class UmlsRelationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsRelationResolver extends AbstractRrfRelationResolver {

	/** The mrconso staging dao. */
	private MrconsoStagingDao mrconsoStagingDao;
	
	public UmlsRelationResolver(){
		super();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver#getSource(org.lexgrid.loader.rrf.model.Mrrel)
	 */
	public String getSource(Mrrel item) {
		return mrconsoStagingDao.getCodeAndSab(item.getCui2(), item.getAui2()).getCode();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver#getTarget(org.lexgrid.loader.rrf.model.Mrrel)
	 */
	public String getTarget(Mrrel item) {
		return mrconsoStagingDao.getCodeAndSab(item.getCui1(), item.getAui1()).getCode();
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver#getContainerName()
	 */
	@Override
	public String getContainerName() {
		return RrfLoaderConstants.UMLS_RELATIONS_NAME;
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