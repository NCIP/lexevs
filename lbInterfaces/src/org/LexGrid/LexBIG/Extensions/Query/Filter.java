
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