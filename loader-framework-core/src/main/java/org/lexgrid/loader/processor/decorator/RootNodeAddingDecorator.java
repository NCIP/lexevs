
package org.lexgrid.loader.processor.decorator;

import java.util.ArrayList;
import java.util.List;

import org.lexgrid.loader.processor.support.RootNodeResolver;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class RootNodeAddingDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RootNodeAddingDecorator<I,O> implements RootResolvingNodeDecorator<I,O> {

	/** The root node resolver. */
	private RootNodeResolver<O> rootNodeResolver;
	
	/** The decorated item processor. */
	private ItemProcessor<I,O> decoratedItemProcessor;
	
	/** The replace relation. */
	private boolean replaceRelation = false;
	
	
	/**
	 * Instantiates a new root node adding decorator.
	 * 
	 * @param itemProcessor the item processor
	 */
	public RootNodeAddingDecorator(ItemProcessor<I,O> itemProcessor){
		this.decoratedItemProcessor = itemProcessor;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<O> process(I item) throws Exception {
		O processedItem = decoratedItemProcessor.process(item);
		
		//If the decorated Process wants to skip this record, skip it.
		if(processedItem == null) {
			return null;
		}

		List<O> buffer = new ArrayList<O>();

		if(rootNodeResolver.isRootNode(processedItem)){
			buffer.add(processRootNode(processedItem));
			if(!replaceRelation){
				buffer.add(processedItem);
			} 
		} else {
			buffer.add(processedItem);
		}

		return buffer;
	}
	
	/**
	 * Process root node.
	 * 
	 * @param item the item
	 * 
	 * @return the o
	 * 
	 * @throws Exception the exception
	 */
	protected O processRootNode(O item) throws Exception {
		return rootNodeResolver.process(item);
	}


	/**
	 * Gets the root node resolver.
	 * 
	 * @return the root node resolver
	 */
	public RootNodeResolver<O> getRootNodeResolver() {
		return rootNodeResolver;
	}

	/**
	 * Sets the root node resolver.
	 * 
	 * @param rootNodeResolver the new root node resolver
	 */
	public void setRootNodeResolver(RootNodeResolver<O> rootNodeResolver) {
		this.rootNodeResolver = rootNodeResolver;
	}

	/**
	 * Checks if is replace relation.
	 * 
	 * @return true, if is replace relation
	 */
	public boolean isReplaceRelation() {
		return replaceRelation;
	}

	/**
	 * Sets the replace relation.
	 * 
	 * @param replaceRelation the new replace relation
	 */
	public void setReplaceRelation(boolean replaceRelation) {
		this.replaceRelation = replaceRelation;
	}
}