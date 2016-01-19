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
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Search;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.IndexQueryParserFactory;
import org.LexGrid.LexBIG.Impl.dataAccess.RestrictionImplementations;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.indexer.lucene.query.SerializableRegexQuery;
import org.lexevs.exceptions.InternalException;
import org.lexevs.locator.LexEvsServiceLocator;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Holder for the RestrictToMatchingProperties operation.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToMatchingProperties extends RestrictToProperties {

    private static final long serialVersionUID = -6595704213491369563L;
    private BooleanQuery textQuery_;
    private List<Term> queryTerms_ = new ArrayList<Term>();
    private String language_;
    
    @LgClientSideSafe
    public String getLanguage() {
        return this.language_;
    }

    @LgClientSideSafe
    public Query getTextQuery() {
        // Until the RegexQuery is completely serializable this cannot be done
        // until the code is on the client side
        for (int i = 0; i < queryTerms_.size(); i++) {
            textQuery_.add(new BooleanClause(new SerializableRegexQuery(queryTerms_.get(i)), Occur.MUST));
        }
        return textQuery_;
    }

    public RestrictToMatchingProperties(LocalNameList propertyList, PropertyType[] propertyTypes,
            LocalNameList sourceList, LocalNameList contextList, NameAndValueList qualifierList, String matchText,
            String matchAlgorithm, String language, String internalCodeSystemName, String internalVersionString)
            throws LBInvocationException, LBParameterException {
        super(sourceList, contextList, qualifierList, internalCodeSystemName, internalVersionString);
        try {
           
            String conceptCodeField = SQLTableConstants.TBLCOL_ENTITYCODE;
            String conceptCodeLCField = conceptCodeField + "LC";
            String conceptCodeTokenizedField = conceptCodeField + "Tokenized";

            // this is slightly broken - because I really can't validate the
            // properties since
            // they could be valid in a unioned code system, but invalid here.
            // But - it wouldn't really
            // make any sense to do that union anyway, since this code system
            // won't return any results
            // if the property is invalid here.
            if ((propertyList == null || propertyList.getEntryCount() == 0)
                    && (propertyTypes == null || propertyTypes.length == 0)) {
                throw new LBParameterException(
                        "At least one propertyList or one propertyType parameter must be supplied.",
                        "propertyList or propertyType");
            }

            propertyList_ = new LocalNameList();
            textQuery_ = new BooleanQuery();
            boolean containsConceptClause = false;

            if (propertyList != null) {
                Enumeration<? extends String> items = propertyList.enumerateEntry();
                while (items.hasMoreElements()) {
                    String item = items.nextElement();
                    if (item.equalsIgnoreCase("conceptCode")) {
                        Query temp = null;
                        if (matchAlgorithm.equalsIgnoreCase("exactMatch")) {
                            temp = new TermQuery(new Term(conceptCodeField, matchText));
                        } else if (matchAlgorithm.equalsIgnoreCase("contains")) {
                            // Emulate contains using a regular expression
                            // match.
                            // The 'contains' algorithm is defined as being
                            // Equivalent
                            // to '* term* *'. In other words, a trailing
                            // wildcard on
                            // a term (but no leading wild card) and the term
                            // can
                            // appear at any position.
                            matchAlgorithm = "RegExp";
                            queryTerms_.add(new Term(conceptCodeLCField, new StringBuffer().append("\\b*").append(
                                    Pattern.quote(matchText.toLowerCase())).append(".*").toString()));
                        } else if (matchAlgorithm.equalsIgnoreCase("LuceneQuery")) {
                            IndexQueryParserFactory queryParserFactory = new IndexQueryParserFactory();
                            
                            temp = queryParserFactory.parseQueryForField(
                                    conceptCodeTokenizedField, matchText);
                        } else if (matchAlgorithm.equalsIgnoreCase("RegExp")) {
                            queryTerms_.add(new Term(conceptCodeLCField, matchText.toLowerCase()));
                        } else {
                            throw new LBParameterException("The match algorithm '" + matchAlgorithm
                                    + "' is not supported on a 'conceptCode' search.");
                        }
                        if (temp != null) {
                            // If mixed with other properties, do not make this
                            // check exclusive (GForge #15015).
                            textQuery_.add(new BooleanClause(temp, propertyList.getEntryCount() > 1 ? Occur.SHOULD
                                    : Occur.MUST));
                            containsConceptClause = true;
                        }
                    } else if (item.equalsIgnoreCase(SQLTableConstants.TBLCOL_CONCEPTSTATUS)) {
                        throw new LBParameterException(
                                "'"
                                        + SQLTableConstants.TBLCOL_CONCEPTSTATUS
                                        + "' is no longer supported in this restriction - please use 'RestrictToStatus' instead");
                    } else {
                        
                        if(internalCodeSystemName!= null && internalVersionString != null) {
                            String uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(internalCodeSystemName, internalVersionString);
                            // this will throw the necessary exceptions
                            if(!LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService().
                                    validatedSupportedAttribute(uri, internalVersionString, item, SupportedProperty.class)) {
                                throw new LBParameterException("Property: " + item + " is not a Supported Property.");
                            }
                        }
                        
                        propertyList_.addEntry(item);
                    }
                }
            }

            propertyTypes_ = propertyTypes;

            // this may be empty, because the special cases above don't get
            // added to the local property
            // list.
            if (propertyList_.getEntryCount() > 0 || (propertyTypes_ != null && propertyTypes_.length > 0)) {
                // this validates the match text and match algorithm (throws
                // exceptions as necessary)

                Search search = ExtensionRegistryImpl.instance().getSearchAlgorithm(matchAlgorithm);
                Query query = search.buildQuery(matchText);

                textQuery_.add(new BooleanClause(query, containsConceptClause ? Occur.SHOULD : Occur.MUST));
            }

            if (language != null && language.length() > 0) {
                // this validated that language (throws exceptions as necessary)
                String uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(internalCodeSystemName, internalVersionString);
                if(!LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService().
                        validatedSupportedAttribute(uri, internalVersionString, language, SupportedLanguage.class)) {
                        throw new LBParameterException(language = " is not a Supported Language.");
                    }
                language_ = language;
            }
        } catch (LBParameterException e) {
            throw e;
        }
    }


    @Override
    public Query doGetQuery() throws LBException, InternalException {
        return RestrictionImplementations.getQuery(this);
    }
}