/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
	
	private int globalPosition = 0;
	
	private int inCachePosition = 0;
	
	private Pager<T> pager;
	
	private NextDecorator<T> nextDecorator;
	
	private boolean decorateNext = false;
	
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
		this.nextDecorator = new NextDecorator<T>();
	}
	
	@Override
	public boolean hasNext() {
		pageIfNecessary();
		
		if(cache == null || cache.size() == 0) {
			return false;
		}
		
		int cacheSize = cache.size();
		
		return inCachePosition < cacheSize ;
	}

	@Override
	public T next() {
		pageIfNecessary();
		
		T returnItem = cache.get( inCachePosition );
		
		globalPosition++;
		inCachePosition++;
		
		if(this.decorateNext) {
			return this.nextDecorator.decorateNext(this, returnItem);
		} else {
			return returnItem;
		}
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
	
	
	protected boolean isPageNeeded() {
		boolean page = inCachePosition > ( cache.size() - 1 );
		return page;
	}
	
	protected final void page() {
		cache = doExecutePage();

		inCachePosition = 0;
	}
	
	protected List<? extends T> doExecutePage(){
		return this.pager.doPage(this, globalPosition, pageSize);
	}
	
	protected abstract List<? extends T> doPage(int currentPosition, int pageSize);
	
	protected T decorateNext(T item) {
		//no-op -- for sublcasses
		return item;
	}
	
	protected void setDecorateNext(boolean decorateNext) {
		this.decorateNext = decorateNext;
	}

	protected boolean isDecorateNext() {
		return decorateNext;
	}

	@LgProxyClass
	public static class Pager<T> implements Serializable {

		private static final long serialVersionUID = 6142588013131141095L;

		public Pager() {
			super();
		}
		
		public List<? extends T> doPage(AbstractPageableIterator<T> abstractPageableIterator, int currentPosition, int pageSize){
			List<? extends T> returnList = abstractPageableIterator.doPage(currentPosition, pageSize);
			
			return returnList;
		}
	}
	
	
	@LgProxyClass
	public static class NextDecorator<T> implements Serializable {

		private static final long serialVersionUID = 6142588013131141095L;

		public NextDecorator() {
			super();
		}
		
		public T decorateNext(AbstractPageableIterator<T> abstractPageableIterator, T item){
			return abstractPageableIterator.decorateNext(item);
		}
	}


	protected int getPageSize() {
		return pageSize;
	}

	protected int getGlobalPosition() {
		return globalPosition;
	}	
}