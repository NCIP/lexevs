
package org.LexGrid.LexBIG.Impl.codedNodeSetOperations;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.AbstractJoinQueryRestriction;
import org.LexGrid.LexBIG.Impl.dataAccess.RestrictionImplementations;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.annotations.LgClientSideSafe;
import org.apache.lucene.search.Query;
import org.lexevs.exceptions.InternalException;

/**
 * Holder for the RestrictToStatus operation.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToStatus extends AbstractJoinQueryRestriction {

    private static final long serialVersionUID = 4030470756164999491L;
    private ActiveOption activeOption_;
    private String[] status_;

    public RestrictToStatus(ActiveOption activeOption, String[] status) throws LBParameterException {
        if (activeOption == null && (status == null || status.length == 0)) {
            throw new LBParameterException("You must provide at least one parameter", "activeOption or status");
        }
        this.activeOption_ = activeOption;
        this.status_ = status;
    }

    /**
     * @return the activeOption_
     */
    @LgClientSideSafe
    public ActiveOption getActiveOption() {
        return this.activeOption_;
    }

    @Override
    protected Query doGetQuery() throws LBException, InternalException {
        return RestrictionImplementations.getQuery(this);
    }

    /**
     * @return the status_
     */
    @LgClientSideSafe
    public String[] getStatus() {
        return this.status_;
    }
}