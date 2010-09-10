/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class VersionProperties {

	Properties props;

	String product;
	String version;
	String date;

	public VersionProperties() {
		props = new Properties();
		try {
			// loading properties from the build.properties in the LexBXX
			// install
			props.load(new FileInputStream("../build.properties"));
		} catch (FileNotFoundException e) {

			// Running from webstart or eclipse so try the local directory
			try {
				props.load(new FileInputStream("build.properties"));
			} catch (FileNotFoundException e1) {

				// do nothing
			} catch (IOException e1) {
				// do nothing
			}

		} catch (IOException e) {
			// Do Nothing no properties to show
		}

		// If the properties file does not exist, then null is displayed in the
		// gui help->about for these properties.
		product = props.getProperty("build.product");
		version = props.getProperty("build.version");
		date = props.getProperty("build.timestamp");
	}

	public String getDate() {
		return date;
	}

	public String getProduct() {
		return product;
	}

	public String getVersion() {
		return version;
	}

}