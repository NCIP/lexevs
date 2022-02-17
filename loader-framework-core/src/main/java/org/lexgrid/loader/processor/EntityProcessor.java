
package org.lexgrid.loader.processor;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;
import org.lexgrid.loader.processor.support.EntityResolver;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class EntityProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityProcessor<I> extends CodingSchemeIdAwareProcessor implements ItemProcessor<I,CodingSchemeIdHolder<Entity>>{

	/** The entity resolver. */
	private EntityResolver<I> entityResolver;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public CodingSchemeIdHolder<Entity> process(I item) throws Exception {
		CodingSchemeIdHolder<Entity> holder = new CodingSchemeIdHolder<Entity>();
		Entity entity = new Entity();
		
		entity.setEntityCodeNamespace(entityResolver.getEntityCodeNamespace(item));
		entity.setEntityCode(entityResolver.getEntityCode(item));		
		entity.setEntityDescription(Constructors.createEntityDescription(entityResolver.getEntityDescription(item)));
		entity.setIsActive(entityResolver.getIsActive(item));
		entity.setIsAnonymous(entityResolver.getIsAnonymous(item));
		entity.setIsDefined(entityResolver.getIsDefined(item));
		entity.setEntityType(entityResolver.getEntityTypes(item));
		
		holder.setItem(entity);
		holder.setCodingSchemeIdSetter(this.getCodingSchemeIdSetter());
		
		return holder;
	}

	/**
	 * Gets the entity resolver.
	 * 
	 * @return the entity resolver
	 */
	public EntityResolver<I> getEntityResolver() {
		return entityResolver;
	}

	/**
	 * Sets the entity resolver.
	 * 
	 * @param entityResolver the new entity resolver
	 */
	public void setEntityResolver(EntityResolver<I> entityResolver) {
		this.entityResolver = entityResolver;
	}


}