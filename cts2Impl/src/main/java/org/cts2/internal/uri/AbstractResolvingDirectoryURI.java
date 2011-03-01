/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.uri;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.core.Directory;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;
import org.cts2.uri.operation.Resolvable;
import org.cts2.uri.operation.Restrictable;

/**
 * The Class AbstractResolvingDirectoryURI.
 *
 * @param <T> the
 * @param <U> the
 * @param <D> the
 * @param <L> the
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractResolvingDirectoryURI
	<T,U extends DirectoryURI & Restrictable<U>,
	D extends Directory<U>,
	L extends Directory<U>> extends AbstractRestrictingDirectoryURI<T,U> implements Restrictable<U>, Resolvable<D,L>{
	
	/** The executor service. */
	private ExecutorService executorService = Executors.newCachedThreadPool();

	/**
	 * Instantiates a new abstract resolving directory uri.
	 *
	 * @param lexBIGService the lex big service
	 * @param beanMapper the bean mapper
	 */
	protected AbstractResolvingDirectoryURI(LexBIGService lexBIGService,
			BeanMapper beanMapper) {
		super(lexBIGService, beanMapper);
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.uri.operation.Resolvable#resolve(org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public D resolve(QueryControl queryControl, final ReadContext readContext) {
		final QueryControl validatedQueryControl = validateQueryControl(queryControl);
		return new AbstractTimedMethodCallback<D>(validatedQueryControl.getTimeLimit()){

			@Override
			protected D doExecuteMethod() {
				return doResolve(getLexEvsBackingObject(), validatedQueryControl.getFormat(), validatedQueryControl.getMaxToReturn(), readContext);
			}
			
		}.executeMethod();
	}
	
	/**
	 * Do resolve.
	 *
	 * @param lexEvsBackingObject the lex evs backing object
	 * @param format the format
	 * @param maxToReturn the max to return
	 * @param readContext the read context
	 * @return the d
	 */
	protected abstract D doResolve(T lexEvsBackingObject, NameOrURI format, Long maxToReturn, ReadContext readContext);
	
	/**
	 * Do resolve as list.
	 *
	 * @param lexEvsBackingObject the lex evs backing object
	 * @param format the format
	 * @param maxToReturn the max to return
	 * @param readContext the read context
	 * @return the l
	 */
	protected abstract L doResolveAsList(T lexEvsBackingObject, NameOrURI format, Long maxToReturn, ReadContext readContext);
	
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
	 * @see org.cts2.uri.operation.Resolvable#resolveAsList(org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public L resolveAsList(final QueryControl queryControl, final ReadContext readContext) {
		final QueryControl validatedQueryControl = validateQueryControl(queryControl);
		
		return new AbstractTimedMethodCallback<L>(validatedQueryControl.getTimeLimit()){

			@Override
			protected L doExecuteMethod() {
				return doResolveAsList(getLexEvsBackingObject(), validatedQueryControl.getFormat(), validatedQueryControl.getMaxToReturn(), readContext);
			}
			
		}.executeMethod();
	}
	
	/**
	 * The Class AbstractTimedMethodCallback.
	 *
	 * @param <E> the
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private abstract class AbstractTimedMethodCallback<E> implements TimedMethodCallback<E>{

		/** The time. */
		private Long time;
		
		/**
		 * Instantiates a new abstract timed method callback.
		 *
		 * @param time the time
		 */
		private AbstractTimedMethodCallback(Long time){
			this.time = time;
		}

		/* (non-Javadoc)
		 * @see org.cts2.internal.uri.AbstractResolvingDirectoryURI.TimedMethodCallback#executeMethod()
		 */
		@Override
		public E executeMethod() {

			if(this.time == null){
				return doExecuteMethod();
			} else {

				Future<E> result = AbstractResolvingDirectoryURI.this.executorService.submit(new Callable<E>(){

					@Override
					public E call() throws Exception {
						return doExecuteMethod();
					}

				});

				try {
					return result.get(this.time, TimeUnit.MILLISECONDS);
				} catch (TimeoutException e) {
					throw new RuntimeException("Operation has timed out.");
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}
		}

		/**
		 * Do execute method.
		 *
		 * @return the e
		 */
		protected abstract E doExecuteMethod();
	}

	/**
	 * The Interface TimedMethodCallback.
	 *
	 * @param <T> the
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private interface TimedMethodCallback<T> {
		
		/**
		 * Execute method.
		 *
		 * @return the t
		 */
		public T executeMethod();

	}
	
}
