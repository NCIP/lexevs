
package org.LexGrid.LexBIG.LexBIGService;

import java.io.Serializable;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Export.Exporter;
import org.LexGrid.LexBIG.Extensions.Index.Index;
import org.LexGrid.LexBIG.Extensions.Load.Loader;

/**
 * The service manager provides a single write and update access point for all
 * of a service's content.
 * 
 * The service manager allows new coding schemes to be validated and loaded,
 * existing coding schemes to be retired and removed and the status of various
 * coding schemes to be updated and changed.
 */
public interface LexBIGServiceManager extends Serializable {

	/**
	 * Activate an inactive coding scheme version.
	 * 
	 * @param codingSchemeVersion
	 *            The absolute version identifier for the coding scheme
	 *            to activate; not null.
	 * @throws LBException
	 */
	void activateCodingSchemeVersion(
			AbsoluteCodingSchemeVersionReference codingSchemeVersion)
			throws LBException;

	/**
	 * Mark a coding scheme as inactive.
	 * 
	 * @param codingSchemeVersion
	 *            The absolute version identifier for the coding scheme
	 *            to deactivate; not null.
	 * @param deactivateDate
	 *            Date/Time to deactivate the coding scheme.
	 * @throws LBException
	 */
	void deactivateCodingSchemeVersion(
			AbsoluteCodingSchemeVersionReference codingSchemeVersion,
			Date deactivateDate) throws LBException;

	/**
	 * Returns the object used to manage all externally registered extensions
	 * to this service.
	 */
	ExtensionRegistry getExtensionRegistry();
	
	/**
	 * Return an instance of the named export extension.
	 * 
	 * @param name
	 *            Name of the extension to return.
	 * @throws LBException
	 */
	Exporter getExporter(String name) throws LBException;

	/**
	 * Returns a description of all registered extensions used to export
	 * information loaded to a LexBIGService.
	 * 
	 * @return The list containing the description of extensions registered for
	 *         this category. Each description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Export.Exporter and providing a
	 *         public parameterless constructor.
	 */
	ExtensionDescriptionList getExportExtensions();

	/**
	 * Return an instance of the named index extension.
	 * <p>
	 * Note that the service may build required or default indices during load
	 * operations. Because these indices are required and automatically
	 * generated, they are not considered to be extensions and are therefore not
	 * reported through this method.
	 * 
	 * @param name
	 *            Name of the extension to return.
	 * @throws LBException
	 */
	Index getIndex(String name) throws LBException;

	/**
	 * Return a list of registered index extensions supported by this service;
	 * empty if none are defined.
	 * <p>
	 * Note that the service may build required or default indices during load
	 * operations. Because these indices are required and automatically
	 * generated, they are not considered to be extensions and are therefore not
	 * reported through this method.
	 * 
	 * @return The list containing the description of extensions registered for
	 *         this category. Each description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Index.Index and providing a public
	 *         parameterless constructor.
	 */
	ExtensionDescriptionList getIndexExtensions();

	/**
	 * Return an instance of the named loader extension.
	 * 
	 * @param name
	 *            Name of the extension to return.
	 * @throws LBException
	 */
	Loader getLoader(String name) throws LBException;

	/**
	 * Returns a description of all registered extensions used to load
	 * information for access by a LexBIGService.
	 * 
	 * @return The list containing the description of extensions registered for
	 *         this category. Each description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Load.Loader and providing a public
	 *         parameterless constructor.
	 */
	ExtensionDescriptionList getLoadExtensions();

	/**
	 * Remove a pending or inactive coding scheme from the service.
	 * 
	 * @param codingSchemeVersion
	 *            The absolute version identifier of the coding scheme to remove.
	 * @throws LBException
	 */
	void removeCodingSchemeVersion(
			AbsoluteCodingSchemeVersionReference codingSchemeVersion)
			throws LBException;
	
	/**
	 * Remove the metadata for a coding scheme from the service.
	 * 
	 * @param codingSchemeVersion
	 *            The absolute version identifier of the coding scheme to remove.
	 * @throws LBException
	 */
	void removeCodingSchemeVersionMetaData(
			AbsoluteCodingSchemeVersionReference codingSchemeVersion)
			throws LBException;

	/**
	 * Remove pending or inactive coding scheme history information.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @throws LBException
	 */
	void removeHistoryService(String codingScheme)
			throws LBException;
	
	/**
	 * Assign a symbolic tag to a specified coding scheme version.
	 * 
	 * @param codingSchemeVersion
	 *            The absolute version identifier of the coding scheme to
	 *            set the tag on.
	 * @param tag
	 *            Tag or label to assign to the given version; if null
	 *            any existing tag is cleared.
	 * @throws LBException
	 */
	void setVersionTag(
			AbsoluteCodingSchemeVersionReference codingSchemeVersion, String tag)
			throws LBException;
	
	/**
	 * Register a coding scheme as a supplement to a specified coding scheme.
	 * 
	 * @param parentCodingScheme
	 *            The coding scheme to which the supplement will be applied
	 * @param supplementCodingScheme
	 *            The coding scheme to use as a supplement
	 * @throws LBException
	 */
	void registerCodingSchemeAsSupplement(
			AbsoluteCodingSchemeVersionReference parentCodingScheme,
			AbsoluteCodingSchemeVersionReference supplementCodingScheme
		) throws LBException;
	
	/**
	 * Unregister a coding scheme as a supplement to a specified coding scheme.
	 * 
	 * @param parentCodingScheme
	 *            The coding scheme to which the supplement was registered
	 * @param supplementCodingScheme
	 *            The coding scheme supplement to be unregistered
	 * @throws LBException
	 */
	void unRegisterCodingSchemeAsSupplement(
			AbsoluteCodingSchemeVersionReference parentCodingScheme,
			AbsoluteCodingSchemeVersionReference supplementCodingScheme
		) throws LBException;
	
	/**
	 * Shuts down LexBIG Services and all associated resources. 
	 * All database connections, caches, and other internal resources 
	 * will be released.
	 */
	void shutdown();
}