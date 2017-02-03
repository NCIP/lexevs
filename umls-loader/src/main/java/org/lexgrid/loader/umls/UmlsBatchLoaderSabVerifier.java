package org.lexgrid.loader.umls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Verify the file and SAB are valid.
 *
 */
public class UmlsBatchLoaderSabVerifier {

	private static final String SAB_FILE = "MRSAB.RRF";
	private static final String DELIMETER = "[|]";
	private static final String PROP_RRF_DIR = "rrfDir";
	private static final String PROP_SAB = "sab";
	private static final int SAB_INDEX = 3;
	
	private String rrfDir = null;
	private String sab = null;
	
	public UmlsBatchLoaderSabVerifier(Properties props) {
		rrfDir = props.getProperty(PROP_RRF_DIR);
		sab = props.getProperty(PROP_SAB);
	}
	
	protected boolean isSabValid() {
		boolean valid = false;
		URI uri = null;
		
		try {
			uri = new URI(rrfDir + SAB_FILE);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Invalid RRF file: " + rrfDir, e);
		}
		
		File file = new File(uri.getPath());
		BufferedReader br = null;
		try {			
		    br = new BufferedReader(new FileReader(file));
		    String line;
		    while ((line = br.readLine()) != null && !valid) {
		    	// process the line.
		    	valid = sab.equals(getSab(line));
		    }
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Invalid RRF file: " + rrfDir, e);
		} catch (IOException e) {
			throw new RuntimeException("Invalid RRF file contents: " + rrfDir, e);
		}
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		
		return valid;
	}
	
	private String getSab(String entry) {
		String[] tokens = entry.split(DELIMETER);
		return tokens[SAB_INDEX];
	}
	
}
