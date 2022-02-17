
package org.lexgrid.loader.umls.processor;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.apache.commons.beanutils.BeanUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.data.property.ParameterizedListIdSetter;
import org.lexgrid.loader.processor.AbstractParameterPassingDoubleListProcessor;
import org.lexgrid.loader.rrf.model.Mrsty;
import org.lexgrid.loader.rrf.staging.MrconsoStagingDao;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class MrstyListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrstyListProcessor extends AbstractParameterPassingDoubleListProcessor<Mrsty, ParentIdHolder<Property>>{	
	
	/** The mrconso staging dao. */
	private MrconsoStagingDao mrconsoStagingDao;
	
	private DatabaseServiceManager databaseServiceManager;
	
	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	/** The sab. */
	private String sab;
	
	/** The parameterizedlist id setter. */
	private ParameterizedListIdSetter<ParentIdHolder<Property>> parameterizedlistIdSetter;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.AbstractParameterPassingListProcessor#beforeProcessing(java.util.List)
	 */
	@Override
	protected List<Mrsty> beforeProcessing(List<Mrsty> items) {
		//noop
		return items;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.AbstractParameterPassingListProcessor#afterProcessing(java.util.List, java.util.List)
	 */
	@Override
	protected List<ParentIdHolder<Property>> afterProcessing(List<ParentIdHolder<Property>> processedItems, List<Mrsty> originalItems){
		List<ParentIdHolder<Property>> buffer = new ArrayList<ParentIdHolder<Property>>();

		List<String> codes = mrconsoStagingDao.getCodes(getGroupCui(originalItems), sab);

		for(String code : codes){
			for(ParentIdHolder<Property> prop : processedItems){
				
				try {
					ParentIdHolder<Property> holder = new ParentIdHolder<Property>();

					//shallow clone the bean, as deep cloning is too expensive to do this many times
					Property clonedProp = (Property)BeanUtils.cloneBean(prop.getItem());
					
					String codingSchemeUri = 
						codingSchemeIdSetter.getCodingSchemeUri();
					
					String version = 
						codingSchemeIdSetter.getCodingSchemeVersion();
					
					String codingSchemeId = getCodingSchemeId(codingSchemeUri, version);
					
					String entityId = getEntityIdForCode(codingSchemeId, code, codingSchemeIdSetter.getCodingSchemeName());
					
					holder.setItem(clonedProp);
					holder.setParentId(entityId);
					holder.setCodingSchemeIdSetter(this.codingSchemeIdSetter);
					
					buffer.add(holder);
					
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		parameterizedlistIdSetter.addIds(buffer, getGroupCui(originalItems));
		return buffer;
	}
	
	protected String getEntityIdForCode(final String codingSchemeId, final String entityCode, final String entityCodeNamespace) {
		return this.databaseServiceManager.getDaoCallbackService().
			executeInDaoLayer(new DaoCallback<String>() {

			public String execute(DaoManager daoManager) {
				return 
					daoManager.getCurrentEntityDao().getEntityUId(codingSchemeId, entityCode, entityCodeNamespace);
			}
			
		});
	}

	protected String getCodingSchemeId(final String codingSchemeUri, final String version) {
		return this.databaseServiceManager.getDaoCallbackService().
			executeInDaoLayer(new DaoCallback<String>() {

			public String execute(DaoManager daoManager) {
				return 
					daoManager.getCurrentCodingSchemeDao().getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
			}
		});
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
	
	/**
	 * Gets the group cui.
	 * 
	 * @param items the items
	 * 
	 * @return the group cui
	 */
	protected String getGroupCui(List<Mrsty> items){
		return items.get(0).getCui();
	}

	/**
	 * Gets the sab.
	 * 
	 * @return the sab
	 */
	public String getSab() {
		return sab;
	}

	/**
	 * Sets the sab.
	 * 
	 * @param sab the new sab
	 */
	public void setSab(String sab) {
		this.sab = sab;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}

	public void setDatabaseServiceManager(
			DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public ParameterizedListIdSetter<ParentIdHolder<Property>> getParameterizedlistIdSetter() {
		return parameterizedlistIdSetter;
	}

	public void setParameterizedlistIdSetter(
			ParameterizedListIdSetter<ParentIdHolder<Property>> parameterizedlistIdSetter) {
		this.parameterizedlistIdSetter = parameterizedlistIdSetter;
	}

	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}

	public void setCodingSchemeIdSetter(CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}
}