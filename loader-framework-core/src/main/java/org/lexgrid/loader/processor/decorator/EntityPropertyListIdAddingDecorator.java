
package org.lexgrid.loader.processor.decorator;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexgrid.loader.data.property.ListIdSetter;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class EntityPropertyListIdAddingDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyListIdAddingDecorator<I> implements ItemProcessor<List<I>, List<ParentIdHolder<Property>>> {

	
	/** The list id setter. */
	private ListIdSetter<ParentIdHolder<Property>> listIdSetter;
	
	/** The decorated processor. */
	private ItemProcessor<List<I>, List<ParentIdHolder<Property>>> decoratedProcessor;
	
	/**
	 * Instantiates a new entity property list id adding decorator.
	 * 
	 * @param decoratedProcessor the decorated processor
	 */
	public EntityPropertyListIdAddingDecorator(ItemProcessor<List<I>, List<ParentIdHolder<Property>>> decoratedProcessor){
		this.decoratedProcessor = decoratedProcessor;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<ParentIdHolder<Property>> process(List<I> items)
			throws Exception {
		List<ParentIdHolder<Property>> processedItems = decoratedProcessor.process(items);
		listIdSetter.addIds(processedItems);
		return processedItems;
	}

	public ListIdSetter<ParentIdHolder<Property>> getListIdSetter() {
		return listIdSetter;
	}

	public void setListIdSetter(ListIdSetter<ParentIdHolder<Property>> listIdSetter) {
		this.listIdSetter = listIdSetter;
	}

	public ItemProcessor<List<I>, List<ParentIdHolder<Property>>> getDecoratedProcessor() {
		return decoratedProcessor;
	}

	public void setDecoratedProcessor(
			ItemProcessor<List<I>, List<ParentIdHolder<Property>>> decoratedProcessor) {
		this.decoratedProcessor = decoratedProcessor;
	}
}