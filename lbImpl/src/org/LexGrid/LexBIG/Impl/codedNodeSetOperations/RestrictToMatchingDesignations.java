/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.codedNodeSetOperations;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.Restriction;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLImplementedMethods;
import org.LexGrid.LexBIG.Impl.internalExceptions.InternalException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.annotations.LgClientSideSafe;
import org.apache.lucene.search.Query;

/**
 * Holder for the RestrictToMatchingDesignations operation.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToMatchingDesignations implements Restriction, Operation {

    private static final long serialVersionUID = 202284486636220340L;
    private Query textQuery_;
    private SearchDesignationOption preferredOnly_;
    private String language_;

    public RestrictToMatchingDesignations(String matchText, SearchDesignationOption preferredOnly,
            String matchAlgorithm, String language, String internalCodeSystemName, String internalVersionString)
            throws LBInvocationException, LBParameterException {
        try {
            // this validates the match text and match algorithm (throws
            // exceptions as necessary)
 
            Search search = ExtensionRegistryImpl.instance().getSearchAlgorithm(matchAlgorithm);
            textQuery_ = search.buildQuery(matchText);

            preferredOnly_ = preferredOnly;

            if (language != null && language.length() > 0) {
                // this validated that language (throws exceptions as necessary)
                SQLImplementedMethods.validateLanguage(internalCodeSystemName, internalVersionString, language);
            }

            language_ = language;
        } catch (LBParameterException e) {
            throw e;
        } catch (InternalException e) {
            throw new LBInvocationException("There was an unexpected error while validating the language.", e
                    .getLogId());
        }
    }

    @LgClientSideSafe
    public String getLanguage() {
        return this.language_;
    }

    @LgClientSideSafe
    public Query getTextQuery() {
         return textQuery_;
    }

    @LgClientSideSafe
    public SearchDesignationOption getPreferredOption() {
        return this.preferredOnly_;
    }

}