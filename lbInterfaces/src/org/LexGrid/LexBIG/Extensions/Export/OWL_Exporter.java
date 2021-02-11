
package org.LexGrid.LexBIG.Extensions.Export;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * Exports content to OWL XML format.
 */
public interface OWL_Exporter extends Exporter {

	/**
	 * Export content from the underlying LexGrid repository.
	 * 
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            The absolute version identifier of the coding scheme to export.
	 * @param destination
	 *            URI corresponding to the OWL file to write.
	 * @param overwrite
	 *            True indicates to overwrite an existing file if present. False
	 *            indicates to stop if the destination file already exists.
	 * @param stopOnErrors
	 *            True means stop if any export error is detected. False means
	 *            attempt to continue writing what can be exported if
	 *            recoverable errors are encountered.
	 * @param async
	 *            Flag controlling whether export occurs in the calling thread.
	 *            If true, the export will occur in a separate asynchronous
	 *            process. If false, this method blocks until the export
	 *            operation completes or fails. Regardless of setting, the
	 *            getStatus and getLog calls are used to fetch results.
	 * @throws LBException
	 */
	public void export(AbsoluteCodingSchemeVersionReference source,
			URI destination, boolean overwrite, boolean stopOnErrors,
			boolean async) throws LBException;

}