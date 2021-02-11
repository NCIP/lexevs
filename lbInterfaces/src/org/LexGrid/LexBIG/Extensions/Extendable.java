
package org.LexGrid.LexBIG.Extensions;

import java.io.Serializable;

/**
 * Marks a class as an extension to the LexBIG application programming
 * interface.  This allows for centralized registration, lookup, and access
 * to defined functions.
 */
public interface Extendable extends Serializable {

	/**
	 * Return the name assigned to this service extension. This name must be unique
	 * within context of the installed node and is used to register and lookup
	 * the extension through a LexBIGService.
	 */
	String getName();

	/**
	 * Return a description of the extension.
	 */
	String getDescription();

	/**
	 * Return an identifier for the extension provider.
	 */
	String getProvider();

	/**
	 * Return version information about the extension.
	 */
	String getVersion();

}