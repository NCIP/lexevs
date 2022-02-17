
package org.lexgrid.loader.processor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.DataUtils;
import org.lexgrid.loader.data.association.AssociationInstanceIdResolver;
import org.lexgrid.loader.database.key.AssociationPredicateKeyResolver;
import org.lexgrid.loader.processor.support.OptionalQualifierResolver;
import org.lexgrid.loader.processor.support.PropertyIdResolver;
import org.lexgrid.loader.processor.support.RelationResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


public class MedRtEntityAssnsToEntityProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I,ParentIdHolder<AssociationSource>> implements InitializingBean {

/** The iso map. */
private Map<String,String> isoMap;
	
	
	public enum SelfReferencingAssociationPolicy {IGNORE, AS_ASSOCIATIONS, AS_PROPERTY_LINKS, BOTH }
	
	/** The relation resolver. */
	private RelationResolver<I> relationResolver;
	
	/** The key resolver. */
	private AssociationInstanceIdResolver<I> associationInstanceIdResolver;
	
	private AssociationPredicateKeyResolver associationPredicateKeyResolver;
	
	private SelfReferencingAssociationPolicy selfReferencingAssociationPolicy = 
		SelfReferencingAssociationPolicy.AS_PROPERTY_LINKS;
	
	private DatabaseServiceManager databaseServiceManager;
	
	private PropertyIdResolver<I> sourcePropertyIdResolver;
	
	private PropertyIdResolver<I> targetPropertyIdResolver;
	
	private List<OptionalQualifierResolver<I>> qualifierResolvers;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(relationResolver);
		Assert.notNull(associationInstanceIdResolver);
		Assert.notNull(associationPredicateKeyResolver);
		Assert.notNull(databaseServiceManager);
		Assert.notNull(sourcePropertyIdResolver);
		Assert.notNull(targetPropertyIdResolver);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public ParentIdHolder<AssociationSource> doProcess(I item) throws Exception {
		String rel = relationResolver.getRelation(item);
		if(StringUtils.isEmpty(rel)){
			return null;
		}
			
		AssociationSource source = new AssociationSource();
		AssociationTarget target = new AssociationTarget();
		
		String sourceCode = relationResolver.getSource(item);
		String sourceNamespace = relationResolver.getSourceNamespace(item);
		
		String targetCode = relationResolver.getTarget(item);
		String targetNamespace = relationResolver.getTargetNamespace(item);
		
		if(sourceCode.equals(targetCode) && sourceNamespace.equals(targetNamespace)) {

			if(selfReferencingAssociationPolicy.equals(SelfReferencingAssociationPolicy.AS_PROPERTY_LINKS) ||
					selfReferencingAssociationPolicy.equals(SelfReferencingAssociationPolicy.IGNORE)){
				return null;
			}
		}
		
		source.setSourceEntityCode(sourceCode);
		source.setSourceEntityCodeNamespace(sourceNamespace);
		
		target.setTargetEntityCode(targetCode);
		target.setTargetEntityCodeNamespace(targetNamespace);
		
		target.setAssociationInstanceId(associationInstanceIdResolver.resolveAssociationInstanceId(item));		
		
		target.setIsActive(true);
		target.setIsDefining(true);
		
		if(qualifierResolvers != null) {
			for(OptionalQualifierResolver<I> qualResolver : qualifierResolvers) {
				if(qualResolver.toProcess(item)) {
					target.addAssociationQualification(DataUtils.createAssociationQualifier(qualResolver, item));	
				}
			}
		}
		
		source.addTarget(target);

		String associationPredicateKey = associationPredicateKeyResolver.resolveKey(
				this.getCodingSchemeIdSetter().getCodingSchemeUri(),
				this.getCodingSchemeIdSetter().getCodingSchemeVersion(),
				relationResolver.getContainerName(),
				relationResolver.getRelation(item));
		
		return new ParentIdHolder<AssociationSource>(
				this.getCodingSchemeIdSetter(),
				associationPredicateKey, 
				source);	
	}
	
