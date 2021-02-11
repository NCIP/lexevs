
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.SimpleMemUsageReporter;
import org.LexGrid.util.SimpleMemUsageReporter.Snapshot;
import org.LexGrid.versions.SystemRelease;
import org.apache.commons.lang.StringUtils;
import org.lexevs.locator.LexEvsServiceLocator;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;

public class UMLSHistoryFileToSQL {

/** Holds reference to message director. */
private LgMessageDirectorIF message_;
    /** Holds the token which seperates the fields in a flat file. */
    private static String token_ = "|";
    /** Holds a boolean value failOnAllErrors */
    private boolean failOnAllErrors_ = true;
    /** Holds reference to a map containing concept name and description. */
    private Map<String, String> mrconsoConceptName_ = new HashMap<String, String>();
    /** Holds the reference for the DB table prefix */
    private Map<String, Date> systemReleaseDates_ = new HashMap<String, Date>();
    /** Holds string constant "http://nlm.gov" */
    private static final String releaseAgency = "http://nlm.gov";
    
    private static final String metaURN = "urn:oid:2.16.840.1.113883.3.26.1.2";
  
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
     *            username for server authentication
     * @param sqlPassword
     *            password for server authentication
     * @param messageDirector
     *            log message output
     * @throws SQLException
     * @throws Exception
     */
    public UMLSHistoryFileToSQL(
            String codingSchemeUri, 
            boolean failOnAllErrors, 
            LgMessageDirectorIF 
            messageDirector, String token)
            throws SQLException {
        message_ = messageDirector;
        failOnAllErrors_ = failOnAllErrors;
        if (StringUtils.isNotBlank(token)) {
            token_ = token;
        }
        this.codingSchemeUri = codingSchemeUri;
    }

    /**
     * This method reads the History data from specified metaFolderPath and
     * loads it into the database;
     * 
     * @param metaFolderPath
     * @throws SQLException
     */
    public void loadUMLSHistory(URI folderPath) throws Exception {

        BufferedReader reader = getReader(folderPath.resolve("MRCUI.RRF"));

        try {
            String line = reader.readLine();
            
            int lineNo = 0;

            readMrConso(folderPath);

            message_.info("Loading History info...");

            while (line != null) {

                ++lineNo;

                if (line.startsWith("#") || line.length() == 0) {
                    line = reader.readLine();
                    continue;
                }
                List<String> elements = deTokenizeString(line, token_);

                try {
                    loadSystemReleaseInfo(elements);

                } catch (Exception e) {
                    if (failOnAllErrors_) {
                        // this call rethrow the exception
                        message_.fatalAndThrowException("Exception while loading System Release Info @ line : "
                                + lineNo, e);
                    } else {
                        message_.error("Error occured in line: " + lineNo, e);
                        // go to next line, continue.
                        line = reader.readLine();
                        continue;
                    }
                }

                try {
                    loadUMLSHistoryInfo(elements);

                } catch (Exception e) {
                    if (failOnAllErrors_) {
                        // this call rethrow the exception
                        message_.fatalAndThrowException("Exception while loadUMLSHistoryInfo @ line : " + lineNo, e);
                    } else {
                        message_.error("Error occured in line: " + lineNo, e);
                        // go to next line, continue.
                        line = reader.readLine();
                        continue;
                    }
                }

                line = reader.readLine();
            }
        } finally {
            reader.close();
        }

    }

