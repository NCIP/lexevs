package org.lexevs.paging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.annotations.LgProxyClass;
import org.springframework.util.Assert;

public abstract class AbstractPageableIterator<T> implements Iterator<T>, Iterable<T>, Serializable{

	private static final long serialVersionUID = -5398591025205732109L;

	private static int DEFAULT_PAGE_SIZE = 100;
	
	private List<? extends T> cache = new ArrayList<T>();
	
	private int pageSize;
	
	private int currentPage = 0;
	
	private int globalPosition = 0;
	
	private Pager<T> pager;
	
	protected AbstractPageableIterator(){
		this(DEFAULT_PAGE_SIZE);
	}
	
	@Override
	public Iterator<T> iterator() {
		return this;
	}

	public AbstractPageableIterator(int pageSize){
		Assert.isTrue(pageSize > 0, "Cannot specify a Page Size less than 0.");
		this.pageSize = pageSize;
		
		this.pager = new Pager<T>();
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
		cache = this.pager.doPage(this, globalPosition, pageSize);
		currentPage++;
	}
	
	protected abstract List<? extends T> doPage(int currentPosition, int pageSize);
	
	@LgProxyClass
	public static class Pager<T> implements Serializable {

		private static final long serialVersionUID = 6142588013131141095L;

		public Pager() {
			super();
		}
		
		public List<? extends T> doPage(AbstractPageableIterator<T> abstractPageableIterator, int currentPosition, int pageSize){
			return abstractPageableIterator.doPage(currentPosition, pageSize);
		}
	}
}
