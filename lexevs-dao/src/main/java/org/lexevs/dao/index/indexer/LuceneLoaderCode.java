
package org.lexevs.dao.index.indexer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;
import org.apache.lucene.analysis.phonetic.DoubleMetaphoneFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.util.AttributeFactory;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.lucene.v2010.entity.LuceneEntityDao;
import org.lexevs.dao.indexer.api.generators.DocumentFromStringsGenerator;

/**
 * Base Lucene Loader code.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 */
public abstract class LuceneLoaderCode {
	
	/** The lex grid white space index set. */
	public static char[] lexGridWhiteSpaceIndexSet = new char[] { '-', ';', '(', ')', '{', '}', '[', ']', '<', '>', '|' };

	/** The use compound file_. */
    protected boolean useCompoundFile_ = false;
    
    /** The generator_. */
    protected DocumentFromStringsGenerator generator_;
    
    /** The norm enabled_. */
    protected static boolean normEnabled_ = false;
    
    /** The double metaphone enabled_. */
    protected static boolean doubleMetaphoneEnabled_ = true;
    
    /** The stemming enabled_. */
    protected static boolean stemmingEnabled_ = true;
    
    /** The logger. */
    protected final Logger logger = LogManager.getLogger("CTS.loader");
    
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
    
    /** The entity code. */
    protected static String UNIQUE_ID = "code";
    
    /** The PROPERT y_ valu e_ field. */
    public static String PROPERTY_VALUE_FIELD = "propertyValue";
    
    /** The CODIN g_ schem e_ nam e_ field. */
    public static String CODING_SCHEME_NAME_FIELD = "codingSchemeName";
    
    /** The CODIN g_ schem e_ i d_ field. */
    public static String CODING_SCHEME_ID_FIELD = "codingSchemeUri";
    
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

    /** Used to identify the parent document as opposed to the child document. */
    protected boolean isParent = true;
    
    /** The analyzer_. */
    protected static PerFieldAnalyzerWrapper analyzer_ = null;

    /** The Constant STRING_TOKEINZER_TOKEN. */
    public static final String STRING_TOKENIZER_TOKEN = "<:>";
    
    /** The Constant QUALIFIER_NAME_VALUE_SPLIT_TOKEN. */
    public static final String QUALIFIER_NAME_VALUE_SPLIT_TOKEN = ":";

	private static final String ENTITY_TYPE = "entityType";
 
    private LuceneEntityDao luceneEntityDao;
    
    protected LuceneLoaderCode(){
    	try {
			this.initIndexes();
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
    }
    
    /** The literal analyzer. */
    public static Analyzer literalAnalyzer = new Analyzer() {
    	
		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			final WhitespaceTokenizer source = new WhitespaceTokenizer();
			TokenStream filter = new LowerCaseFilter(source);
			return new TokenStreamComponents(source, filter);
		}
	};
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

        String  propertyFieldName = SQLTableConstants.TBLCOL_PROPERTYNAME;
        String formatFieldName = SQLTableConstants.TBLCOL_FORMAT;
       
        generator_.startNewDocument(codingSchemeName + "-" + entityCode + "-" + propertyId);
        generator_.addTextField(UNIQUE_ID + "Tokenized", entityCode, false, true, true);
        generator_.addTextField(UNIQUE_ID, entityCode, false, true, false);// must be anyalyzed with KeywordAnalyzer
        generator_.addTextField(UNIQUE_ID + "LC", entityCode.toLowerCase(), false, true, false);
        
        if(entityTypes != null) {
        	for(String entityType : entityTypes) {
        		generator_.addTextField(ENTITY_TYPE, entityType, false, true, false);// must be analyzed with KeywordAnalyzer
        	}
        }
        
        generator_.addTextField(CODING_SCHEME_URI_VERSION_KEY_FIELD, 
        		createCodingSchemeUriVersionKey(codingSchemeId, codingSchemeVersion), false, true, false);
        generator_.addTextField(CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, 
        		createCodingSchemeUriVersionCodeNamespaceKey(codingSchemeId, codingSchemeVersion, 
        				entityCode, entityNamespace), false, true, false);
     // must be analyzed with KeywordAnalyzer
        generator_.addTextField(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE, entityNamespace, false, true, false);

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
        generator_.addTextField("propertyType", tempPropertyType, false, true, false);

