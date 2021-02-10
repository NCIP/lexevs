
package org.lexgrid.loader.processor.support;

import org.springframework.batch.item.ItemProcessor;

/**
 * The Interface RootNodeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface RootNodeResolver<I> extends ItemProcessor<I,I> {

		/**
		 * Checks if is root node.
		 * 
		 * @param item the item
		 * 
		 * @return true, if is root node
		 */
		public boolean isRootNode(I item);
}