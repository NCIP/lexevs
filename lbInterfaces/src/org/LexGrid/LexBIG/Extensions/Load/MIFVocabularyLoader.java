
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * The loader interface validates and/or loads HL7 Model Interchange Format (MIF) sources that are of
 * the VocabularyModel type content for a service.
 * 
 * @author m046445
 *
 */
public interface MIFVocabularyLoader extends Loader {

    public final static String name = "MIFVocabularyLoader";
    public final static String description = "This loader loads the XML files conforming to the HL7 MIF Vocabulary format into the LexGrid database.";
	
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
	 * @throws URISyntaxException 
	 */
	public void load(URI source, URI codingSchemeManifestURI, boolean stopOnErrors, boolean async) throws LBException;

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