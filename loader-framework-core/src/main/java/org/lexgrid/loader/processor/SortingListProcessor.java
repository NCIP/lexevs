
package org.lexgrid.loader.processor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The Class SortingListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SortingListProcessor<I,O> extends AbstractParameterPassingListProcessor<I,O> {

	/** The property comparator. */
	private Comparator<I> propertyComparator;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.AbstractListProcessor#beforeProcessing(java.util.List)
	 */
	@Override
	protected List<I> beforeProcessing(List<I> items) {
		Collections.sort(items, propertyComparator);
		return items;
	}
	
	@Override
	protected List<O> afterProcessing(List<O> processedItems,
			List<I> originalItems) {
		return processedItems;
	}	

	/**
	 * Gets the property comparator.
	 * 
	 * @return the property comparator
	 */
	public Comparator<I> getPropertyComparator() {
		return propertyComparator;
	}

	/**
	 * Sets the property comparator.
	 * 
	 * @param propertyComparator the new property comparator
	 */
	public void setPropertyComparator(Comparator<I> propertyComparator) {
		this.propertyComparator = propertyComparator;
	}
}