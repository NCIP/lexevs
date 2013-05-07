/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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