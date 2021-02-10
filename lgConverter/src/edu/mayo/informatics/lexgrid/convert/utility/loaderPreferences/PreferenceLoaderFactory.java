
package edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.exceptions.UnexpectedError;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.interfaces.PreferenceLoader;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.loaders.HL7PreferenceLoader;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.loaders.MetaPreferenceLoader;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.loaders.OBOPreferenceLoader;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.loaders.OWLPreferenceLoader;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.loaders.SemNetPreferenceLoader;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.loaders.UMLSPreferenceLoader;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.loaders.XMLPreferenceLoader;

public class PreferenceLoaderFactory {

    private static final String HL7LOADER = "HL7LoaderPreferences";
    private static final String METALOADER = "MetaLoaderPreferences";
    private static final String OBOLOADER = "OBOLoaderPreferences";
    private static final String OWLLOADER = "OWLLoaderPreferences";
    private static final String SEMNETLOADER = "SemNetLoaderPreferences";
    private static final String UMLSLOADER = "UMLSLoaderPreferences";
    private static final String XMLLOADER = "XMLLoaderPreferences";

    private PreferenceLoaderFactory() {

    }

    public static PreferenceLoader createPreferenceLoader(URI xml) throws LgConvertException {
        PreferenceLoaderFactory factory = new PreferenceLoaderFactory();
        String loaderType;
        try {
            loaderType = factory.getXMLType(xml);
        } catch (UnexpectedError e) {
            throw new LgConvertException("Unexpected Error loading Preferences", e);
        }
        if (loaderType.equals(PreferenceLoaderFactory.HL7LOADER)) {
            return new HL7PreferenceLoader(xml);
        } else if (loaderType.equals(PreferenceLoaderFactory.METALOADER)) {
            return new MetaPreferenceLoader(xml);
        } else if (loaderType.equals(PreferenceLoaderFactory.OBOLOADER)) {
            return new OBOPreferenceLoader(xml);
        } else if (loaderType.equals(PreferenceLoaderFactory.OWLLOADER)) {
            return new OWLPreferenceLoader(xml);
        } else if (loaderType.equals(PreferenceLoaderFactory.SEMNETLOADER)) {
            return new SemNetPreferenceLoader(xml);
        } else if (loaderType.equals(PreferenceLoaderFactory.UMLSLOADER)) {
            return new UMLSPreferenceLoader(xml);
        } else if (loaderType.equals(PreferenceLoaderFactory.XMLLOADER)) {
            return new XMLPreferenceLoader(xml);
        } else {
            throw new LgConvertException("Cannot create a loader from the given XML file");
        }
    }

    private String getXMLType(URI file) throws UnexpectedError {
        // get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            // parse using builder to get DOM representation of the XML file
            Document doc = db.parse(new File(file));

            // get the root element
            Element root = doc.getDocumentElement();

            // return the root element's name
            return root.getNodeName();
        } catch (ParserConfigurationException e) {
            throw new UnexpectedError("Unexpected Error Parsing XML File", e);
        } catch (SAXException e) {
            throw new UnexpectedError("Error Parsing XML File", e);
        } catch (IOException e) {
            throw new UnexpectedError("Error Finding XML File", e);
        }
    }

}