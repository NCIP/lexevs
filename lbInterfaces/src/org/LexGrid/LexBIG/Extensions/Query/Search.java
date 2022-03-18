
package org.LexGrid.LexBIG.Extensions.Query;

import org.LexGrid.LexBIG.Extensions.Extendable;
import org.apache.lucene.search.Query;

public interface Search extends Extendable {

/**
	 * Generate a Lucene Query given a text String.
	 * 
	 * @param searchText
	 * 		The text to search
	 * @return
	 * 		The Query
	 */
public Query buildQuery(String searchText);
}