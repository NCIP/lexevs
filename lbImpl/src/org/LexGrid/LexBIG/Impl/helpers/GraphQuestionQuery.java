
package org.LexGrid.LexBIG.Impl.helpers;

import java.util.ArrayList;

/**
 * Utility class that helps in putting together the query for graph builiding.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class GraphQuestionQuery {
    public StringBuffer whereClause_;
    public boolean qualiferTableRequired_ = false;
    public ArrayList<String> parameters_;
}