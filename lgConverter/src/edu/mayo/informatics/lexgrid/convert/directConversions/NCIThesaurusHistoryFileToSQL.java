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
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;

/**
 * Converstion tool for loading a delimited text format into SQL.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 1154 $ checked in on $Date: 2006-02-08
 *          11:32:42 -0600 (Wed, 08 Feb 2006) $
 */
public class NCIThesaurusHistoryFileToSQL {
    private static String token_ = "|";
    private static SimpleDateFormat dateFormat_ = new SimpleDateFormat("dd-MMM-yy");
    private LgMessageDirectorIF md_;
    private Connection sqlConnection_;
    private SQLTableUtilities tableUtility_;
    private SQLTableConstants stc_;

    private static String codingSchemeName_ = "NCI Thesaurus History File";

    /**
     * @return the codingSchemeName
     */
    public String getCodingSchemeName() {
        return codingSchemeName_;
    }

    /**
     * NCI Thesaurus History File to SQL Converter.
     * 
     * @param filePath
     *            location of the delimited file (local file or URL)
     * @param token
     *            parsing token, if null default is "|"
     * @param sqlServer
     *            location of the SQLLite server
     * @param sqlDriver
     *            driver class
     * @param sqlUsername
     *            username for server authentification
     * @param sqlPassword
     *            password for server authenification
     * @param messageDirector
     *            log message output
     * @throws Exception
     */
    public NCIThesaurusHistoryFileToSQL(URI filePath, URI versionsFilePath, String token, boolean failOnAllErrors,
            String sqlServer, String sqlDriver, String sqlUsername, String sqlPassword, String tablePrefix,
            LgMessageDirectorIF messageDirector) throws Exception {
        md_ = messageDirector;
        if (token != null && token.length() > 0) {
            token_ = token;
        }

        // set up the sql tables
        prepareDatabase(sqlServer, sqlDriver, sqlUsername, sqlPassword, tablePrefix);

        // load the data, verify the description status.

        loadSystemReleaseFile(versionsFilePath, failOnAllErrors);

        loadFile(filePath, failOnAllErrors);

        sqlConnection_.close();

    }

    /**
     * Reads all or part of the file, makes sure that the content is the
     * expected format.
     * 
     * @param filePath
     *            fiel to read.
     * @param token
     *            delimiter (defaults to '|')
     * @param entireFile
     *            - read the entire file, or just the first 10 lines?
     * @throws MalformedURLException
     * @throws IOException
     * @throws ParseException
     */
    public static void validateFile(URI filePath, URI versionsFilePath, String token, boolean entireFile)
            throws MalformedURLException, IOException, ParseException {
        if (token != null) {
            token_ = token;
        }
        BufferedReader reader = getReader(filePath);
        int max = 10;
        if (entireFile) {
            max = Integer.MAX_VALUE;
        }

        String line = reader.readLine();
        while (line != null && max >= 0) {
            if (line.startsWith("#") || line.length() == 0) {
                line = reader.readLine();
                continue;
            }
            // format of line is
            // conceptcode|conceptname|editaction|editdate|referencecode|referencename

            String[] vals = new String[6];
            int startPos = 0;
            int endPos = 0;
            for (int i = 0; i < 6; i++) {
                endPos = line.indexOf(token_, startPos);
                if (endPos == -1) {
                    endPos = line.length();
                }
                vals[i] = line.substring(startPos, endPos);
                startPos = endPos + 1;

            }
            dateFormat_.parse(vals[3]);
            max--;
            line = reader.readLine();
        }
        reader.close();

        // validate the historyVersionsFile
        reader = getReader(versionsFilePath);

        line = reader.readLine();
        while (line != null) {
            if (line.startsWith("#") || line.length() == 0) {
                line = reader.readLine();
                continue;
            }
            // format of line is
            // releaseDate|releaseAgency|releaseURN|releaseId|basedOnRelease|entityDescription
            String[] vals = new String[6];
            int startPos = 0;
            int endPos = 0;
            for (int i = 0; i < 6; i++) {
                endPos = line.indexOf(token_, startPos);
                if (endPos == -1) {
                    endPos = line.length();
                }
                vals[i] = line.substring(startPos, endPos);
                startPos = endPos + 1;

            }
            dateFormat_.parse(vals[0]);
            line = reader.readLine();
        }
    }

    private static BufferedReader getReader(URI filePath) throws MalformedURLException, IOException {
        BufferedReader reader;
        if (filePath.getScheme().equals("file")) {
            reader = new BufferedReader(new FileReader(new File(filePath)));
        } else {
            reader = new BufferedReader(new InputStreamReader(filePath.toURL().openConnection().getInputStream()));
        }
        return reader;
    }

