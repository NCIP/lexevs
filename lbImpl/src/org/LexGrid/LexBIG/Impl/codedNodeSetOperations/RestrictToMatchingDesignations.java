
package org.LexGrid.LexBIG.Impl.codedNodeSetOperations;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.AbstractJoinQueryRestriction;
import org.LexGrid.LexBIG.Impl.dataAccess.RestrictionImplementations;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.naming.SupportedLanguage;
import org.apache.lucene.search.Query;
import org.lexevs.exceptions.InternalException;

/**
 * Holder for the RestrictToMatchingDesignations operation.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToMatchingDesignations extends AbstractJoinQueryRestriction {

    private static final long serialVersionUID = 202284486636220340L;
    private String matchText_;
    private Search search_;
    private SearchDesignationOption preferredOnly_;
    private String language_;
    
    public RestrictToMatchingDesignations(String matchText, SearchDesignationOption preferredOnly,
            String matchAlgorithm, String language) throws LBInvocationException, LBParameterException {
        this(matchText, preferredOnly, matchAlgorithm, language, null, null);
    }

    public RestrictToMatchingDesignations(String matchText, SearchDesignationOption preferredOnly,
            String matchAlgorithm, String language, String internalCodeSystemName, String internalVersionString)
            throws LBInvocationException, LBParameterException {
        try {
            // this validates the match text and match algorithm (throws
            // exceptions as necessary)
 
        	search_ = ((ExtensionRegistryImpl)LexBIGServiceImpl.defaultInstance().getServiceManager(null).getExtensionRegistry()).getSearchAlgorithm(matchAlgorithm);

            preferredOnly_ = preferredOnly;
            matchText_ = matchText;
            
            boolean canValidate = internalCodeSystemName != null && internalVersionString != null;

            if (canValidate && language != null && language.length() > 0) {
                // this validated that language (throws exceptions as necessary)
                ServiceUtility.validateParameter(internalCodeSystemName, internalVersionString, language, SupportedLanguage.class);
            }

            language_ = language;
        } catch (LBParameterException e) {
            throw new LBInvocationException("There was an unexpected error while validating the language.", e.getLocalizedMessage());
        }
    }

    @Override
    protected Query doGetQuery() throws LBException, InternalException {
        return RestrictionImplementations.getQuery(this);
    }

    @LgClientSideSafe
    public String getLanguage() {
        return this.language_;
    }

    @LgClientSideSafe
    public Query getTextQuery() {
         return search_.buildQuery(matchText_);
    }

    @LgClientSideSafe
    public SearchDesignationOption getPreferredOption() {
        return this.preferredOnly_;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((language_ == null) ? 0 : language_.hashCode());
        result = prime * result + ((preferredOnly_ == null) ? 0 : preferredOnly_.hashCode());
        result = prime * result + ((matchText_ == null) ? 0 : matchText_.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RestrictToMatchingDesignations other = (RestrictToMatchingDesignations) obj;
        if (language_ == null) {
            if (other.language_ != null)
                return false;
        } else if (!language_.equals(other.language_))
            return false;
        if (preferredOnly_ == null) {
            if (other.preferredOnly_ != null)
                return false;
        } else if (!preferredOnly_.equals(other.preferredOnly_))
            return false;
        if (matchText_ == null) {
            if (other.matchText_ != null)
                return false;
        } else if (!matchText_.equals(other.matchText_))
            return false;
        return true;
    }
}