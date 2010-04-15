/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexgrid.loader.processor;

import java.util.List;

import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.data.association.AssociationInstanceIdResolver;
import org.lexgrid.loader.database.key.AssociationPredicateKeyResolver;
import org.lexgrid.loader.processor.support.PropertyIdResolver;
import org.lexgrid.loader.processor.support.QualifierResolver;
import org.lexgrid.loader.processor.support.RelationResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class EntityAssnsToEntityProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityProcessor<I> extends CodingSchemeIdAwareProcessor implements ItemProcessor<I,ParentIdHolder<AssociationSource>> {
	
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
	
	private List<QualifierResolver<I>> qualifierResolvers;
	
	private boolean skipNullValueQualifiers = true;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public ParentIdHolder<AssociationSource> process(I item) throws Exception {
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
					selfReferencingAssociationPolicy.equals(SelfReferencingAssociationPolicy.BOTH)){
				this.insertPropertyLink(
						sourceCode, 
						sourceNamespace, 
						rel, 
						sourcePropertyIdResolver.getPropertyId(item), 
						targetPropertyIdResolver.getPropertyId(item));
			}
			
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
			for(QualifierResolver<I> qualResolver : qualifierResolvers) {
				String qualName = qualResolver.getQualifierName();
				String qualValue = qualResolver.getQualifierValue(item);

				if(StringUtils.isNotBlank(qualValue) || !skipNullValueQualifiers) {
					AssociationQualification qual = new AssociationQualification();

					qual.setAssociationQualifier(qualName);
					qual.setQualifierText(DaoUtility.createText(qualValue));

					target.addAssociationQualification(qual);	
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
				
				daoManager.getPropertyDao(uri, version).insertPropertyLink(codingSchemeId, entityId, propertyLink);
				
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

	public void setQualifierResolvers(List<QualifierResolver<I>> qualifierResolvers) {
		this.qualifierResolvers = qualifierResolvers;
	}

	public List<QualifierResolver<I>> getQualifierResolvers() {
		return qualifierResolvers;
	}

	public void setSkipNullValueQualifiers(boolean skipNullValueQualifiers) {
		this.skipNullValueQualifiers = skipNullValueQualifiers;
	}

	public boolean isSkipNullValueQualifiers() {
		return skipNullValueQualifiers;
	}
}
