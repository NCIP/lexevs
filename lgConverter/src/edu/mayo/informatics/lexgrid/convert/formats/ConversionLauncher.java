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

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;
import org.LexGrid.messaging.LgMessageDirectorIF;
import org.LexGrid.util.SimpleMemUsageReporter;
import org.LexGrid.util.SimpleMemUsageReporter.Snapshot;
import org.LexGrid.util.config.parameter.StringParameter;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLLiteTableUtilities;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.LexGrid.util.sql.lgTables.TransitiveClosure;

import edu.mayo.informatics.LexGridServicesIndex.LexGridServiceAdmin;
import edu.mayo.informatics.lexgrid.convert.directConversions.LdapToSQLLite;
import edu.mayo.informatics.lexgrid.convert.directConversions.MetaThesaurusToSQL;
import edu.mayo.informatics.lexgrid.convert.directConversions.NCIThesaurusHistoryFileToSQL;
import edu.mayo.informatics.lexgrid.convert.directConversions.SQLLiteToLdap;
import edu.mayo.informatics.lexgrid.convert.directConversions.SQLToLdap;
import edu.mayo.informatics.lexgrid.convert.directConversions.SQLToSQLLite;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextToSQL;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextToSQLLite;
import edu.mayo.informatics.lexgrid.convert.directConversions.UMLSHistoryFileToSQL;
import edu.mayo.informatics.lexgrid.convert.directConversions.UMLSToSQL;
import edu.mayo.informatics.lexgrid.convert.directConversions.UmlsCommon.LoadRRFToDB;
import edu.mayo.informatics.lexgrid.convert.emfConversions.HL7ReadWrite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.OboReadWrite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.OwlReadWrite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.ProtegeFramesRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.SNODENTRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.SQLReadWrite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.SemNetRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.XMLRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.XMLWrite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFWrite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.protegeOwl.ProtegeOwl2EMFConstants;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.HL7SQL;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridDelimitedText;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridLDAP;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridSQL;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridSQLLite;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridXML;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.NCIMetaThesaurusSQL;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.NCIThesaurusHistoryFile;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.OBO;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.Owl;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.ProtegeFrames;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.RRFFiles;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.SemNetFiles;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.SnodentSQL;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.UMLSHistoryFile;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.UMLSSQL;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.ComputeTransitiveExpansionTable;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.DeleteLexGridTerminology;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.IndexLexGridDatabase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridLDAPOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLLiteOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridXMLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.OBOOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.RegisterLexGridTerminology;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.SQLOut;
import edu.mayo.informatics.lexgrid.convert.indexer.LdapIndexer;
import edu.mayo.informatics.lexgrid.convert.indexer.SQLIndexer;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Tool to take a pair of input and output formats, map them to the appropriate
 * conversion tool, and run the conversion.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 */
