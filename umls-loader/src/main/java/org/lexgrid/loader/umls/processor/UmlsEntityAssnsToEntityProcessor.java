package org.lexgrid.loader.umls.processor;

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.lexgrid.loader.data.association.EntityAssnsToEntityReproducibleKeyResolver;
import org.lexgrid.loader.data.association.MultiAttribKeyResolver;
import org.lexgrid.loader.processor.support.EntityCodeAndCodingSchemeResolver;
import org.lexgrid.loader.processor.support.RelationContainerResolver;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.lexgrid.loader.rrf.processor.GroupRrfEntityAssnsToEntityProcessor;
import org.lexgrid.loader.wrappers.CodeCodingSchemePair;

public class UmlsEntityAssnsToEntityProcessor extends GroupRrfEntityAssnsToEntityProcessor{

	/** The key resolver. */
	private MultiAttribKeyResolver<EntityAssnsToEntity> keyResolver = new EntityAssnsToEntityReproducibleKeyResolver();

	/** The source entity code and coding scheme resolver. */
	private EntityCodeAndCodingSchemeResolver<Mrrel> sourceEntityCodeAndCodingSchemeResolver;
	
	/** The target entity code and coding scheme resolver. */
	private EntityCodeAndCodingSchemeResolver<Mrrel> targetEntityCodeAndCodingSchemeResolver;
	
	/** The container resolver. */
	private RelationContainerResolver<Mrrel> containerResolver;
	
	@Override
	protected EntityAssnsToEntity buildEntityAssnsToEntity(Mrrel item) {
		String container = containerResolver.getRelationContainer(item);
		
		CodeCodingSchemePair sourceCode = sourceEntityCodeAndCodingSchemeResolver.getEntityCodeAndCodingScheme(item);
		CodeCodingSchemePair targetCode = targetEntityCodeAndCodingSchemeResolver.getEntityCodeAndCodingScheme(item);

		return buildEntityAssnsToEntity(sourceCode, targetCode, container, super.getRelation(item), item.getRui());
	}
	
	/**
	 * Builds the entity assns to entity.
	 * 
	 * @param sourceCode the source code
	 * @param targetCode the target code
	 * @param container the container
	 * @param relation the relation
	 * 
	 * @return the entity assns to entity
	 */
	protected EntityAssnsToEntity buildEntityAssnsToEntity(CodeCodingSchemePair sourceCode, CodeCodingSchemePair targetCode, String container, String relation, String rui){
		EntityAssnsToEntity relAssoc = new EntityAssnsToEntity();
		relAssoc.setCodingSchemeName(getCodingSchemeNameSetter().getCodingSchemeName());
		relAssoc.setEntityCodeNamespace(getCodingSchemeNameSetter().getCodingSchemeName());
		relAssoc.setContainerName(container);
		relAssoc.setEntityCode(relation);
		relAssoc.setSourceEntityCode(sourceCode.getCode());
		relAssoc.setSourceEntityCodeNamespace(sourceCode.getCodingScheme());
		relAssoc.setTargetEntityCode(targetCode.getCode());
		relAssoc.setTargetEntityCodeNamespace(targetCode.getCodingScheme());
		
		relAssoc.setIsActive(true);
		relAssoc.setIsDefining(true);
		relAssoc.setIsInferred(false);
		relAssoc.setMultiAttributesKey(rui);
		
		return relAssoc;		
	}
	

	/**
	 * Gets the source entity code and coding scheme resolver.
	 * 
	 * @return the source entity code and coding scheme resolver
	 */
	public EntityCodeAndCodingSchemeResolver<Mrrel> getSourceEntityCodeAndCodingSchemeResolver() {
		return sourceEntityCodeAndCodingSchemeResolver;
	}

	/**
	 * Sets the source entity code and coding scheme resolver.
	 * 
	 * @param sourceEntityCodeAndCodingSchemeResolver the new source entity code and coding scheme resolver
	 */
	public void setSourceEntityCodeAndCodingSchemeResolver(
			EntityCodeAndCodingSchemeResolver<Mrrel> sourceEntityCodeAndCodingSchemeResolver) {
		this.sourceEntityCodeAndCodingSchemeResolver = sourceEntityCodeAndCodingSchemeResolver;
	}

	/**
	 * Gets the target entity code and coding scheme resolver.
	 * 
	 * @return the target entity code and coding scheme resolver
	 */
	public EntityCodeAndCodingSchemeResolver<Mrrel> getTargetEntityCodeAndCodingSchemeResolver() {
		return targetEntityCodeAndCodingSchemeResolver;
	}

	/**
	 * Sets the target entity code and coding scheme resolver.
	 * 
	 * @param targetEntityCodeAndCodingSchemeResolver the new target entity code and coding scheme resolver
	 */
	public void setTargetEntityCodeAndCodingSchemeResolver(
			EntityCodeAndCodingSchemeResolver<Mrrel> targetEntityCodeAndCodingSchemeResolver) {
		this.targetEntityCodeAndCodingSchemeResolver = targetEntityCodeAndCodingSchemeResolver;
	}

	/**
	 * Gets the container resolver.
	 * 
	 * @return the container resolver
	 */
	public RelationContainerResolver<Mrrel> getContainerResolver() {
		return containerResolver;
	}

	/**
	 * Sets the container resolver.
	 * 
	 * @param containerResolver the new container resolver
	 */
	public void setContainerResolver(
			RelationContainerResolver<Mrrel> containerResolver) {
		this.containerResolver = containerResolver;
	}

	/**
	 * Gets the key resolver.
	 * 
	 * @return the key resolver
	 */
	public MultiAttribKeyResolver<EntityAssnsToEntity> getKeyResolver() {
		return keyResolver;
	}

	/**
	 * Sets the key resolver.
	 * 
	 * @param keyResolver the new key resolver
	 */
	public void setKeyResolver(
			MultiAttribKeyResolver<EntityAssnsToEntity> keyResolver) {
		this.keyResolver = keyResolver;
	}
}