        generator_.addTextField(propertyFieldName, propertyName, false, true, false);

        if (StringUtils.isNotBlank(propertyValue)) {
            generator_.addTextField(PROPERTY_VALUE_FIELD, propertyValue, false, true, true);
            
            generator_.addTextField(REVERSE_PROPERTY_VALUE_FIELD, 
                    reverseTermsInPropertyValue(propertyValue), false, true, true);
            
            generator_.addTextField(LITERAL_PROPERTY_VALUE_FIELD, propertyValue, false, true, true);
            
            generator_.addTextField(LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD, 
                    reverseTermsInPropertyValue(propertyValue), false, true, true);

            // This copy of the content is required for making "startsWith" or
            // "exactMatch" types of queries
            generator_.addTextField(UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD, propertyValue.getBytes().length > 32000? propertyValue.substring(0, 1000) : propertyValue.toLowerCase(), false, true, false);
            if(propertyValue.getBytes().length > 32000){
            	logger.warn("Term is of a size exceeding 32k bytes.  Truncating term that starts with: \"" + propertyValue.substring(0, 100) + "\"");
            }
            if (normEnabled_) {
                generator_.addTextField(NORM_PROPERTY_VALUE_FIELD, propertyValue, false, true, true);
            }

            if (doubleMetaphoneEnabled_) {
                generator_.addTextField(DOUBLE_METAPHONE_PROPERTY_VALUE_FIELD, propertyValue, false, true, true);
            }

            if (stemmingEnabled_) {
                generator_.addTextField(STEMMING_PROPERTY_VALUE_FIELD, propertyValue, false, true, true);
            }
        }

        if (isActive != null) {
            if (isActive.booleanValue()) {
                generator_.addTextField("isActive", "T", false, true, false);
            } else {
                generator_.addTextField("isActive", "F", false, true, false);
            }
        }else{
        	generator_.addTextField("isActive", "T", false, true, false);
        }
        
        if (isAnonymous != null) {
            if (isAnonymous.booleanValue()) {
                generator_.addTextField("isAnonymous", "T", false, true, false);
            } else {
                generator_.addTextField("isAnonymous", "F", false, true, false);
            }
        }else{
        	generator_.addTextField("isAnonymous", "F", false, true, false);
        }
        
        if(entityTypes != null) {
        	for(String entityType : entityTypes) {
        		generator_.addTextField("entityType", entityType, true, true, false);
        	}
        }
        if (isPreferred != null) {
            if (isPreferred.booleanValue()) {
                generator_.addTextField("isPreferred", "T", false, true, false);
            } else {
                generator_.addTextField("isPreferred", "F", false, true, false);
            }
        }
        if (format != null && format.length() > 0) {
            generator_.addTextField(formatFieldName, format, false, true, false);
        }

        // in ldap and sql, languages are optional (missing means default. But
        // we don't allow that here
        // you must supply the lanaguage (send in the default if a concept
        // doesn't have one)
        if (language != null && language.length() > 0) {
            generator_.addTextField("language", language, false, true, false);
        } 

        if (conceptStatus != null && conceptStatus.length() > 0) {
            generator_.addTextField(SQLTableConstants.TBLCOL_CONCEPTSTATUS, conceptStatus, false, true, false);
        }

        if (propertyId != null && propertyId.length() > 0) {
            generator_.addTextField("propertyId", propertyId, false, true, false);
        }

        if (degreeOfFidelity != null && degreeOfFidelity.length() > 0) {
            generator_.addTextField("degreeOfFidelity", degreeOfFidelity, false, true, false);
        }

        if (representationalForm != null && representationalForm.length() > 0) {
            generator_.addTextField("representationalForm", representationalForm, false, true, false);
        }

        if (matchIfNoContext != null) {
            if (matchIfNoContext.booleanValue()) {
                generator_.addTextField("matchIfNoContext", "T", false, true, false);
            } else {
                generator_.addTextField("matchIfNoContext", "F", false, true, false);
            }
        }