public class ConversionLauncher {
    private static EMFRead emfIn = null;

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
    @SuppressWarnings("unchecked")
    public static URNVersionPair[] startConversion(InputFormatInterface inputFormat,
            OutputFormatInterface outputFormat, String[] codingSchemes, OptionHolder options, LgMessageDirectorIF md)
            throws Exception {
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

        if (outputFormat.getDescription().equals(DeleteLexGridTerminology.description)) {
            return URNVersionPair.stringArrayToNullVersionPairArray(doDelete(inputFormat, codingSchemes, options, md));
        }

        if (outputFormat.getDescription().equals(ComputeTransitiveExpansionTable.description)) {
            return URNVersionPair.stringArrayToNullVersionPairArray(doTransitiveExpansion(inputFormat, codingSchemes,
                    options, md));
        }

        if (outputFormat.getDescription().equals(RegisterLexGridTerminology.description)) {
            return URNVersionPair.stringArrayToNullVersionPairArray(doRegister(inputFormat, outputFormat,
                    codingSchemes, options, md));
        }

        // first, figure out if it is a specific (non emf) conversion
        if (inputFormat.getDescription().equals(LexGridLDAP.description)) {
            // LDAP input
            LexGridLDAP in = (LexGridLDAP) inputFormat;

            if (outputFormat.getDescription().equals(LexGridSQLLiteOut.description)) {
                // custom LDAP -> sql lite converter.

                boolean enforceIntegrity = getBooleanOption(options, Option.ENFORCE_INTEGRITY);
                String pageSize = getStringOption(options, Option.LDAP_PAGE_SIZE);
                try {
                    int temp = Integer.parseInt(pageSize);
                    Constants.ldapPageSize = temp;
                } catch (NumberFormatException e) {
                    md.info("Invalid LDAP Page size - using default");
                }

                LexGridSQLLiteOut out = (LexGridSQLLiteOut) outputFormat;
                for (int i = 0; i < codingSchemes.length; i++) {
                    new LdapToSQLLite(out.getServer(), out.getDriver(), out.getUsername(), out.getPassword(), in
                            .getUsername(), in.getPassword(), in.getAddress(), in.getServiceDN(), codingSchemes[i],
                            enforceIntegrity, md);
                }
                return URNVersionPair.stringArrayToNullVersionPairArray(codingSchemes);

            } else if (outputFormat.getDescription().equals(IndexLexGridDatabase.description)) {
                // Build an index from a LDAP database

                String pageSize = getStringOption(options, Option.LDAP_PAGE_SIZE);
                try {
                    int temp = Integer.parseInt(pageSize);
                    Constants.ldapPageSize = temp;
                } catch (NumberFormatException e) {
                    md.info("Invalid LDAP Page size - using default");
                }

                boolean buildDBMeta = getBooleanOption(options, Option.BUILD_DB_METAPHONE);
                boolean useCompoundFormat = getBooleanOption(options, Option.USE_COMPOUND_FMT);
                boolean buildStem = getBooleanOption(options, Option.BUILD_STEM);

                IndexLexGridDatabase out = (IndexLexGridDatabase) outputFormat;

                edu.mayo.informatics.indexer.lucene.analyzers.NormAnalyzer.LVG_CONFIG_FILE_ABSOLUTE = out
                        .getNormConfigFile();

                new LdapIndexer(out.getIndexName(), out.getIndexLocation(), in.getUsername(), in.getPassword(), in
                        .getAddress(), in.getServiceDN(), codingSchemes, md, out.isNormEnabled(), buildDBMeta,
                        buildStem, useCompoundFormat);
                return URNVersionPair.stringArrayToNullVersionPairArray(codingSchemes);
            }
        } else if (inputFormat.getDescription().equals(NCIMetaThesaurusSQL.description)) {
            // metathesaurus input format
            NCIMetaThesaurusSQL in = (NCIMetaThesaurusSQL) inputFormat;
            if (outputFormat.getDescription().equals(LexGridSQLOut.description)) {
                // custom nci metathsaurus to sql converter
                LexGridSQLOut out = (LexGridSQLOut) outputFormat;

                boolean enforceIntegrity = getBooleanOption(options, Option.ENFORCE_INTEGRITY);
                boolean rootRecalc = getBooleanOption(options, Option.ROOT_RECALC);

                String fetchSize = getStringOption(options, Option.SQL_FETCH_SIZE);

                try {
                    int temp = Integer.parseInt(fetchSize);
                    Constants.mySqlBatchSize = temp;
                } catch (NumberFormatException e) {
                    md.info("Invalid SQL Fetch Size - using default");
                }

                return URNVersionPair.stringArrayToNullVersionPairArray(new String[] { new MetaThesaurusToSQL(out
                        .getServer(), out.getDriver(), out.getUsername(), out.getPassword(), out.getTablePrefix(), in
                        .getServer(), in.getDriver(), in.getUsername(), in.getPassword(), in.getLoaderPreferences(), in
                        .getManifestLocation(), enforceIntegrity, rootRecalc, md).getCodingSchemeName() });
            }
        } else if (inputFormat.getDescription().equals(LexGridSQLLite.description)) {
            // LexGridSQLLite inputformat
            LexGridSQLLite in = (LexGridSQLLite) inputFormat;
            if (outputFormat.getDescription().equals(LexGridLDAPOut.description)) {
                // custom sql lite to ldap converter
                LexGridLDAPOut out = (LexGridLDAPOut) outputFormat;
                boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);

                for (int i = 0; i < codingSchemes.length; i++) {
                    new SQLLiteToLdap(in.getServer(), in.getDriver(), in.getUsername(), in.getPassword(), out
                            .getUsername(), out.getPassword(), out.getAddress(), out.getServiceDN(), codingSchemes[i],
                            failOnErrors, md);
                }
                return URNVersionPair.stringArrayToNullVersionPairArray(codingSchemes);
            }
        }

