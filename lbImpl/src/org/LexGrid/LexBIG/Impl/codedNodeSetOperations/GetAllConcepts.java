/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.Restriction;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgClientSideSafe;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.service.SystemResourceService;

/**
 * Holder for the GetAllConcepts operation.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class GetAllConcepts implements Restriction {

    private static final long serialVersionUID = -1190549389939991576L;
    private String internalCodingSchemeName_;
    private String internalVersion_;

    public GetAllConcepts(String codingScheme, CodingSchemeVersionOrTag tagOrVersion) throws LBParameterException,
            LBResourceUnavailableException {
        
        String version = null;
        SystemResourceService rm = LexEvsServiceLocator.getInstance().getSystemResourceService();
        Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
        
        
        if (tagOrVersion == null || tagOrVersion.getVersion() == null || tagOrVersion.getVersion().length() == 0) {
            version = rm.getInternalVersionStringForTag(codingScheme,
                    (tagOrVersion == null ? null : tagOrVersion.getTag()));
        } else {
            version = tagOrVersion.getVersion();
        }

        // this throws the necessary exceptions if it can't be mapped / found
        rm.getInternalCodingSchemeNameForUserCodingSchemeName(codingScheme, version);

        // Assign the URI passed in as the coding scheme
        internalCodingSchemeName_ = codingScheme;
        
        // make sure that it is active.
        String urn = rm.getUriForUserCodingSchemeName(internalCodingSchemeName_, version);
        if (!registry.getCodingSchemeEntry(
                Constructors.createAbsoluteCodingSchemeVersionReference(urn, version)).getStatus().
                    equals(CodingSchemeVersionStatus.ACTIVE.toString())) {
            throw new LBResourceUnavailableException("The requested coding scheme is not currently active");
        }

        internalVersion_ = version;

    }

    public GetAllConcepts(String internalCodingSchemeName, String internalVersionString) {
        // this throws the necessary exceptions if it can't be mapped / found
        internalCodingSchemeName_ = internalCodingSchemeName;
        internalVersion_ = internalVersionString;

    }

    @Override
    public Query getQuery() {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(new TermQuery(new Term("codingSchemeUri", this.internalCodingSchemeName_)), BooleanClause.Occur.MUST);
        builder.add(new TermQuery(new Term("codingSchemeVersion", this.internalVersion_)), BooleanClause.Occur.MUST);

        return builder.build();
    }

    @LgClientSideSafe
    public String getInternalCodingSchemeName() {
        return this.internalCodingSchemeName_;
    }

    @LgClientSideSafe
    public String getInternalVersionString() {
        return this.internalVersion_;
    }

}