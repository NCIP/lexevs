
package edu.mayo.informatics.lexgrid.convert.formats.inputFormats;

import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.FileBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLLiteOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;

/**
 * Details for reading a LexGrid Delimited Text File.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class LexGridDelimitedText extends FileBase implements InputFormatInterface {
    public static String description = "LexGrid Delimited Text File";

    public LexGridDelimitedText(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public LexGridDelimitedText() {

    }

    public String getDescription() {
        return description;
    }

    public String[] getSupportedOutputFormats() {
        return new String[] { LexGridSQLOut.description, LexGridSQLLiteOut.description };
    }

    public String getConnectionSummary() {
        return getConnectionSummary(description);
    }

    public Option[] getOptions() {
        return new Option[] { new Option(Option.DELIMITER, ""), new Option(Option.FORCE_FORMAT_B, new Boolean(false)) };
    }

    public String[] getAvailableTerminologies() {
        return null;
    }
}