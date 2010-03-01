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
package org.lexevs.dao.index.indexer;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;

import edu.mayo.informatics.indexer.api.IndexerService;
import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;
import edu.mayo.informatics.indexer.api.generators.DocumentFromStringsGenerator;
import edu.mayo.informatics.indexer.lucene.analyzers.EncoderAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.NormAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.SnowballAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.StringAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.WhiteSpaceLowerCaseAnalyzer;

import org.apache.lucene.analysis.Analyzer;

/**
 * Base Lucene Loader code.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 */
public abstract class LuceneLoaderCode {
	
	public static char[] lexGridWhiteSpaceIndexSet = new char[] { '-', ';', '(', ')', '{', '}', '[', ']', '<', '>', '|' };

    protected IndexerService indexerService_;
    protected String simpleIndexName_;
    // protected String normIndexName_;
    protected boolean useCompoundFile_ = false;
    private DocumentFromStringsGenerator generator_;
    protected boolean normEnabled_ = true;
    protected boolean doubleMetaphoneEnabled_ = true;
    protected boolean stemmingEnabled_ = true;
    protected final Logger logger = Logger.getLogger("CTS.loader");
    protected static final String NORM_PREFIX = "norm_";
    protected static final String DOUBLE_METAPHONE_PREFIX = "dm_";
    protected static final String STEMMING_PREFIX = "stem_";
    protected static final String LITERAL_PREFIX = "literal_";
    protected static final String REVERSE_PREFIX = "reverse_";
    public static final String LITERAL_AND_REVERSE_PREFIX = LITERAL_PREFIX + REVERSE_PREFIX;
    
    public static String PROPERTY_VALUE_FIELD = "propertyValue";
    
    public static String CODING_SCHEME_NAME_FIELD = "codingSchemeName";
    public static String CODING_SCHEME_ID_FIELD = "codingSchemeId";
    
    public static String UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD = "untokenizedLCPropertyValue";
    
    public static String LITERAL_PROPERTY_VALUE_FIELD = LITERAL_PREFIX + PROPERTY_VALUE_FIELD;
    public static String REVERSE_PROPERTY_VALUE_FIELD = REVERSE_PREFIX + PROPERTY_VALUE_FIELD;
    public static String NORM_PROPERTY_VALUE_FIELD = NORM_PREFIX + PROPERTY_VALUE_FIELD;
    public static String STEMMING_PROPERTY_VALUE_FIELD = STEMMING_PREFIX + PROPERTY_VALUE_FIELD;
    public static String DOUBLE_METAPHONE_PROPERTY_VALUE_FIELD = DOUBLE_METAPHONE_PREFIX + PROPERTY_VALUE_FIELD;
    public static String LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD = LITERAL_AND_REVERSE_PREFIX + PROPERTY_VALUE_FIELD;
  
    private String lastConceptCode = "";
    protected boolean createBoundryDocuments = true;
    protected PerFieldAnalyzerWrapper analyzer_ = null;

    public static final String STRING_TOKEINZER_TOKEN = "<:>";
    public static final String QUALIFIER_NAME_VALUE_SPLIT_TOKEN = ":";
    public static boolean storeLexBIGMinimum = false;

    // by default, the index stores a copy of most of the information. Switching
    // this to
    // true will cause the indexer to only store the values that are required by
    // the LexBIG
    // implementation at runtime.

    // TODO add a GUI option for the codeBoundry stuff.
    
    public static Analyzer literalAnalyzer = new WhiteSpaceLowerCaseAnalyzer(new String[] {},
            new char[]{}, new char[]{}); 

    protected void openIndexesClearExisting(String[] codingSchemes) throws Exception {
        indexerService_.forceUnlockIndex(simpleIndexName_);
        indexerService_.openBatchRemover(simpleIndexName_);

        for (int i = 0; i < codingSchemes.length; i++) {
            logger.info("clearing index of code system " + codingSchemes[i]);
            indexerService_.removeDocument(simpleIndexName_, CODING_SCHEME_NAME_FIELD, codingSchemes[i]);
        }

        indexerService_.closeBatchRemover(simpleIndexName_);
        openIndexes();
    }

    protected void openIndexes() throws Exception {
        indexerService_.forceUnlockIndex(simpleIndexName_);
        indexerService_.openWriter(simpleIndexName_, false);
        indexerService_.setUseCompoundFile(simpleIndexName_, useCompoundFile_);
        indexerService_.setMaxBufferedDocs(simpleIndexName_, 500);
        indexerService_.setMergeFactor(simpleIndexName_, 20);
    }

