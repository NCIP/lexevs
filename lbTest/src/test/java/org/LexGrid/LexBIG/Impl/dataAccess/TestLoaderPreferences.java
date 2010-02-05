/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Impl.dataAccess;

import java.io.File;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.Preferences.loader.OWLLoadPreferences.OWLLoaderPreferences;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.PreferenceLoaderFactory;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.interfaces.PreferenceLoader;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.loaders.OWLPreferenceLoader;

public class TestLoaderPreferences extends TestCase {
    String loaderXML = "resources/testData/OWLPrefs.xml";
    PreferenceLoader loader;

    public void setUp() {
        File owlLoaderPreferences = new File(loaderXML);
        try {
            loader = PreferenceLoaderFactory.createPreferenceLoader(owlLoaderPreferences.toURI());
        } catch (LgConvertException e) {
            e.printStackTrace();
        }
    }

    public void testCreateLoader() {
        assertNotNull(loader);
    }

    public void testFactory() {
        boolean isOwlLoader = loader instanceof OWLPreferenceLoader;
        assertTrue(isOwlLoader);
    }

    public void testCastLoader() {
        try {
            OWLPreferenceLoader owlLoader = (OWLPreferenceLoader) loader;
        } catch (RuntimeException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testGetLoadPreferencesObject() {
        OWLLoaderPreferences prefs = null;
        try {
            prefs = (OWLLoaderPreferences) loader.load();
        } catch (LgConvertException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertNotNull(prefs);
        assertTrue(prefs instanceof OWLLoaderPreferences);
    }
}