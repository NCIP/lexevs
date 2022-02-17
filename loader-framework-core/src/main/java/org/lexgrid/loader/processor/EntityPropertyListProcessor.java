
package org.lexgrid.loader.processor;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexgrid.loader.data.property.ListIdSetter;
import org.lexgrid.loader.data.property.PreferredSetter;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class EntityPropertyListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyListProcessor<I> extends SortingListProcessor <I,ParentIdHolder<Property>>{

	/** The list id setter. */
	private ListIdSetter<ParentIdHolder<Property>> listIdSetter;
	
	/** The preferred setter. */
	private PreferredSetter<ParentIdHolder<Property>> preferredSetter;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.SortingListProcessor#afterProcessing(java.util.List)
	 */
	@Override
	protected List<ParentIdHolder<Property>> afterProcessing(List<ParentIdHolder<Property>> items, List<I> originalItems) {
		listIdSetter.addIds(items);
		preferredSetter.setPreferred(items);
		return items;
	}

	public ListIdSetter<ParentIdHolder<Property>> getListIdSetter() {
		return listIdSetter;
	}

	public void setListIdSetter(ListIdSetter<ParentIdHolder<Property>> listIdSetter) {
		this.listIdSetter = listIdSetter;
	}

	public PreferredSetter<ParentIdHolder<Property>> getPreferredSetter() {
		return preferredSetter;
	}

	public void setPreferredSetter(
			PreferredSetter<ParentIdHolder<Property>> preferredSetter) {
		this.preferredSetter = preferredSetter;
	}
}