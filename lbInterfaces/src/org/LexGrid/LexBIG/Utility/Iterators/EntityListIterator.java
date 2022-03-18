
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