	protected void insertPropertyLink(
			final String code,
			final String namespace,
			final String link, 
			final String sourcePropertyId, 
			final String targetPropertyId) {
		final String uri = this.getCodingSchemeIdSetter().getCodingSchemeUri();
		final String version = this.getCodingSchemeIdSetter().getCodingSchemeVersion();
		
		final PropertyLink propertyLink = new PropertyLink();
		propertyLink.setPropertyLink(link);
		propertyLink.setSourceProperty(sourcePropertyId);
		propertyLink.setTargetProperty(targetPropertyId);
		
		databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<Object>() {

			public Object execute(DaoManager daoManager) {
				String codingSchemeId = 
					daoManager.getCodingSchemeDao(uri, version).getCodingSchemeUIdByUriAndVersion(uri, version);
				
				String entityId = 
					daoManager.getEntityDao(uri, version).getEntityUId(codingSchemeId, code, namespace);
				
				if (StringUtils.isBlank(entityId)) {
					System.out.println("daoManager.getEntityDao(uri, version).getEntityUId cannot find " + code + " " + codingSchemeId + " " + namespace );
				}
				else {
					daoManager.getPropertyDao(uri, version).insertPropertyLink(codingSchemeId, entityId, propertyLink);
				}
				
				return null;
			}
		});
	}

	/**
	 * Gets the relation resolver.
	 * 
	 * @return the relation resolver
	 */
	public RelationResolver<I> getRelationResolver() {
		return relationResolver;
	}

	/**
	 * Sets the relation resolver.
	 * 
	 * @param relationResolver the new relation resolver
	 */
	public void setRelationResolver(RelationResolver<I> relationResolver) {
		this.relationResolver = relationResolver;
	}

	public AssociationInstanceIdResolver<I> getAssociationInstanceIdResolver() {
		return associationInstanceIdResolver;
	}

	public void setAssociationInstanceIdResolver(
			AssociationInstanceIdResolver<I> associationInstanceIdResolver) {
		this.associationInstanceIdResolver = associationInstanceIdResolver;
	}

	public AssociationPredicateKeyResolver getAssociationPredicateKeyResolver() {
		return associationPredicateKeyResolver;
	}

	public void setAssociationPredicateKeyResolver(
			AssociationPredicateKeyResolver associationPredicateKeyResolver) {
		this.associationPredicateKeyResolver = associationPredicateKeyResolver;
	}

	public void setSelfReferencingAssociationPolicy(
			SelfReferencingAssociationPolicy selfReferencingAssociationPolicy) {
		this.selfReferencingAssociationPolicy = selfReferencingAssociationPolicy;
	}

	public SelfReferencingAssociationPolicy getSelfReferencingAssociationPolicy() {
		return selfReferencingAssociationPolicy;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}

	public void setDatabaseServiceManager(
			DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public PropertyIdResolver<I> getSourcePropertyIdResolver() {
		return sourcePropertyIdResolver;
	}

	public void setSourcePropertyIdResolver(
			PropertyIdResolver<I> sourcePropertyIdResolver) {
		this.sourcePropertyIdResolver = sourcePropertyIdResolver;
	}

	public PropertyIdResolver<I> getTargetPropertyIdResolver() {
		return targetPropertyIdResolver;
	}

	public void setTargetPropertyIdResolver(
			PropertyIdResolver<I> targetPropertyIdResolver) {
		this.targetPropertyIdResolver = targetPropertyIdResolver;
	}

	public void setQualifierResolvers(List<OptionalQualifierResolver<I>> qualifierResolvers) {
		this.qualifierResolvers = qualifierResolvers;
	}

	public List<OptionalQualifierResolver<I>> getQualifierResolvers() {
		return qualifierResolvers;
	}
	
	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate template,
			ParentIdHolder<AssociationSource> item) {

			if(! ArrayUtils.isEmpty(item.getItem().getTarget())){
				for(AssociationTarget target : item.getItem().getTarget()){
					if(! ArrayUtils.isEmpty(target.getAssociationQualification())){
						for(AssociationQualification qual : target.getAssociationQualification()) {
							template.addSupportedAssociationQualifier(
									this.getCodingSchemeIdSetter().getCodingSchemeUri(), 
									this.getCodingSchemeIdSetter().getCodingSchemeVersion(), 
									qual.getAssociationQualifier(), 
									null, 
									qual.getQualifierText().getContent());
						}
					}
				}
			}

		// likely not needed -- there is only one target
		Set<String> set = item.getItem().getTargetAsReference().stream().map(x -> x.getTargetEntityCodeNamespace())
				.distinct().collect(Collectors.toSet());
		// namespaces might be the same we'll eliminate them as duplicates
		set.add(item.getItem().getSourceEntityCodeNamespace());
		//These are all sabs, we'll see if they are MED-RT, if not we'll create
		//supported attibutes for external value sets
		for (String sab : set) {
			if (!this.getCodingSchemeIdSetter().getCodingSchemeName().equals(sab)) {
				this.getSupportedAttributeTemplate().addSupportedCodingScheme(sab, null, sab, isoMap.get(sab), sab,
						false);
				this.getSupportedAttributeTemplate().addSupportedNamespace(sab, null, sab, isoMap.get(sab), sab, sab);
			}
		}
	}

	public Map<String, String> getIsoMap() {
		return isoMap;
	}

	public void setIsoMap(Map<String, String> isoMap) {
		this.isoMap = isoMap;
	}

}