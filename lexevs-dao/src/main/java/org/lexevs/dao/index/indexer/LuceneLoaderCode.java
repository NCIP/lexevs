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
package org.lexevs.dao.index.indexer;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.lexevs.dao.index.lucene.v2010.entity.LuceneEntityDao;

import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;
import edu.mayo.informatics.indexer.api.generators.DocumentFromStringsGenerator;
import edu.mayo.informatics.indexer.lucene.analyzers.EncoderAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.NormAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.SnowballAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.StringAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.WhiteSpaceLowerCaseAnalyzer;

/**
 * Base Lucene Loader code.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 */
public abstract class LuceneLoaderCode {
	
	/** The lex grid white space index set. */
	public static char[] lexGridWhiteSpaceIndexSet = new char[] { '-', ';', '(', ')', '{', '}', '[', ']', '<', '>', '|' };

	/** The use compound file_. */
    protected boolean useCompoundFile_ = false;
    
    /** The generator_. */
    private DocumentFromStringsGenerator generator_;
    
    /** The norm enabled_. */
    protected static boolean normEnabled_ = false;
    
    /** The double metaphone enabled_. */
    protected static boolean doubleMetaphoneEnabled_ = true;
    
    /** The stemming enabled_. */
    protected static boolean stemmingEnabled_ = true;
    
    /** The logger. */
    protected final Logger logger = Logger.getLogger("CTS.loader");
    
    /** The Constant NORM_PREFIX. */
    protected static final String NORM_PREFIX = "norm_";
    
    /** The Constant DOUBLE_METAPHONE_PREFIX. */
    protected static final String DOUBLE_METAPHONE_PREFIX = "dm_";
    
    /** The Constant STEMMING_PREFIX. */
    protected static final String STEMMING_PREFIX = "stem_";
    
    /** The Constant LITERAL_PREFIX. */
    protected static final String LITERAL_PREFIX = "literal_";
    
    /** The Constant REVERSE_PREFIX. */
    protected static final String REVERSE_PREFIX = "reverse_";
    
    /** The Constant LITERAL_AND_REVERSE_PREFIX. */
    public static final String LITERAL_AND_REVERSE_PREFIX = LITERAL_PREFIX + REVERSE_PREFIX;
    
    /** The PROPERT y_ valu e_ field. */
    public static String PROPERTY_VALUE_FIELD = "propertyValue";
    
    /** The CODIN g_ schem e_ nam e_ field. */
    public static String CODING_SCHEME_NAME_FIELD = "codingSchemeName";
    
    /** The CODIN g_ schem e_ i d_ field. */
    public static String CODING_SCHEME_ID_FIELD = "codingSchemeId";
    
    /** The CODIN g_ schem e_ i d_ field. */
    public static String CODING_SCHEME_VERSION_FIELD = "codingSchemeVersion";
    
    /** The CODIN g_ schem e_ ur i_ versio n_ ke y_ field. */
    public static String CODING_SCHEME_URI_VERSION_KEY_FIELD = "csUriVersionKey";
    
    /** The UNTOKENIZE d_ lowercas e_ propert y_ valu e_ field. */
    public static String UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD = "untokenizedLCPropertyValue";
    
    /** The LITERA l_ propert y_ valu e_ field. */
    public static String LITERAL_PROPERTY_VALUE_FIELD = LITERAL_PREFIX + PROPERTY_VALUE_FIELD;
    
    /** The REVERS e_ propert y_ valu e_ field. */
    public static String REVERSE_PROPERTY_VALUE_FIELD = REVERSE_PREFIX + PROPERTY_VALUE_FIELD;
    
    /** The NOR m_ propert y_ valu e_ field. */
    public static String NORM_PROPERTY_VALUE_FIELD = NORM_PREFIX + PROPERTY_VALUE_FIELD;
    
    /** The STEMMIN g_ propert y_ valu e_ field. */
    public static String STEMMING_PROPERTY_VALUE_FIELD = STEMMING_PREFIX + PROPERTY_VALUE_FIELD;
    
