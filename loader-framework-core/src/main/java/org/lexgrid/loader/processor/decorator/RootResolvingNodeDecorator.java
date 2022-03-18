
package org.lexgrid.loader.processor.decorator;

import java.util.List;

import org.lexgrid.loader.processor.support.RootNodeResolver;
import org.springframework.batch.item.ItemProcessor;

public interface RootResolvingNodeDecorator<I,O> extends ItemProcessor<I,List<O>>{

/**
	 * Gets the root node resolver.
	 * 
	 * @return the root node resolver
	 */
public RootNodeResolver<O> getRootNodeResolver();

	/**
	 * Sets the root node resolver.
	 * 
	 * @param rootNodeResolver the new root node resolver
	 */
	public void setRootNodeResolver(RootNodeResolver<O> rootNodeResolver);

	/**
	 * Checks if is replace relation.
	 * 
	 * @return true, if is replace relation
	 */
	public boolean isReplaceRelation();

	/**
	 * Sets the replace relation.
	 * 
	 * @param replaceRelation the new replace relation
	 */
	public void setReplaceRelation(boolean replaceRelation);
}