        else if (inputFormat.getDescription().equals(LexGridSQL.description)) {
            // LexGridSQL inputformat
            LexGridSQL in = (LexGridSQL) inputFormat;
            if (outputFormat.getDescription().equals(LexGridLDAPOut.description)) {
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
                return URNVersionPair.stringArrayToNullVersionPairArray(codingSchemes);
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
                return URNVersionPair.stringArrayToNullVersionPairArray(codingSchemes);

            } else if (outputFormat.getDescription().equals(IndexLexGridDatabase.description)) {
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
                return URNVersionPair.stringArrayToNullVersionPairArray(codingSchemes);
            }
        } else if (inputFormat.getDescription().equals(LexGridDelimitedText.description)) {
            // LexGrid text file input
            LexGridDelimitedText in = (LexGridDelimitedText) inputFormat;

            if (outputFormat.getDescription().equals(LexGridSQLOut.description)) {
                LexGridSQLOut out = (LexGridSQLOut) outputFormat;
                boolean forceFormatB = getBooleanOption(options, Option.FORCE_FORMAT_B);
                String delimeter = getStringOption(options, Option.DELIMITER);

                TextToSQL textToSQL = new TextToSQL(in.getFileLocation(), delimeter, out.getServer(), out.getDriver(),
                        out.getUsername(), out.getPassword(), out.getTablePrefix(), in.getLoaderPreferences(), md,
                        forceFormatB);

                return new URNVersionPair[] { new URNVersionPair(textToSQL.getCodingSchemeName(), textToSQL
                        .getVersion()) };

            } else if (outputFormat.getDescription().equals(LexGridSQLLiteOut.description)) {
                LexGridSQLLiteOut out = (LexGridSQLLiteOut) outputFormat;
                boolean forceFormatB = getBooleanOption(options, Option.FORCE_FORMAT_B);
                String delimeter = getStringOption(options, Option.DELIMITER);

                return URNVersionPair.stringArrayToNullVersionPairArray(new String[] { new TextToSQLLite(in
                        .getFileLocation(), delimeter, out.getServer(), out.getDriver(), out.getUsername(), out
                        .getPassword(), in.getLoaderPreferences(), md, forceFormatB).getCodingSchemeName() });
            }
        } else if (inputFormat.getDescription().equals(NCIThesaurusHistoryFile.description)) {
            // LexGrid text file input
            NCIThesaurusHistoryFile in = (NCIThesaurusHistoryFile) inputFormat;

            if (outputFormat.getDescription().equals(LexGridSQLOut.description)) {
                LexGridSQLOut out = (LexGridSQLOut) outputFormat;
                boolean failOnError = getBooleanOption(options, Option.FAIL_ON_ERROR);
                String delimeter = getStringOption(options, Option.DELIMITER);

                return URNVersionPair
                        .stringArrayToNullVersionPairArray(new String[] { new NCIThesaurusHistoryFileToSQL(in
                                .getFileLocation(), in.getHistoryVersionFileLocation(), delimeter, failOnError, out
                                .getServer(), out.getDriver(), out.getUsername(), out.getPassword(), out
                                .getTablePrefix(), md).getCodingSchemeName() });

            }
        } else if (inputFormat.getDescription().equals(UMLSHistoryFile.description)) {
            // LexGrid text file input
            UMLSHistoryFile in = (UMLSHistoryFile) inputFormat;

            if (outputFormat.getDescription().equals(LexGridSQLOut.description)) {
                LexGridSQLOut out = (LexGridSQLOut) outputFormat;
                boolean failOnError = getBooleanOption(options, Option.FAIL_ON_ERROR);
                String delimeter = getStringOption(options, Option.DELIMITER);

                UMLSHistoryFileToSQL UMLSHistFileToSQL = new UMLSHistoryFileToSQL(failOnError, md, delimeter);
                try {
                    UMLSHistFileToSQL.prepareDatabase(out.getServer(), out.getDriver(), out.getUsername(), out
                            .getPassword(), out.getTablePrefix());
                    UMLSHistFileToSQL.loadUMLSHistory(in.getFileLocation());
                } catch (Exception e) {
                    md.error("Exception in NCI MetaThesaurus History Loader: " + e.getMessage());
                } finally {
                    UMLSHistFileToSQL.closeDBConnection();
                }
                return URNVersionPair.stringArrayToNullVersionPairArray(new String[] { UMLSHistFileToSQL
                        .getCodingSchemeName() });
            }
        } else if (inputFormat.getDescription().equals(RRFFiles.description)) {
            // RRF Directory input
            RRFFiles in = (RRFFiles) inputFormat;

            if (outputFormat.getDescription().equals(SQLOut.description)) {
                SQLOut out = (SQLOut) outputFormat;
                boolean skipNonLexGridFiles = getBooleanOption(options, Option.SKIP_NON_LEXGRID_FILES);
                boolean rootRecalc = getBooleanOption(options, Option.ROOT_RECALC);

                return URNVersionPair.stringArrayToNullVersionPairArray(LoadRRFToDB.loadRRFToDB(in.getFileLocation(),
                        skipNonLexGridFiles, rootRecalc, out.getServer(), out.getDriver(), out.getUsername(), out
                                .getPassword(), md));
            }
        } else if (inputFormat.getDescription().equals(UMLSSQL.description)) {
            // UMLS SQL input
            UMLSSQL in = (UMLSSQL) inputFormat;
            if (outputFormat.getDescription().equals(LexGridSQLOut.description)) {
                // UMLS to SQL custom converter
                LexGridSQLOut out = (LexGridSQLOut) outputFormat;

                boolean enforceIntegrity = getBooleanOption(options, Option.ENFORCE_INTEGRITY);

                String fetchSize = getStringOption(options, Option.SQL_FETCH_SIZE);

                try {
                    int temp = Integer.parseInt(fetchSize);
                    Constants.mySqlBatchSize = temp;
                } catch (NumberFormatException e) {
                    md.info("Invalid SQL Fetch Size - using default");
                }

                String[] loadedCodingSchemes = new String[codingSchemes.length];
                for (int i = 0; i < codingSchemes.length; i++) {
                    String temp;
                    // This GUI populates the codingSchemes box with a
                    // combination string - I just
                    // want the first part. Others don't....
                    if (codingSchemes[i].indexOf(" --") > 0) {
                        temp = codingSchemes[i].substring(0, codingSchemes[i].indexOf(" --"));
                    } else {
                        temp = codingSchemes[i];
                    }

                    loadedCodingSchemes[i] = new UMLSToSQL(out.getServer(), out.getDriver(), out.getUsername(), out
                            .getPassword(), out.getTablePrefix(), in.getServer(), in.getDriver(), in.getUsername(), in
                            .getPassword(), temp, in.getLoaderPreferences(), in.getManifestLocation(),
                            enforceIntegrity, md).getLoadedCodingSchemeName();
                }
                return URNVersionPair.stringArrayToNullVersionPairArray(loadedCodingSchemes);
            }
        }

