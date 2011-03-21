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

import java.util.ArrayList;
import java.util.List;

import org.cts2.core.Directory;
import org.cts2.core.Filter;
import org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler;
import org.cts2.internal.model.uri.restrict.Restriction;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;

/**
 * The Class AbstractResolvingDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractNonIterableLexEvsBackedResolvingDirectoryURI<L,T extends DirectoryURI> extends AbstractResolvingDirectoryURI<T> {
	
	/** The restriction handler. */
	private NonIterableBasedResolvingRestrictionHandler<L> restrictionHandler;
	
	/**
	 * Instantiates a new abstract non iterable lex evs backed resolving directory uri.
	 *
	 * @param restrictionHandler the restriction handler
	 */
	protected AbstractNonIterableLexEvsBackedResolvingDirectoryURI(NonIterableBasedResolvingRestrictionHandler<L> restrictionHandler){
		this.restrictionHandler = restrictionHandler;
	}
	
	/** The restrictions. */
	private List<Restriction<L>> restrictions = new ArrayList<Restriction<L>>();
	
	/**
	 * Adds the restriction.
	 *
	 * @param restriction the restriction
	 */
	protected void addRestriction(Restriction<L> restriction){
		this.restrictions.add(restriction);
	}
	
	/**
	 * Run restrictions.
	 *
	 * @param state the state
	 * @return the l
	 */
	protected L runRestrictions(L state){
		for(Restriction<L> restriction : this.restrictions){
			state = restriction.processRestriction(state);
		}
		
		return state;
	}
	
	/**
	 * Gets the original state.
	 *
	 * @return the original state
	 */
	protected abstract L getOriginalState();
	
	/**
	 * Transform.
	 *
	 * @param <O> the
	 * @param lexevsObject the lexevs object
	 * @param clazz the clazz
	 * @return the o
	 */
	protected abstract <O> O transform(L lexevsObject, Class<O> clazz);
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractDirectoryURI#restrict(org.cts2.core.Filter)
	 */
	@Override
	public T restrict(Filter filter) {

		this.restrictions.add(
				this.restrictionHandler.restrict(filter));
				
		return this.clone();
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractResolvingDirectoryURI#doGet(org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext, java.lang.Class)
	 */
	@Override
	protected <D extends Directory<?>> D doGet(
			QueryControl queryControl, 
			final ReadContext readContext, 
			final Class<D> resolveClass) {
		
		L originalState = makeOriginalStateUnmodifiable(this.getOriginalState());
		
		L restrictedState = this.runRestrictions(originalState);
		
		return this.transform(restrictedState, resolveClass);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected abstract T clone();
	
	/**
	 * Make original state unmodifiable.
	 *
	 * @param originalState the original state
	 * @return the l
	 */
	protected L makeOriginalStateUnmodifiable(L originalState){
		return originalState;
	}
	
	/**
	 * Gets the restriction handler.
	 *
	 * @return the restriction handler
	 */
	protected NonIterableBasedResolvingRestrictionHandler<L> getRestrictionHandler(){
		return this.restrictionHandler;
	}
}
