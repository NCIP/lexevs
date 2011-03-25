package org.cts2.internal.model.uri;

import org.cts2.core.Filter;
import org.cts2.core.types.SetOperator;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;
import org.cts2.uri.SetOperable;

public abstract class AbstractDirectoryURI<T extends DirectoryURI> implements DirectoryURI, SetOperable<T> {

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
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractDirectoryURI#restrict(org.cts2.core.Filter)
	 */
	@Override
	public T restrict(Filter filter) {
		this.getRestrictionState().getFilters().add(filter);
		
		return clone();
	}
	
	protected abstract T createSetOperatedDirectoryURI(SetOperator setOperator, T directoryUri1, T directoryUri2);

	public T union(T directoryUri) {
		return this.createSetOperatedDirectoryURI(SetOperator.UNION, this.clone() , directoryUri);
	}

	public T intersect(T directoryUri) {
		return this.createSetOperatedDirectoryURI(SetOperator.INTERSECT, this.clone() , directoryUri);
	}

	public T difference(T directoryUri) {
		return this.createSetOperatedDirectoryURI(SetOperator.SUBTRACT, this.clone(), directoryUri);
	}

	@Override
	public String marshall() {
		//TODO: Context based marshalling
		return this.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected abstract T clone();
}
