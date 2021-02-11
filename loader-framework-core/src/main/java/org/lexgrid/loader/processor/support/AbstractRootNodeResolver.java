
package org.lexgrid.loader.processor.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.util.Assert;

public abstract class AbstractRootNodeResolver implements RootNodeResolver<ParentIdHolder<AssociationSource>>{

	private DatabaseServiceManager databaseServiceManager;
	
	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	private Map<String,String> associationKeyToNameMap = new HashMap<String,String>();
	
	private SupportedAttributeTemplate supportedAttributeTemplate;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RootNodeResolver#isRootNode(java.lang.Object)
	 */
	public boolean isRootNode(ParentIdHolder<AssociationSource> item) {
		
		String relationName = getAssociationName(item.getParentId());
		
		return isHierarchicalRelation(relationName) &&
			(pointsToRoot(item) || pointsToTail(item));
	}
	
	protected String getAssociationName(final String associationPredicateId) {

		if(associationKeyToNameMap.containsKey(associationPredicateId)) {
			return associationKeyToNameMap.get(associationPredicateId);
		} else {
			String codingSchemeUri = 
				codingSchemeIdSetter.getCodingSchemeUri();

			String version = 
				codingSchemeIdSetter.getCodingSchemeVersion();

			final String codingSchemeId = getCodingSchemeId(codingSchemeUri, version);

			String name = databaseServiceManager.getDaoCallbackService().
				executeInDaoLayer(new DaoCallback<String>() {

				public String execute(DaoManager daoManager) {
					return daoManager.getCurrentAssociationDao().getAssociationPredicateNameForUId(codingSchemeId, associationPredicateId);
				}

			});
			
			associationKeyToNameMap.put(associationPredicateId, name);
			
			return name;
		}
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

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public ParentIdHolder<AssociationSource> process(ParentIdHolder<AssociationSource> item)
			throws Exception {
		//If the decorated Processors wants to skip this, don't try to process it.
		if(item == null) {
			return null;
		}
		
		Assert.isTrue(item.getItem().getTargetCount() == 1,
				"Only one (1) AssocationSource and one (1) AssociationTarget may be processed at a time.");
		
		AssociationSource source = new AssociationSource();
		AssociationTarget target = new AssociationTarget();
		
		if(pointsToTail(item)){
			target.setTargetEntityCode("@@");
			target.setTargetEntityCodeNamespace(this.getCodingSchemeIdSetter().getCodingSchemeName());
			
			source.setSourceEntityCode(item.getItem().getSourceEntityCode());
			source.setSourceEntityCodeNamespace(item.getItem().getSourceEntityCodeNamespace());
			
			source.addTarget(target);
			
			this.register(false, this.getAssociationName(item.getParentId()), "@@");
		}
		else if(pointsToRoot(item)){
			source.setSourceEntityCode("@");
			source.setSourceEntityCodeNamespace(this.getCodingSchemeIdSetter().getCodingSchemeName());
			
			target.setTargetEntityCode(item.getItem().getTarget(0).getTargetEntityCode());
			target.setTargetEntityCodeNamespace(item.getItem().getTarget(0).getTargetEntityCodeNamespace());
			
			source.addTarget(target);
			
			this.register(true, this.getAssociationName(item.getParentId()), "@");
		}	
		
		return new ParentIdHolder<AssociationSource>(item.getCodingSchemeIdSetter(),item.getParentId(), source);
	}
	
	protected void register(boolean resolveForward, String association, String rootCode) {
		supportedAttributeTemplate.addSupportedHierarchy(
				this.getCodingSchemeIdSetter().getCodingSchemeUri(),
				this.getCodingSchemeIdSetter().getCodingSchemeVersion(),
				"is_a", 
				null, 
				Arrays.asList(association), 
				resolveForward, 
				rootCode);
	}
	
	/**
	 * Points to root.
	 * 
	 * @param item the item
	 * 
	 * @return true, if successful
	 */
	protected boolean pointsToRoot(ParentIdHolder<AssociationSource> item){
		String sourceCode = item.getItem().getSourceEntityCode();
		return isSourceRootNode(sourceCode);
	}
	
	/**
	 * Points to tail.
	 * 
	 * @param item the item
	 * 
	 * @return true, if successful
	 */
	protected boolean pointsToTail(ParentIdHolder<AssociationSource> item){
		String targetCode = item.getItem().getTarget()[0].getTargetEntityCode();
		return isSourceRootNode(targetCode);	
	}
	
	/**
	 * Checks if is hierarchical relation.
	 * 
	 * @param relation the relation
	 * 
	 * @return true, if is hierarchical relation
	 */
	protected abstract boolean isHierarchicalRelation(String relation);
	
	/**
	 * Construct sab root node.
	 * 
	 * @return the string
	 */
	protected abstract boolean isSourceRootNode(String code);

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}

	public void setDatabaseServiceManager(
			DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}

	public void setCodingSchemeIdSetter(CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}

	public void setSupportedAttributeTemplate(SupportedAttributeTemplate supportedAttributeTemplate) {
		this.supportedAttributeTemplate = supportedAttributeTemplate;
	}

	public SupportedAttributeTemplate getSupportedAttributeTemplate() {
		return supportedAttributeTemplate;
	}
}