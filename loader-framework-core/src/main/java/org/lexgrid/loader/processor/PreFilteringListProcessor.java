
package org.lexgrid.loader.processor;

import java.util.List;

import org.lexgrid.loader.processor.support.ListFilter;

/**
 * The Class PreFilteringListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PreFilteringListProcessor<I,O> extends AbstractListProcessor<I,O> {

	private ListFilter<I> listFilter;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.AbstractListProcessor#beforeProcessing(java.util.List)
	 */
	@Override
	protected List<I> beforeProcessing(List<I> items) {	
		return listFilter.filter(items);
	}

	@Override
	protected List<O> afterProcessing(List<O> items) {
		return items;
	}

	public ListFilter<I> getListFilter() {
		return listFilter;
	}

	public void setListFilter(ListFilter<I> listFilter) {
		this.listFilter = listFilter;
	}	
}