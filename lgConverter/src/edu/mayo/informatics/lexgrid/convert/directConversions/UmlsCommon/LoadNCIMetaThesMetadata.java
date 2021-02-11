
package edu.mayo.informatics.lexgrid.convert.directConversions.UmlsCommon;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;

/**
 * This class loads NCI MetaThesaurus Metadata from RRF files.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LoadNCIMetaThesMetadata {

    private URI fileLocation;
    LgMessageDirectorIF md;
    private String dbServer;
    private String dbDriver;
    private String username;
    private String password;

    public LoadNCIMetaThesMetadata(URI rrfLocation, String dbServer, String dbDriver, String username, String password,
            LgMessageDirectorIF md) {
        this.fileLocation = rrfLocation;
        this.md = md;
        this.dbServer = dbServer;
        this.dbDriver = dbDriver;
        this.username = username;
        this.password = password;
    }

    public void loadMetadata() throws ConnectionFailure {
        Connection sqlConnection;
        try {
            sqlConnection = DBUtility.connectToDatabase(dbServer, dbDriver, username, password);
        } catch (Exception e) {
            throw new ConnectionFailure("Cannot connect to Metadata Database", e);
        }
        // reate the filename of the metadata file to be created
        String filename = fileLocation.getPath();

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filename);

            OutputFormat of = new OutputFormat("XML", "ISO-8859-1", true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(fos, of);

            // SAX2.0 ContentHandler.
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();

            // Element attributes.
            AttributesImpl atts = new AttributesImpl();

            // CODINGSCHEMES tag.
            hd.startElement("", "", "codingSchemes", atts);

            String defaultLanguage = "en";
            String isNative = "0";
            String dataMissing = SQLTableConstants.TBLCOLVAL_MISSING;
            String codeSystemId;
            String codeSystemType;
            String codeSystemName;
            String fullName;
            String description;
            String releaseId;
            String copyrightNotice;

            try {

                PreparedStatement getCodingSchemeMetaData = sqlConnection
                        .prepareStatement("SELECT VSAB, RSAB, SON, SLC, LAT, SSN, SCIT FROM MRSAB");
                ResultSet codingSchemeMetaData = getCodingSchemeMetaData.executeQuery();

                while (codingSchemeMetaData.next()) {
                    codeSystemId = codingSchemeMetaData.getString("SSN");
                    if (codeSystemId == null) {
                        codeSystemId = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    codeSystemType = codingSchemeMetaData.getString("SON");
                    if (codeSystemType == null) {
                        codeSystemType = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    codeSystemName = codingSchemeMetaData.getString("RSAB");
                    if (codeSystemName == null) {
                        codeSystemName = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    fullName = codingSchemeMetaData.getString("SON");
                    if (fullName == null) {
                        fullName = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    description = codingSchemeMetaData.getString("SCIT");
                    if (description == null) {
                        description = SQLTableConstants.TBLCOLVAL_MISSING;
                    }
                    // The description contains HTML tags. We try to remove
                    // them.
                    int begin = description.lastIndexOf("<p>");
                    int end = description.lastIndexOf("</p>");
                    if (begin > -1) {
                        description = description.substring(begin + 3, end);
                    }

                    releaseId = codingSchemeMetaData.getString("VSAB");
                    if (releaseId == null) {
                        releaseId = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    copyrightNotice = codingSchemeMetaData.getString("SLC");
                    if (copyrightNotice == null) {
                        copyrightNotice = SQLTableConstants.TBLCOLVAL_MISSING;
                    }

                    // Begin codingScheme element
                    atts.clear();
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_CODINGSCHEME, "CDATA", codeSystemName); // May
                                                                                                               // want
                                                                                                               // to
                                                                                                               // change
                                                                                                               // to
                                                                                                               // _fullName
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_FORMALNAME, "CDATA", fullName);
                    // atts.addAttribute("","",SQLTableConstants.TBLCOL_REGISTEREDNAME,"CDATA",
                    // codeSystemId);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_CODINGSCHEMEURI, "CDATA", codeSystemId);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_DEFAULTLANGUAGE, "CDATA", defaultLanguage);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_REPRESENTSVERSION, "CDATA", releaseId);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_ISNATIVE, "CDATA", isNative);
                    atts.addAttribute("", "", SQLTableConstants.TBLCOL_APPROXNUMCONCEPTS, "CDATA", "0");
                    // atts.addAttribute("","",SQLTableConstants.TBLCOL_FIRSTRELEASE,"CDATA",dataMissing);
                    // atts.addAttribute("","",SQLTableConstants.TBLCOL_MODIFIEDINRELEASE,"CDATA",dataMissing);
                    // atts.addAttribute("","",SQLTableConstants.TBLCOL_DEPRECATED,"CDATA",
                    // dataMissing);
                    hd.startElement("", "", SQLTableConstants.TBLCOL_CODINGSCHEME, atts);

                    // localname
                    atts.clear();
                    hd.startElement("", "", "localName", atts);
                    hd.characters(codeSystemName.toCharArray(), 0, codeSystemName.length());
                    hd.endElement("", "", SQLTableConstants.TBLCOLVAL_LOCALNAME);

                    // entityDescription
                    atts.clear();
                    hd.startElement("", "", SQLTableConstants.TBLCOL_ENTITYDESCRIPTION, atts);
                    hd.characters(description.toCharArray(), 0, description.length());
                    hd.endElement("", "", SQLTableConstants.TBLCOL_ENTITYDESCRIPTION);

                    // copyright
                    atts.clear();
                    hd.startElement("", "", SQLTableConstants.TBLCOL_COPYRIGHT, atts);
                    hd.characters(copyrightNotice.toCharArray(), 0, copyrightNotice.length());
                    hd.endElement("", "", SQLTableConstants.TBLCOL_COPYRIGHT);

                    // End codingScheme element
                    hd.endElement("", "", SQLTableConstants.TBLCOL_CODINGSCHEME);
                } // End while there are result rows to process
                codingSchemeMetaData.close();

                hd.endElement("", "", "codingSchemes");
                hd.endDocument();
                fos.close();
            } catch (SQLException e) {
                md.error("Error Creating Metadata", e);
            } finally {
                try {
                    sqlConnection.close();
                } catch (SQLException e) {
                    md.error("Error Closing SQL connection", e);
                }
            }

            // this is the file not found exception
        } catch (FileNotFoundException e) {
            md.error("Error Finding Metadata File", e);
        } catch (IOException e) {
            md.error("Error Opening Metadata File", e);
        } catch (SAXException e) {
            md.error("Error Parsing Metadata File", e);
        }

    }
}