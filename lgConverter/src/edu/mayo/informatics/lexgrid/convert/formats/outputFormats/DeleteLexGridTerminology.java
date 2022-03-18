
package edu.mayo.informatics.lexgrid.convert.formats.outputFormats;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatInterface;

/**
 * Details for removing a terminology from a server.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class DeleteLexGridTerminology implements OutputFormatInterface {

    public static final String description = "Delete LexGrid Terminology";

    public DeleteLexGridTerminology() {

    }

    public String getConnectionSummary() {
        return "The terminologies that you select from the input location will be deleted from the input server.";
    }

    public String getDescription() {
        return description;
    }

    public Option[] getOptions() {
        return new Option[] {};
    }

    public String testConnection() throws ConnectionFailure {
        return "";

    }
}