    protected void closeIndexes() throws Exception {
        indexerService_.optimizeIndex(simpleIndexName_);
        indexerService_.closeWriter(simpleIndexName_);
    }

    protected void addEntity(String codingSchemeName, String codingSchemeId, String entityId, String entityNamespace, String entityType,
            String entityDescription, String propertyType, String propertyName, String propertyValue, Boolean isActive,
            String format, String language, Boolean isPreferred, String conceptStatus, String propertyId,
            String degreeOfFidelity, Boolean matchIfNoContext, String representationalForm, String[] sources,
            String[] usageContexts, Qualifier[] qualifiers, SQLTableConstants stc) throws Exception {
        if (createBoundryDocuments) {
            String key = codingSchemeName + ":" + codingSchemeId + ":" + entityId + ":" + entityNamespace;

            if (!lastConceptCode.equals(key)) {
                addEntityBoundryDocument(codingSchemeName, codingSchemeId, entityId);
                lastConceptCode = key;
            }
        }

        String idFieldName = "id";
        String propertyFieldName = "propertyName";
        String formatFieldName = "format";
        if (stc != null && stc.supports2009Model()) {
            idFieldName = SQLTableConstants.TBLCOL_ENTITYCODE;
            propertyFieldName = SQLTableConstants.TBLCOL_PROPERTYNAME;
            formatFieldName = SQLTableConstants.TBLCOL_FORMAT;
        }

        StringBuffer fields = new StringBuffer();
        generator_.startNewDocument(codingSchemeName + "-" + entityId + "-" + propertyId);
        generator_.addTextField(CODING_SCHEME_NAME_FIELD, codingSchemeName, store(), true, false);
        fields.append(CODING_SCHEME_NAME_FIELD + " ");
        generator_.addTextField(CODING_SCHEME_ID_FIELD, codingSchemeId, true, true, false);
        fields.append(CODING_SCHEME_ID_FIELD + " ");
        generator_.addTextField(idFieldName + "Tokenized", entityId, false, true, true);
        fields.append(idFieldName + "Tokenized ");
        generator_.addTextField(idFieldName, entityId, true, true, false);
        fields.append(idFieldName + " ");
        generator_.addTextField(idFieldName + "LC", entityId.toLowerCase(), false, true, false);
        fields.append(idFieldName + "LC ");
        generator_.addTextField("entityType", entityType, true, true, false);
        fields.append("entityType ");
       
        //If the EntityDescription is an empty String, replace it with a single space.
        //Lucene will not index an empty String but it will index a space.
        if(StringUtils.isBlank(entityDescription)){
            entityDescription = " ";
        }
        generator_.addTextField("entityDescription", entityDescription, true, true, false);
        fields.append("entityDescription ");
      
        generator_.addTextField(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE, entityNamespace, true, true, false);
        fields.append("namespace ");

        String tempPropertyType;
        if (propertyType == null || propertyType.length() == 0) {
            if (propertyName.equalsIgnoreCase("textualPresentation")) {
                tempPropertyType = "presentation";
            } else if (propertyName.equals("definition")) {
                tempPropertyType = "definition";
            } else if (propertyName.equals("comment")) {
                tempPropertyType = "comment";
            } else if (propertyName.equals("instruction")) {
                tempPropertyType = "instruction";
            } else {
                tempPropertyType = propertyFieldName;
            }
        } else {
            tempPropertyType = propertyType;
        }
        generator_.addTextField("propertyType", tempPropertyType, store(), true, false);
        fields.append("propertyType ");

        generator_.addTextField(propertyFieldName, propertyName, store(), true, false);
        fields.append(propertyFieldName + " ");

        if (propertyValue != null && propertyValue.length() > 0) {
            generator_.addTextField(PROPERTY_VALUE_FIELD, propertyValue, store(), true, true);
            fields.append(PROPERTY_VALUE_FIELD + " ");
            
            generator_.addTextField(REVERSE_PROPERTY_VALUE_FIELD, 
                    reverseTermsInPropertyValue(propertyValue), false, true, true);
            fields.append(REVERSE_PROPERTY_VALUE_FIELD + " ");
            
            generator_.addTextField(LITERAL_PROPERTY_VALUE_FIELD, propertyValue, false, true, true);
            fields.append(LITERAL_PROPERTY_VALUE_FIELD + " ");
            
            generator_.addTextField(LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD, 
                    reverseTermsInPropertyValue(propertyValue), false, true, true);
            fields.append(LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD + " ");

            // This copy of the content is required for making "startsWith" or
            // "exactMatch" types of queries
            generator_.addTextField(UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD, propertyValue.toLowerCase(), false, true, false);
            fields.append(UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD + " ");

            if (normEnabled_) {
                generator_.addTextField(NORM_PROPERTY_VALUE_FIELD, propertyValue, false, true, true);
                fields.append(NORM_PROPERTY_VALUE_FIELD + " ");
            }

            if (doubleMetaphoneEnabled_) {
                generator_.addTextField(DOUBLE_METAPHONE_PROPERTY_VALUE_FIELD, propertyValue, false, true, true);
                fields.append(DOUBLE_METAPHONE_PROPERTY_VALUE_FIELD + " ");
            }

            if (stemmingEnabled_) {
                generator_.addTextField(STEMMING_PROPERTY_VALUE_FIELD, propertyValue, false, true, true);
                fields.append(STEMMING_PROPERTY_VALUE_FIELD + " ");
            }
        }

        if (isActive != null) {
            if (isActive.booleanValue()) {
                generator_.addTextField("isActive", "T", store(), true, false);
            } else {
                generator_.addTextField("isActive", "F", store(), true, false);
            }

            fields.append("isActive ");
        }
        if (isPreferred != null) {
            if (isPreferred.booleanValue()) {
                generator_.addTextField("isPreferred", "T", store(), true, false);
            } else {
                generator_.addTextField("isPreferred", "F", store(), true, false);
            }
            fields.append("isPreferred ");
        }
        if (format != null && format.length() > 0) {
            generator_.addTextField(formatFieldName, format, store(), true, false);
            fields.append(formatFieldName + " ");
        }

        // in ldap and sql, languages are optional (missing means default. But
        // we don't allow that here
        // you must supply the lanaguage (send in the default if a concept
        // doesn't have one)
        if (language != null && language.length() > 0) {
            generator_.addTextField("language", language, store(), true, false);
            fields.append("language ");
        } else {
            throw new Exception("Language is required");
        }

        if (conceptStatus != null && conceptStatus.length() > 0) {
            generator_.addTextField(SQLTableConstants.TBLCOL_CONCEPTSTATUS, conceptStatus, store(), true, false);
            fields.append("conceptStatus ");
        }

        if (propertyId != null && propertyId.length() > 0) {
            generator_.addTextField("propertyId", propertyId, store(), true, false);
            fields.append("propertyId ");
        }

        if (degreeOfFidelity != null && degreeOfFidelity.length() > 0) {
            generator_.addTextField("degreeOfFidelity", degreeOfFidelity, store(), true, false);
            fields.append("degreeOfFidelity ");
        }

        if (representationalForm != null && representationalForm.length() > 0) {
            generator_.addTextField("representationalForm", representationalForm, store(), true, false);
            fields.append("representationalForm ");
        }

        if (matchIfNoContext != null) {
            if (matchIfNoContext.booleanValue()) {
                generator_.addTextField("matchIfNoContext", "T", store(), true, false);
            } else {
                generator_.addTextField("matchIfNoContext", "F", store(), true, false);
            }

            fields.append("matchIfNoContext ");
        }

        if (sources != null && sources.length > 0) {
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < sources.length; i++) {
                temp.append(sources[i]);
                if (i + 1 < sources.length) {
                    temp.append(STRING_TOKEINZER_TOKEN);
                }
            }
            generator_.addTextField("sources", temp.toString(), false, true, true);
            fields.append("sources ");
        }

