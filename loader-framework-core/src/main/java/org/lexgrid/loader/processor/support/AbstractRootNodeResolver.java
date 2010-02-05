package org.lexgrid.loader.processor.support;

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.lexgrid.loader.data.DataUtils;
import org.lexgrid.loader.data.association.RandomUuidKeyResolver;

public abstract class AbstractRootNodeResolver implements RootNodeResolver<EntityAssnsToEntity>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RootNodeResolver#isRootNode(java.lang.Object)
	 */
	public boolean isRootNode(EntityAssnsToEntity item) {
		String relation = item.getEntityCode();
		
		return isHierarchicalRelation(relation) &&
			(pointsToRoot(item) || pointsToTail(item));
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public EntityAssnsToEntity process(EntityAssnsToEntity item)
			throws Exception {
		EntityAssnsToEntity copy = DataUtils.deepCloneEntityAssnsToEntity(item);
		
		if(pointsToTail(item)){
			copy.setTargetEntityCode("@@");
		}
		if(pointsToRoot(item)){
			copy.setSourceEntityCode("@");
		}

		String codingScheme = copy.getCodingSchemeName();
		copy.setSourceEntityCodeNamespace(codingScheme);
		copy.setTargetEntityCodeNamespace(codingScheme);
		
		//set the multiattributeskey to a random uuid
		copy.setMultiAttributesKey(RandomUuidKeyResolver.getRandomUuidKey());
		
		return copy;
	}
	
	/**
	 * Points to root.
	 * 
	 * @param item the item
	 * 
	 * @return true, if successful
	 */
	protected boolean pointsToRoot(EntityAssnsToEntity item){
		String sourceCode = item.getSourceEntityCode();
		return isSourceRootNode(sourceCode);
	}
	
	/**
	 * Points to tail.
	 * 
	 * @param item the item
	 * 
	 * @return true, if successful
	 */
	protected boolean pointsToTail(EntityAssnsToEntity item){
		String targetCode = item.getTargetEntityCode();
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
}
