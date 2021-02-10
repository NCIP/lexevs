
package edu.mayo.informatics.lexgrid.convert.Conversions;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexOnt.CodingSchemeManifest;

public class ReadImpl {
    CodingSchemeManifest codingSchemeManifest;
    LoaderPreferences loaderPreferences;

/**
     * @return the codingSchemeManifest
     */
public CodingSchemeManifest getCodingSchemeManifest() {
        return codingSchemeManifest;
    }

    /**
     * @param codingSchemeManifest
     *            the codingSchemeManifest to set
     */
    public void setCodingSchemeManifest(CodingSchemeManifest codingSchemeManifest) {
        this.codingSchemeManifest = codingSchemeManifest;
    }

    /**
     * Returns the loader's current LoaderPreferences
     * 
     * @return The Loader's current LoaderPreferences
     */
    public LoaderPreferences getLoaderPreferences() {
        return this.loaderPreferences;
    }

    /**
     * Sets the Loader's LoaderPreferences
     * 
     * @param loadPrefs
     *            The LoaderPreferences object to use
     */
    public void setLoaderPreferences(LoaderPreferences loadPrefs) {
        this.loaderPreferences = loadPrefs;
    }

}