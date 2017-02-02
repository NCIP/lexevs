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

	private static final String DELIMETER = "[|]";
	
	private String rrfDir = null;
	private String sab = null;
	
	public UmlsBatchLoaderSabVerifier(Properties props){
		
		rrfDir = props.getProperty("rrfDir");
		sab = props.getProperty("sab");
	}
	
	protected boolean isSabValid() {
		boolean valid = false;
		URI uri = null;
		
		try {
			uri = new URI(rrfDir + "MRSAB.RRF");
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File file = new File(uri.getPath());
		BufferedReader br = null;
		try {			
		    br = new BufferedReader(new FileReader(file));
		    String line;
		    while ((line = br.readLine()) != null && !valid) {
		       // process the line.
		    	valid = sab.equals(getSab(line));
		    	System.out.println(getSab(line));
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return tokens[3];
	}
	
	public static void main(String[] args) {
		
		URI uri = new File("/Users/endlecm/deployment/lexevs6412/test/resources/testData/sampleUMLS-AIRs").toURI();
		String sab = "AIR";
		
		Properties props = new Properties();
		props.put("sab", sab);
		props.put("rrfDir", uri.toString());
		
		
		UmlsBatchLoaderSabVerifier verifier = new UmlsBatchLoaderSabVerifier(props);
		
		boolean isValid = verifier.isSabValid();
		System.out.println("SAB found = " + isValid);
    }
}
