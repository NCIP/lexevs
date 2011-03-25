package org.cts2.internal.model.uri;

import org.cts2.core.Filter;
import org.cts2.core.types.SetOperator;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.CodeSystemVersionDirectoryURI;
import org.cts2.uri.DirectoryURI;

public abstract class AbstractDirectoryURI<T extends DirectoryURI> implements DirectoryURI {

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
	
	public static void main(String[] args){
		System.out.println(DirectoryURI.class.isAssignableFrom(CodeSystemVersionDirectoryURI.class));
	}

	private void validateType(DirectoryURI directoryUri){
		boolean isAssignable = 
			this.getClass().isAssignableFrom(directoryUri.getClass());
		
		if(!isAssignable){
			throw new IllegalStateException();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <D extends DirectoryURI> D union(D directoryUri) {
		this.validateType(directoryUri);
		return (D) this.createSetOperatedDirectoryURI(SetOperator.UNION, (T)this.clone() , (T)directoryUri);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <D extends DirectoryURI> D intersect(D directoryUri) {
		this.validateType(directoryUri);
		return (D) this.createSetOperatedDirectoryURI(SetOperator.INTERSECT, (T)this.clone() , (T)directoryUri);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <D extends DirectoryURI> D difference(D directoryUri) {
		this.validateType(directoryUri);
		return (D) this.createSetOperatedDirectoryURI(SetOperator.SUBTRACT, (T)this.clone() , (T)directoryUri);
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
