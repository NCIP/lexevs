
package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.lexevs.logging.LoggerFactory;

import edu.mayo.informatics.lexgrid.convert.directConversions.claml.config.ClaMLConfig;
import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;

public class ClaML2LGMain {

/**
	 * @param args
	 */
public static void main(String[] args) {
	    ClaML2LGMain main = new ClaML2LGMain();
	    try {
            main.map(new File("W:/ontologies/icd10sources/claml/icd10ClaML2008/icd/icd.xml").toURI(), null, LoggerFactory.getLogger());
        } catch (LgConvertException e) {
            e.printStackTrace();
        }
	}
	
	public CodingScheme map(URI inFileName, LoaderPreferences loaderPreferences, LgMessageDirectorIF lg_messages) throws LgConvertException {
		
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