
package org.lexevs.paging;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.annotations.LgProxyClass;

/**
 * The Class AbstractRefereshingPageableIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractRefereshingPageableIterator<R,T> extends AbstractPageableIterator<T>{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5398591025205732109L;
	
	/** The refershing pager. */
	private RefershingPager<R,T> refershingPager = new RefershingPager<R,T>();

	/**
	 * Instantiates a new abstract refereshing pageable iterator.
	 * 
	 * @param pageSize the page size
	 */
	public AbstractRefereshingPageableIterator(int pageSize) {
		super(pageSize);
	}

	public AbstractRefereshingPageableIterator() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.paging.AbstractPageableIterator#doExecutePage()
	 */
	@Override
	protected List<? extends T> doExecutePage() {
		RefreshResult<R, T> result = refershingPager.doRefershingPage(this, this.getGlobalPosition(), this.getPageSize());
		
		this.doRefresh(result.getRefresh());
		
		return result.getResult();
	}

	/**
	 * The Class RefershingPager.
	 */
	@LgProxyClass
	public static class RefershingPager<R,T> implements Serializable {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 6142588013131141095L;

		/**
		 * Instantiates a new refershing pager.
		 */
		public RefershingPager() {
			super();
		}
		
		/**
		 * Do refershing page.
		 * 
		 * @param abstractPageableIterator the abstract pageable iterator
		 * @param currentPosition the current position
		 * @param pageSize the page size
		 * 
		 * @return the refresh result< r, t>
		 */
		public RefreshResult<R,T> doRefershingPage(AbstractRefereshingPageableIterator<R,T> abstractPageableIterator, int currentPosition, int pageSize){
			List<? extends T> returnList = abstractPageableIterator.doPage(currentPosition, pageSize);
			
			RefreshResult<R,T> result = new RefreshResult<R,T>();
			result.setRefresh( abstractPageableIterator.doGetRefresh() );
			result.setResult(returnList);
			
			return result;
		}
	}
	
	/**
	 * Do get refresh.
	 * 
	 * @return the r
	 */
	protected abstract R doGetRefresh();
	
	/**
	 * Do refresh.
	 * 
	 * @param refresh the refresh
	 */
	protected abstract void doRefresh(R refresh);
	
	/**
	 * The Class RefreshResult.
	 */
	public static class RefreshResult<R,T> implements Serializable {
	
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 6632808883074854419L;
		
		/** The result. */
		private List<? extends T> result;
		
		/** The refresh. */
		private R refresh;
		
		/**
		 * Gets the result.
		 * 
		 * @return the result
		 */
		public List<? extends T> getResult() {
			return result;
		}
		
		/**
		 * Sets the result.
		 * 
		 * @param result the new result
		 */
		public void setResult(List<? extends T> result) {
			this.result = result;
		}
		
		/**
		 * Gets the refresh.
		 * 
		 * @return the refresh
		 */
		public R getRefresh() {
			return refresh;
		}
		
		/**
		 * Sets the refresh.
		 * 
		 * @param refresh the new refresh
		 */
		public void setRefresh(R refresh) {
			this.refresh = refresh;
		}
	}
}