    /** The STEMMIN g_ propert y_ valu e_ field. */
    public static String ENTITY_UID_FIELD = "entityUid";
    
    /** The DOUBL e_ metaphon e_ propert y_ valu e_ field. */
    public static String DOUBLE_METAPHONE_PROPERTY_VALUE_FIELD = DOUBLE_METAPHONE_PREFIX + PROPERTY_VALUE_FIELD;
    
    /** The LITERA l_ an d_ revers e_ propert y_ valu e_ field. */
    public static String LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD = LITERAL_AND_REVERSE_PREFIX + PROPERTY_VALUE_FIELD;
    
    public static String CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD = "codingSchemeUriVersionCodeNamespaceKey";

    /** The create boundry documents. */
    protected boolean createBoundryDocuments = true;
    
    /** The analyzer_. */
    protected PerFieldAnalyzerWrapper analyzer_ = null;

    /** The Constant STRING_TOKEINZER_TOKEN. */
    public static final String STRING_TOKEINZER_TOKEN = "<:>";
    
    /** The Constant QUALIFIER_NAME_VALUE_SPLIT_TOKEN. */
    public static final String QUALIFIER_NAME_VALUE_SPLIT_TOKEN = ":";
    
    /** The store lex big minimum. */
    public static boolean storeLexBIGMinimum = false;
    
    private LuceneEntityDao luceneEntityDao;
    
    protected LuceneLoaderCode(){
    	try {
			this.initIndexes();
		} catch (InternalErrorException e) {
			throw new RuntimeException(e);
		}
    }

    // by default, the index stores a copy of most of the information. Switching
    // this to
    // true will cause the indexer to only store the values that are required by
    // the LexBIG
    // implementation at runtime.

    // TODO add a GUI option for the codeBoundry stuff.
    
