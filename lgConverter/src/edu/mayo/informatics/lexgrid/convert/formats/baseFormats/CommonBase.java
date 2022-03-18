
package edu.mayo.informatics.lexgrid.convert.formats.baseFormats;

import java.net.URI;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexOnt.CodingSchemeManifest;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;

public class CommonBase {
    private URI manifestLocation = null;
    private LoaderPreferences loadPreferences = null;
    protected CodingSchemeManifest codingSchemeManifest;

/**
     * @return the manifestLocation
     */
public URI getManifestLocation() {
        return manifestLocation;
    }

    /**
     * @param manifestLocation
     *            the manifestLocation to set
     */
    public void setManifestLocation(URI manifestLocation) {
        this.manifestLocation = manifestLocation;
    }

    /**
     * @return the manifest
     */
    public CodingSchemeManifest getCodingSchemeManifest() {
        return codingSchemeManifest;
    }

    /**
     * @param manifest
     *            the manifest to set
     */
    public void setCodingSchemeManifest(CodingSchemeManifest manifest) {
        this.codingSchemeManifest = manifest;
    }

    /**
     * Returns the loader's current LoaderPreferences
     * 
     * @return The Loader's current LoaderPreferences
     */
    public LoaderPreferences getLoaderPreferences() {
        return this.loadPreferences;
    }

    /**
     * Sets the Loader's LoaderPreferences
     * 
     * @param loadPrefs
     *            The LoaderPreferences object to use
     */
    public void setLoaderPreferences(LoaderPreferences loadPrefs) {
        this.loadPreferences = loadPrefs;
    }

    public String testConnection() throws ConnectionFailure {

        if (manifestLocation != null) {

            ManifestUtil manf = new ManifestUtil();
            if (!manf.isValidManifest(manifestLocation)) {
                throw new ConnectionFailure("The manifest file '" + manifestLocation + "' is not valid. ");
            }

        }
        return "";
    }
}