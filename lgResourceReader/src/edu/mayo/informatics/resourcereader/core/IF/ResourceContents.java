
package edu.mayo.informatics.resourcereader.core.IF;

import java.util.Collection;

/**
 * Interface for ResourceContents
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public interface ResourceContents {
    public Collection getConcepts();

    public Collection getRelations();
}