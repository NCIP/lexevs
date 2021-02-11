
package org.LexGrid.xml;

import java.io.File;
import java.io.IOException;

public class XmlMain {

/**
	 * @param args
	 * @throws IOException 
	 */
public static void main(String[] args) throws IOException {
		SchemeXml sg = new SchemeXml();
		sg.createXml(new File("test_associations.xml")); // make sure this file does not exist
		System.out.println("done");
		
	}
}