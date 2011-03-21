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

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.cts2.internal.model.uri.restrict.IterableBasedResolvingRestrictionHandler;
import org.cts2.internal.model.uri.restrict.Restriction;
import org.cts2.uri.DirectoryURI;

/**
 * The Class AbstractLexEvsBackedResolvingDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLexEvsBackedResolvingDirectoryURI<L,T extends DirectoryURI> extends AbstractDirectoryURI<T> {
	
	/** The restriction handler. */
	IterableBasedResolvingRestrictionHandler<CodingSchemeRendering> restrictionHandler;
	
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
}
