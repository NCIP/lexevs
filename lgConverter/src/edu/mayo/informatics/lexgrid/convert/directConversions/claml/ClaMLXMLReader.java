
package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.lexevs.system.utility.MyClassLoader;
import org.LexGrid.LexBIG.claml.ClaML;

import edu.mayo.informatics.lexgrid.convert.directConversions.claml.config.ClaMLConfig;
import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;

public class ClaMLXMLReader {


	public ClaML readClaMLXML(URI clamlXMLuri, ClaMLConfig config) throws LgConvertException, FileNotFoundException {

		ClaML claml;
		try {
			JAXBContext jc = JAXBContext.newInstance(config.getClamlPackageName(), MyClassLoader.instance());

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			
			claml = (ClaML)
			    unmarshaller.unmarshal(new File(clamlXMLuri));
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new LgConvertException("Problem reading ClaML XML file.", e);
		}
		
		return claml;
	}
	public boolean verifyClaML(String fileName){
		return true;
	}
	
	public static void main(String args[]) throws Exception {
		URI uri = new URI("W:/user/m005256/EclipseWorkSpaces/LexBIG/claml/resources/icd.xml");
		new ClaMLXMLReader().readClaMLXML(uri, new ClaMLConfig());
	}
}