        // End of specific direct conversions. Havent found a match yet - can we
        // do it with EMF?
        return doEMFConversion(inputFormat, outputFormat, codingSchemes, options, md);

    }

    private static URNVersionPair[] doEMFConversion(InputFormatInterface inputFormat,
            OutputFormatInterface outputFormat, String[] codingSchemes, OptionHolder options, LgMessageDirectorIF md)
            throws Exception {
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
                    .getTablePrefix(), in.getLoaderPreferences(), failOnErrors, md, URNVersionPair
                    .stringArrayToNullVersionPairArray(codingSchemes));
        } else if (inputFormat.getDescription().equals(LexGridXML.description)) {
            LexGridXML in = (LexGridXML) inputFormat;
            boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);
            emfIn = new XMLRead(in.getFileLocation(), in.getCodingSchemeManifest(), md, failOnErrors);
        } else if (inputFormat.getDescription().equals(Owl.description)) {
            Owl in = (Owl) inputFormat;
            LexGridSQLOut emfOut = (LexGridSQLOut) outputFormat;

            boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);
            int memorySafe = getIntOption(options, Option.MEMORY_SAFE);

            OwlReadWrite owl_rw = new OwlReadWrite(in.getFileLocation(), in.getCodingSchemeManifest(), in
                    .getLoaderPreferences(), failOnErrors, memorySafe, false, md);
            owl_rw.setCodingSchemeManifest(in.getCodingSchemeManifest());
            emfIn = owl_rw;
        } else if (inputFormat.getDescription().equals(OBO.description)) {
            OBO in = (OBO) inputFormat;
            boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);
            OboReadWrite obo_rw = new OboReadWrite(in.getFileLocation(), in.getLoaderPreferences(), failOnErrors,
                    false, md);
            obo_rw.setCodingSchemeManifest(in.getCodingSchemeManifest());
            emfIn = obo_rw;
        } else if (inputFormat.getDescription().equals(ProtegeFrames.description)) {
            ProtegeFrames in = (ProtegeFrames) inputFormat;
            boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);
            ProtegeFramesRead protegeFrame = new ProtegeFramesRead(in.getFileLocation(), in.getLoaderPreferences(),
                    failOnErrors, false, md);
            protegeFrame.setCodingSchemeManifest(in.getCodingSchemeManifest());
            emfIn = protegeFrame;
        } else if (inputFormat.getDescription().equals(SemNetFiles.description)) {
            SemNetFiles in = (SemNetFiles) inputFormat;
            SemNetRead semNet = new SemNetRead(in.getFileLocation(), in.getLoaderPreferences(), md);
            semNet.setCodingSchemeManifest(in.getCodingSchemeManifest());
            emfIn = semNet;

        } else if (inputFormat.getDescription().equals(HL7SQL.description)) {
            HL7SQL in = (HL7SQL) inputFormat;
            emfIn = new HL7ReadWrite(in.getPath().toString(), in.getCurrentCodingScheme(), in.getLoaderPreferences(),
                    false, false, md);
            ((HL7ReadWrite) emfIn).setCodingSchemeManifest(in.getCodingSchemeManifest());
            ((HL7ReadWrite) emfIn).setLoaderPreferences(in.getLoaderPreferences()); // CRS
        } else if (inputFormat.getDescription().equals(SnodentSQL.description)) {
            SnodentSQL in = (SnodentSQL) inputFormat;
            emfIn = new SNODENTRead(in.getServer(), in.getDriver(), in.getUsername(), in.getPassword(), in
                    .getLoaderPreferences(), md);
        } else {
            throw new Exception("The conversion that you are attempting to do is not yet possible.");
        }

        return emfIn.getUrnVersionPairs();
    }

    public static URNVersionPair[] finishConversion(InputFormatInterface inputFormat,
            OutputFormatInterface outputFormat, URNVersionPair[] codingSchemes, OptionHolder options,
            LgMessageDirectorIF md) throws Exception {

        if (inputFormat.getDescription().equals(LexGridDelimitedText.description)) {
            return codingSchemes;
        }

        // this only applies to EMF loaders at the moment
        if (emfIn == null) {
            return null;
        }

        EMFWrite emfOut = null;
        if (outputFormat.getDescription().equals(LexGridSQLOut.description)) {
            LexGridSQLOut out = (LexGridSQLOut) outputFormat;

            boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);

            emfOut = new SQLReadWrite(out.getServer(), out.getDriver(), out.getUsername(), out.getPassword(), out
                    .getTablePrefix(), failOnErrors, md, codingSchemes);

            if (inputFormat.getDescription().equals(Owl.description))
                ((OwlReadWrite) emfIn).setEmfOut((SQLReadWrite) emfOut);
        } else if (outputFormat.getDescription().equals(LexGridXMLOut.description)) {
            boolean overwrite = getBooleanOption(options, Option.OVERWRITE);
            boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);
            LexGridXMLOut out = (LexGridXMLOut) outputFormat;
            emfOut = new XMLWrite(out.getFolderLocation(), overwrite, failOnErrors, md);
        } else if (outputFormat.getDescription().equals(OBOOut.description)) {
            boolean failOnErrors = getBooleanOption(options, Option.FAIL_ON_ERROR);
            boolean overwrite = getBooleanOption(options, Option.OVERWRITE);
            OBOOut out = (OBOOut) outputFormat;
            emfOut = new OboReadWrite(new File(out.getFolderLocation()).toURI(), failOnErrors, overwrite, md);
        } else {
            throw new Exception("The conversion that you are attempting to do is not yet possible.");
        }

        if (!inputFormat.getDescription().equals(Owl.description)
                || getIntOption(options, Option.MEMORY_SAFE) != ProtegeOwl2EMFConstants.MEMOPT_STREAMING_PROTEGE_DB_AND_LEXGRID_DIRECT_DB) {
            md
                    .info("This is an in-memory transformation - you need to have enough RAM to hold the entire coding scheme in memory at once.");
        }

        URNVersionPair[] convertedCodingSchemes;

        try {
            if (codingSchemes == null || codingSchemes.length == 0) {
                throw new UnsupportedOperationException();
            }
            for (int i = 0; i < codingSchemes.length; i++) {
                // Primary read/write ...
                long ms = System.currentTimeMillis();
                md.info("Reading from source of " + codingSchemes[i].getUrn() + "...");
                CodingScheme temp = emfIn.readCodingScheme(codingSchemes[i].getUrn());
                md.info("Read Time (ms): " + (System.currentTimeMillis() - ms));

                if (!emfIn.getStreamingOn()) {
                    ms = System.currentTimeMillis();
                    md.info("Clearing target of " + codingSchemes[i].getUrn() + "...");
                    emfOut.clearCodingScheme(codingSchemes[i].getUrn());
                    md.info("Clear Time (ms): " + (System.currentTimeMillis() - ms));

                    ms = System.currentTimeMillis();
                    md.info("Writing to target...");
                    emfOut.writeCodingScheme(temp);
                    md.info("Write Time (ms): " + (System.currentTimeMillis() - ms));

                    // Stream remaining content if available ...
                    doStreamContent(temp, emfIn, emfOut, md, ms);
                }
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

                String codingSchemeName = temp.getCodingSchemeName();
                convertedCodingSchemes = new URNVersionPair[] { new URNVersionPair(codingSchemeName, null) };

                if (!emfIn.getStreamingOn()) {
                    ms = System.currentTimeMillis();
                    md.info("Clearing target of " + temp.getCodingSchemeName() + "...");
                    emfOut.clearCodingScheme(codingSchemeName);
                    md.info("Clear Time (ms): " + (System.currentTimeMillis() - ms));

                    ms = System.currentTimeMillis();
                    md.info("Writing " + temp.getCodingSchemeName() + " to target...");
                    snap1 = SimpleMemUsageReporter.snapshot();
                    emfOut.writeCodingScheme(temp);
                    snap2 = SimpleMemUsageReporter.snapshot();
                    md.info("Write Time : " + SimpleMemUsageReporter.formatTimeDiff(snap2.getTimeDelta(snap1))
                            + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsage())
                            + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsageDelta(snap1)));

                    // Stream remaining content if available ...
                    doStreamContent(temp, emfIn, emfOut, md, ms);
                }
            } catch (UnsupportedOperationException e1) {
                // read without specifying a coding scheme also not implemented.
                // try reading all
                // xml reader does this
                long ms = System.currentTimeMillis();
                CodingScheme[] allCodingSchemes = emfIn.readAllCodingSchemes();
                convertedCodingSchemes = new URNVersionPair[allCodingSchemes.length];
                md.info("Read Time (ms): " + (System.currentTimeMillis() - ms));

                if (!emfIn.getStreamingOn()) {
                    for (int i = 0; i < allCodingSchemes.length; i++) {
                        ms = System.currentTimeMillis();
                        md.info("Clearing target of " + allCodingSchemes[i].getCodingSchemeName() + "...");
                        emfOut.clearCodingScheme(allCodingSchemes[i].getCodingSchemeName());
                        convertedCodingSchemes[i] = new URNVersionPair(allCodingSchemes[i].getCodingSchemeName(), null);
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
        }

        md.info("Conversion Finished.");
        return convertedCodingSchemes;
    }

    private static String[] doDelete(InputFormatInterface inputFormat, String[] codingSchemes, OptionHolder options,
            LgMessageDirectorIF md) throws Exception {
        if (inputFormat.getDescription().equals(LexGridSQL.description)) {
            LexGridSQL in = (LexGridSQL) inputFormat;
            Connection c = null;
            try {
                c = DBUtility.connectToDatabase(in.getServer(), in.getDriver(), in.getUsername(), in.getPassword());

                SQLTableUtilities utility = new SQLTableUtilities(c, in.getTablePrefix());
                for (int i = 0; i < codingSchemes.length; i++) {
                    long ms = System.currentTimeMillis();
                    md.info("Clearing source of " + codingSchemes[i] + "...");
                    utility.cleanTables(codingSchemes[i]);
                    md.info("Clear Time (ms): " + (System.currentTimeMillis() - ms));
                }
            } finally {
                if (c != null) {
                    try {
                        c.close();
                    } catch (SQLException e) {
                        // do nothing
                    }
                }
            }
        } else if (inputFormat.getDescription().equals(LexGridSQLLite.description)) {
            LexGridSQLLite in = (LexGridSQLLite) inputFormat;
            Connection c = null;
            try {
                c = DBUtility.connectToDatabase(in.getServer(), in.getDriver(), in.getUsername(), in.getPassword());

                SQLLiteTableUtilities utility = new SQLLiteTableUtilities(c);
                for (int i = 0; i < codingSchemes.length; i++) {
                    long ms = System.currentTimeMillis();
                    md.info("Clearing source of " + codingSchemes[i] + "...");
                    utility.cleanTables(codingSchemes[i]);
                    md.info("Clear Time (ms): " + (System.currentTimeMillis() - ms));
                }
            } finally {
                if (c != null) {
                    try {
                        c.close();
                    } catch (SQLException e) {
                        // do nothing
                    }
                }
            }
        } else {
            throw new Exception("The conversion that you are attempting to do is not yet possible.");
        }
        return codingSchemes;
    }

    private static String[] doTransitiveExpansion(InputFormatInterface inputFormat, String[] codingSchemes,
            OptionHolder options, LgMessageDirectorIF md) throws Exception {
        if (inputFormat.getDescription().equals(LexGridSQL.description)) {
            LexGridSQL in = (LexGridSQL) inputFormat;
            Connection c = null;
            try {
                c = DBUtility.connectToDatabase(in.getServer(), in.getDriver(), in.getUsername(), in.getPassword());

                SQLTableUtilities utility = new SQLTableUtilities(c, in.getTablePrefix());
                for (int i = 0; i < codingSchemes.length; i++) {
                    Snapshot snap1 = SimpleMemUsageReporter.snapshot();

                    md.info("Computing the transitivity table " + codingSchemes[i] + "...");
                    utility.computeTransitivityTable(codingSchemes[i], md);
                    //TransitiveClosure tc= new TransitiveClosure(c, utility, codingSchemes[i],md);
                    //tc.computeTransitivityTable();
                    Snapshot snap2 = SimpleMemUsageReporter.snapshot();
                    md.info("Compute Time : " + SimpleMemUsageReporter.formatTimeDiff(snap2.getTimeDelta(snap1))
                            + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsage())
                            + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsageDelta(snap1)));
                }
            } finally {
                if (c != null) {
                    try {
                        c.close();
                    } catch (SQLException e) {
                        // do nothing
                    }
                }
            }
        } else {
            throw new Exception("The conversion that you are attempting to do is not yet possible.");
        }
        return codingSchemes;
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
        } else if (inputFormat.getDescription().equals(NCIMetaThesaurusSQL.description)) {
            // metathesaurus input format
            if (outputFormat.getDescription().equals(LexGridSQLOut.description)) {
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
        } else if (inputFormat.getDescription().equals(LexGridDelimitedText.description)) {
            // LexGrid text file input
            if (outputFormat.getDescription().equals(LexGridSQLOut.description)) {
                return false;
            } else if (outputFormat.getDescription().equals(LexGridSQLLiteOut.description)) {
                return false;
            }
        } else if (inputFormat.getDescription().equals(UMLSSQL.description)) {
            // UMLS SQL input
            if (outputFormat.getDescription().equals(LexGridSQLOut.description)) {
                return false;
            }
        }

        // End of specific direct conversions. Havent found a match yet - can we
        // do it with EMF?
        return true;
    }

    private static String[] doRegister(InputFormatInterface inputFormat, OutputFormatInterface outputFormat,
            String[] codingSchemes, OptionHolder options, LgMessageDirectorIF md) throws Exception {

        RegisterLexGridTerminology out = (RegisterLexGridTerminology) outputFormat;

        md.info("Connecting to the LexGrid Service Index");

        edu.mayo.informatics.LexGridServicesIndex.sql.SQLStatements.DB_URL = new StringParameter(out.getServer());
        edu.mayo.informatics.LexGridServicesIndex.sql.SQLStatements.DB_DRIVER = new StringParameter(out.getDriver());
        edu.mayo.informatics.LexGridServicesIndex.sql.SQLStatements.DB_USERNAME = new StringParameter(out.getUsername());
        edu.mayo.informatics.LexGridServicesIndex.sql.SQLStatements.DB_PASSWORD = new StringParameter(out.getPassword());

        LexGridServiceAdmin lsa = new LexGridServiceAdmin(false);

        String hostedBy = getStringOption(options, Option.HOSTED_BY);
        String contactInfo = getStringOption(options, Option.CONTACT_INFO);
        String publicUsername = getStringOption(options, Option.PUBLIC_USERNAME);
        String publicPassword = getStringOption(options, Option.PUBLIC_PASSWORD);

        for (int i = 0; i < codingSchemes.length; i++) {
            md.info("Reading information for " + codingSchemes[i]);

            String typeId = "";
            String name = "";
            String description = "";
            String version = "";
            String uri = "";
            String connectionString = "";

            if (inputFormat.getDescription().equals(LexGridSQL.description)) {
                LexGridSQL in = (LexGridSQL) inputFormat;
                typeId = "LexGrid-SQL";

                connectionString = LexGridServiceAdmin.createSQLConnectionString(publicUsername, publicPassword, in
                        .getServer(), in.getDriver());

                Connection c = DBUtility.connectToDatabase(in.getServer(), in.getDriver(), in.getUsername(), in
                        .getPassword());

                SQLTableConstants stc = new SQLTableUtilities(c, in.getTablePrefix()).getSQLTableConstants();

                PreparedStatement temp = c.prepareStatement("Select "
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                        + ", "
                        + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION
                        + ", "
                        + SQLTableConstants.TBLCOL_REPRESENTSVERSION
                        + ", "
                        + (stc.supports2009Model() ? SQLTableConstants.TBLCOL_CODINGSCHEMEURI
                                : SQLTableConstants.TBLCOL_REGISTEREDNAME) + " from "
                        + stc.getTableName(SQLTableConstants.CODING_SCHEME) + " where "
                        + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ?");

                temp.setString(1, codingSchemes[i]);

                ResultSet results = temp.executeQuery();
                if (results.next()) {
                    name = results.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
                    description = results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION);
                    version = results.getString(SQLTableConstants.TBLCOL_REPRESENTSVERSION);
                    uri = (stc.supports2009Model() ? results.getString(SQLTableConstants.TBLCOL_CODINGSCHEMEURI)
                            : results.getString(SQLTableConstants.TBLCOL_REGISTEREDNAME));
                } else {
                    throw new Exception("Implementation error - terminology not found that should be present");
                }

                results.close();
                temp.close();
                c.close();

            } else if (inputFormat.getDescription().equals(LexGridLDAP.description)) {
                LexGridLDAP in = (LexGridLDAP) inputFormat;
                typeId = "LexGrid-LDAP";

                connectionString = LexGridServiceAdmin.createLDAPConnectionString(in.getServiceDN(), publicUsername,
                        publicPassword, in.getAddress());

                String searchFilter = "(&(objectclass=codingSchemeClass)(codingScheme=" + codingSchemes[i] + "))";
                SearchControls ctrl = new SearchControls();
                ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
                ctrl.setReturningAttributes(new String[] { SQLTableConstants.TBLCOL_CODINGSCHEMENAME,
                        SQLTableConstants.TBLCOL_ENTITYDESCRIPTION, SQLTableConstants.TBLCOL_REPRESENTSVERSION,
                        SQLTableConstants.TBLCOL_REGISTEREDNAME });

                Hashtable env = new Hashtable(5);
                env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                env.put(Context.PROVIDER_URL, in.getAddress() + "/" + in.getServiceDN());
                env.put("com.sun.jndi.ldap.connect.timeout", "1000");

                env.put(Context.SECURITY_PRINCIPAL, in.getUsername());
                env.put(Context.SECURITY_CREDENTIALS, in.getPassword());

                DirContext ctx = new InitialDirContext(env);

                NamingEnumeration results = ctx.search("", searchFilter, ctrl);

                if (results.hasMore()) {
                    SearchResult nextEntry = (SearchResult) results.next();
                    Attributes attributes = nextEntry.getAttributes();
                    name = (String) attributes.get("codingScheme").get();
                    description = attributes.get(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION) == null ? ""
                            : (String) attributes.get(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION).get();
                    version = (String) attributes.get(SQLTableConstants.TBLCOL_REPRESENTSVERSION).get();
                    uri = (String) attributes.get(SQLTableConstants.TBLCOL_REGISTEREDNAME).get();
                }

                else {
                    throw new Exception("Implementation error - terminology not found that should be present");
                }

            } else {
                throw new Exception("Unsupported input format");
            }

            md.info("Registering " + codingSchemes[i] + " with the connection string " + connectionString);
            lsa.addService(name, description, version, uri, hostedBy, contactInfo, connectionString, typeId);
        }

        md.info("Removing duplicates from the index");
        lsa.removeDuplicates();
        return codingSchemes;
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

    private static int getIntOption(OptionHolder options, int option) throws Exception {
        try {
            return ((Integer) options.get(Option.getNameForType(option)).getOptionValue()).intValue();
        } catch (RuntimeException e) {
            throw new Exception(
                    "Converter implementation error - required option is missing - needs a string option of "
                            + Option.getNameForType(option));
        }
    }

}