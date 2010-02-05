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
package org.LexGrid.LexBIG.Utility.Iterators;

import java.io.Serializable;

import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;

/**
 * @created 17-Jan-2006 3:02:47 PM
 * @author Harold Solbrig
 * @version 1.0
 */
public interface EntityListIterator extends Serializable {

	/**
	 * True means that there still values that can be returned. False means all
	 * values have already been returned.  
     * 
     * This method only applies to calls to next() and next(int) - it does not 
     * have any bearing on calls to get(int, int).
	 */
	public boolean hasNext() throws LBResourceUnavailableException;

	/**
	 * Release the iterator. Iterators can have a fixed lifespan as determined
	 * by the service, but this allows the iterator to be returned gracefully to
	 * the system.
	 */
	public void release() throws LBResourceUnavailableException;
    
    /**
     * Returns a count of the number of items remaining in the iterator.
     * This is optional, and may only be an estimate.  Implementations should 
     * return -1 if they cannot count or estimate the number remaining.
     * 
     * Note: This method is not affected by calls to get(int, int).  It only
     * returns counts with respect to calls to next() or next(int).
     * 
     * @see hasNext() for an authoritative answer whether or not the iterator 
     * has returned all possible items.
     */
    public int numberRemaining() throws LBResourceUnavailableException;

}