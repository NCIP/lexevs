
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