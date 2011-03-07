package org.cts2.internal.model.uri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;

public abstract class AbstractDirectoryURI<T extends DirectoryURI> implements DirectoryURI {

	protected abstract void doRestrict(FilterComponent filterComponent);
	
	protected abstract int doCount(ReadContext readContext);
	
	private Comparator<FilterComponent> FILTER_ORDER_COMPARATOR = new FilterOrderComparator();
	
	
	@SuppressWarnings("unchecked")
	@Override
	public T restrict(Filter filter) {
		List<FilterComponent> filterComponents = new ArrayList<FilterComponent>();
		
		filterComponents = Arrays.asList(filter.getComponent());
		
		Collections.sort(filterComponents, FILTER_ORDER_COMPARATOR);
		
		for (FilterComponent filterComponent : filterComponents) {
			this.doRestrict(filterComponent);
		}	
		
		return (T) this;
	}

	@Override
	public int count(ReadContext readContext) {
		return this.doCount(readContext);
	}
	
	//TODO: throw some sort of validation exception
	/**
	 * Validate query control.
	 *
	 * @param queryControl the query control
	 * @return the query control
	 */
	protected QueryControl validateQueryControl(QueryControl queryControl){
		if(queryControl == null){
			return new QueryControl();
		} else {
			return queryControl;
		}
	}
	
	/**
	 * The Class FilterOrderComparator.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private static class FilterOrderComparator implements Comparator<FilterComponent>{

		@Override
		public int compare(FilterComponent o1, FilterComponent o2) {
			return (int) (o1.getComponentOrder() - o2.getComponentOrder());
		}
	}
}
