
package org.LexGrid.LexBIG.Extensions;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.SortDescriptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

/**
 * Allows registration and lookup of implementors for extensible pieces of the
 * LexBIG architecture.
 */
public interface ExtensionRegistry extends Serializable {

	/**
	 * Returns the description of a registered extension matching the given name.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @return A matching extension description; null if no match is found. If
	 *         not null, the description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Export.Exporter and
	 *         providing a public parameterless constructor.
	 */
	ExtensionDescription getExportExtension(String name);

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
	 * Returns the description of a registered extension matching the given name.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @return A matching extension description; null if no match is found. If
	 *         not null, the description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Query.Filter and providing a
	 *         public parameterless constructor.
	 */
	ExtensionDescription getFilterExtension(String name);

	/**
	 * Returns a description of all registered extensions used to provide
	 * additional filtering of query results.
     * 
     * Filters should _NOT_ be used for search criteria that can be 
     * done with built in restrictions.  Filters are very inefficient.
	 * 
	 * @return The list containing the description of extensions registered for
	 *         this category. Each description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Query.Filter and providing a public
	 *         parameterless constructor.
	 */
	ExtensionDescriptionList getFilterExtensions();

	/**
	 * Returns the description of a registered extension matching the given name.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @return A matching extension description; null if no match is found. If
	 *         not null, the description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Generic.GenericExtension and
	 *         providing a public parameterless constructor.
	 */
	ExtensionDescription getGenericExtension(String name);

	/**
	 * Returns a description of all registered extensions used to implement
	 * application-specific behavior that is centrally accessible from a
	 * LexBIGService.
	 * <p>
	 * Note that only generic extensions (base class GenericExtension) will
	 * be listed here. All other classes are retrievable at the appropriate
	 * interface point (filter, sort, etc).
	 * 
	 * @return The list containing the description of extensions registered for
	 *         this category. Each description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Generic.GenericExtension and
	 *         providing a public parameterless constructor.
	 */
	ExtensionDescriptionList getGenericExtensions();

	/**
	 * Returns the description of a registered extension matching the given name.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @return A matching extension description; null if no match is found. If
	 *         not null, the description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Index.Index and providing a
	 *         public parameterless constructor.
	 */
	ExtensionDescription getIndexExtension(String name);

	/**
	 * Returns a description of all registered extensions used to index
	 * information that has been previously loaded to a LexBIGService.
	 * 
	 * @return The list containing the description of extensions registered for
	 *         this category. Each description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Index.Index and providing a public
	 *         parameterless constructor.
	 */
	ExtensionDescriptionList getIndexExtensions();

	/**
	 * Returns the description of a registered extension matching the given name.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @return A matching extension description; null if no match is found. If
	 *         not null, the description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Load.Loader and providing a
	 *         public parameterless constructor.
	 */
	ExtensionDescription getLoadExtension(String name);

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
	 * Returns the description of a registered extension matching the given name.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @return A matching extension description; null if no match is found. If
	 *         not null, the description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Query.Sort and providing a public
	 *         parameterless constructor.
	 */
	SortDescription getSortExtension(String name);

	/**
	 * Returns a description of all registered extensions used to provide
	 * additional sorting of query results.
	 * 
	 * @return The list containing the description of extensions registered for
	 *         this category. Each description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Query.Sort and providing a public
	 *         parameterless constructor.
	 */
	SortDescriptionList getSortExtensions();
	
	/**
	 * Returns the description of a registered extension matching the given name.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @return A matching extension description; null if no match is found. If
	 *         not null, the description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Query.Search and providing a public
	 *         parameterless constructor.
	 */
	ExtensionDescription getSearchExtension(String name);
	
	/**
	 * Returns a description of all registered extensions used to provide
	 * additional sorting of query results.
	 * 
	 * @return The list containing the description of extensions registered for
	 *         this category. Each description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Query.Search and providing a public
	 *         parameterless constructor.
	 */
	ExtensionDescriptionList getSearchExtensions();

	/**
	 * Registers a class of extension used to export existing content from the
	 * LexGrid repository.
	 * 
	 * @param description
	 *            The description of the extension to register, which must
	 *            provide a unique name for this type of extension and identify
	 *            a class of item implementing
	 *            org.LexGrid.LexBIG.Extensions.Export.Exporter with a public
	 *            parameterless constructor.
	 * @throws LBParameterException
	 *             If the given description does not meet the specified
	 *             criteria.
	 */
	void registerExportExtension(ExtensionDescription description)
			throws LBParameterException;

	/**
	 * Registers a class of extension used to filter query content.
     * 
     * Filters should _NOT_ be used for search criteria that can be 
     * done with built in restrictions.  Filters are very inefficient.
	 * 
	 * @param description
	 *            The description of the extension to register, which must
	 *            provide a unique name for this type of extension and identify
	 *            a class of item implementing
	 *            org.LexGrid.LexBIG.Extensions.Query.Filter with a public
	 *            parameterless constructor.
	 * @throws LBParameterException
	 *             If the given description does not meet the specified
	 *             criteria.
	 */
	void registerFilterExtension(ExtensionDescription description)
			throws LBParameterException;

