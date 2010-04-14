package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import java.net.URI;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

import edu.mayo.informatics.lexgrid.convert.directConversions.claml.config.ClaMLConfig;
import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;

public class ClaML2LGMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
	}
	
	public CodingScheme map(URI inFileName, LoaderPreferences loaderPreferences, boolean failOnAllErrors, LgMessageDirectorIF lg_messages) throws LgConvertException {
		
		ClaMLConfig config = new ClaMLConfig(loaderPreferences);
		
		ClaML2LG converter = new ClaML2LG();		
		try {
			return converter.convertClaMLToEMF(inFileName, config, lg_messages);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LgConvertException("Error Converting: " + e);
		}
	}
}


