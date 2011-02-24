package org.cts2.internal.uri;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.core.Directory;
import org.cts2.directory.Resolvable;
import org.cts2.directory.Restrictable;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;

public abstract class AbstractResolvingDirectoryURI
	<T,U extends DirectoryURI & Restrictable<U>,
	D extends Directory<U>,
	L extends Directory<U>> extends AbstractRestrictingDirectoryURI<T,U> implements Restrictable<U>, Resolvable<D,L>{
	
	private ExecutorService executorService = Executors.newCachedThreadPool();

	protected AbstractResolvingDirectoryURI(LexBIGService lexBIGService,
			BeanMapper beanMapper) {
		super(lexBIGService, beanMapper);
	}

	@Override
	public D resolve(final QueryControl queryControl, final ReadContext readContext) {
		return new AbstractTimedMethodCallback<D>(queryControl.getTimeLimit()){

			@Override
			protected D doExecuteMethod() {
				return doResolve(getLexEvsBackingObject(), queryControl.getFormat(), queryControl.getMaxToReturn(), readContext);
			}
			
		}.executeMethod();
	}
	
	protected abstract D doResolve(T lexEvsBackingObject, NameOrURI format, long maxToReturn, ReadContext readContext);
	
	protected abstract L doResolveAsList(T lexEvsBackingObject, NameOrURI format, long maxToReturn, ReadContext readContext);

	@Override
	public L resolveAsList(final QueryControl queryControl, final ReadContext readContext) {
		return new AbstractTimedMethodCallback<L>(queryControl.getTimeLimit()){

			@Override
			protected L doExecuteMethod() {
				return doResolveAsList(getLexEvsBackingObject(), queryControl.getFormat(), queryControl.getMaxToReturn(), readContext);
			}
			
		}.executeMethod();
	}
	
	private abstract class AbstractTimedMethodCallback<E> implements TimedMethodCallback<E>{

		private Long time;
		
		private AbstractTimedMethodCallback(Long time){
			this.time = time;
		}

		@Override
		public E executeMethod() {

			if(time == null){
				return doExecuteMethod();
			} else {

				Future<E> result = executorService.submit(new Callable<E>(){

					@Override
					public E call() throws Exception {
						return doExecuteMethod();
					}

				});

				try {
					return result.get(time, TimeUnit.MILLISECONDS);
				} catch (TimeoutException e) {
					throw new RuntimeException("Operation has timed out.");
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}
		}

		protected abstract E doExecuteMethod();
	}

	private interface TimedMethodCallback<T> {
		
		public T executeMethod();

	}
	
}
