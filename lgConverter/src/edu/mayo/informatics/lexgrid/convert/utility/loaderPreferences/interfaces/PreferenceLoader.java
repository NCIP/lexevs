
package edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.interfaces;

import java.net.URI;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;

public interface PreferenceLoader {

/**
     * Validates the loaded Preference XML file against the appropriate XSD
     * 
     * @return Whether or not the XML validates against the given XSD. Any
     *         errors will be logged.
     */
public boolean validate();

    /**
     * Returns a generic Preferences object. This method will NOT validate
     * against an XSD, but will do minimum checks to determine if the XML is
     * well-formed.
     * 
     * @return The generic Preferences Object. Because each loader implements
     *         its preferences independently, this method returns a generic
     *         Object. Use a specific loader to get each format's specific
     *         preferences object.
     * @throws LgConvertException
     */
    public LoaderPreferences load() throws LgConvertException;

    /**
     * Gets the URI of the loaded Preferences XML file.
     * 
     * @return The URI of the XML Preferences file assigned to the loader
     */
    public URI getPreferencesURI();

    /**
     * Sets the Loader to use the specified XML Preferences XML file.
     * 
     * @param input
     *            The URI of the XML Preferences file to be assigned to the
     *            loader
     */
    public void setPreferencesURI(URI input);
}