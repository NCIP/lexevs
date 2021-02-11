
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * Load an OBO change history file. Format to be determined.
 * 
 * @author solbrigcvs
 * @version 1.0
 * @created 09-Feb-2006 10:22:05 PM
 */
public interface OBOHistoryLoader extends Loader {

	/**
	 * Load history from a candidate resource.
	 * 
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            URI corresponding to the history file.
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
	 * Validate history for a candidate resource without performing a load.
	 * Returns without exception if validation succeeds.
	 * 
	 * @param source
	 *            URI corresponding to the history file.
	 * @param validationLevel
	 *            Supported levels of validation include: 0 = Verify file format
	 *            is valid.
	 * @throws LBException
	 */
	public void validate(URI source, int validationLevel) throws LBException;

}