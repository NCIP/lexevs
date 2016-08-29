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

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.AbstractJoinQueryRestriction;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

/**
 * Holder for the RestrictToEntityTypes operation.
 */
public class RestrictToEntityTypes extends AbstractJoinQueryRestriction {

    private static final long serialVersionUID = -2027504567305925339L;
    private String[] typeList_;

    @LgClientSideSafe
    public String[] getTypeList() {
        return this.typeList_;
    }

    public RestrictToEntityTypes(LocalNameList typeList) throws LBParameterException {
        typeList_ = 
            (typeList == null) ? new String[0]
                : typeList.getEntry();
    }

    public RestrictToEntityTypes(String[] typeList) throws LBParameterException {
        typeList_ =
            (typeList == null) ? new String[0]
                : typeList;
    }

    @Override
    protected Query doGetQuery() {
        BooleanQuery.Builder nestedQuery = new BooleanQuery.Builder();
        nestedQuery.setMinimumNumberShouldMatch(1);

        for (int i = 0; i < typeList_.length; i++) {
            nestedQuery.add(new BooleanClause(new TermQuery(new Term(SQLTableConstants.TBLCOL_ENTITYTYPE,
                    typeList_[i])), BooleanClause.Occur.SHOULD));
        }
        return nestedQuery.build();
    }
}