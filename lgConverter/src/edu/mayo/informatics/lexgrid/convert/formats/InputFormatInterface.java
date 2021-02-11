
package edu.mayo.informatics.lexgrid.convert.formats;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.UnexpectedError;

/**
 * Defines the interface for input formats.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public interface InputFormatInterface {
    /**
     * Get the supported Output formats for this input format.
     * 
     * @return
     */
    public String[] getSupportedOutputFormats();

    /**
     * Get the description of the input type.
     * 
     * @return
     */
    public String getDescription();

    /**
     * Get the additional options that must be supplied before the reading can
     * happen.
     * 
     * @return
     */
    public Option[] getOptions();

    /**
     * Get a summary of the connection parameters.
     * 
     * @return the summary.
     */
    public String getConnectionSummary();

    /**
     * See if the provided parameters make a valid connection
     * 
     * @return An optional warning about the connection - for example, SQL
     *         implementations may want to return a warning if the existing sql
     *         tables are a different version than what is expected.
     * @throws ConnectionFailure
     *             If a connection can't be made.
     */
    public String testConnection() throws ConnectionFailure;

    public String[] getAvailableTerminologies() throws ConnectionFailure, UnexpectedError;
}