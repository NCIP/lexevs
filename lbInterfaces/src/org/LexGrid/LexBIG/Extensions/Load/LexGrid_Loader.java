
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * Validates and/or loads content provided in LexGrid cononical XML format.
 * 
 * @created 09-Feb-2006 10:22:03 PM
 * @author solbrigcvs
 * @version 1.0
 * @updated 28-Feb-2006 12:25:56 PM
 */
public interface LexGrid_Loader extends Loader {
    public final static String name = "LexGrid_Loader";
    public final static String description = "This loader loads LexGrid XML files into the LexGrid database.";

	/**
	 * Return a reference to the XML Schema that this loader supports.
	 */
	public URI getSchemaURL();

	/**
	 * Return the version identifier of the schema that this loader supports.
	 * (e.g. 2004/02, 2005/01, ...).
	 */
	public String getSchemaVersion();

	/**
	 * Load content from a candidate resource. This will also result in implicit
	 * generation of standard indices required by the LexBIG runtime.
	 * 
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            URI corresponding to the XML file.
	 * @param stopOnErrors
	 *            True means stop if any load error is detected. False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 * @param async
	 *            Flag controlling whether load occurs in the calling thread.  
	 *            If true, the load will occur in a separate asynchronous process.
	 *            If false, this method blocks until the load operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @throws LBException
	 */
	public void load(URI source, boolean stopOnErrors, boolean async) throws LBException;

	/**
	 * Validate content for a candidate resource without performing a load.
	 * Returns without exception if validation succeeds.
	 * 
	 * @param source
	 *            URI corresponding to the XML file.
	 * @param validationLevel
	 *            Supported levels of validation include: 0 = Verify XML is well
	 *            formed. 1 = Verify XML is valid.
	 * @throws LBException
	 */
	public void validate(URI source, int validationLevel) throws LBException;

}