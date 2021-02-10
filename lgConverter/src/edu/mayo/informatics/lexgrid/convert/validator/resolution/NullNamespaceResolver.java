
package edu.mayo.informatics.lexgrid.convert.validator.resolution;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.lexevs.dao.database.utility.DaoUtility;

import edu.mayo.informatics.lexgrid.convert.validator.error.NullNamespaceError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport.ResolutionStatus;

/**
 * The Class NullNamespaceResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NullNamespaceResolver extends AbstractResolver<Object> {

    /** The default namespace. */
    private String defaultNamespace;
    
    /**
     * Instantiates a new null namespace resolver.
     * 
     * @param codingScheme the coding scheme
     */
    public NullNamespaceResolver(CodingScheme codingScheme) {
        this.defaultNamespace = getDefaultNamespaceFromCodingScheme(codingScheme);
    }

    /**
     * Gets the default namespace from coding scheme.
     * 
     * @param codingScheme the coding scheme
     * 
     * @return the default namespace from coding scheme
     */
    protected String getDefaultNamespaceFromCodingScheme(CodingScheme codingScheme) {
        return codingScheme.getCodingSchemeName();
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.resolution.AbstractResolver#doGetValidErrorCodes()
     */
    @Override
    protected List<String> doGetValidErrorCodes() {
        return
            DaoUtility.createList(String.class, NullNamespaceError.NULL_NAMESPACE_CODE);
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.resolution.AbstractResolver#doResolveError(java.lang.Object)
     */
    @Override
    public ResolutionStatus doResolveError(Object errorObject) {
        if(errorObject instanceof Entity) {
            ((Entity)errorObject).setEntityCodeNamespace(this.defaultNamespace);
            return ResolutionStatus.RESOLVED;
        } else if(errorObject instanceof AssociationSource) {
            ((AssociationSource)errorObject).setSourceEntityCodeNamespace(this.defaultNamespace);
            return ResolutionStatus.RESOLVED;
        } else if(errorObject instanceof AssociationTarget) {
            ((AssociationTarget)errorObject).setTargetEntityCodeNamespace(this.defaultNamespace);
            return ResolutionStatus.RESOLVED;
        } else {
            return ResolutionStatus.NOT_ADDRESSED;
        }
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.resolution.AbstractResolver#getResolutionDetails()
     */
    @Override
    public String getResolutionDetails() {
        return "Namespace set to default: " + this.defaultNamespace;
    }
}