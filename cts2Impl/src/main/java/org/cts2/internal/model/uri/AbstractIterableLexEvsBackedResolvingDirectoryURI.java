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
import org.cts2.internal.model.uri.restrict.IterableBasedResolvingRestrictionHandler;
import org.cts2.internal.model.uri.restrict.IterableRestriction;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

/**
 * The Class AbstractIterableLexEvsBackedResolvingDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractIterableLexEvsBackedResolvingDirectoryURI<T,L extends DirectoryURI> extends AbstractResolvingDirectoryURI<L> {
	
	/** The restriction handler. */
	private IterableBasedResolvingRestrictionHandler<T,L> restrictionHandler;
	
	/**
	 * Instantiates a new abstract iterable lex evs backed resolving directory uri.
	 *
	 * @param restrictionHandler the restriction handler
	 * @param restrictionState 
	 */
	protected AbstractIterableLexEvsBackedResolvingDirectoryURI(IterableBasedResolvingRestrictionHandler<T,L> restrictionHandler){
		this.restrictionHandler = restrictionHandler;
	}
	
	/**
	 * Gets the original state.
	 *
	 * @return the original state
	 */
	protected abstract Iterable<T> getOriginalState();
	
	/**
	 * Transform.
	 *
	 * @param <O> the
	 * @param lexevsObject the lexevs object
	 * @param clazz the clazz
	 * @return the o
	 */
	protected abstract <O> O transform(Iterable<T> lexevsObject, Class<O> clazz);

	@Override
	protected int doCount(ReadContext readContext) {
		IterableRestriction<T> restriction = this.restrictionHandler.compile( this.clone() );
		
		return Iterators.size(this.restrictionHandler.apply(restriction, this.getOriginalState()).iterator());
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractResolvingDirectoryURI#doGet(org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext, java.lang.Class)
	 */
	@Override
	protected <D extends Directory<?>> D doGet(
			QueryControl queryControl, 
			final ReadContext readContext, 
			final Class<D> resolveClass) {
		
		Iterable<T> originalState = Iterables.unmodifiableIterable(this.getOriginalState());
		
		IterableRestriction<T> restriction = this.restrictionHandler.compile( this.clone() );
		
		Iterable<T> restrictedState = this.restrictionHandler.apply(restriction, originalState);
		
		return this.transform(restrictedState, resolveClass);
	}

	protected IterableBasedResolvingRestrictionHandler<T, L> getRestrictionHandler() {
		return restrictionHandler;
	}
}