    /** The literal analyzer. */
    public static Analyzer literalAnalyzer = new WhiteSpaceLowerCaseAnalyzer(new String[] {},
            new char[]{}, new char[]{}); 
    /**
     * Adds the entity.
     * 
     * @param codingSchemeName the coding scheme name
     * @param codingSchemeId the coding scheme id
     * @param codingSchemeVersion the coding scheme version
     * @param entityId the entity id
     * @param entityNamespace the entity namespace
     * @param entityType the entity type
     * @param entityDescription the entity description
     * @param propertyType the property type
     * @param propertyName the property name
     * @param propertyValue the property value
     * @param isActive the is active
     * @param format the format
     * @param language the language
     * @param isPreferred the is preferred
     * @param conceptStatus the concept status
     * @param propertyId the property id
     * @param degreeOfFidelity the degree of fidelity
     * @param matchIfNoContext the match if no context
     * @param representationalForm the representational form
     * @param sources the sources
     * @param usageContexts the usage contexts
     * @param qualifiers the qualifiers
     * @param stc the stc
     * 
     * @throws Exception the exception
     */
    protected Document addEntity(String codingSchemeName, String codingSchemeId, String codingSchemeVersion, 
    		String entityUid, String entityCode, String entityNamespace, String[] entityTypes,
            String entityDescription, String propertyType, String propertyName, String propertyValue, Boolean isActive, Boolean isAnonymous,
            String format, String language, Boolean isPreferred, String conceptStatus, String propertyId,
            String degreeOfFidelity, Boolean matchIfNoContext, String representationalForm, String[] sources,
            String[] usageContexts, Qualifier[] qualifiers) throws Exception {

        String idFieldName = SQLTableConstants.TBLCOL_ENTITYCODE;
        String  propertyFieldName = SQLTableConstants.TBLCOL_PROPERTYNAME;
        String formatFieldName = SQLTableConstants.TBLCOL_FORMAT;
       
        StringBuffer fields = new StringBuffer();
        generator_.startNewDocument(codingSchemeName + "-" + entityCode + "-" + propertyId);
        generator_.addTextField(CODING_SCHEME_NAME_FIELD, codingSchemeName, store(), true, false);
        fields.append(CODING_SCHEME_NAME_FIELD + " ");
        generator_.addTextField(CODING_SCHEME_ID_FIELD, codingSchemeId, true, true, false);
        fields.append(CODING_SCHEME_ID_FIELD + " ");
        generator_.addTextField(CODING_SCHEME_VERSION_FIELD, codingSchemeVersion, true, true, false);
        fields.append(CODING_SCHEME_VERSION_FIELD + " ");
        generator_.addTextField(idFieldName + "Tokenized", entityCode, false, true, true);
        fields.append(idFieldName + "Tokenized ");
        generator_.addTextField(idFieldName, entityCode, true, true, false);
        fields.append(idFieldName + " ");
        generator_.addTextField(idFieldName + "LC", entityCode.toLowerCase(), false, true, false);
        fields.append(idFieldName + "LC ");
        
        if(entityTypes != null) {
        	for(String entityType : entityTypes) {
        		generator_.addTextField("entityType", entityType, true, true, false);
        	}
        }
        
        fields.append("entityType ");
        generator_.addTextField(CODING_SCHEME_URI_VERSION_KEY_FIELD, createCodingSchemeUriVersionKey(codingSchemeId, codingSchemeVersion), false, true, false);
        fields.append(CODING_SCHEME_URI_VERSION_KEY_FIELD + " ");
        generator_.addTextField(CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, createCodingSchemeUriVersionCodeNamespaceKey(codingSchemeId, codingSchemeVersion, entityCode, entityNamespace), false, true, false);
        fields.append(CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD + " ");
       
        if(StringUtils.isNotBlank(entityUid)) {
        	generator_.addTextField(ENTITY_UID_FIELD, entityUid, true, false, false);
            fields.append(ENTITY_UID_FIELD + " ");
        }
        
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

        if (StringUtils.isNotBlank(propertyValue)) {
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
        
        if (isAnonymous != null) {
            if (isAnonymous.booleanValue()) {
                generator_.addTextField("isAnonymous", "T", store(), true, false);
            } else {
                generator_.addTextField("isAnonymous", "F", store(), true, false);
            }

            fields.append("isAnonymous ");
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

        return generator_.getDocument();
    }

    /*
     * caGrid used these "boundry" documents to speed up multiple successive
     * queries. A boundry document should be added whenever a new entity id is
     * started.
     */
    /**
     * Adds the entity boundry document.
     * 
     * @param codingSchemeName the coding scheme name
     * @param codingSchemeId the coding scheme id
     * @param codingSchemeVersion the coding scheme version
     * @param entityId the entity id
     * 
     * @throws Exception the exception
     */
    protected Document addEntityBoundryDocument(
    		String codingSchemeName, 
    		String codingSchemeId, 
    		String codingSchemeVersion, 
    		String entityId,
    		String entityCodeNamespace) throws Exception {
        StringBuffer fields = new StringBuffer();
        generator_.startNewDocument(codingSchemeName + "-" + entityId);
        generator_.addTextField("codingSchemeName", codingSchemeName, store(), true, false);
        fields.append("codingSchemeName ");
        generator_.addTextField("codingSchemeId", codingSchemeId, true, true, false);
        fields.append("codingSchemeId ");
        generator_.addTextField("codeBoundry", "T", false, true, false);
        fields.append("codeBoundry ");
        generator_.addTextField(CODING_SCHEME_URI_VERSION_KEY_FIELD, createCodingSchemeUriVersionKey(codingSchemeId, codingSchemeVersion), false, true, false);
        fields.append(CODING_SCHEME_URI_VERSION_KEY_FIELD + " ");
        generator_.addTextField(CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, createCodingSchemeUriVersionCodeNamespaceKey(codingSchemeId, codingSchemeVersion, entityId, entityCodeNamespace), false, true, false);
        fields.append(CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD + " ");

        generator_.addTextField("fields", fields.toString(), store(), true, true);

        return generator_.getDocument();
    }

    /**
     * Store.
     * 
     * @return true, if successful
     */
    private boolean store() {
        if (storeLexBIGMinimum) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Inits the indexes.
     * 
     * @param indexName the index name
     * @param indexLocation the index location
     * 
     * @throws InternalErrorException the internal error exception
     */
    protected void initIndexes() throws InternalErrorException {
    	this.analyzer_ = getAnaylzer();
        generator_ = new DocumentFromStringsGenerator();
    }
    
    public static PerFieldAnalyzerWrapper getAnaylzer() {
    	   // no stop words, default character removal set.
    	PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new WhiteSpaceLowerCaseAnalyzer(new String[] {},
                WhiteSpaceLowerCaseAnalyzer.getDefaultCharRemovalSet(), lexGridWhiteSpaceIndexSet));
        
        //add a literal analyzer -- keep all special characters
        analyzer.addAnalyzer(LITERAL_PROPERTY_VALUE_FIELD, literalAnalyzer); 
        analyzer.addAnalyzer(LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD, literalAnalyzer);    

        if (doubleMetaphoneEnabled_) {
            EncoderAnalyzer temp = new EncoderAnalyzer(new DoubleMetaphone(), new String[] {},
                    WhiteSpaceLowerCaseAnalyzer.getDefaultCharRemovalSet(), lexGridWhiteSpaceIndexSet);
            analyzer.addAnalyzer(DOUBLE_METAPHONE_PROPERTY_VALUE_FIELD, temp);
        }

        if (normEnabled_) {
            try {
                NormAnalyzer temp = new NormAnalyzer(false, new String[] {}, WhiteSpaceLowerCaseAnalyzer
                        .getDefaultCharRemovalSet(), lexGridWhiteSpaceIndexSet);
                analyzer.addAnalyzer(NORM_PROPERTY_VALUE_FIELD, temp);
            } catch (NoClassDefFoundError e) {
               //
            }
        }

        if (stemmingEnabled_) {
            SnowballAnalyzer temp = new SnowballAnalyzer(false, "English", new String[] {}, WhiteSpaceLowerCaseAnalyzer
                    .getDefaultCharRemovalSet(), lexGridWhiteSpaceIndexSet);
            analyzer.addAnalyzer(STEMMING_PROPERTY_VALUE_FIELD, temp);
        }

        // these fields just get simple analyzing.
        StringAnalyzer sa = new StringAnalyzer(STRING_TOKEINZER_TOKEN);
        analyzer.addAnalyzer("sources", sa);
        analyzer.addAnalyzer("usageContexts", sa);
        analyzer.addAnalyzer("qualifiers", sa);
        
        return analyzer;
    }
    
 /*
    protected void createIndex(String indexName) {
        indexerService_.createIndex(indexName, analyzer_);
    }
  */  
    /**
     * Reverse terms in property value.
     * 
     * @param propertyValue the property value
     * 
     * @return the string
     */
    public String reverseTermsInPropertyValue(String propertyValue){
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
    
    /**
     * Creates the coding scheme uri version key.
     * 
     * @param uri the uri
     * @param version the version
     * 
     * @return the string
     */
    public static String createCodingSchemeUriVersionKey(String uri, String version) {
    	return uri + "-" + version;
    }
    
    public static String createCodingSchemeUriVersionCodeNamespaceKey(String uri, String version, String code, String namespace) {
    	return createCodingSchemeUriVersionKey(uri, version) + "-" + code + "-" + namespace;
    }

    public void setLuceneEntityDao(LuceneEntityDao luceneEntityDao) {
		this.luceneEntityDao = luceneEntityDao;
	}

	public LuceneEntityDao getLuceneEntityDao() {
		return luceneEntityDao;
	}
	
	public Analyzer getAnalyzer() {
		return this.analyzer_;
	}

	/**
     * The Class Qualifier.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    protected class Qualifier {
        
        /** The qualifier name. */
        String qualifierName;
        
        /** The qualifier value. */
        String qualifierValue;

        /**
         * Instantiates a new qualifier.
         * 
         * @param qualifierName the qualifier name
         * @param qualifierValue the qualifier value
         */
        public Qualifier(String qualifierName, String qualifierValue) {
            this.qualifierName = qualifierName;
            this.qualifierValue = qualifierValue;
        }
    }
}