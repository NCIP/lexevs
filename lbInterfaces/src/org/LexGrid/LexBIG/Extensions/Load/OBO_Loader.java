
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * Validates and/or loads content provided in OBO text format.
 * 
 * @author solbrigcvs
 * @version 1.0
 * @created 09-Feb-2006 10:22:05 PM
 */
public interface OBO_Loader extends Loader {

	/**
	 * Return the OBO text file version that is supported by the loader. (e.g.
	 * 1.0, 1.2, etc.).
	 */
    public final static String name = "OBOLoader";
    public final static String description = "This loader loads version 1.2 OBO files into the LexGrid format.";

	public String getOBOVersion();

	/**
	 * Load content from a candidate resource. This will also result in implicit
	 * generation of standard indices required by the LexBIG runtime.
	 * 
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            URI corresponding to the OBO file.
     * @param metaSource
     *            URI corresponding to the OBO metadata XML file.  Optional.           
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
	public void load(URI source, URI metaSource, boolean stopOnErrors, boolean async) throws LBException;

	/**
	 * Validate content for a candidate resource without performing a load.
	 * 
	 * Returns without exception if validation succeeds.
	 * 
	 * @throws LBException
	 * 
	 * @param source
	 *            URI corresponding to the OBO file.
     * @param metaSource
     *            URI corresponding to the OBO metadata XML file.  Optional.          
	 * @param validationLevel
	 *            Supported levels of validation include: 0 = Verify the source file
	 *            conforms to the OBO format, and the metadata file conforms to the 
     *            OBO metadata format.
	 * @throws LBException
	 */
	public void validate(URI source, URI metaSource, int validationLevel) throws LBException;

}