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
package org.lexevs.dao.index.metadata;

import java.util.ArrayList;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.logging.LoggerFactory;

import edu.mayo.informatics.indexer.api.IndexerService;
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
public class BaseMetaDataLoader extends MetaDataIndex {
    private String metaDataIndexName_;
    private IndexerService indexerService_;
    protected boolean useCompoundFile_ = true;
    private DocumentFromStringsGenerator generator_;
    private int indexDocCount = 0;
    protected boolean normEnabled_ = false;
    protected boolean doubleMetaphoneEnabled_ = true;
    protected boolean stemmingEnabled_ = true;
    private final String normPrefix_ = "norm_";
    private final String doubleMetaphonePrefix_ = "dm_";
    private final String stemmingPrefix_ = "stem_";

    private String codingSchemeRegisteredName_;
    private String codingSchemeVersion_;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public void init() {
        metaDataIndexName_ = getIndexName();
        indexerService_ = getIndexerService();
        initLoader();
    }

    protected void openIndex(String codingSchemeRegisteredName, String codingSchemeVersion) throws Exception {
        codingSchemeRegisteredName_ = codingSchemeRegisteredName;
        codingSchemeVersion_ = codingSchemeVersion;

        indexerService_.openWriter(metaDataIndexName_, false);
        indexerService_.setUseCompoundFile(metaDataIndexName_, useCompoundFile_);
        indexerService_.setMaxBufferedDocs(metaDataIndexName_, 500);
        indexerService_.setMergeFactor(metaDataIndexName_, 20);
    }

    protected void closeIndexes() throws Exception {
        indexerService_.optimizeIndex(metaDataIndexName_);
        indexerService_.closeWriter(metaDataIndexName_);
    }

    protected void addProperty(ArrayList<String> propertyValueParents, String propertyName, String propertyValue)
            throws Exception {
        StringBuffer fields = new StringBuffer();
        generator_.startNewDocument(codingSchemeRegisteredName_ + CONCATINATED_VALUE_SPLIT_TOKEN + codingSchemeVersion_
                + CONCATINATED_VALUE_SPLIT_TOKEN + indexDocCount);
        generator_.addTextField("codingSchemeRegisteredName", codingSchemeRegisteredName_, true, true, false);
        fields.append("codingSchemeRegisteredName ");
        generator_.addTextField("codingSchemeVersion", codingSchemeVersion_, true, true, false);
        fields.append("codingSchemeVersion ");

        // this field is used to make deletions easier.
        generator_.addTextField("codingSchemeNameVersion", codingSchemeRegisteredName_ + CONCATINATED_VALUE_SPLIT_TOKEN
                + codingSchemeVersion_, false, true, false);
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

        indexerService_.addDocument(metaDataIndexName_, generator_.getDocument());

        indexDocCount++;
    }

    private void initLoader() {
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
                getLogger()
                        .warn(
                                "Normalized index will not be built becaues Norm could not be launched.  Is Norm (lvg) on the classpath?",
                                e);
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

        // in case it doesn't exist
        indexerService_.createIndex(metaDataIndexName_, analyzer);

        generator_ = new DocumentFromStringsGenerator();
    }
}