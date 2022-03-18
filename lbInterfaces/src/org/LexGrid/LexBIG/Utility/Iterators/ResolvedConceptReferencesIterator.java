
package org.LexGrid.LexBIG.Utility.Iterators;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;

/**
 * An iterator for retrieving Resolved coding scheme references
 * 
 * @created 17-Jan-2006 3:02:49 PM
 * @author solbrigcvs
 * @version 1.0
 */
public interface ResolvedConceptReferencesIterator extends EntityListIterator {

	/**
	 * Return the next available concept reference, in order, from the underlying list.
	 * <p>
	 * Note: This is effectively the same as performing next(1), but eliminates the
	 * need to continually unwrap the concept reference from the resolved list.
     * 
     * Note: This method is not affected by calls to get(int, int).
	 */
	public ResolvedConceptReference next()
			throws LBResourceUnavailableException, LBInvocationException;

	/**
	 * Return concept references from the underlying list, in order and up to
	 * the specified maximum.
     * 
     * Note: This method is not affected by calls to get(int, int).
	 * 
	 * @param maxToReturn
	 *            The maximum number of entries to return. Note that the
	 *            interface may return less than the supplied number of entries
	 *            even if there are still more to come. -1 means return a
	 *            "natural" block size, potentially up to the maximum amount
	 *            available, as determined by the service software. 0 returns an
	 *            empty list and can be used to keep the iterator "alive".
	 */
	public ResolvedConceptReferenceList next(int maxToReturn)
			throws LBResourceUnavailableException, LBInvocationException;
    
    /**
     * Return concept references from the underlying list, from the start point (inclusive)
     * to the end point (exclusive).  
     * 
     * Calling this method has no effect on the next() calls - next() will still iterate
     * through the results in the same sequential order regardless of if this method has
     * been used.
     * 
     * the result of numberRemaining() is not affected by calls to this method.   
     * 
     * Note: This method may return fewer results than requested  even if there are still 
     * more results after the last result returned by the service.
     * 
     * Note: The method is optional, and may not be implemented by all implementations.
     * 
     * Note: Using this method in combination with Filters may cause significant performance 
     * reductions.
     * 
     * @param start
     *          The start point of the range of results to return - inclusive.  The entries
     *          list is 0 indexed - so to get the first result, you should start with '0'.
     *          LBParameterException is thrown if the start position is > the last item 
     *          available.
     * @param end
     *          The end point of the range of results to return - exclusive.  A range request 
     *          of (0, 50) will return 50 items (0 through 49 in an array)
     */
    public ResolvedConceptReferenceList get(int start, int end)
        throws LBResourceUnavailableException, LBInvocationException, LBParameterException;
    
    /**
     * Return the next batch of resolved concept references.
     * 
     * @param maxToReturn 
     * 		The maximum number of remaining concepts to return
     * 
     * @return
     * @throws LBResourceUnavailableException
     * @throws LBInvocationException
     */
    public ResolvedConceptReferencesIterator scroll(int maxToReturn)
    	throws LBResourceUnavailableException, LBInvocationException ;

    /**
     * Return a list containing the items specified in the last scroll call.
     * @return
     */
    public ResolvedConceptReferenceList getNext();

}