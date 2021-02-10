
package org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.apache.lucene.search.Query;
import org.lexevs.exceptions.InternalException;

import java.io.Serializable;

/**
 * Interface for all operation holders that are Restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public interface Restriction extends Serializable {

    public Query getQuery() throws LBException, InternalException;

}