
package edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.loaders;

import java.net.URI;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Preferences.loader.XMLLoadPreferences.XMLLoaderPreferences;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.PreferenceLoaderConstants;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.interfaces.PreferenceLoader;

/**
 * Class to load XML loader preferences from an XML file
 * 
 * @author <A HREF="mailto:peterson.kevin@mayo.edu">Kevin Peterson</A>
 */
public class XMLPreferenceLoader extends BasePreferenceLoader implements PreferenceLoader {

    /**
     * Constructor for creating an XMLPreferenceLoader. This class is used to
     * populate specific XML constants that will be used during the load
     * 
     * @param XMLPreferences
     *            The location of the XML preferences file.
     * @param validate
     *            Whether or not to validate the preferences XML against the
     *            associated XML schema. This will look for invalid, extra, or
     *            missing values. Note that even if this is 'false', the XML
     *            will be structurally validated. This means that even if it is
     *            not validated against the schema, it will still be validated
     *            to ensure it is a well-structured XML file.
     * @throws LgConvertException
     *             Thrown if the XML is not a valid XML file, or if 'validate'
     *             is true this exception will be thrown if the XML does not
     *             validate against the schema.
     */
    public XMLPreferenceLoader(URI XMLPreferences, boolean validate) throws LgConvertException {
        if (validate) {
            if (validate(XMLPreferences, ClassLoader.getSystemResource(PreferenceLoaderConstants.XML_XSD))) {
                prefs = XMLPreferences;
            } else {
                log.error("Validation failed, XML does not conform to the schema");
                throw new LgConvertException("Validation failed, XML does not conform to the schema");
            }
        } else {
            prefs = XMLPreferences;
        }
    }

    /**
     * Constructor for creating an XMLPreferenceLoader. This class is used to
     * populate specific XML constants that will be used during the load.
     * 
     * NOTE: This will by default NOT validate the XML against its schema.
     * 
     * @param XMLPreferences
     *            The location of the XML preferences file.
     * @throws LgConvertException
     *             Thrown if the XML is not a valid XML file, or if the XML does
     *             not validate against the schema.
     */
    public XMLPreferenceLoader(URI XMLPreferences) throws LgConvertException {
        this(XMLPreferences, false);
    }

    /**
     * Validates an XML preferences file against its corresponding XSD. If
     * errors are found, the exception will be logged.
     * 
     * @return false if the XML does not validate, otherwise true.
     */
    public boolean validate() {
        return validate(prefs, ClassLoader.getSystemResource(PreferenceLoaderConstants.XML_XSD));
    }

    public LoaderPreferences load() throws LgConvertException {
        return unmarshal(XMLLoaderPreferences.class);
    }

}