        if (usageContexts != null && usageContexts.length > 0) {
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < usageContexts.length; i++) {
                temp.append(usageContexts[i]);
                if (i + 1 < usageContexts.length) {
                    temp.append(STRING_TOKEINZER_TOKEN);
                }
            }
            generator_.addTextField("usageContexts", temp.toString(), false, true, true);
            fields.append("usageContexts ");
        }

        if (qualifiers != null && qualifiers.length > 0) {
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < qualifiers.length; i++) {
                temp.append(qualifiers[i].qualifierName + QUALIFIER_NAME_VALUE_SPLIT_TOKEN
                        + qualifiers[i].qualifierValue);
                if (i + 1 < qualifiers.length) {
                    temp.append(STRING_TOKEINZER_TOKEN);
                }
            }
            generator_.addTextField("qualifiers", temp.toString(), false, true, true);
            fields.append("qualifiers ");
        }

        generator_.addTextField("fields", fields.toString(), store(), true, true);

        indexerService_.addDocument(simpleIndexName_, generator_.getDocument(), analyzer_);
    }

    /*
     * caGrid used these "boundry" documents to speed up multiple successive
     * queries. A boundry document should be added whenever a new entity id is
     * started.
     */
    protected void addEntityBoundryDocument(String codingSchemeName, String codingSchemeId, String entityId) throws Exception {
        StringBuffer fields = new StringBuffer();
        generator_.startNewDocument(codingSchemeName + "-" + entityId);
        generator_.addTextField("codingSchemeName", codingSchemeName, store(), true, false);
        fields.append("codingSchemeName ");
        generator_.addTextField("codingSchemeId", codingSchemeId, true, true, false);
        fields.append("codingSchemeId ");
        generator_.addTextField("codeBoundry", "T", false, true, false);
        fields.append("codeBoundry ");

        generator_.addTextField("fields", fields.toString(), store(), true, true);

        indexerService_.addDocument(simpleIndexName_, generator_.getDocument(), analyzer_);
    }

    private boolean store() {
        if (storeLexBIGMinimum) {
            return false;
        } else {
            return true;
        }
    }

    protected void initIndexes(String indexName, String indexLocation) throws InternalErrorException {
        indexerService_ = new IndexerService(indexLocation, false);
        simpleIndexName_ = indexName;
        // normIndexName_ = indexName + "_Normed";

        // no stop words, default character removal set.
        analyzer_ = new PerFieldAnalyzerWrapper(new WhiteSpaceLowerCaseAnalyzer(new String[] {},
                WhiteSpaceLowerCaseAnalyzer.getDefaultCharRemovalSet(), lexGridWhiteSpaceIndexSet));
        
        //add a literal analyzer -- keep all special characters
        analyzer_.addAnalyzer(LITERAL_PROPERTY_VALUE_FIELD, literalAnalyzer); 
        analyzer_.addAnalyzer(LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD, literalAnalyzer);    

        if (doubleMetaphoneEnabled_) {
            EncoderAnalyzer temp = new EncoderAnalyzer(new DoubleMetaphone(), new String[] {},
                    WhiteSpaceLowerCaseAnalyzer.getDefaultCharRemovalSet(), lexGridWhiteSpaceIndexSet);
            analyzer_.addAnalyzer(DOUBLE_METAPHONE_PROPERTY_VALUE_FIELD, temp);
        }

        if (normEnabled_) {
            try {
                NormAnalyzer temp = new NormAnalyzer(false, new String[] {}, WhiteSpaceLowerCaseAnalyzer
                        .getDefaultCharRemovalSet(), lexGridWhiteSpaceIndexSet);
                analyzer_.addAnalyzer(NORM_PROPERTY_VALUE_FIELD, temp);
            } catch (NoClassDefFoundError e) {
                // norm is not available
                normEnabled_ = false;
                logger
                        .warn(
                                "Normalized index will not be built becaues Norm could not be launched.  Is Norm (lvg) on the classpath?",
                                e);
            }
        }

        if (stemmingEnabled_) {
            SnowballAnalyzer temp = new SnowballAnalyzer(false, "English", new String[] {}, WhiteSpaceLowerCaseAnalyzer
                    .getDefaultCharRemovalSet(), lexGridWhiteSpaceIndexSet);
            analyzer_.addAnalyzer(STEMMING_PROPERTY_VALUE_FIELD, temp);
        }

        // these fields just get simple analyzing.
        StringAnalyzer sa = new StringAnalyzer(STRING_TOKEINZER_TOKEN);
        analyzer_.addAnalyzer("sources", sa);
        analyzer_.addAnalyzer("usageContexts", sa);
        analyzer_.addAnalyzer("qualifiers", sa);

        generator_ = new DocumentFromStringsGenerator();
    }
    
    protected void createIndex() {
        indexerService_.createIndex(simpleIndexName_, analyzer_);
    }
    
    protected String reverseTermsInPropertyValue(String propertyValue){
        StringBuffer buffer = new StringBuffer();
        String[] terms = propertyValue.split(" ");
        for(int i=0;i<terms.length;i++){
            buffer.append(StringUtils.reverse(terms[i]));
            if(i < terms.length -1){
                buffer.append(" ");
            }
        }
        return buffer.toString();
    }

    protected class Qualifier {
        String qualifierName;
        String qualifierValue;

        public Qualifier(String qualifierName, String qualifierValue) {
            this.qualifierName = qualifierName;
            this.qualifierValue = qualifierValue;
        }
    }
}