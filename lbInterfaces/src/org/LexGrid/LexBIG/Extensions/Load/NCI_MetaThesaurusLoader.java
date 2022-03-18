
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * Validates and/or loads the complete NCI MetaThesaurus. Content is supplied in
 * RRF format files. If individual coding schemes are desired, use the
 * UMLS_Loader instead.
 */
public interface NCI_MetaThesaurusLoader extends Loader {

	/**
	 * Load content from a candidate resource. This will also result in implicit
	 * generation of standard indices required by the LexBIG runtime.
	 * 
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            URI corresponding to the directory containing the
	 *            MetaThesaurus files as provided by NCI.
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
	 * Rebuild relationships between the system-designated top ('@')
	 * and end ('@@') nodes and those concepts linked to root hierarchical
	 * terms defined by the UMLS.  This function is provided to allow
	 * re-evaluation of the root nodes in cases where the LexBIG
	 * algorithm has changed, without requiring a reload of the content.
	 * 
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            URI corresponding to the directory containing the
	 *            MetaThesaurus files as provided by NCI.
	 * @param async
	 *            Flag controlling whether load occurs in the calling thread.  
	 *            If true, the load will occur in a separate asynchronous process.
	 *            If false, this method blocks until the load operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @throws LBException
	 */
	public void recalcRootNodes(URI source, boolean async) throws LBException;

	/**
	 * Validate content for a candidate resource without performing a load.
	 * Returns without exception if validation succeeds.
	 * 
	 * @param source
	 *            URI corresponding to the directory containing the
	 *            MetaThesaurus files as provided by NCI.
	 * @param validationLevel
	 *            Loader-specific level of validation; 0 = verify all files are
	 *            present and conform to the anticipated format.
	 * @throws LBException
	 */
	public void validate(URI source, int validationLevel) throws LBException;

}