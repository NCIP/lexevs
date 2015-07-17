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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.indexer.api.generators.QueryGenerator;
import org.lexevs.dao.indexer.lucene.analyzers.EncoderAnalyzer;
import org.lexevs.dao.indexer.lucene.analyzers.FieldSkippingAnalyzer;
import org.lexevs.dao.indexer.lucene.analyzers.NormAnalyzer;
import org.lexevs.dao.indexer.lucene.analyzers.SnowballAnalyzer;
import org.lexevs.dao.indexer.lucene.analyzers.WhiteSpaceLowerCaseAnalyzer;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.ResourceManager;

import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * Build the query parser to use for parsing the text portion of a user query.
 * One parser is shared across all indexes.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class IndexQueryParserFactory {
    private static IndexQueryParserFactory instance;
    
    private QueryParser parser_;
    private Set extraWhiteSpaceChars_;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    public static synchronized IndexQueryParserFactory getInstance(){
        if(instance == null){
            instance = new IndexQueryParserFactory();
        }
        return instance;
    }

    protected IndexQueryParserFactory() {
        WhiteSpaceLowerCaseAnalyzer wslca = new WhiteSpaceLowerCaseAnalyzer(new String[] {},
                WhiteSpaceLowerCaseAnalyzer.getDefaultCharRemovalSet(), Constants.lexGridWhiteSpaceIndexSet);

        Map<String,Analyzer> analyzerPerField = new HashMap<>();
        
        extraWhiteSpaceChars_ = wslca.getCurrentCharRemovalTable();

        
        EncoderAnalyzer ea = new EncoderAnalyzer(new DoubleMetaphone(), new String[] {}, WhiteSpaceLowerCaseAnalyzer
                .getDefaultCharRemovalSet(), Constants.lexGridWhiteSpaceIndexSet);
        analyzerPerField.put("dm_propertyValue", ea);

        if (ResourceManager.instance().getSystemVariables().isNormEnabled()) {
            try {
                NormAnalyzer temp = new NormAnalyzer(ResourceManager.instance().getSystemVariables()
                        .getNormConfigFile(), false, new String[] {}, WhiteSpaceLowerCaseAnalyzer
                        .getDefaultCharRemovalSet(), Constants.lexGridWhiteSpaceIndexSet);
                analyzerPerField.put("norm_propertyValue", temp);
            } catch (NoClassDefFoundError e) {
                getLogger().error("Error initializing Normalized Searcher", e);
            }
        }
        
        analyzerPerField.put("literal_propertyValue", LuceneLoaderCode.literalAnalyzer);
        
        analyzerPerField.put(LuceneLoaderCode.UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD, 
                new KeywordAnalyzer());

        SnowballAnalyzer sa = new SnowballAnalyzer(false, "English", new String[] {}, WhiteSpaceLowerCaseAnalyzer
                .getDefaultCharRemovalSet(), Constants.lexGridWhiteSpaceIndexSet);
        analyzerPerField.put("stem_propertyValue", sa);
        
       // The PerFieldAnalyzerWrapper allows me to use the proper analyzer per
        // field - if you are
        // doing a norm search, you need to analyze with a norm analyzer etc.

        // I also have a FieldSkippingAnalyzer wrapping the default
        // (WhiteSpaceLowerCaseAnalyzer) - so
        // that it doesn't tokenize on untokenized fields. But this isn't
        // necessary anymore, since
        // I'm building these parts of the query manually. But it doesn't hurt,
        // so i will leave it in.
        PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new FieldSkippingAnalyzer(
                new String[] { SQLTableConstants.TBLCOL_CODINGSCHEMENAME, SQLTableConstants.TBLCOL_ENTITYCODE,
                        SQLTableConstants.TBLCOL_ID, SQLTableConstants.TBLCOL_ENTITYTYPE, "codeBoundry",
                        SQLTableConstants.TBLCOL_ISACTIVE, SQLTableConstants.TBLCOL_ISPREFERRED,
                        SQLTableConstants.TBLCOL_PRESENTATIONFORMAT, SQLTableConstants.TBLCOL_FORMAT,
                        SQLTableConstants.TBLCOL_LANGUAGE, SQLTableConstants.TBLCOL_CONCEPTSTATUS,
                        SQLTableConstants.TBLCOL_PROPERTYID, "dataType", SQLTableConstants.TBLCOL_DEGREEOFFIDELITY,
                        SQLTableConstants.TBLCOL_REPRESENTATIONALFORM, SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT,
                        SQLTableConstants.TBLCOL_PROPERTY, SQLTableConstants.TBLCOL_PROPERTYNAME }, wslca),
                        analyzerPerField);
        
        parser_ = new QueryParser(SQLTableConstants.TBLCOL_PROPERTYVALUE, analyzer);
        
        //Allow leading wildcards for searches.
        parser_.setAllowLeadingWildcard(true);
        
        parser_.setDefaultOperator(QueryParser.AND_OPERATOR);
    }

    /*
     * white space characters need special treatment before going into the query
     * parser - needs to align with what was done during indexing. Also, I need
     * to escape ':' for them if they provided a colon but didn't escape it.
     */
    private String handleWhiteSpaceCharacters(String query) {
        int pos = query.indexOf(':');
        if (pos > 0) {
            StringBuffer temp = new StringBuffer(query);
            while (pos > 0) {
                if (temp.charAt(pos - 1) != '\\') {
                    temp.insert(pos, '\\');
                    pos++;
                }
                pos++;
                if (pos > temp.length()) {
                    pos = -1;
                } else {
                    pos = temp.indexOf(":", pos);
                }
            }
            query = temp.toString();
        }
        return QueryGenerator.removeExtraWhiteSpaceCharacters(query, extraWhiteSpaceChars_);
    }

    /*
     * special method for use in constructing a query that searches by concept
     * code
     */
    public Query parseQueryForField(String field, String text) throws LBParameterException {
        try {
            String modifiedMatchText = handleWhiteSpaceCharacters(text);
            synchronized (parser_) {
                return parser_.parse(field + ":(" + modifiedMatchText + ")");
            }
        } catch (ParseException e) {
            throw new LBParameterException("Invalid match text" + text, "matchText", text);
        }
    }
    
    public QueryParser getQueryProcessor(){
        return this.parser_;
    }
    
    public static QueryParser getQueryThreadSafeQueryParser(){
        return new IndexQueryParserFactory().getQueryProcessor();
    }
    
    public Set getExtraWhitespaceCharaters(){
        return this.extraWhiteSpaceChars_;
    }
}