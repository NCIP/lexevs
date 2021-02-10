
package org.lexevs.dao.database.sqlimplementedmethods.codednodegraph.model;

import java.util.ArrayList;

/**
 * Utility class that helps in putting together the query for graph builiding.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class GraphQuestionQuery {
    
    /** The where clause_. */
    public StringBuffer whereClause_;
    
    /** The qualifer table required_. */
    public boolean qualiferTableRequired_ = false;
    
    /** The parameters_. */
    public ArrayList<String> parameters_;
}