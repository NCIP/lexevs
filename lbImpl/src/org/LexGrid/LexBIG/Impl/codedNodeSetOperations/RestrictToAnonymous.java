
package org.LexGrid.LexBIG.Impl.codedNodeSetOperations;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.AbstractJoinQueryRestriction;
import org.LexGrid.LexBIG.Impl.dataAccess.RestrictionImplementations;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.AnonymousOption;
import org.apache.lucene.search.Query;
import org.lexevs.exceptions.InternalException;

/**
 * Holder for the RestrictToStatus operation.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToAnonymous extends AbstractJoinQueryRestriction {

    private static final long serialVersionUID = 4030470756164999491L;
    private AnonymousOption anonymousOption;

    public RestrictToAnonymous(AnonymousOption anonymousOption) throws LBParameterException {
        if (anonymousOption == null) {
            throw new LBParameterException("You must provide at least one parameter", "anonymousOption");
        }
        this.anonymousOption = anonymousOption;
    }

    @Override
    protected Query doGetQuery() throws LBException, InternalException {
        return RestrictionImplementations.getQuery(this);
    }

    public AnonymousOption getAnonymousOption() {
        return anonymousOption;
    }

    public void setAnonymousOption(AnonymousOption anonymousOption) {
        this.anonymousOption = anonymousOption;
    }
}