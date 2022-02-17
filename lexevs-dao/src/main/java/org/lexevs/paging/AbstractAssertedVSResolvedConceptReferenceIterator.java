
package org.lexevs.paging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.annotations.LgProxyClass;
import org.springframework.util.Assert;

/**
 *
 * 
 * @author <a href="mailto:bauer.scott@mayo.edu">Scott Bauer</a>
 */
public abstract class AbstractAssertedVSResolvedConceptReferenceIterator<T> implements Iterator<T>, Iterable<T>, Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5398591025205732109L;

	/** The DEFAUL t_ pag e_ size. */
	private static int DEFAULT_PAGE_SIZE = 100;
	
	/** The cache. */
	private List<? extends T> cache = new ArrayList<T>();
	
	/** The page size. */
	protected int pageSize;
	
	/** The global position. */
	private int globalPosition = 0;
	
	/** The in cache position. */
	protected int inCachePosition = 0;
	
	protected int maxValueSets = 0;
	
	/** The pager. */
	protected Pager<T> pager;
	
	private RemainingRefresher<T> remainingRefresher;
	
	protected boolean inPagingNext = false;
	
	protected boolean isExhausted = false;
	
	/**
	 * Instantiates a new abstract pageable iterator.
	 */
	protected AbstractAssertedVSResolvedConceptReferenceIterator(){
		this(DEFAULT_PAGE_SIZE);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return this;
	}

	/**
	 * Instantiates a new abstract pageable iterator.
	 * 
	 * @param pageSize the page size
	 */
	public AbstractAssertedVSResolvedConceptReferenceIterator(int pageSize){
		Assert.isTrue(pageSize > 0, "Cannot specify a Page Size less than 0.");
		this.pageSize = pageSize;
		
		this.pager = new Pager<T>();
		this.remainingRefresher = new RemainingRefresher<T>();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if(isExhausted){
			return false;
		}
		if(inPagingNext) {
			//pageIfNecessary(globalPosition, pageSize); Do Nothing
		}
		else {
		pageIfNecessary();
		}
		
		if(cache == null || cache.size() == 0) {
			isExhausted = true;
			return false;
		}
		
		int cacheSize = cache.size();
		
		boolean hasNext = inCachePosition < cacheSize;
		
		isExhausted = !hasNext;
		
		return hasNext;
		
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public T next() {
		pageIfNecessary();
		T returnItem = cache.get( inCachePosition );		
		globalPosition++;
		inCachePosition++;
		return returnItem;

	}
	

	public List<T> protoNext(final int size, int remains) {

		List<T> returnItem = new ArrayList<T>();

			pageOnSize(size, remains);
			returnItem.addAll(cache);

			return  returnItem;
	}

	/**
	 * Page if necessary.
	 */
	protected void pageIfNecessary() {
		if(isPageNeeded()) {
			page();
		}
	}
	
	
	protected void  pageIfNecessary(int size, int remains) {
			pageOnSize(size, remains);
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	
	/**
	 * Checks if is page needed.
	 * 
	 * @return true, if is page needed
	 */
	protected boolean isPageNeeded() {
		boolean page = inCachePosition > ( cache.size() - 1 );
		return page;
	}
	
	/**
	 * Page.
	 */
	protected final void page() {
		cache = doExecutePage();

		inCachePosition = 0;
	}
	
	
	protected final void pageOnSize(int size, int remains) {
		cache = doExecutePageOnSize(size, remains);
		inCachePosition = 0;
	}
	
	/**
	 * Do execute page.
	 * 
	 * @return the list<? extends t>
	 */
	protected List<? extends T> doExecutePage(){
		List<? extends T> returnList = this.pager.doPage(this, globalPosition, pageSize);

		return returnList;
	}
	
	/**
	 * Do execute page.
	 * 
	 * @return the list<? extends t>
	 */
	protected List<? extends T> doExecutePageOnSize(int size, int remains){
		List<? extends T> returnList = this.pager.doPage(this, globalPosition, size, remains);
		return returnList;
	}
	
	/**
	 * Returns a page of results.
	 * 
	 * NOTE: 'pageSize' is not binding -- it is the suggested page size.
	 * Implementing classes may return more or less than the suggested
	 * 'pageSize' parameter, although it is generally recommended to abide
	 * by the 'pageSize' parameter when possible.
	 * 
	 * A null or empty list returned from this method will signify
	 * that the underlying results are exhausted and paging should halt.
	 * 
	 * @param currentPosition the current position
	 * @param pageSize the page size
	 * 
	 * @return the list<? extends t>
	 */
	protected abstract List<? extends T> doPage(int currentPosition, int pageSize);
	protected abstract List<? extends T> doPage(int currentPosition, int pageSize, int remains);
	protected abstract int refreshNumberRemaining(int remaining);
	
	public int getRefreshedRemaining() {
		return this.remainingRefresher.getRemaining();
	}
	
	public void refreshRemaining(int refreshRemain) {
		this.remainingRefresher.doRefreshRemaining(this, refreshRemain);
	}

	/**
	 * The Class Pager.
	 */
	@LgProxyClass
	public static class Pager<T> implements Serializable {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 6142588013131141095L;

		/**
		 * Instantiates a new pager.
		 */
		public Pager() {
			super();
		}

		/**
		 * Do page.
		 * 
		 * @param abstractPageableIterator
		 *            the abstract pageable iterator
		 * @param currentPosition
		 *            the current position
		 * @param pageSize
		 *            the page size
		 * 
		 * @return the list<? extends t>
		 */
		public List<? extends T> doPage(AbstractAssertedVSResolvedConceptReferenceIterator<T> abstractPageableIterator,
				int currentPosition, int pageSize) {
			List<? extends T> returnList = abstractPageableIterator.doPage(currentPosition, pageSize);
			return returnList;
		}
		

		public List<? extends T> doPage(AbstractAssertedVSResolvedConceptReferenceIterator<T> abstractPageableIterator,
				int currentPosition, int pageSize, int remains) {
			List<? extends T> returnList = abstractPageableIterator.doPage(currentPosition, pageSize, remains);
			abstractPageableIterator.refreshRemaining(remains - pageSize <= 0? 0: remains - pageSize);
			return returnList;
		}
	}
	

	public static class RemainingRefresher<T> implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5132659905928838406L;
		
		private int remainingNumber;

		/**
		 * Instantiates a new pager.
		 */
		public RemainingRefresher() {
			super();
		}
		
		/**
		 * Do page.
		 * 
		 * @param abstractPageableIterator the abstract pageable iterator
		 * @param currentPosition the current position
		 * @param pageSize the page size
		 * 
		 * @return the list<? extends t>
		 */
		public int doRefreshRemaining(AbstractAssertedVSResolvedConceptReferenceIterator<T> abstractPageableIterator, int remaining){
			remainingNumber = abstractPageableIterator.refreshNumberRemaining(remaining);
			
			return remainingNumber;
		}
		
		public int getRemaining() {
			return remainingNumber;
		}
	}

	/**
	 * Gets the page size.
	 * 
	 * @return the page size
	 */
	protected int getPageSize() {
		return pageSize;
	}

	/**
	 * Gets the global position.
	 * 
	 * @return the global position
	 */
	protected int getGlobalPosition() {
		return globalPosition;
	}	
	
//	protected void setGlobalPosition(int increment, int maximum) {
//		globalPosition = globalPosition + increment > maximum? 
//				maximum:globalPosition + increment ;
//	}
	
	protected void setGlobalPostion(int increment) {
		globalPosition = increment > cache.size()? 
				globalPosition + cache.size():globalPosition + increment ;
		globalPosition = globalPosition > maxValueSets? maxValueSets: globalPosition;
	}
	
	protected List<? extends T> getCache(){
		return this.cache;
	}
	
	protected void emptyCache() {
		cache = new ArrayList<T>();
	}
	
	
}