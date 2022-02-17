
package edu.mayo.informatics.lexgrid.convert.formats.inputFormats;

import java.net.URI;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

import edu.mayo.informatics.lexgrid.convert.directConversions.UMLSHistoryFileToSQL;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.UnexpectedError;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.URIBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;

/**
 * Details for reading a LexGrid Delimited Text File.
 * 
 * @author <A HREF="mailto:Rao.Ramachandra@mayo.edu">Rao</A>
 */
public class UMLSHistoryFile extends URIBase implements InputFormatInterface {
    public static String description = "UMLS History File";
    private boolean stopOnErrors_ = false;
    private LgMessageDirectorIF message_ = null;

    public UMLSHistoryFile(URI sourcePath, boolean stopOnErrors, LgMessageDirectorIF md) {
        fileLocation = sourcePath;
        this.stopOnErrors_ = stopOnErrors;
        this.message_ = md;
    }

    public UMLSHistoryFile() {

    }

    public String testConnection() throws ConnectionFailure {

        try {
            message_.info("Validating Input File(s)...");
            UMLSHistoryFileToSQL.validateFile(fileLocation, null, false);
            message_.info("Validation Success.");
        } catch (Exception e) {
            throw new ConnectionFailure("Validation error: Problem in reading the file: " + e.getMessage());
        }
        return "";
    }

    public String getDescription() {
        return description;
    }

    public String[] getSupportedOutputFormats() {
        return new String[] { LexGridSQLOut.description };
    }

    public String getConnectionSummary() {
        return getConnectionSummary(description);
    }

    public Option[] getOptions() {
        return new Option[] { new Option(Option.DELIMITER, "|"), new Option(Option.FAIL_ON_ERROR, new Boolean(true)) };
    }

    public String[] getAvailableTerminologies() throws ConnectionFailure, UnexpectedError {
        return null;
    }
}