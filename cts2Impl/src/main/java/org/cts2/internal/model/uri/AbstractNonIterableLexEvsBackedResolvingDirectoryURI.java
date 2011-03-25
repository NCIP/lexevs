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
package org.cts2.internal.model.uri;

import org.cts2.core.Directory;
import org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler;
import org.cts2.internal.model.uri.restrict.OriginalStateProvider;
import org.cts2.internal.model.uri.restrict.Restriction;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;

/**
 * The Class AbstractResolvingDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractNonIterableLexEvsBackedResolvingDirectoryURI<T,L extends DirectoryURI> extends AbstractResolvingDirectoryURI<L> {
	
	/** The restriction handler. */
	private NonIterableBasedResolvingRestrictionHandler<T,L> restrictionHandler;
	
	/**
	 * Instantiates a new abstract non iterable lex evs backed resolving directory uri.
	 *
	 * @param restrictionHandler the restriction handler
	 */
	protected AbstractNonIterableLexEvsBackedResolvingDirectoryURI(NonIterableBasedResolvingRestrictionHandler<T,L> restrictionHandler){
		this.restrictionHandler = restrictionHandler;
	}
	
	/**
	 * Gets the original state.
	 *
	 * @return the original state
	 */
	protected abstract T getOriginalState();
	
	protected OriginalStateProvider<T> getOriginalStateProvider(){
		return new OriginalStateProvider<T>(){

			@Override
			public T getOriginalState() {
				return AbstractNonIterableLexEvsBackedResolvingDirectoryURI.this.getOriginalState();
			}	
		};
	}
	
	/**
	 * Transform.
	 *
	 * @param <O> the
	 * @param lexevsObject the lexevs object
	 * @param clazz the clazz
	 * @return the o
	 */
	protected abstract <O> O transform(T lexevsObject, Class<O> clazz);

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractResolvingDirectoryURI#doGet(org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext, java.lang.Class)
	 */
	@Override
	protected <D extends Directory<?>> D doGet(
			QueryControl queryControl, 
			final ReadContext readContext, 
			final Class<D> resolveClass) {
		
		T originalState = makeOriginalStateUnmodifiable(this.getOriginalState());
		
		Restriction<T> restriction = this.restrictionHandler.compile(this.getThis(), new OriginalStateProvider<T>(){

			@Override
			public T getOriginalState() {
				return AbstractNonIterableLexEvsBackedResolvingDirectoryURI.this.getOriginalState();
			}
		});
		
		T restrictedState = this.restrictionHandler.apply(restriction, originalState);
		
		return this.transform(restrictedState, resolveClass);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected abstract L clone();
	
	/**
	 * Make original state unmodifiable.
	 *
	 * @param originalState the original state
	 * @return the l
	 */
	protected T makeOriginalStateUnmodifiable(T originalState){
		return originalState;
	}
	
	/**
	 * Gets the restriction handler.
	 *
	 * @return the restriction handler
	 */
	protected NonIterableBasedResolvingRestrictionHandler<T,L> getRestrictionHandler(){
		return this.restrictionHandler;
	}
}
