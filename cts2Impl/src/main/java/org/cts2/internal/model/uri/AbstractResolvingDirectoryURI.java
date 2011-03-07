package org.cts2.internal.model.uri;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.cts2.core.Directory;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;

public abstract class AbstractResolvingDirectoryURI<T extends DirectoryURI> extends AbstractDirectoryURI<T> {
	
	private ExecutorService executorService = Executors.newCachedThreadPool();

	public <D extends Directory<?>> D get(final QueryControl queryControl, final ReadContext readContext, final Class<D> content){
		final QueryControl validatedQueryControl = validateQueryControl(queryControl);
		return new AbstractTimedMethodCallback<D>(validatedQueryControl.getTimeLimit()){

			@Override
			protected D doExecuteMethod() {
				return doGet(validatedQueryControl.getFormat(), validatedQueryControl.getMaxToReturn(), readContext, content);
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
	protected abstract <D extends Directory<?>> D doGet(
			NameOrURI format, 
			Long maxToReturn,
			ReadContext readContext,
			Class<D> resolveClass);
	
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
