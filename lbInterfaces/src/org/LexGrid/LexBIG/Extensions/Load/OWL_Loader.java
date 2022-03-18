
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * Validates and/or loads content provided in OWL XML format.
 */
public interface OWL_Loader extends Loader {
    public final static String name = "OWLLoader";
    public final static String description = "This loader loads 'OWL Full' files into the LexGrid format.";

	/**
	 * Load content from a candidate resource. This will also result in implicit
	 * generation of standard indices required by the LexBIG runtime.
	 * <p>
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            URI corresponding to the OWL file.
	 * @param manifest
	 *            URI corresponding to the XML document containing load
	 *            coding scheme manifest list; null if not applicable.
	 *            Must be a valid xml file for schema
	 *            http://LexGrid.org/schema/LexBIG/2007/01/CodingSchemeManifestList.xsd
	 * @param memorySetting
	 * 			  If specified, indicates the profile used to tune
	 *            memory/performance tradeoffs. Options are:
	 *            1 = Faster/more memory (holds OWL in memory)
	 *            2 = Slower/less memory (cache OWL to database)
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
	public void load(URI source, URI manifest, int memorySetting, boolean stopOnErrors,  boolean async)
			throws LBException;

	/**
	 * Validate content for a candidate resource without performing a load.
	 * <p>
	 * Returns without exception if validation succeeds.
	 * 
	 * @param source
	 *            URI corresponding to the OWL file.
	 * @param manifest
	 *            URI corresponding to the XML document containing load
	 *            coding scheme manifest list; null if not applicable.
	 *            Must be a valid xml file for schema
	 *            http://LexGrid.org/schema/LexBIG/2007/01/CodingSchemeManifestList.xsd
	 * @param validationLevel
	 *            Supported levels of validation include: 0 = Verify XML is well
	 *            formed. 1 = Verify XML is valid.
	 * @throws LBException
	 */
	public void validate(URI source, URI manifest, int validationLevel)
			throws LBException;
}