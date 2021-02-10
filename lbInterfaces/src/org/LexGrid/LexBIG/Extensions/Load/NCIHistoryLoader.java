
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * A loader that takes the delimited NCI history file and applies it to a coding
 * scheme.
 * 
 * @created 09-Feb-2006 10:22:05 PM
 * @version 1.0
 */
public interface NCIHistoryLoader extends Loader {

	/**
	 * Load history from a candidate resource.
	 * 
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            URI specifying location of the history file.
	 * @param versions
	 *            URI specifying location of the file containing version
	 *            identifiers for the history to be loaded.
	 * @param append
	 *            True means that the provided history file will be added into
	 *            the current history database (a new db will be created if none
	 *            exist) False means that the current database will be replaced
	 *            by the new content.
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
	public void load(URI source, URI versions, boolean append,
			boolean stopOnErrors, boolean async) throws LBException;

	/**
	 * Validate history for a candidate resource without performing a load.
	 * 
	 * Returns without exception if validation succeeds.
	 *  
	 * @param source
	 *            URI corresponding to the history file.
	 * @param versions
	 *            URI specifying location of the file containing version
	 *            identifiers for the history to be loaded.
	 * @param validationLevel
	 *            Supported levels of validation include: 0 = Verify top 10
	 *            lines are correct format. 1 = Verify entire file.
	 * @throws LBException
	 */
	public void validate(URI source, URI versions, int validationLevel)
			throws LBException;

}