    private Date getSystemReleaseDate(String releaseId) throws Exception {
        String sYear = releaseId.substring(0, 4);
        int year = new Integer(sYear).intValue();

        String sMonth = releaseId.substring(4);
        int mon = 0;

        Calendar cal = Calendar.getInstance();
        if ("AA".equalsIgnoreCase(sMonth)) {
            mon = Calendar.JANUARY;
        } else if ("AB".equalsIgnoreCase(sMonth)) {
            mon = Calendar.APRIL;
        } else if ("AC".equalsIgnoreCase(sMonth)) {
            mon = Calendar.JULY;
        } else if ("AD".equalsIgnoreCase(sMonth)) {
            mon = Calendar.OCTOBER;
        } else {
            try {
                int i = Integer.parseInt(sMonth);

                switch (i) {
                case 1:
                    mon = Calendar.JANUARY;
                    break;
                case 2:
                    mon = Calendar.FEBRUARY;
                    break;
                case 3:
                    mon = Calendar.MARCH;
                    break;
                case 4:
                    mon = Calendar.APRIL;
                    break;
                case 5:
                    mon = Calendar.MAY;
                    break;
                case 6:
                    mon = Calendar.JUNE;
                    break;
                case 7:
                    mon = Calendar.JULY;
                    break;
                case 8:
                    mon = Calendar.AUGUST;
                    break;
                case 9:
                    mon = Calendar.SEPTEMBER;
                    break;
                case 10:
                    mon = Calendar.OCTOBER;
                    break;
                case 11:
                    mon = Calendar.NOVEMBER;
                    break;
                case 12:
                    mon = Calendar.DECEMBER;
                    break;

                default:
                    throw new Exception("Release ID is not in required format: " + sMonth);
                }
            } catch (NumberFormatException e) {
                throw new Exception("Release ID is not in required format." + sMonth);
            }

        }
        cal.set(year, mon, 01, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new Date(cal.getTimeInMillis());
    }

    /**
     * Method reads MRDOC RRF file and loads RELEASE data into systemRelease DB
     * table.
     * 
     * @param historyInfo
     * @param elements
     * @throws Exception
     */
    private void loadSystemReleaseInfo(List<String> elements) throws Exception {

        String releaseId = elements.get(1);
        
        Date releaseDate = getSystemReleaseDate(releaseId);

        if (!systemReleaseDates_.keySet().contains(releaseId)) {
            systemReleaseDates_.put(releaseId, releaseDate);
            
            SystemRelease systemRelease = new SystemRelease();
            systemRelease.setReleaseURI(metaURN + ":" + releaseId);
            systemRelease.setReleaseId(releaseId);
            systemRelease.setReleaseDate(releaseDate);
            systemRelease.setReleaseAgency(releaseAgency);
            
            LexEvsServiceLocator.getInstance().
                getDatabaseServiceManager().
                    getNciHistoryService().
                        insertSystemRelease(
                                   codingSchemeUri, 
                                   systemRelease);
        }
    }

    /**
     * Methods reads DELETED and MERGED history data from CUI RRF files and
     * loads into conceptHistory DB table.
     * 
     * @param metaFolderPath
     * @throws Exception
     */
    private void loadUMLSHistoryInfo(List<String> elements) throws Exception {
        NCIChangeEvent nciChangeEvent = new NCIChangeEvent();
        
        nciChangeEvent.setConceptcode(elements.get(0));

        if (mrconsoConceptName_.keySet().contains(elements.get(0))) {
            nciChangeEvent.setConceptName(mrconsoConceptName_.get(elements.get(0)));
        } else {
            nciChangeEvent.setConceptName("Not Available.");
        }

        if ("DEL".equalsIgnoreCase(elements.get(2)) || "SUBX".equalsIgnoreCase(elements.get(2))
                || "RB".equalsIgnoreCase(elements.get(2)) || "RN".equalsIgnoreCase(elements.get(2))
                || "RO".equalsIgnoreCase(elements.get(2))) {
            nciChangeEvent.setEditaction(ChangeType.RETIRE);
        } else if ("SY".equalsIgnoreCase(elements.get(2))) {
            nciChangeEvent.setEditaction(ChangeType.MERGE);
        } else {
            throw new Exception("Relation field is not in required format.");
        }

        if (systemReleaseDates_.keySet().contains(elements.get(1))) {
            nciChangeEvent.setEditDate(systemReleaseDates_.get(elements.get(1)));
        } else {
            throw new Exception("Couldn't find Edit Date for the concept'" + elements.get(0) + "'");
        }

        nciChangeEvent.setReferencecode(elements.get(5));

        if (mrconsoConceptName_.keySet().contains(elements.get(5))) {
            nciChangeEvent.setReferencename(mrconsoConceptName_.get(elements.get(5)));
        } else {
            nciChangeEvent.setReferencename("Not Available.");
        }

        LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
                getNciHistoryService().
                    insertNCIChangeEvent(codingSchemeUri, nciChangeEvent);
    }

    /**
     * Method returns a BufferedReader for the passes URI.
     * 
     * @param filePath
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    private static BufferedReader getReader(URI filePath) throws MalformedURLException, IOException {
        BufferedReader reader = null;
        if (filePath.getScheme().equals("file")) {

            reader = new BufferedReader(new FileReader(new File(filePath)));
        } else {

            reader = new BufferedReader(new InputStreamReader(filePath.toURL().openConnection().getInputStream()));
        }
        return reader;
    }

    /**
     * This method de-tokenizes the give string from the passed string token.
     * 
     * @param str
     * @param token
     * @return
     */
    private static List<String> deTokenizeString(String str, String token) {
        int beginIndex = 0;
        int endIndex = str.indexOf(token);
        List<String> elementList = new ArrayList<String>();

        while (endIndex > -1) {
            elementList.add(str.substring(beginIndex, endIndex));
            beginIndex = endIndex + 1;
            endIndex = str.indexOf(token, beginIndex);
        }

        return elementList;
    }

    /**
     * This method converts a date in string format to java.sql.Date Format.
     * 
     * @param sDate
     * @param format
     * @return
     * @throws Exception
     */
    public static Date convertStringToDate(String sDate, String format) throws Exception {
        java.util.Date dateUtil = null;

        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        try {
            dateUtil = dateformat.parse(sDate);
        } catch (ParseException e) {
            throw new Exception("Exception while parsing the date: " + e.getMessage());
        }

        return new Date(dateUtil.getTime());
    }

    /**
     * Private method, reads MRCONSO.RRF file and loads the concept descriptions
     * in a Map.
     * 
     * @param metaFolderPath
     * @throws Exception
     */
    private void readMrConso(URI metaFolderPath) throws Exception {

        if (metaFolderPath == null) {
            if (failOnAllErrors_) {
                message_.fatalAndThrowException("URI unspecified for 'MRCONSO.RRF' file.");
            }
        }

        Snapshot snap1 = SimpleMemUsageReporter.snapshot();
        message_.info("Reading 'MRCONSO.RRF'...");

        BufferedReader mrconsoFile = null;

        try {
            mrconsoFile = getReader(metaFolderPath.resolve("MRCONSO.RRF"));

            String line = mrconsoFile.readLine();

            int lineNo = 0;
            while (line != null) {

                ++lineNo;

                if (line.startsWith("#") || line.length() == 0) {
                    line = mrconsoFile.readLine();
                    continue;
                }

                List<String> elements = deTokenizeString(line, token_);
                if (elements.size() > 14 && "y".equalsIgnoreCase(elements.get(6))) {

                    if (!mrconsoConceptName_.keySet().contains(elements.get(0))) {
                        mrconsoConceptName_.put(elements.get(0), elements.get(14));
                    }
                }
                line = mrconsoFile.readLine();
            }

        } catch (MalformedURLException e) {
            message_.error("Exceptions while reading MRCONSO.RRF: " + e.getMessage());
        } catch (IOException e) {
            message_.error("Exceptions while reading MRCONSO.RRF: " + e.getMessage());
        } finally {
            mrconsoFile.close();
        }

        Snapshot snap2 = SimpleMemUsageReporter.snapshot();
        message_.info("Done reading 'MRCONSO.RRF': Time taken: "
                + SimpleMemUsageReporter.formatTimeDiff(snap2.getTimeDelta(snap1)));

    }

    public static void validateFile(URI fileLocation, String token, boolean validateLevel) throws Exception {

        BufferedReader reader = null;
        int lineNo = 1;
        if (token == null) {
            token = token_;
        }

        // test MRCUI.RRF
        URI mrCUIFile = fileLocation.resolve("MRCUI.RRF");
        if (mrCUIFile == null) {

            throw new ConnectionFailure("Did not find the expected MRCUI.RRF file in the location provided.");
        }
        if (mrCUIFile.getScheme().equals("file")) {

            new FileReader(new File(mrCUIFile)).close();
        } else {
            new InputStreamReader(mrCUIFile.toURL().openConnection().getInputStream()).close();
        }

        try {
            reader = getReader(mrCUIFile);
            String line = reader.readLine();
            lineNo = 1;
            boolean notAMonth = false;
            while (line != null) {

                if (line.startsWith("#") || line.length() == 0) {
                    line = reader.readLine();
                    continue;
                }

                if (validateLevel && lineNo > 10) {
                    break;
                }

                List<String> elements = deTokenizeString(line, token_);

                if (elements.size() != 7) {
                    throw new Exception("MRCUI.RRF " + "(" + "Line:" + lineNo + ")" + " is not in the required format.");
                }
                if (!elements.get(0).toLowerCase().startsWith("c")) {
                    throw new Exception("MRCUI.RRF " + "(" + "Line:" + lineNo + "): " + "The concept("
                            + elements.get(0) + ") is not in the required format.");
                }

                try {
                    String month = elements.get(1).substring(4);
                    int i = Integer.parseInt(month);
                    if (i < 0 || i > 12) {
                        throw new Exception();
                    } else {
                        notAMonth = false;
                    }
                } catch (Exception e) {
                    notAMonth = true;
                }

                if (!elements.get(1).endsWith("AA") && !elements.get(1).endsWith("AB")
                        && !elements.get(1).endsWith("AC") && !elements.get(1).endsWith("AD") && notAMonth) {
                    throw new Exception("MRCUI.RRF " + "(" + "Line:" + lineNo + "): " + "The Release id ("
                            + elements.get(1) + ") is not in the required format.");
                }
                lineNo++;
                line = reader.readLine();
            }

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            reader.close();
        }

        // test MRCONSO.RRF
        URI mrCONSOFile = fileLocation.resolve("MRCONSO.RRF");
        if (mrCONSOFile == null) {

            throw new ConnectionFailure("Did not find the expected MRCONSO.RRF file in the location provided.");
        }
        if (mrCONSOFile.getScheme().equals("file")) {

            new FileReader(new File(mrCONSOFile)).close();
        } else {
            new InputStreamReader(mrCONSOFile.toURL().openConnection().getInputStream()).close();
        }

        try {
            reader = getReader(mrCONSOFile);
            String line = reader.readLine();
            lineNo = 1;
            while (line != null) {

                if (line.startsWith("#") || line.length() == 0) {
                    line = reader.readLine();
                    continue;
                }

                if (validateLevel && lineNo > 10) {
                    break;
                }

                List<String> elements = deTokenizeString(line, token_);

                if (elements.size() != 18) {
                    throw new Exception("MRCONSO.RRF " + "(" + "Line:" + lineNo + ")"
                            + " is not in the required format.");
                }

                lineNo++;
                line = reader.readLine();
            }

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            reader.close();
        }
    }
}