/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Extensions.Query;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Extensions.Extendable;

/**
 * Allows for additional filtering of query results.
 */
public interface Filter extends Extendable {
	
	/**
	 * Indicates whether or not the given reference satisfies criteria
	 * for this filter.
     * 
     * Filters should _NOT_ be used for search criteria that can be 
     * done with built in restrictions.  Filters are very inefficient.
     * 
	 * @param ref The concept reference to evaluate.
	 * @return true if the reference is to be included in returned results;
	 * otherwise false.
	 */
	boolean match(ResolvedConceptReference ref);
}