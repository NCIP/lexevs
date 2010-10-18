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
package org.lexevs.dao.index.metadata;

import java.util.List;
import java.util.UUID;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.logging.LoggerFactory;

import edu.mayo.informatics.indexer.api.generators.DocumentFromStringsGenerator;
import edu.mayo.informatics.indexer.lucene.analyzers.EncoderAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.NormAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.SnowballAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.StringAnalyzer;
import edu.mayo.informatics.indexer.lucene.analyzers.WhiteSpaceLowerCaseAnalyzer;

/**
 * Base class for building a metadata index for LexFoo metadata.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class BaseMetaDataLoader {

    private DocumentFromStringsGenerator generator_ = new DocumentFromStringsGenerator();
    protected static boolean normEnabled_ = false;
    protected static boolean doubleMetaphoneEnabled_ = true;
    protected static boolean stemmingEnabled_ = true;
    private static final String normPrefix_ = "norm_";
    private static final String doubleMetaphonePrefix_ = "dm_";
    private static final String stemmingPrefix_ = "stem_";
    
    public static final String STRING_TOKEINZER_TOKEN = "<:>";
    public static final String CONCATINATED_VALUE_SPLIT_TOKEN = ":";

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public Document addProperty(
    		String codingSchemeUri,
    		String codingSchemeVersion,
    		List<String> propertyValueParents, 
    		String propertyName, 
    		String propertyValue)
            throws Exception {
        StringBuffer fields = new StringBuffer();
        generator_.startNewDocument(codingSchemeUri + CONCATINATED_VALUE_SPLIT_TOKEN + codingSchemeVersion
                + CONCATINATED_VALUE_SPLIT_TOKEN + UUID.randomUUID().toString());
        generator_.addTextField("codingSchemeRegisteredName", codingSchemeUri, true, true, false);
        fields.append("codingSchemeRegisteredName ");
        generator_.addTextField("codingSchemeVersion", codingSchemeVersion, true, true, false);
        fields.append("codingSchemeVersion ");

        // this field is used to make deletions easier.
        generator_.addTextField("codingSchemeNameVersion", codingSchemeUri + CONCATINATED_VALUE_SPLIT_TOKEN
                + codingSchemeVersion, false, true, false);
        fields.append("codingSchemeNameVersion ");

        if (propertyValueParents != null && propertyValueParents.size() > 0) {
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < propertyValueParents.size(); i++) {
                temp.append(propertyValueParents.get(i));
                if (i + 1 < propertyValueParents.size()) {
                    temp.append(STRING_TOKEINZER_TOKEN);
                }
            }
            generator_.addTextField("parentContainers", temp.toString(), true, true, true);
            fields.append("parentContainers ");
        }

        if (propertyName != null && propertyName.length() > 0) {
            generator_.addTextField("propertyName", propertyName, true, true, false);
            fields.append("propertyName ");
        }

        if (propertyValue != null && propertyValue.length() > 0) {
            generator_.addTextField("propertyValue", propertyValue, true, true, true);
            fields.append("propertyValue ");

            // This copy of the content is required for making "startsWith" or
            // "exactMatch" types of queries
            generator_.addTextField("untokenizedLCPropertyValue", propertyValue.toLowerCase(), false, true, false);
            fields.append("untokenizedLCPropertyValue ");

            if (normEnabled_) {
                generator_.addTextField(normPrefix_ + "propertyValue", propertyValue, false, true, true);
                fields.append(normPrefix_ + "propertyValue ");
            }

            if (doubleMetaphoneEnabled_) {
                generator_.addTextField(doubleMetaphonePrefix_ + "propertyValue", propertyValue, false, true, true);
                fields.append(doubleMetaphonePrefix_ + "propertyValue ");
            }

            if (stemmingEnabled_) {
                generator_.addTextField(stemmingPrefix_ + "propertyValue", propertyValue, false, true, true);
                fields.append(stemmingPrefix_ + "propertyValue ");
            }
        }

        generator_.addTextField("fields", fields.toString(), true, true, true);

        return generator_.getDocument();
    }

    public static Analyzer getMetadataAnalyzer() {
        // no stop words, default character removal set.
        PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new WhiteSpaceLowerCaseAnalyzer(new String[] {},
                WhiteSpaceLowerCaseAnalyzer.getDefaultCharRemovalSet(), LuceneLoaderCode.lexGridWhiteSpaceIndexSet));

        if (doubleMetaphoneEnabled_) {
            EncoderAnalyzer temp = new EncoderAnalyzer(new DoubleMetaphone(), new String[] {},
                    WhiteSpaceLowerCaseAnalyzer.getDefaultCharRemovalSet(), LuceneLoaderCode.lexGridWhiteSpaceIndexSet);
            analyzer.addAnalyzer(doubleMetaphonePrefix_ + "propertyValue", temp);
        }

        if (normEnabled_) {
            try {
                NormAnalyzer temp = new NormAnalyzer(false, new String[] {}, WhiteSpaceLowerCaseAnalyzer
                        .getDefaultCharRemovalSet(), LuceneLoaderCode.lexGridWhiteSpaceIndexSet);
                analyzer.addAnalyzer(normPrefix_ + "propertyValue", temp);
            } catch (NoClassDefFoundError e) {
                // norm is not available
                normEnabled_ = false;
            }
        }

        if (stemmingEnabled_) {
            SnowballAnalyzer temp = new SnowballAnalyzer(false, "English", new String[] {}, WhiteSpaceLowerCaseAnalyzer
                    .getDefaultCharRemovalSet(), LuceneLoaderCode.lexGridWhiteSpaceIndexSet);
            analyzer.addAnalyzer(stemmingPrefix_ + "propertyValue", temp);
        }

        // these fields just get simple analyzing.
        StringAnalyzer sa = new StringAnalyzer(STRING_TOKEINZER_TOKEN);
        analyzer.addAnalyzer("parentContainers", sa);

        return analyzer;
    }
}