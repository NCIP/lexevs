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
package org.LexGrid.LexBIG.Impl.dataAccess;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToAnonymous;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToCodes;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToEntityTypes;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToMatchingDesignations;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToMatchingProperties;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToProperties;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.RestrictToStatus;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.Restriction;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.AnonymousOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.logging.LoggerFactory;

import java.util.ArrayList;

/**
 * 
 * Legacy documentation below:  Currently we are using Lucene 5.3.1 which allows 
 * us to use the toParentBlockJoint mechanism to group related entity properties.
 * 
 * this was finessed to some extent by creating a boolean query over to parent block
 * join queries for each term insuring that parent docs returned contained both 
 * terms when the restrictions are "anded"
 * 
 * ***legacy docuementation of the bitset implementation*******
 * 
 * Class which implements all of the restriction operations using Lucene
 * searches.
 * 
 * Implementation details - The restrictions work by taking advantage of the
 * fact that lucene searches natively return bitsets - one bit for every
 * document in the index. True for yes, return this document, false for do not
 * return this document. When you do successive searches, you can and the bit
 * sets together to get the merged view of the queries.
 * 
 * Its not quite that simple in our case, however, since our indexes have one
 * document per presentation. So each concept code will have multiple lucene
 * "documents" - and since each document gets its own bit in the bitset - if we
 * match a presentation in search one for Concept A, and then match a different
 * presentation in search 2 for Concept A, and join the bit sets, it won't
 * return any trues for Concept A.
 * 
 * So, after each search, for any concept code which has a bit set to true, we
 * need to set all bits for that concept code to true.
 * 
 * Now, I take advantage of the fact that the documents are added to Lucene in
 * order, and all concept codes are grouped together. The codeBoundyFilter is a
 * special bitset which has a bit set to true for each boundry between concepts.
 * So we can use the codeBoundry filter to rapidly set all of the bits for a
 * concept to true.
 * 
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictionImplementations {   
    protected static LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public static BooleanQuery getQuery(Restriction restriction) throws UnexpectedInternalError,
            MissingResourceException, LBParameterException {
        try {             
           
            String codeField = "code";
            String propertyNameField = SQLTableConstants.TBLCOL_PROPERTYNAME;
            String language = null;
            Query textQueryPart = null;
            SearchDesignationOption preferredOnly = null;
            ArrayList<String> propertyName = null;
            ArrayList<String> sources = null;
            ArrayList<String> usageContexts = null;
            NameAndValueList propertyQualifiers = null;
            ConceptReferenceList crl = null;
            PropertyType[] propertyType = null;
            String[] conceptStatus = null;
            ActiveOption activeOption = null;
            AnonymousOption anonymousOption = null;
            String[] entityTypes = null;

            if (restriction instanceof RestrictToMatchingDesignations) {
                RestrictToMatchingDesignations temp = (RestrictToMatchingDesignations) restriction;
                language = temp.getLanguage();
                textQueryPart = temp.getTextQuery();
                propertyType = new PropertyType[] { PropertyType.PRESENTATION };
                preferredOnly = temp.getPreferredOption();
                
            } else if (restriction instanceof RestrictToMatchingProperties) {
                RestrictToMatchingProperties temp = (RestrictToMatchingProperties) restriction;
                language = temp.getLanguage();
                textQueryPart = temp.getTextQuery();
                propertyName = localNameListToArrayList(temp.getPropertyList());
                propertyType = temp.getPropertyTypes();
                sources = localNameListToArrayList(temp.getSourceList());
                usageContexts = localNameListToArrayList(temp.getContextList());
                propertyQualifiers = temp.getQualifierList();

            } else if (restriction instanceof RestrictToProperties) {
                RestrictToProperties temp = (RestrictToProperties) restriction;
                propertyName = localNameListToArrayList(temp.getPropertyList());
                propertyType = temp.getPropertyTypes();
                sources = localNameListToArrayList(temp.getSourceList());
                usageContexts = localNameListToArrayList(temp.getContextList());
                propertyQualifiers = temp.getQualifierList();
                
            } else if (restriction instanceof RestrictToCodes) {
                RestrictToCodes temp = (RestrictToCodes) restriction;
                crl = temp.getConceptReferenceList();
                
            } else if (restriction instanceof RestrictToStatus) {
                RestrictToStatus temp = (RestrictToStatus) restriction;
                activeOption = temp.getActiveOption();
                conceptStatus = temp.getStatus();
                
            } else if (restriction instanceof RestrictToAnonymous) {
                RestrictToAnonymous temp = (RestrictToAnonymous) restriction;
                anonymousOption = temp.getAnonymousOption();
                
            } else if (restriction instanceof RestrictToEntityTypes) {
                RestrictToEntityTypes temp = (RestrictToEntityTypes) restriction;
                entityTypes = temp.getTypeList();
                
            } else {
                throw new UnexpectedInternalError("An unsupported restriction type was provided.  The type was "
                        + restriction);
            }
            
            BooleanQuery.Builder masterQuery = new BooleanQuery.Builder();

            if (textQueryPart != null) {
                masterQuery.add(new BooleanClause(textQueryPart, Occur.MUST));
            }

            if (language != null && language.length() > 0) {
                masterQuery.add(new BooleanClause(new TermQuery(new Term(SQLTableConstants.TBLCOL_LANGUAGE, language)),
                        Occur.MUST));
            }

            if (activeOption != null) {
                if (activeOption.equals(ActiveOption.ACTIVE_ONLY)) {
                    // This is a MUST_NOT query - do not set addedSomething to true
                    //Remove the MUST_NOT to make this work with BlockJoin.
                    masterQuery.add(new BooleanClause(new TermQuery(new Term(SQLTableConstants.TBLCOL_ISACTIVE, "T")),
                            Occur.MUST));
                } else if (activeOption.equals(ActiveOption.INACTIVE_ONLY)) {
                    masterQuery.add(new BooleanClause(new TermQuery(new Term(SQLTableConstants.TBLCOL_ISACTIVE, "F")),
                            Occur.MUST));
                } else if (activeOption.equals(ActiveOption.ALL)) {
                    BooleanQuery.Builder builder = new BooleanQuery.Builder();
                    builder.add(new TermQuery(new Term(SQLTableConstants.TBLCOL_ISACTIVE, "T")),
                            Occur.SHOULD);
                    builder.add(new TermQuery(new Term(SQLTableConstants.TBLCOL_ISACTIVE, "F")),
                            Occur.SHOULD);


                    masterQuery.add(builder.build(), Occur.MUST);
                }
            }

            if (anonymousOption != null) {
                if (anonymousOption.equals(AnonymousOption.ANONYMOUS_ONLY)) {
                    masterQuery.add(new BooleanClause(new TermQuery(new Term("isAnonymous", "T")),
                            Occur.MUST));
                } else if (anonymousOption.equals(AnonymousOption.NON_ANONYMOUS_ONLY)) {
                    masterQuery.add(new BooleanClause(new TermQuery(new Term("isAnonymous", "F")),
                            Occur.MUST));
                }
            }
            
            if (conceptStatus != null && conceptStatus.length > 0) {
                BooleanQuery.Builder nestedQuery = new BooleanQuery.Builder();
                nestedQuery.setMinimumNumberShouldMatch(1);

                for (int i = 0; i < conceptStatus.length; i++) {
                    nestedQuery.add(new BooleanClause(new TermQuery(new Term(SQLTableConstants.TBLCOL_CONCEPTSTATUS,
                            conceptStatus[i])), Occur.SHOULD));
                }
                masterQuery.add(nestedQuery.build(), Occur.MUST);
            }

            if (preferredOnly != null) {
                if (preferredOnly.name().equals(SearchDesignationOption.PREFERRED_ONLY.name())) {
                    masterQuery.add(new BooleanClause(
                            new TermQuery(new Term(SQLTableConstants.TBLCOL_ISPREFERRED, "T")), Occur.MUST));
                } else if (preferredOnly.name().equals(SearchDesignationOption.NON_PREFERRED_ONLY.name())) {
                    masterQuery.add(new BooleanClause(
                            new TermQuery(new Term(SQLTableConstants.TBLCOL_ISPREFERRED, "T")), Occur.MUST_NOT));
                }
                // else it must be all, so I don't need to add anything to the
                // query for that.
            }

            if (propertyName != null && propertyName.size() > 0) {
                int propertiesSize = propertyName.size();
                BooleanQuery.Builder nestedQuery = new BooleanQuery.Builder();
                nestedQuery.setMinimumNumberShouldMatch(1);

                for (int i = 0; i < propertiesSize; i++) {
                    nestedQuery.add(new BooleanClause(new TermQuery(new Term(propertyNameField, propertyName.get(i))),
                            Occur.SHOULD));
                }
                masterQuery.add(nestedQuery.build(), Occur.MUST);
            }

            // propertyType
            if (propertyType != null && propertyType.length > 0) {
                BooleanQuery.Builder nestedQuery = new BooleanQuery.Builder();
                nestedQuery.setMinimumNumberShouldMatch(1);

                for (int i = 0; i < propertyType.length; i++) {
                    nestedQuery.add(new BooleanClause(new TermQuery(new Term("propertyType",
                            mapPropertyType(propertyType[i]))), Occur.SHOULD));
                }

                masterQuery.add(nestedQuery.build(), Occur.MUST);
            }

            // sources
            if (sources != null && sources.size() > 0) {
                BooleanQuery.Builder nestedQuery = new BooleanQuery.Builder();
                nestedQuery.setMinimumNumberShouldMatch(1);

                for (int i = 0; i < sources.size(); i++) {
                    String name = sources.get(i);
                    QueryParser parser = new QueryParser("sources",LuceneLoaderCode.getAnaylzer());
                    Query nameOnly = parser.parse(name
                            + "*");
                    nestedQuery
                            .add(new BooleanClause(nameOnly, Occur.SHOULD));
                }
                masterQuery.add(nestedQuery.build(), Occur.MUST);
            }

            // usage contexts
            if (usageContexts != null && usageContexts.size() > 0) {
                BooleanQuery.Builder nestedQuery = new BooleanQuery.Builder();
                nestedQuery.setMinimumNumberShouldMatch(1);

                for (int i = 0; i < usageContexts.size(); i++) {
                    nestedQuery.add(new BooleanClause(new TermQuery(new Term("usageContexts", usageContexts.get(i))),
                            Occur.SHOULD));
                }
                masterQuery.add(nestedQuery.build(), Occur.MUST);
            }

            // propertyQualifiers
            if (propertyQualifiers != null && propertyQualifiers.getNameAndValueCount() > 0) {
                BooleanQuery.Builder nestedQuery = new BooleanQuery.Builder();
                nestedQuery.setMinimumNumberShouldMatch(1);

                for (int i = 0; i < propertyQualifiers.getNameAndValueCount(); i++) {
                    NameAndValue qualNameAndValue = propertyQualifiers.getNameAndValue(i);
                    String name = qualNameAndValue.getName();
                    String value = qualNameAndValue.getContent();
                    if(name.equals("source-code")){
                        nestedQuery.add(new BooleanClause(new TermQuery(new Term("hasSource", name)), Occur.SHOULD));
                            if(value != null){
                                nestedQuery.add(new BooleanClause(new TermQuery(new Term("sourceValue", value)), Occur.SHOULD));
                            }
                        }
                    QueryParser parser = new QueryParser("qualifiers",LuceneLoaderCode.getAnaylzer());
                    Query queryNameAndValue = parser.parse("\"" +name
                                + LuceneLoaderCode.QUALIFIER_NAME_VALUE_SPLIT_TOKEN
                                + value + "\"");
                    Query nameOnly = parser.parse(name
                            + "*");
                    if(StringUtils.isNotBlank(value)){
                        nestedQuery.add(new BooleanClause(queryNameAndValue, Occur.SHOULD));
                    } else {
                        nestedQuery.add(new BooleanClause(nameOnly, Occur.SHOULD));
                    }
                }
                masterQuery.add(nestedQuery.build(), Occur.MUST);
            }

            // entityTypes
            if (entityTypes != null && entityTypes.length > 0) {
                BooleanQuery.Builder nestedQuery = new BooleanQuery.Builder();
                nestedQuery.setMinimumNumberShouldMatch(1);

                for (int i = 0; i < entityTypes.length; i++) {
                    nestedQuery.add(new BooleanClause(new TermQuery(new Term(SQLTableConstants.TBLCOL_ENTITYTYPE,
                            entityTypes[i])), Occur.SHOULD));
                }
                masterQuery.add(nestedQuery.build(), Occur.MUST);
            }

            if (crl != null && crl.getConceptReferenceCount() > 0) {
                BooleanQuery.Builder nestedQuery = new BooleanQuery.Builder();
                nestedQuery.setMinimumNumberShouldMatch(1);

                for (int i = 0; i < crl.getConceptReferenceCount(); i++) {
                    ConceptReference cr = crl.getConceptReference(i);

                    if (cr.getConceptCode() != null && cr.getConceptCode().length() > 0) {
                        BooleanQuery.Builder codeAndNamespaceQuery = new BooleanQuery.Builder();
                        
                        codeAndNamespaceQuery.add(new BooleanClause(new TermQuery(new Term(codeField, cr.getConceptCode())),
                                Occur.MUST));
                        if(StringUtils.isNotBlank(cr.getCodeNamespace())){
                            codeAndNamespaceQuery.add(new BooleanClause(new TermQuery(new Term(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE, cr.getCodeNamespace())),
                                    Occur.MUST));
                        }
                      nestedQuery.add(codeAndNamespaceQuery.build(), Occur.SHOULD);
                    }
                }
                masterQuery.add(nestedQuery.build(), Occur.MUST);
            }

            getLogger().debug("Generated Query: " + masterQuery.toString());          

            return masterQuery.build();

        } catch (LBParameterException e) {
            throw e;
        } catch (UnexpectedInternalError e) {
            throw e;
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        }
    }

  

    /*
     * Utility method.
     */
    private static ArrayList<String> localNameListToArrayList(LocalNameList lnl) {
        ArrayList<String> properties = new ArrayList<String>();
        if (lnl != null) {
            String[] localNames = lnl.getEntry();
            if (localNames != null) {
                for (int i = 0; i < localNames.length; i++) {
                    String property = localNames[i];
                    if (property != null && property.length() > 0) {
                        properties.add(property);
                    }
                }

            }
        }
        return properties;
    }

    protected static String mapPropertyType(PropertyType propertyType) throws LBInvocationException {
        if (propertyType.equals(PropertyType.COMMENT)) {
            return SQLTableConstants.TBLCOLVAL_COMMENT;
        } else if (propertyType.equals(PropertyType.DEFINITION)) {
            return SQLTableConstants.TBLCOLVAL_DEFINITION;
        } else if (propertyType.equals(PropertyType.GENERIC)) {
            return SQLTableConstants.TBLCOLVAL_PROPERTY;
        } else if (propertyType.equals(PropertyType.PRESENTATION)) {
            return SQLTableConstants.TBLCOLVAL_PRESENTATION;
        } else {
            String id = getLogger().error("UnexpectedPropertyType - " + propertyType);
            throw new LBInvocationException("Unexpected PropertyType", id);
        }
    }
}