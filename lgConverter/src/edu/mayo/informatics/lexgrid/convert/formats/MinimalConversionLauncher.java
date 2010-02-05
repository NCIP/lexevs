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
package edu.mayo.informatics.lexgrid.convert.formats;

import java.util.Iterator;

import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;
import org.LexGrid.messaging.LgMessageDirectorIF;
import org.LexGrid.util.SimpleMemUsageReporter;
import org.LexGrid.util.SimpleMemUsageReporter.Snapshot;

import edu.mayo.informatics.lexgrid.convert.directConversions.SQLToLdap;
import edu.mayo.informatics.lexgrid.convert.directConversions.SQLToSQLLite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.SQLReadWrite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.XMLRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFWrite;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridLDAP;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridSQL;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridSQLLite;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridXML;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.IndexLexGridDatabase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridLDAPOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLLiteOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.indexer.SQLIndexer;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Tool to take a pair of input and output formats, map them to the appropriate
 * conversion tool, and run the conversion.
 * 
 * This is a subset of the Conversion Launcher class - only supports a subset of
 * conversions as required by CTS. Helps trim down the package and dependencies
 * for CTS.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 3797 $ checked in on $Date: 2006-11-02
 *          20:48:47 +0000 (Thu, 02 Nov 2006) $
 */
public class MinimalConversionLauncher {
    /**
     * Execute the conversion
     * 
     * @param inputFormat
     * @param outputFormat
     * @param codingSchemes
     * @param options
     * @param md
     * @return The coding schemes that were converted - this can be useful in
     *         cases where the conversion doesn't take in any coding schemes
     *         (for example, owl). In the case of RRF, this actually returns the
     *         table names that were created.
     * @throws Exception
     */
    public static String[] startConversion(InputFormatInterface inputFormat, OutputFormatInterface outputFormat,
            String[] codingSchemes, OptionHolder options, LgMessageDirectorIF md) throws Exception {
        if (options == null) {
            options = new OptionHolder();
        }

        // Has an override flag been set to do a direct conversion with EMF
        // instead?
        Option overrideOption = options.get(Option.getNameForType(Option.DO_WITH_EMF));
        if (overrideOption != null) {
            if (((Boolean) overrideOption.getOptionValue()).booleanValue()) {
                return doEMFConversion(inputFormat, outputFormat, codingSchemes, options, md);
            }
        }

        if (inputFormat.getDescription().equals(LexGridSQL.description)) {
            // LexGridSQL inputformat
            LexGridSQL in = (LexGridSQL) inputFormat;
            if (outputFormat.getDescription().equals(IndexLexGridDatabase.description)) {
                // custom sql lucene indexer
                IndexLexGridDatabase out = (IndexLexGridDatabase) outputFormat;

                String fetchSize = getStringOption(options, Option.SQL_FETCH_SIZE);

                try {
                    int temp = Integer.parseInt(fetchSize);
                    Constants.mySqlBatchSize = temp;
                } catch (NumberFormatException e) {
                    md.info("Invalid SQL Fetch Size - using default");
                }

                boolean buildDBMeta = getBooleanOption(options, Option.BUILD_DB_METAPHONE);
                boolean useCompoundFormat = getBooleanOption(options, Option.USE_COMPOUND_FMT);
                boolean buildStem = getBooleanOption(options, Option.BUILD_STEM);

                edu.mayo.informatics.indexer.lucene.analyzers.NormAnalyzer.LVG_CONFIG_FILE_ABSOLUTE = out
                        .getNormConfigFile();

                new SQLIndexer(out.getIndexName(), out.getIndexLocation(), in.getUsername(), in.getPassword(), in
                        .getServer(), in.getDriver(), in.getTablePrefix(), codingSchemes, md, out.isNormEnabled(),
                        buildDBMeta, buildStem, useCompoundFormat);
                return codingSchemes;
            } else if (outputFormat.getDescription().equals(LexGridSQLLiteOut.description)) {
                // custom SQL -> sql lite converter.

                boolean enforceIntegrity;
                try {
                    enforceIntegrity = getBooleanOption(options, Option.ENFORCE_INTEGRITY);

                    String fetchSize = getStringOption(options, Option.SQL_FETCH_SIZE);

                    try {
                        int temp = Integer.parseInt(fetchSize);
                        Constants.mySqlBatchSize = temp;
                    } catch (NumberFormatException e) {
                        md.info("Invalid SQL Fetch Size - using default");
                    }
                } catch (RuntimeException e) {
                    throw new Exception("Converter implementation error - required option is missing");
                }

                LexGridSQLLiteOut out = (LexGridSQLLiteOut) outputFormat;
                for (int i = 0; i < codingSchemes.length; i++) {
                    new SQLToSQLLite(out.getServer(), out.getDriver(), out.getUsername(), out.getPassword(), in
                            .getServer(), in.getDriver(), in.getUsername(), in.getPassword(), in.getTablePrefix(),
                            codingSchemes[i], md, enforceIntegrity);
                }
                return codingSchemes;

            } else if (outputFormat.getDescription().equals(LexGridLDAPOut.description)) {
                // custom sql to ldap converter
                LexGridLDAPOut out = (LexGridLDAPOut) outputFormat;
                boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);

                String fetchSize = getStringOption(options, Option.SQL_FETCH_SIZE);

                try {
                    int temp = Integer.parseInt(fetchSize);
                    Constants.mySqlBatchSize = temp;
                } catch (NumberFormatException e) {
                    md.info("Invalid SQL Fetch Size - using default");
                }

                for (int i = 0; i < codingSchemes.length; i++) {
                    new SQLToLdap(in.getServer(), in.getDriver(), in.getUsername(), in.getPassword(), in
                            .getTablePrefix(), out.getUsername(), out.getPassword(), out.getAddress(), out
                            .getServiceDN(), codingSchemes[i], failOnErrors, md);

                }
                return codingSchemes;
            }
        }

