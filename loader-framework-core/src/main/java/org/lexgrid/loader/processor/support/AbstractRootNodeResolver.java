package org.lexgrid.loader.processor.support;

import junit.framework.Assert;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.lexgrid.loader.wrappers.ParentIdHolder;

public abstract class AbstractRootNodeResolver implements RootNodeResolver<ParentIdHolder<AssociationSource>>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RootNodeResolver#isRootNode(java.lang.Object)
	 */
	public boolean isRootNode(ParentIdHolder<AssociationSource> item) {
		//TODO: FIX THIS
		String relation = null;
		
		return isHierarchicalRelation(relation) &&
			(pointsToRoot(item) || pointsToTail(item));
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public ParentIdHolder<AssociationSource> process(ParentIdHolder<AssociationSource> item)
			throws Exception {
		Assert.assertEquals("Only one (1) AssocationSource and one (1) AssociationTarget may be processed at a time." , 
				item.getItem().getTargetCount() == 1);
		
		AssociationSource source = new AssociationSource();
		AssociationTarget target = new AssociationTarget();
		
		if(pointsToTail(item)){
			target.setTargetEntityCode("@@");
		}
		if(pointsToRoot(item)){
			source.setSourceEntityCode("@");
		}
		
		source.addTarget(target);
		
		return new ParentIdHolder<AssociationSource>(item.getCodingSchemeIdSetter(),item.getParentId(), source);
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
		//TODO: Fix this
		String targetCode = null;
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