        if (sources != null && sources.length > 0) {
            for (int i = 0; i < sources.length; i++) {
            	  generator_.addTextField("sources", StringUtils.lowerCase(sources[i]), false, true, false);
            }
        }

        if (usageContexts != null && usageContexts.length > 0) {
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < usageContexts.length; i++) {
                temp.append(usageContexts[i]);
                if (i + 1 < usageContexts.length) {
                    temp.append(STRING_TOKENIZER_TOKEN);
                }
            }
            generator_.addTextField("usageContexts", temp.toString(), false, true, true);
        }

        if (qualifiers != null && qualifiers.length > 0) {
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < qualifiers.length; i++) {
            	if(qualifiers[i].qualifierName.equals("source-code")){
            		generator_.addTextField("hasSource", qualifiers[i].qualifierName, true, true, false);
            		if(qualifiers[i].qualifierValue != null){
            		generator_.addTextField("sourceValue", qualifiers[i].qualifierValue, true, true, false);
            		}
            	}else{
                temp.append(qualifiers[i].qualifierName + QUALIFIER_NAME_VALUE_SPLIT_TOKEN
                        + qualifiers[i].qualifierValue);
                if (i + 1 < qualifiers.length) {
                    temp.append(STRING_TOKENIZER_TOKEN);
                }
            	}
            }
            if(temp.length() > 0){ generator_.addTextField("qualifiers", temp.toString(), false, true, true);}
        }

        return generator_.getDocument();
    }


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
    
    protected Document createParentDocument(
    		String codingSchemeName,
			String codingSchemeUri, 
			String codingSchemeVersion,
			String entityCode, 
			String entityCodeNamespace,
			EntityDescription entityDescription, 
			Boolean isActive,
			Boolean isAnonymous, 
			Boolean isDefined,
			String[] entityTypes,
			String conceptStatus,
			String entityUid,
			Boolean isParentDoc) {
    	
    	generator_.startNewDocument(codingSchemeName + "-" + entityCode);
    	generator_.addTextField("codingSchemeName", codingSchemeName, true, true, false);
    	generator_.addTextField("codingSchemeUri", codingSchemeUri, true, true, false);
    	generator_.addTextField("codingSchemeVersion", codingSchemeVersion, true, true, false);
    	generator_.addTextField("entityCode", entityCode, true, true, false);
    	generator_.addTextField("entityCodeNamespace", entityCodeNamespace, true, true, false);
    	generator_.addTextField("entityDescription", entityDescription !=null ? entityDescription.getContent() : "ENTITY DESCRIPTION ABSENT", true, true, false);
    
    	
        if (isDefined != null) {
            if (isDefined.booleanValue()) {
                generator_.addTextField("isDefined", "T", false, true, false);
            } else {
                generator_.addTextField("isDefined", "F", false, true, false);
            }
        }
        
        if(StringUtils.isNotBlank(entityUid)) {
        	generator_.addTextField(ENTITY_UID_FIELD, entityUid, true, false, false);
        }
        
        if (isParentDoc != null) {
        	generator_.addTextField("isParentDoc", Boolean.toString(isParentDoc), true, true, false);
        }else {
        	throw new RuntimeException("isParentDoc is not defined.");
        }
    	for(String entityType: entityTypes){
    		generator_.addTextField("type", entityType, true, true, false);
    	}
    	generator_.addTextField(CODING_SCHEME_URI_VERSION_KEY_FIELD, 
    			createCodingSchemeUriVersionKey(codingSchemeUri, codingSchemeVersion), false, true, false);
    	generator_.addTextField(CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, 
    			createCodingSchemeUriVersionCodeNamespaceKey(codingSchemeUri, codingSchemeVersion, entityCode, entityCodeNamespace), false, true, false);
        
    	return generator_.getDocument();
	}


    /**
     * Inits the indexes.
     * 
     * @param indexName the index name
     * @param indexLocation the index location
     * 
     * @throws RuntimeException
     */
    protected void initIndexes() throws RuntimeException {
    	this.analyzer_ = getAnaylzer();
        generator_ = new DocumentFromStringsGenerator();
    }
    
    public static PerFieldAnalyzerWrapper getAnaylzer() {
    	
    	Map<String,Analyzer> analyzerPerField = new HashMap<>();
    	    	
        //add a literal analyzer -- keep all special characters
    	analyzerPerField.put(LITERAL_PROPERTY_VALUE_FIELD, literalAnalyzer);
    	analyzerPerField.put(LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD, literalAnalyzer); 
    	
    	//treat as string field by analyzing with the KeywordAnalyzer
    	analyzerPerField.put(UNIQUE_ID, new KeywordAnalyzer());
    	analyzerPerField.put(ENTITY_TYPE, new KeywordAnalyzer());
        analyzerPerField.put("isPreferred", new KeywordAnalyzer());
    	analyzerPerField.put(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE, new KeywordAnalyzer());

        if (doubleMetaphoneEnabled_) {
            Analyzer temp = new Analyzer() {
            	
                @Override
                protected TokenStreamComponents createComponents(String fieldName) {
                    final StandardTokenizer source = new StandardTokenizer(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
                    source.setMaxTokenLength(StandardAnalyzer.DEFAULT_MAX_TOKEN_LENGTH);
                    TokenStream filter = new StandardFilter(source);
                    filter = new LowerCaseFilter( filter);
                    filter = new StopFilter(filter, StandardAnalyzer.STOP_WORDS_SET);
                    filter = new DoubleMetaphoneFilter(filter, 4, false);
                    return new TokenStreamComponents(source, filter);
                }
            };
            analyzerPerField.put(DOUBLE_METAPHONE_PROPERTY_VALUE_FIELD, temp);
        }

        if (normEnabled_) {
            try {
                Analyzer temp = new StandardAnalyzer(CharArraySet.EMPTY_SET);
                analyzerPerField.put(NORM_PROPERTY_VALUE_FIELD, temp);
            } catch (NoClassDefFoundError e) {
               //
            }
        }

        if (stemmingEnabled_) {
        Analyzer temp = new Analyzer() {
            	
                @Override
                protected TokenStreamComponents createComponents(String fieldName) {
                    final StandardTokenizer source = new StandardTokenizer(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
                    source.setMaxTokenLength(StandardAnalyzer.DEFAULT_MAX_TOKEN_LENGTH);
                    TokenStream filter = new StandardFilter(source);
                    filter = new LowerCaseFilter( filter);
                    filter = new StopFilter(filter, StandardAnalyzer.STOP_WORDS_SET);
                    filter = new SnowballFilter(filter, "English");
                    return new TokenStreamComponents(source, filter);
                }
            };
            analyzerPerField.put(STEMMING_PROPERTY_VALUE_FIELD, temp);
        }
        
        final CharArraySet dividerList = new CharArraySet(10, true);
        dividerList.add(STRING_TOKENIZER_TOKEN);
        Analyzer sa = new StandardAnalyzer(new CharArraySet(dividerList, true));
        Analyzer qualifierAnalyzer = new Analyzer(){

			@Override
			protected TokenStreamComponents createComponents(String arg0) {
                final StandardTokenizer source = new StandardTokenizer(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
                source.setMaxTokenLength(StandardAnalyzer.DEFAULT_MAX_TOKEN_LENGTH);
                TokenStream filter = new LowerCaseFilter( source);
                Pattern pattern = Pattern.compile("\\-|\\;|\\(|\\)|\\{|\\}|\\[|\\]|\\<|\\>|\\||(\\<\\:\\>)");
				filter = new PatternReplaceFilter(filter, pattern, " ", true);
                return new TokenStreamComponents(source, filter);
			}
        	
        };
        
        Analyzer sourcesAnalyzer = new KeywordAnalyzer();

        analyzerPerField.put("sources", sourcesAnalyzer);
        analyzerPerField.put("usageContexts", sa);
        analyzerPerField.put("qualifiers", qualifierAnalyzer);
        
        // no stop words, default character removal set.
    	PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(CharArraySet.EMPTY_SET), analyzerPerField);
        return analyzer;
    }

    /**
     * Reverse terms in property value.
     * 
     * @param propertyValue the property value
     * 
     * @return the string
     */
    public static String reverseTermsInPropertyValue(String propertyValue){
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

	public EntityDao getLuceneEntityDao() {
		return luceneEntityDao;
	}
	
	public Analyzer getAnalyzer() {
		return analyzer_;
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