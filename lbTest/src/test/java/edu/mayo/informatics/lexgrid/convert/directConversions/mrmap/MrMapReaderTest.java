
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;


import java.io.IOException;

import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.RRFLineReader;
import junit.framework.TestCase;

public class MrMapReaderTest extends TestCase {
	
	public  void testReader() throws IOException{
    RRFLineReader reader = null;
        
       reader = new RRFLineReader("resources/testData/mrmap_mapping/NCIt-to-ICD9mappings.MRMAP.RRF");
       String[] list = reader.readRRFLine();
       assertTrue(list.length == 25);
       reader.close();

	}
}