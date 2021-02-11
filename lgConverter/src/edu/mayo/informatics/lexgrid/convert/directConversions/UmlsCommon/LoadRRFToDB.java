
package edu.mayo.informatics.lexgrid.convert.directConversions.UmlsCommon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.apache.commons.lang.StringUtils;

/**
 * This class loads that tables that I need from RRF files into a database.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LoadRRFToDB {
    public static String[] validateRRF(URI rrfLocation, boolean skipNonLexGridFiles, LgMessageDirectorIF md)
            throws Exception {
        Random r = new Random();
        // generate a random db label
        return createAndLoadTables(rrfLocation, skipNonLexGridFiles, false, "jdbc:hsqldb:mem:" + r.nextInt(100),
                "org.hsqldb.jdbcDriver", "sa", "", md, true);
    }

    public static String[] loadRRFToDB(URI rrfLocation, boolean skipNonLexGridFiles, boolean recalcRootOnly,
            String dbServer, String dbDriver, String username, String password, LgMessageDirectorIF md)
            throws Exception {
        return createAndLoadTables(rrfLocation, skipNonLexGridFiles, recalcRootOnly, dbServer, dbDriver, username,
                password, md, false);
    }

    private static String[] createAndLoadTables(URI rrfLocation, boolean skipNonLexGridFiles, boolean recalcRootOnly,
            String dbServer, String dbDriver, String username, String password, LgMessageDirectorIF md,
            boolean validateMode) throws Exception {
        md.info("Connecting to RRF Files");
        BufferedReader reader = getReader(rrfLocation.resolve("MRFILES.RRF"));

        md.info("Connecting to db Files");
        Connection sqlConnection = DBUtility.connectToDatabase(dbServer, dbDriver, username, password);

        GenericSQLModifier gsm = new GenericSQLModifier(sqlConnection);

        Hashtable columnTypeMap = readMRCols(rrfLocation);
        Hashtable tableColumnMap = new Hashtable();

        List tables = new ArrayList();

        if (skipNonLexGridFiles) {
            // the only tables that I need to load
            tables.add("MRCONSO");
            tables.add("MRDOC");
            tables.add("MRREL");
            tables.add("MRSAB");
            tables.add("MRRANK");

            if (!recalcRootOnly) {
                tables.add("MRDEF");
                tables.add("MRSTY");
                tables.add("MRSAT");
                tables.add("MRHIER");
            }
        }

        md.info("Creating SQL database tables");

        PreparedStatement create = null;
        PreparedStatement drop = null;
        String line = reader.readLine();

        int mrhierHCDCol = -1;
        while (line != null) {
            String[] vals = stringToArray(line, '|');

            // for MRFILES, all I care about is the following
            String file = vals[0];
            String tableName = file.substring(0, file.indexOf('.'));

            // if file is MRHIER, remember HCD column number (base 0)
            if ("MRHIER".equalsIgnoreCase(tableName) && vals.length > 1) {
                mrhierHCDCol = Arrays.asList(vals[2].split(",")).indexOf("HCD");
            }

            if (skipNonLexGridFiles || recalcRootOnly) {
                if (!tables.contains(tableName)) {
                    line = reader.readLine();
                    continue;
                }
            } else {
                if (file.indexOf('/') != -1) {
                    // skip files in subfolders.
                    line = reader.readLine();
                    continue;
                }
                if (!tables.contains(tableName))
                    tables.add(tableName);
            }

            String[] columns = stringToArray(vals[2], ',');

            tableColumnMap.put(file, columns);

            StringBuffer tableCreateSQL = new StringBuffer();
            tableCreateSQL.append("CREATE TABLE {IF NOT EXISTS} ^" + tableName + "^ (");

            for (int i = 0; i < columns.length; i++) {

                tableCreateSQL.append(" ^" + columns[i] + "^ "
                        + mapUMLSType((String) columnTypeMap.get(columns[i] + "|" + file)) + " default NULL,");
            }

            // chop the last comma
            tableCreateSQL.deleteCharAt(tableCreateSQL.length() - 1);
            tableCreateSQL.append(") {TYPE}");

            // make sure the table doesn't exist
            try {
                drop = sqlConnection.prepareStatement(gsm.modifySQL("DROP TABLE " + tableName + " {CASCADE}"));
                drop.executeUpdate();
                drop.close();
            } catch (SQLException e) {
                // most likely means that the table didn't exist.
            }

            create = sqlConnection.prepareStatement(gsm.modifySQL(tableCreateSQL.toString()));
            create.executeUpdate();

            create.close();

            line = reader.readLine();
        }
        reader.close();

        md.info("Creating indexes");

        PreparedStatement createIndex = null;

        createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi1^ ON ^MRCONSO^ (^CUI^, ^SAB^)"));
        createIndex.executeUpdate();
        createIndex.close();

        createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi2^ ON ^MRCONSO^ (^CUI^, ^AUI^)"));
        createIndex.executeUpdate();
        createIndex.close();

        createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi3^ ON ^MRCONSO^ (^AUI^, ^CODE^)"));
        createIndex.executeUpdate();
        createIndex.close();

        createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi4^ ON ^MRREL^ (^RELA^)"));
        createIndex.executeUpdate();
        createIndex.close();

        createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi5^ ON ^MRREL^ (^REL^)"));
        createIndex.executeUpdate();
        createIndex.close();

        createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi6^ ON ^MRREL^ (^RUI^)"));
        createIndex.executeUpdate();
        createIndex.close();

        createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi7^ ON ^MRREL^ (^SAB^, ^RELA^)"));
        createIndex.executeUpdate();
        createIndex.close();

        createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi8^ ON ^MRSAB^ (^RSAB^)"));
        createIndex.executeUpdate();
        createIndex.close();

        createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi9^ ON ^MRRANK^ (^SAB^)"));
        createIndex.executeUpdate();
        createIndex.close();

        createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi10^ ON ^MRRANK^ (^TTY^)"));
        createIndex.executeUpdate();
        createIndex.close();

        if (!recalcRootOnly) {
            createIndex = sqlConnection
                    .prepareStatement(gsm.modifySQL("CREATE INDEX ^mi11^ ON ^MRDEF^ (^CUI^, ^SAB^)"));
            createIndex.executeUpdate();
            createIndex.close();

            createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi12^ ON ^MRSAT^ (^METAUI^)"));
            createIndex.executeUpdate();
            createIndex.close();

            createIndex = sqlConnection
                    .prepareStatement(gsm.modifySQL("CREATE INDEX ^mi13^ ON ^MRSAT^ (^CUI^, ^SAB^)"));
            createIndex.executeUpdate();
            createIndex.close();

            createIndex = sqlConnection.prepareStatement(gsm
                    .modifySQL("CREATE INDEX ^mi14^ ON ^MRSAT^ (^CODE^, ^SAB^)"));
            createIndex.executeUpdate();
            createIndex.close();

            createIndex = sqlConnection.prepareStatement(gsm.modifySQL("CREATE INDEX ^mi15^ ON ^MRSTY^ (^CUI^)"));
            createIndex.executeUpdate();
            createIndex.close();

            createIndex = sqlConnection.prepareStatement(gsm
                    .modifySQL("CREATE INDEX ^mi16^ ON ^MRHIER^ (^CUI^, ^AUI^, ^HCD^, ^SAB^, ^CXN^)"));
            createIndex.executeUpdate();
            createIndex.close();

            createIndex = sqlConnection.prepareStatement(gsm
                    .modifySQL("CREATE INDEX ^mi17^ ON ^MRHIER^ (^CUI^, ^SAB^, ^CXN^)"));
            createIndex.executeUpdate();
            createIndex.close();
        }

        PreparedStatement insert = null;

        Iterator allTables = tables.iterator();
        Set rootCUIs = new HashSet();
        while (allTables.hasNext()) {
            System.gc();
            String table = (String) allTables.next();
            md.info("Loading " + table);

            boolean loadingMrHier = table.equalsIgnoreCase("MRHIER");

            StringBuffer insertSQL = new StringBuffer();

            insertSQL.append("INSERT INTO " + table + " (");

            String[] vals = (String[]) tableColumnMap.get(table + ".RRF");
            for (int i = 0; i < vals.length; i++) {
                if (gsm.getDatabaseType().equals("ACCESS") && vals[i].equals("VALUE")) {
                    // reserved word in MSAccess
                    insertSQL.append("\"" + vals[i] + "\", ");
                } else {
                    insertSQL.append(vals[i] + ", ");
                }
            }

            // chop the last comma and space
            insertSQL.deleteCharAt(insertSQL.length() - 2);
            insertSQL.append(") VALUES (");

            for (int i = 0; i < vals.length; i++) {
                insertSQL.append("?, ");
            }

            // chop the last comma and space
            insertSQL.deleteCharAt(insertSQL.length() - 2);
            insertSQL.append(")");
            insert = sqlConnection.prepareStatement(gsm.modifySQL(insertSQL.toString()));

            URI tableURI = rrfLocation.resolve(table + ".RRF");

            if (verifyTableExists(tableURI)) {
                try {
                    reader = getReader(tableURI);

                    int count = 1;
                    line = reader.readLine();
                    boolean restrictToRootCUIs = recalcRootOnly && table.equalsIgnoreCase("MRCONSO");
                    boolean restrictToRootRels = recalcRootOnly && table.equalsIgnoreCase("MRREL");
                    while (line != null && line.length() > 0) {
                        // Note: If we are only using the data to recalculate
                        // root nodes,
                        // we only need CUIs defining root hierarchical terms
                        // and any related
                        // relationships.
                        if (restrictToRootCUIs && !line.contains("|SRC|RHT|")) {
                            line = reader.readLine();
                            continue;
                        }
                        String[] data = stringToArray(line, '|');

                        // If processing MRHIER, we only care about entries
                        // relevant to
                        // the specified MRHIER processing option. Many entries
                        // in this file
                        // we do not require since they can be derived from
                        // MRREL.
                        // MRHIER typically is much larger since it pre-computes
                        // the entire
                        // hierarchy, so we want to conserve time and space by
                        // loading only
                        // those entries that require special handling.
                        if (loadingMrHier && mrhierHCDCol > 0 && data.length > mrhierHCDCol
                                && StringUtils.isBlank(data[mrhierHCDCol])) {
                            line = reader.readLine();
                            continue;
                        }

                        if (restrictToRootCUIs && data.length >= 1)
                            rootCUIs.add(data[0]);
                        if (restrictToRootRels
                                && (data.length < 5 || (!rootCUIs.contains(data[0]) && !rootCUIs.contains(data[4])))) {
                            line = reader.readLine();
                            continue;
                        }

                        for (int i = 0; i < vals.length; i++) {
                            insert.setString(i + 1, data[i]);
                        }
                        insert.executeUpdate();

                        count++;
                        line = reader.readLine();

                        if (validateMode && count > 100) {
                            line = null;
                        }

                        if (count % 10000 == 0) {
                            md.busy();
                        }

                        if (count % 100000 == 0) {
                            md.info("Loaded " + count + " into " + table);
                        }
                    }
                    reader.close();
                } catch (Exception e) {
                    md.fatalAndThrowException("problem loading the table " + table, e);
                }

            } else {
                md.warn("Could not load table " + table + ". This" + "most likely means the corresponding RRF file"
                        + "was not found in the source.");
            }

            insert.close();
            System.gc();
        }

        sqlConnection.close();
        return (String[]) tables.toArray(new String[tables.size()]);
    }

    private static String mapUMLSType(String type) throws Exception {
        if (type.startsWith("varchar")) {
            return "{limitedText}" + type.substring(type.indexOf('('), type.length());
        } else if (type.startsWith("char")) {
            return "{limitedText}" + type.substring(type.indexOf('('), type.length());
        } else if (type.startsWith("numeric") || type.equals("integer")) {
            return "{limitedText}(20)";
        } else {
            throw new Exception("Unknown type");
        }
    }

    private static Hashtable readMRCols(URI rrfLocation) throws MalformedURLException, IOException {
        Hashtable result = new Hashtable();
        BufferedReader reader = getReader(rrfLocation.resolve("MRCOLS.RRF"));

        String line = reader.readLine();
        while (line != null) {
            String[] vals = stringToArray(line, '|');
            String key = vals[0] + "|" + vals[6];
            result.put(key, vals[7]);
            line = reader.readLine();
        }
        reader.close();

        return result;
    }

    private static String[] stringToArray(String string, char token) {
        ArrayList vals = new ArrayList();
        int startPos = 0;
        int endPos = string.indexOf(token);
        while (endPos != -1) {
            vals.add(string.substring(startPos, endPos));
            startPos = endPos + 1;
            endPos = string.indexOf(token, startPos);
        }
        vals.add(string.substring(startPos, string.length()));
        return (String[]) vals.toArray(new String[vals.size()]);
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

    public static void main(String[] args) throws Exception {
        URI testURI = new URI("http://www.cnn.com/");
        // URI testURI = new URI("file:///W:/temp");
        // System.out.println(new File(testURI).exists());

        HttpURLConnection connecition = (HttpURLConnection) testURI.toURL().openConnection();
        System.out.println(connecition.getResponseCode());

        System.out.println(testURI.resolve("TEST").toString());
    }

    private static boolean verifyTableExists(URI tableURI) {
        // if its a file, check if it exists
        if (tableURI.getScheme().equals("file")) {
            return new File(tableURI).exists();
        }
        // otherwise, just try to connect..
        // TODO: find a better way to check this instead of catching an
        // exception.
        else {
            try {
                tableURI.toURL().openConnection();
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }
}