        // End of specific direct conversions. Havent found a match yet - can we
        // do it with EMF?
        return doEMFConversion(inputFormat, outputFormat, codingSchemes, options, md);

    }

    private static String[] doEMFConversion(InputFormatInterface inputFormat, OutputFormatInterface outputFormat,
            String[] codingSchemes, OptionHolder options, LgMessageDirectorIF md) throws Exception {
        // TODO figure out how to pass the read size options into EMF - EMF
        // issue.
        EMFRead emfIn;
        if (inputFormat.getDescription().equals(LexGridSQL.description)) {
            LexGridSQL in = (LexGridSQL) inputFormat;

            boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);

            String fetchSize = getStringOption(options, Option.SQL_FETCH_SIZE);

            try {
                int temp = Integer.parseInt(fetchSize);
                Constants.mySqlBatchSize = temp;
            } catch (NumberFormatException e) {
                md.info("Invalid SQL Fetch Size - using default");
            }

            emfIn = new SQLReadWrite(in.getServer(), in.getDriver(), in.getUsername(), in.getPassword(), in
                    .getTablePrefix(), failOnErrors, md, URNVersionPair
                    .stringArrayToNullVersionPairArray(codingSchemes));
        } else if (inputFormat.getDescription().equals(LexGridXML.description)) {
            LexGridXML in = (LexGridXML) inputFormat;
            boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);
            emfIn = new XMLRead(in.getFileLocation(), in.getCodingSchemeManifest(), md, failOnErrors);
        } else {
            throw new Exception("The conversion that you are attempting to do is not yet possible.");
        }
        return doEMFConversionHelper(emfIn, outputFormat, codingSchemes, options, md);
    }

    private static String[] doEMFConversionHelper(EMFRead emfIn, OutputFormatInterface outputFormat,
            String[] codingSchemes, OptionHolder options, LgMessageDirectorIF md) throws Exception {
        EMFWrite emfOut = null;
        if (outputFormat.getDescription().equals(LexGridSQLOut.description)) {
            LexGridSQLOut out = (LexGridSQLOut) outputFormat;

            boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);

            emfOut = new SQLReadWrite(out.getServer(), out.getDriver(), out.getUsername(), out.getPassword(), out
                    .getTablePrefix(), failOnErrors, md, URNVersionPair
                    .stringArrayToNullVersionPairArray(codingSchemes));
        } else {
            throw new Exception("The conversion that you are attempting to do is not yet possible.");
        }

        md
                .info("This is an in-memory transformation - you need to have enough RAM to hold the entire coding scheme in memory at once.");

        String[] convertedCodingSchemes;

        try {
            if (codingSchemes == null || codingSchemes.length == 0) {
                throw new UnsupportedOperationException();
            }
            for (int i = 0; i < codingSchemes.length; i++) {
                // Primary read/write ...
                long ms = System.currentTimeMillis();
                md.info("Reading from source of " + codingSchemes[i] + "...");
                CodingScheme temp = emfIn.readCodingScheme(codingSchemes[i]);
                md.info("Read Time (ms): " + (System.currentTimeMillis() - ms));

                ms = System.currentTimeMillis();
                md.info("Clearing target of " + codingSchemes[i] + "...");
                emfOut.clearCodingScheme(codingSchemes[i]);
                md.info("Clear Time (ms): " + (System.currentTimeMillis() - ms));

                ms = System.currentTimeMillis();
                md.info("Writing to target...");
                emfOut.writeCodingScheme(temp);
                md.info("Write Time (ms): " + (System.currentTimeMillis() - ms));

                // Stream remaining content if available ...
                doStreamContent(temp, emfIn, emfOut, md, ms);
            }
            convertedCodingSchemes = codingSchemes;
        } catch (UnsupportedOperationException e) {
            try {
                // read of a particular coding scheme is not implemented - lets
                // try reading without specifying
                // the coding scheme (obo and nci reader do this)
                long ms = System.currentTimeMillis();
                md.info("Reading from source...");
                SimpleMemUsageReporter.init();
                Snapshot snap1 = SimpleMemUsageReporter.snapshot();

                CodingScheme temp = emfIn.readCodingScheme();
                Snapshot snap2 = SimpleMemUsageReporter.snapshot();
                md.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap2.getTimeDelta(snap1))
                        + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsage()) + " Heap Delta:"
                        + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsageDelta(snap1)));

                ms = System.currentTimeMillis();
                md.info("Clearing target of " + temp.getCodingSchemeName() + "...");
                String codingSchemeName = temp.getCodingSchemeName();
                emfOut.clearCodingScheme(codingSchemeName);
                md.info("Clear Time (ms): " + (System.currentTimeMillis() - ms));

                ms = System.currentTimeMillis();
                md.info("Writing " + temp.getCodingSchemeName() + " to target...");
                snap1 = SimpleMemUsageReporter.snapshot();
                emfOut.writeCodingScheme(temp);
                snap2 = SimpleMemUsageReporter.snapshot();
                md.info("Write Time : " + SimpleMemUsageReporter.formatTimeDiff(snap2.getTimeDelta(snap1))
                        + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsage()) + " Heap Delta:"
                        + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsageDelta(snap1)));
                convertedCodingSchemes = new String[] { codingSchemeName };

                // Stream remaining content if available ...
                doStreamContent(temp, emfIn, emfOut, md, ms);
            } catch (UnsupportedOperationException e1) {
                // read without specifying a coding scheme also not implemented.
                // try reading all
                // xml reader does this
                long ms = System.currentTimeMillis();
                // md.info("Reading from source..."); //don't need to say this -
                // it will have been
                // said above - before the exception.
                CodingScheme[] allCodingSchemes = emfIn.readAllCodingSchemes();
                convertedCodingSchemes = new String[allCodingSchemes.length];
                md.info("Read Time (ms): " + (System.currentTimeMillis() - ms));

                for (int i = 0; i < allCodingSchemes.length; i++) {
                    ms = System.currentTimeMillis();
                    md.info("Clearing target of " + allCodingSchemes[i].getCodingSchemeName() + "...");
                    emfOut.clearCodingScheme(allCodingSchemes[i].getCodingSchemeName());
                    convertedCodingSchemes[i] = allCodingSchemes[i].getCodingSchemeName();
                    md.info("Clear Time (ms): " + (System.currentTimeMillis() - ms));

                    ms = System.currentTimeMillis();
                    md.info("Writing " + allCodingSchemes[i].getCodingSchemeName() + " to target...");
                    emfOut.writeCodingScheme(allCodingSchemes[i]);
                    md.info("Write Time (ms): " + (System.currentTimeMillis() - ms));

                    // Stream remaining content if available ...
                    doStreamContent(allCodingSchemes[i], emfIn, emfOut, md, ms);
                }
            }
        }

        md.info("Conversion Finished.");
        return convertedCodingSchemes;
    }

    private static void doStreamContent(CodingScheme temp, EMFRead emfIn, EMFWrite emfOut, LgMessageDirectorIF md,
            long ms) throws Exception {
        // Is this an incremental read/write of associations and concepts?
        if (emfIn.supportsStreamedRead(temp))
            try {
                md.info("Streaming associations to storage...");
                for (Iterator relationsContainers = temp.getRelations().iterator(); relationsContainers.hasNext();) {
                    Relations relationsContainer = (Relations) relationsContainers.next();
                    // Fill in the content ...
                    for (Iterator associations = emfIn.streamedReadOnAssociations(temp, relationsContainer); associations
                            .hasNext();) {
                        // Association container first ...
                        Association association = (Association) associations.next();
                        emfOut.streamedWriteOnAssociation(temp, relationsContainer, association);
                        // Add sources & targets ...
                        Iterator sources = emfIn.streamedReadOnAssociationInstances(temp, relationsContainer,
                                association);
                        emfOut.streamedWriteOnAssociationInstances(temp, relationsContainer, association, sources);
                    }
                }
                md.info("Write Time (ms): " + (System.currentTimeMillis() - ms));

                md.info("Streaming concepts to storage...");
                Entities conceptsContainer = temp.getEntities();
                Iterator concepts = emfIn.streamedReadOnConcepts(temp, conceptsContainer);
                emfOut.streamedWriteOnConcepts(temp, conceptsContainer, concepts);
                md.info("Write Time (ms): " + (System.currentTimeMillis() - ms));

            } finally {
                try {
                    emfIn.closeStreamedRead();
                } catch (Exception ex) {
                }
                try {
                    emfOut.closeStreamedWrite();
                } catch (Exception ex) {
                }
            }
    }

    public static boolean willBeDoneWithEMF(InputFormatInterface inputFormat, OutputFormatInterface outputFormat,
            OptionHolder options) {
        if (inputFormat == null || outputFormat == null) {
            return false;
        }

        if (options == null) {
            options = new OptionHolder();
        }

        // Has an override flag been set to do a direct conversion with EMF
        // instead?
        Option overrideOption = options.get(Option.getNameForType(Option.DO_WITH_EMF));
        if (overrideOption != null) {
            if (((Boolean) overrideOption.getOptionValue()).booleanValue()) {
                return true;
            }
        }

        // first, figure out if it is a specific (non emf) conversion
        if (inputFormat.getDescription().equals(LexGridLDAP.description)) {
            if (outputFormat.getDescription().equals(LexGridSQLLiteOut.description)) {
                return false;
            } else if (outputFormat.getDescription().equals(IndexLexGridDatabase.description)) {
                return false;
            }
        } else if (inputFormat.getDescription().equals(LexGridSQLLite.description)) {
            // LexGridSQLLite inputformat
            if (outputFormat.getDescription().equals(LexGridLDAPOut.description)) {
                return false;
            }
        }

        else if (inputFormat.getDescription().equals(LexGridSQL.description)) {
            // LexGridSQL inputformat
            if (outputFormat.getDescription().equals(LexGridLDAPOut.description)) {
                return false;
            } else if (outputFormat.getDescription().equals(LexGridSQLLiteOut.description)) {
                return false;
            } else if (outputFormat.getDescription().equals(IndexLexGridDatabase.description)) {
                return false;
            }
        }

        // End of specific direct conversions. Havent found a match yet - can we
        // do it with EMF?
        return true;
    }

    private static String getStringOption(OptionHolder options, int option) throws Exception {
        try {
            return ((String) options.get(Option.getNameForType(option)).getOptionValue());
        } catch (RuntimeException e) {
            throw new Exception(
                    "Converter implementation error - required option is missing - needs a string option of "
                            + Option.getNameForType(option));
        }
    }

    private static boolean getBooleanOption(OptionHolder options, int option) throws Exception {
        try {
            return ((Boolean) options.get(Option.getNameForType(option)).getOptionValue()).booleanValue();
        } catch (RuntimeException e) {
            throw new Exception(
                    "Converter implementation error - required option is missing - needs a string option of "
                            + Option.getNameForType(option));
        }
    }
}