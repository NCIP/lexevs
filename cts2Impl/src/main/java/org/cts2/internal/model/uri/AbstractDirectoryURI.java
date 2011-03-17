package org.cts2.internal.model.uri;

import org.cts2.core.Filter;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;

public abstract class AbstractDirectoryURI<T extends DirectoryURI> implements DirectoryURI {

	public abstract T restrict(Filter filter);
	
	protected abstract int doCount(ReadContext readContext);

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
}
