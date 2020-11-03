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
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.versions.SystemRelease;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.ncihistory.NciHistoryService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * Converstion tool for loading a delimited text format into SQL.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 1154 $ checked in on $Date: 2006-02-08
 *          11:32:42 -0600 (Wed, 08 Feb 2006) $
 */
public class NCIThesaurusHistoryFileToSQL {
    private static String token_ = "|";
    private static SimpleDateFormat dateFormat_ = NciHistoryService.dateFormat;
    private LgMessageDirectorIF md_;

    private String codingSchemeUri;

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
    public NCIThesaurusHistoryFileToSQL(String uri, URI filePath, URI versionsFilePath, String token, boolean failOnAllErrors,
            LgMessageDirectorIF messageDirector) throws Exception {
        md_ = messageDirector;
        if (StringUtils.isNotBlank(token)) {
            token_ = token;
        }
        this.codingSchemeUri = uri;

        // load the data, verify the description status.

        loadSystemReleaseFile(versionsFilePath, failOnAllErrors);

        //loadFile(filePath, failOnAllErrors);
        
        loadFileBatch(filePath, failOnAllErrors);
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
                NCIChangeEvent event = new NCIChangeEvent();
                
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

                event.setConceptcode(vals[0]);
                event.setConceptName(useValueOrSpace(vals[1]));
                event.setEditaction(ChangeType.fromValue(vals[2].toLowerCase()));

                try {
                    event.setEditDate(new Timestamp(dateFormat_.parse(vals[3]).getTime()));
                } catch (ParseException e) {
                    md_.fatalAndThrowException("Invalid date on line " + lineNo, e);
                }

                if (! vals[4].equals("(null)")) {
                    event.setReferencecode(vals[4]);
                }

                if (! vals[5].equals("(null)")) {
                    event.setReferencename(useValueOrSpace(vals[5]));
                }

                LexEvsServiceLocator.getInstance().
                    getDatabaseServiceManager().
                        getNciHistoryService().insertNCIChangeEvent(codingSchemeUri, event);

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
    }
    
    private void loadFileBatch(URI filePath, boolean failOnAllErrors) throws Exception {
        BufferedReader reader = getReader(filePath);

        int lineNo = 0;
        int count = 0;
        List<NCIChangeEvent> events = new ArrayList<NCIChangeEvent>();
        String line = reader.readLine();
        while (line != null) {
            

            // format of line is
            // conceptcode|conceptname|editaction|editdate|referencecode|referencename
            if (line.startsWith("#") || line.length() == 0) {
                line = reader.readLine();
                continue;
            }
            try {
                NCIChangeEvent event = new NCIChangeEvent();
                
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

                event.setConceptcode(vals[0]);
                event.setConceptName(useValueOrSpace(vals[1]));
                event.setEditaction(ChangeType.fromValue(vals[2].toLowerCase()));

                try {
                    event.setEditDate(new Timestamp(dateFormat_.parse(vals[3]).getTime()));
                } catch (ParseException e) {
                    md_.fatalAndThrowException("Invalid date on line " + lineNo, e);
                }

                if (! vals[4].equals("(null)")) {
                    event.setReferencecode(vals[4]);
                }

                if (! vals[5].equals("(null)")) {
                    event.setReferencename(useValueOrSpace(vals[5]));
                }
                events.add(event);
                count++;
                if(count == 1000){
                LexEvsServiceLocator.getInstance().
                    getDatabaseServiceManager().
                        getNciHistoryService().insertNCIChangeEventBatch(codingSchemeUri, events);
                count = 0;
                events.clear();
                }
                lineNo++;
                line = reader.readLine();
                if(line == null && events.size() < 1000){
                    LexEvsServiceLocator.getInstance().
                    getDatabaseServiceManager().
                        getNciHistoryService().insertNCIChangeEventBatch(codingSchemeUri, events);
                }
            } catch (Exception e) {
                    e.printStackTrace();
                    md_.fatalAndThrowException("Error processing history event. See stack trace for details", e); 
            }

        }
        
        md_.info("LOAD SUCCESSFUL");
        
    }

    private void loadSystemReleaseFile(URI filePath, boolean failOnAllErrors) throws Exception {
        BufferedReader reader = getReader(filePath);

        int lineNumber = 0;
        String line = reader.readLine();
        while (line != null) {
            // format of line is
            // releaseDate|releaseAgency|releaseURN|releaseId|basedOnRelease|entityDescription
            if (line.startsWith("#") || line.length() == 0) {
                line = reader.readLine();
                continue;
            }
            
            String[] tokens = StringUtils.splitPreserveAllTokens(line, token_);
            
            SystemRelease systemRelease = new SystemRelease();
            try {
                systemRelease.setReleaseDate(new Timestamp(dateFormat_.parse(tokens[0]).getTime()));
            } catch (ParseException e) {
                md_.fatalAndThrowException("Invalid date on line " + lineNumber, e);
            }
            systemRelease.setReleaseAgency(tokens[1]);
            systemRelease.setReleaseURI(tokens[2]);
            systemRelease.setReleaseId(tokens[3]);
            systemRelease.setBasedOnRelease(tokens[4]);
            
            EntityDescription ed = new EntityDescription();
            
            
            ed.setContent(tokens[5]);
            systemRelease.setEntityDescription(ed);
            
            LexEvsServiceLocator.getInstance().
                getDatabaseServiceManager().
                    getNciHistoryService().
                        insertSystemRelease(
                                this.codingSchemeUri, 
                                systemRelease);    
            
            lineNumber++;
            line = reader.readLine();
        }
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