	/**
	 * Registers a class of extension used to implement application-specific
	 * behavior that is centrally accessible from a LexBIGService.
	 * 
	 * @param description
	 *            The description of the extension to register, which must
	 *            provide a unique name for this type of extension and identify
	 *            a class of item implementing
	 *            org.LexGrid.LexBIG.Extensions.Generic.GenericExtension with a
	 *            public parameterless constructor.
	 * @throws LBParameterException
	 *             If the given description does not meet the specified
	 *             criteria.
	 */
	void registerGenericExtension(ExtensionDescription description)
			throws LBParameterException;

	/**
	 * Registers a class of extension used to index information that has been
	 * previously loaded to a LexBIGService.
	 * 
	 * @param description
	 *            The description of the extension to register, which must
	 *            provide a unique name for this type of extension and identify
	 *            a class of item implementing
	 *            org.LexGrid.LexBIG.Extensions.Index.Index with a public
	 *            parameterless constructor.
	 * @throws LBParameterException
	 *             If the given description does not meet the specified
	 *             criteria.
	 */
	void registerIndexExtension(ExtensionDescription description)
			throws LBParameterException;

	/**
	 * Registers a class of extension used to load information for access by a
	 * LexBIGService.
	 * 
	 * @param description
	 *            The description of the extension to register, which must
	 *            provide a unique name for this type of extension and identify
	 *            a class of item implementing
	 *            org.LexGrid.LexBIG.Extensions.Load.Loader with a public
	 *            parameterless constructor.
	 * @throws LBParameterException
	 *             If the given description does not meet the specified
	 *             criteria.
	 */
	void registerLoadExtension(ExtensionDescription description)
			throws LBParameterException;

	/**
	 * Registers a class of extension used to provide additional sorting of
	 * query results.
	 * <p>
	 * <b>NOTE</b>: Sort extensions can only be applied when resolving
	 * standard node set representations.  Therefore the provided description
	 * <b><i>MUST</i></b> be restricted only to a sort context of
	 * SortContext.SET.
	 * 
	 * @param description
	 *            The description of the extension to register, which must
	 *            provide a unique name for this type of extension and identify
	 *            a class of item implementing
	 *            org.LexGrid.LexBIG.Extensions.Query.Sort with a public
	 *            parameterless constructor.
	 * @throws LBParameterException
	 *             If the given description does not meet the specified
	 *             criteria.
	 */
	void registerSortExtension(SortDescription description)
			throws LBParameterException;
	
	void registerSearchExtension(ExtensionDescription description)
		throws LBParameterException;

	/**
	 * Removes registration for the given named extension. Upon completion, the
	 * extension will no longer be available for lookup from the LexBIG service.
	 * 
	 * @param name
	 *            The extension name; not null. The extension version; null to
	 *            match all versions.
	 * @throws LBParameterException
	 *             If the given name does not match a registered extension.
	 */
	void unregisterExportExtension(String name) throws LBParameterException;

	/**
	 * Removes registration for the given named extension. Upon completion, the
	 * extension will no longer be available for lookup from the LexBIG service.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @throws LBParameterException
	 *             If the given name does not match a registered extension.
	 */
	void unregisterFilterExtension(String name) throws LBParameterException;

	/**
	 * Removes registration for the given named extension. Upon completion, the
	 * extension will no longer be available for lookup from the LexBIG service.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @throws LBParameterException
	 *             If the given name does not match a registered extension.
	 */
	void unregisterGenericExtension(String name) throws LBParameterException;

	/**
	 * Removes registration for the given named extension. Upon completion, the
	 * extension will no longer be available for lookup from the LexBIG service.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @throws LBParameterException
	 *             If the given name does not match a registered extension.
	 */
	void unregisterIndexExtension(String name) throws LBParameterException;

	/**
	 * Removes registration for the given named extension. Upon completion, the
	 * extension will no longer be available for lookup from the LexBIG service.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @throws LBParameterException
	 *             If the given name does not match a registered extension.
	 */
	void unregisterLoadExtension(String name) throws LBParameterException;

	/**
	 * Removes registration for the given named extension. Upon completion, the
	 * extension will no longer be available for lookup from the LexBIG service.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @throws LBParameterException
	 *             If the given name does not match a registered extension.
	 */
	void unregisterSortExtension(String name) throws LBParameterException;
	
	void unregisterSearchExtension(String name) throws LBParameterException;
	
	public <T extends Extendable> T getGenericExtension(String extensionName, Class<T> extensionClass) throws LBParameterException;

}