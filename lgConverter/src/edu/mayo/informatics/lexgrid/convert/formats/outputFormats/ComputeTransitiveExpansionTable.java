
package edu.mayo.informatics.lexgrid.convert.formats.outputFormats;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatInterface;

/**
 * Details for removing a terminology from a server.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          05:42:24 -0600 (Mon, 30 Jan 2006) $
 */
public class ComputeTransitiveExpansionTable implements OutputFormatInterface {

    public static final String description = "Compute Transitive Expansion Table";

    public ComputeTransitiveExpansionTable() {

    }

    public String getConnectionSummary() {
        return "The terminologies that you select from the input location will have their transitive expansion tables populated.";
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