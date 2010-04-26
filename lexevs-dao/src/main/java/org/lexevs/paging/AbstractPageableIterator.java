package org.lexevs.paging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.Assert;

public abstract class AbstractPageableIterator<T> implements Iterator<T>, Iterable<T> {

	private static int DEFAULT_PAGE_SIZE = 100;
	
	private List<T> cache = new ArrayList<T>();
	
	private int pageSize;
	
	private int currentPage = 0;
	
	private int globalPosition = 0;

	
	public AbstractPageableIterator(){
		this(DEFAULT_PAGE_SIZE);
	}
	
	@Override
	public Iterator<T> iterator() {
		return this;
	}

	public AbstractPageableIterator(int pageSize){
		Assert.isTrue(pageSize > 0, "Cannot specify a Page Size less than 0.");
		this.pageSize = pageSize;
	}
	
	@Override
	public boolean hasNext() {
		pageIfNecessary();
		
		if(cache == null || cache.size() == 0) {
			return false;
		}
		
		int positionInCache = calculateCachePosition();
		int cacheSize = cache.size();
		
		return positionInCache < cacheSize ;
	}

	@Override
	public T next() {
		pageIfNecessary();
		T returnItem = cache.get( calculateCachePosition() );
		globalPosition++;
		return returnItem;
	}
	
	protected void pageIfNecessary() {
		if(isPageNeeded()) {
			page();
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	protected int calculateCachePosition() {
		return globalPosition - ( (currentPage -1 ) * pageSize) ;
	}
	
	protected boolean isPageNeeded() {
		boolean page = globalPosition >  ( ( currentPage * pageSize) - 1 );
		return page;
	}
	
	protected void page() {
		cache = doPage(globalPosition, pageSize);
		currentPage++;
	}
	
	protected abstract List<T> doPage(int currentPosition, int pageSize);
}
