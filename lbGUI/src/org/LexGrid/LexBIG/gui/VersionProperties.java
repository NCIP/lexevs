
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