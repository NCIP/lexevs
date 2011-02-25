package org.cts2.internal.uri;

import java.util.Arrays;
import java.util.Comparator;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.uri.DirectoryURI;
import org.cts2.uri.operation.Restrictable;

public abstract class AbstractRestrictingDirectoryURI
	<T,U extends DirectoryURI & Restrictable<U>> extends AbstractLexEvsDirectoryURI<T> implements Restrictable<U>{

	private static Comparator<FilterComponent> FILTER_ORDER_COMPARATOR = new FilterOrderComparator();
	
	protected AbstractRestrictingDirectoryURI(LexBIGService lexBIGService,
			BeanMapper beanMapper) {
		super(lexBIGService, beanMapper);
	}

	@SuppressWarnings("unchecked")
	@Override
	public U restrict(Filter filter) {
		FilterComponent[] filterComponents = filter.getComponent();
		
		Arrays.sort(filterComponents, FILTER_ORDER_COMPARATOR);
		
		for(FilterComponent filterComponent : filterComponents){
			this.applyFilterComponent(this.getLexEvsBackingObject(), filterComponent);
		}
		
		return (U) this;
	}
	
	protected abstract void applyFilterComponent(T lexEvsBackingObject, FilterComponent filterComponent);
	
	private static class FilterOrderComparator implements Comparator<FilterComponent>{

		@Override
		public int compare(FilterComponent o1, FilterComponent o2) {
			return (int) (o1.getComponentOrder() - o2.getComponentOrder());
		}
	}
}
