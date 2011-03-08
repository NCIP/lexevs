package org.cts2.internal.model.uri;

import java.util.concurrent.Callable;

import org.cts2.core.Directory;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;
import org.cts2.utility.ExecutionUtils;

public abstract class AbstractResolvingDirectoryURI<T extends DirectoryURI> extends AbstractDirectoryURI<T> {
	
	public <D extends Directory<?>> D get(final QueryControl queryControl, final ReadContext readContext, final Class<D> content){
		final QueryControl validatedQueryControl = validateQueryControl(queryControl);
		return ExecutionUtils.callWithTimeout(new Callable<D>(){

			@Override
			public D call() {
				return doGet(validatedQueryControl.getFormat(), validatedQueryControl.getMaxToReturn(), readContext, content);
			}
			
		}, queryControl);
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
}
