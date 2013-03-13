/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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