    private void loadFile(URI filePath, boolean failOnAllErrors) throws Exception {
        BufferedReader reader = getReader(filePath);

        PreparedStatement insert = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.NCI_THESAURUS_HISTORY));

        int lineNo = 0;
        String line = reader.readLine();
        while (line != null) {
            // format of line is
            // conceptcode|conceptname|editaction|editdate|referencecode|referencename
            if (line.startsWith("#") || line.length() == 0) {
                line = reader.readLine();
                continue;
            }
            try {
                String[] vals = new String[6];
                int startPos = 0;
                int endPos = 0;
                for (int i = 0; i < 6; i++) {
                    endPos = line.indexOf(token_, startPos);
                    if (endPos == -1) {
                        endPos = line.length();
                    }
                    vals[i] = line.substring(startPos, endPos);
                    startPos = endPos + 1;

                }

                insert.setString(1, vals[0]);
                insert.setString(2, useValueOrSpace(vals[1]));
                insert.setString(3, vals[2].toLowerCase());

                try {
                    insert.setTimestamp(4, new Timestamp(dateFormat_.parse(vals[3]).getTime()));
                } catch (ParseException e) {
                    md_.fatalAndThrowException("Invalid date on line " + lineNo, e);
                }

                if (vals[4].equals("(null)")) {
                    insert.setString(5, null);
                } else {
                    insert.setString(5, vals[4]);
                }

                if (vals[5].equals("(null)")) {
                    insert.setString(6, null);
                } else {
                    insert.setString(6, useValueOrSpace(vals[5]));
                }

                insert.executeUpdate();

                lineNo++;
                line = reader.readLine();
            } catch (Exception e) {
                if (failOnAllErrors) {
                    // this call rethrow the exception
                    md_.fatalAndThrowException("Failure on line " + lineNo, e);
                } else {
                    md_.error("Error reading line " + lineNo, e);
                    // go to next line, continue.
                    line = reader.readLine();
                }
            }

        }
        insert.close();
    }

    private void loadSystemReleaseFile(URI filePath, boolean failOnAllErrors) throws Exception {
        BufferedReader reader = getReader(filePath);

        PreparedStatement insert = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.SYSTEM_RELEASE));

        PreparedStatement update = sqlConnection_.prepareStatement(stc_
                .getUpdateStatementSQL(SQLTableConstants.SYSTEM_RELEASE));

        int lineNo = 0;
        String line = reader.readLine();
        while (line != null) {
            // format of line is
            // releaseDate|releaseAgency|releaseURN|releaseId|basedOnRelease|entityDescription
            if (line.startsWith("#") || line.length() == 0) {
                line = reader.readLine();
                continue;
            }
            try {
                String[] vals = new String[6];
                int startPos = 0;
                int endPos = 0;
                for (int i = 0; i < 6; i++) {
                    endPos = line.indexOf(token_, startPos);
                    if (endPos == -1) {
                        endPos = line.length();
                    }
                    vals[i] = line.substring(startPos, endPos);
                    startPos = endPos + 1;

                }

                try {
                    insert.setString(1, vals[3]);
                    insert.setString(2, vals[2]);
                    if (vals[4] == null || vals[4].length() == 0) {
                        insert.setString(3, null);
                    } else {
                        insert.setString(3, vals[4]);
                    }
                    try {
                        insert.setTimestamp(4, new Timestamp(dateFormat_.parse(vals[0]).getTime()));
                    } catch (ParseException e) {
                        md_.fatalAndThrowException("Invalid date on line " + lineNo, e);
                    }
                    insert.setString(5, vals[1]);

                    if (vals[5] == null || vals[5].length() == 0) {
                        insert.setString(6, null);
                    } else {
                        insert.setString(6, vals[5]);
                    }

                    insert.executeUpdate();
                } catch (SQLException e) {
                    try {
                        // assume that this means that we had a key violation
                        // reloading info that is already loaded. Do an update
                        // instead
                        // latest file wins.
                        // If an exception happens on the update, rethrow the
                        // origional exception.
                        update.setString(6, vals[3]);
                        update.setString(1, vals[2]);
                        if (vals[4] == null || vals[4].length() == 0) {
                            update.setString(2, null);
                        } else {
                            update.setString(2, vals[4]);
                        }
                        try {
                            update.setTimestamp(3, new Timestamp(dateFormat_.parse(vals[0]).getTime()));
                        } catch (ParseException e1) {
                            md_.fatalAndThrowException("Invalid date on line " + lineNo, e);
                        }
                        update.setString(4, vals[1]);

                        if (vals[5] == null || vals[5].length() == 0) {
                            update.setString(5, null);
                        } else {
                            update.setString(5, vals[5]);
                        }

                        update.executeUpdate();
                    } catch (SQLException e1) {
                        // throw the origional...
                        throw e;
                    }
                }

                lineNo++;
                line = reader.readLine();
            } catch (Exception e) {
                if (failOnAllErrors) {
                    // this call rethrow the exception
                    md_.fatalAndThrowException("Failure on line " + lineNo, e);
                } else {
                    md_.error("Error reading line " + lineNo, e);
                    // go to next line, continue.
                    line = reader.readLine();
                }
            }

        }
        insert.close();
        update.close();
    }

    private void prepareDatabase(String sqlServer, String sqlDriver, String sqlUsername, String sqlPassword,
            String tablePrefix) throws Exception {
        try {
            md_.info("Connecting to database");
            sqlConnection_ = DBUtility.connectToDatabase(sqlServer, sqlDriver, sqlUsername, sqlPassword);
        } catch (ClassNotFoundException e) {
            md_
                    .fatalAndThrowException("FATAL ERROR - The class you specified for your sql driver could not be found on the path.");
        }

        tableUtility_ = new SQLTableUtilities(sqlConnection_, tablePrefix);
        stc_ = tableUtility_.getSQLTableConstants();

        tableUtility_.createMetaDataTable();
        tableUtility_.createSystemReleaseTables();
        tableUtility_.createNCIHistoryTable();
    }
    
    private String useValueOrSpace(String s) {
        String rv = null;
        if(s == null || s.equalsIgnoreCase("")) {
            rv = " ";
        } else {
            rv = s;
        }
        return rv;
    }    
}