
package edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.loaders;

import java.net.URI;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Preferences.loader.SemNetLoadPreferences.SemNetLoaderPreferences;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.PreferenceLoaderConstants;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.interfaces.PreferenceLoader;

/**
 * Class to load SemNet loader preferences from an XML file
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class SemNetPreferenceLoader extends BasePreferenceLoader implements PreferenceLoader {
    /**
     * Constructor for creating an SemNetPreferenceLoader. This class is used to
     * populate specific constants that will be used during the load
     * 
     * @param SemNetPreferences
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
    public SemNetPreferenceLoader(URI SemNetPreferences, boolean validate) throws LgConvertException {
        if (validate) {
            if (validate(SemNetPreferences, ClassLoader.getSystemResource(PreferenceLoaderConstants.SEMNET_XSD))) {
                prefs = SemNetPreferences;
            } else {
                log.error("Validation failed, XML does not conform to the schema");
                throw new LgConvertException("Validation failed, XML does not conform to the schema");
            }
        } else {
            prefs = SemNetPreferences;
        }
    }

    /**
     * Constructor for creating an SemNetPreferenceLoader. This class is used to
     * populate specific SemNet constants that will be used during the load.
     * 
     * NOTE: This will by default NOT validate the XML against its schema.
     * 
     * @param SemNetPreferences
     *            The location of the XML preferences file.
     * @throws LgConvertException
     *             Thrown if the XML is not a valid XML file, or if the XML does
     *             not validate against the schema.
     */
    public SemNetPreferenceLoader(URI SemNetPreferences) throws LgConvertException {
        this(SemNetPreferences, false);
    }

    /**
     * Validates an XML preferences file against its corresponding XSD. If
     * errors are found, the exception will be logged.
     * 
     * @return false if the XML does not validate, otherwise true.
     */
    public boolean validate() {
        return super.validate(prefs, ClassLoader.getSystemResource(PreferenceLoaderConstants.SEMNET_XSD));
    }

    public LoaderPreferences load() throws LgConvertException {
        return unmarshal(SemNetLoaderPreferences.class);
    }

}