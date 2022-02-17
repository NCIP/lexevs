
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexOnt.CodingSchemeManifest;

/**
 * Validates and/or loads additional data to be maintained and queried as
 * terminology meta-information within the LexBIG system.
 * <p>
 * The LexBIG system allows import and maintenance of many terminology formats.
 * Each terminology, once loaded, is represented as a Coding Scheme in terms
 * of the LexGrid common terminology model.  As a result, each Coding Scheme
 * carries metadata describing the terminology and its content.  This metadata
 * includes, but is not limited to, the following:
 * <ul>
 * <li>Assigned names (formal, local, and registered)</li>
 * <li>Represented version</li>
 * <li>Approximate number of concepts</li>
 * <li>Copyright</li>
 * <li>Supported properties, languages, sources, relationships, hierarchies,
 * and referenced code systems</li>
 * </ul>
 * Where possible, metadata is imported from the source format.  However,
 * this information is not always complete or sufficient to meet the
 * requirements imposed by model constraints (e.g. if a required field such
 * as version is not available) or user expectations.
 * <p>
 * Additional metadata can be loaded in one of two forms, a LexGrid Manifest
 * or generic XML data.
 * <p>
 * The LexGrid Manifest was introduced to accommodate the need to supplement
 * or override default information provided by the source.  More specifically,
 * the manifest provides a means to customize the same coding scheme metadata
 * defined by the LexGrid model, since each element of the manifest extends
 * directly from an element used to define the LexGrid coding scheme object.  
 * Each extended element allows for the administrator to specify whether
 * the manifest definition replaces or supplements original values provided
 * in the terminology source.  Like the LexGrid Terminology model, the manifest
 * is defined by a formal model mastered as XML Schema.
 * <p>
 * Whereas the LexGrid Manifest is tightly coupled with the definition of the
 * LexGrid terminology model, there may be a desire to associate non-LexGrid
 * metadata with a coding scheme.  The LexBIG system handles this by allowing
 * registration of a custom XML file or key/value map of unknown semantics to
 * the coding scheme. This externally defined metadata is interpreted
 * strictly as text-based key-value pairs, which can later be queried as part
 * of a special service metadata index.
 */
public interface MetaData_Loader extends Loader {
    public final static String name = "MetaDataLoader";
    public final static String description = "This loader loads metadata xml files into the system.";

	/**
	 * Loads metadata specified by the given manifest to a previously loaded
	 * coding scheme in the LexBIG repository.
	 * 
	 * @param source
	 *            URI of the xml file containing the manifest definition.
	 * @param codingSchemeVersion
	 * 			  The target coding scheme name and version.    
	 * @param stopOnErrors
	 *            True means stop if any load error is detected.  False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 * @param async
	 *            Flag controlling whether load occurs in the calling thread.  
	 *            If true, the load will occur in a separate asynchronous process.
	 *            If false, this method blocks until the load operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @throws LBException
	 *            If resources cannot be accessed or another load operation is
	 *            already in progress.
	 */
	public void loadLexGridManifest(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion, 
			boolean stopOnErrors, boolean async) throws LBException;

	/**
	 * Loads metadata specified by the given manifest to a previously loaded
	 * coding scheme in the LexBIG repository.
	 * 
	 * @param source
	 *            The manifest to apply.
	 * @param codingSchemeVersion
	 * 			  The target coding scheme name and version.    
	 * @param stopOnErrors
	 *            True means stop if any load error is detected.  False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 * @param async
	 *            Flag controlling whether load occurs in the calling thread.  
	 *            If true, the load will occur in a separate asynchronous process.
	 *            If false, this method blocks until the load operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @throws LBException
	 *            If resources cannot be accessed or another load operation is
	 *            already in progress.
	 */
	public void loadLexGridManifest(CodingSchemeManifest source, AbsoluteCodingSchemeVersionReference codingSchemeVersion, 
			boolean stopOnErrors, boolean async) throws LBException;

	/**
	 * Load auxiliary (non-LexGrid) metadata from the specified XML file
	 * into a searchable metadata index within the LexBIG system.
	 * All tags and values are interpreted as simple text-based key/value
	 * pairs.
	 * 
	 * @param source
	 *            URI of the XML file containing custom metadata.
	 * @param codingSchemeVersion
	 * 			  The target coding scheme name and version.    
     * @param overwrite
     *            If true, existing metadata for the scheme will be erased.
     *            If false, new metadata will be appended to existing metadata.
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
	 *            If resources cannot be accessed or another load operation is
	 *            already in progress.
	 */
	public void loadAuxiliaryData(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion, 
			boolean overwrite, boolean stopOnErrors, boolean async) throws LBException;

	/**
	 * Load auxiliary (non-LexGrid) metadata from the given map into a
	 * searchable metadata index within the LexBIG system.  All map keys and
	 * values are interpreted as simple text.
	 * 
	 * @param source
	 *            Map defining custom metadata.  All keys and values are
	 *            evaluated as strings when inserted to the index.
	 * @param codingSchemeVersion
	 * 			  The target coding scheme name and version.    
     * @param overwrite
     *            If true, existing metadata for the scheme will be erased.
     *            If false, new metadata will be appended to existing metadata.
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
	 *            If resources cannot be accessed or another load operation is
	 *            already in progress.
	 */
	public void loadAuxiliaryData(Map<Object, Object> source, AbsoluteCodingSchemeVersionReference codingSchemeVersion, 
			boolean overwrite, boolean stopOnErrors, boolean async) throws LBException;

	/**
	 * Validate content for a candidate resource without performing a load.
	 * Returns without exception if validation succeeds.
	 * 
	 * @param source
	 *            URI of the XML-based manifest file to check.
     * @param codingSchemeVersion
	 * 			  The target coding scheme name and version.    
	 * @param validationLevel
	 *            Supported levels of validation include:
	 *            0 = Verify the XML is well formed.
	 *            1 = Verify the XML is valid in context of the
	 *                LexGrid Manifest Schema.
	 * @throws LBException
	 */
	public void validateLexGridManifest(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion, int validationLevel) throws LBException;

	/**
	 * Validate content for a candidate resource without performing a load.
	 * Returns without exception if validation succeeds.
	 * 
	 * @param source
	 *            URI of the XML-based auxiliary metadata file to check.
     * @param codingSchemeVersion
	 * 			  The target coding scheme name and version.    
	 * @param validationLevel
	 *            Supported levels of validation include:
	 *            0 = Verify the XML is well formed.
	 * @throws LBException
	 */
	public void validateAuxiliaryData(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion, int validationLevel) throws LBException;

}