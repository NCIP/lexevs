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