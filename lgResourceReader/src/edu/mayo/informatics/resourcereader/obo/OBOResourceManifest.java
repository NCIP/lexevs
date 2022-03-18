
package edu.mayo.informatics.resourcereader.obo;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.IF.ResourceManifest;
import edu.mayo.informatics.resourcereader.obo.exceptions.OBOResourceException;

/**
 * A OBO Specific Resource Manifest
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOResourceManifest extends OBO implements ResourceManifest {
    private URI inputResourceURL = null;
    private URI relationshipsURL = null;
    private URI abbreviationsURL = null;

    private String defaultRelationshipFile = "default_relationship.obo";
    private String defaultXrefFile = "default_GO_xrf_abbs.obo";

    public OBOResourceManifest(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public OBOResourceManifest(URI inputResource, URI relationshipsResource, URI abbreviationsResource,
            CachingMessageDirectorIF rLogger) {
        super(rLogger);
        inputResourceURL = inputResource;

        if (relationshipsResource != null)
            relationshipsURL = relationshipsResource;
        else {
            // messages.showMessage("OBO Relationship file not initialized.
            // Setting
            // default to \"" + defaultRelationshipFile + "\"");
            relationshipsURL = getURI(defaultRelationshipFile);
        }

        if (abbreviationsResource != null)
            abbreviationsURL = abbreviationsResource;
        else {
            // messages.showMessage("OBO Source Abbreviations file not
            // initialized.
            // Setting default to \"" + defaultXrefFile + "\"");
            abbreviationsURL = getURI(defaultXrefFile);
        }
    }

    public void setManifestLocation(URI resource) throws OBOResourceException {
        validate(resource);
        inputResourceURL = resource;
    }

    public void setRelationshipsDefinitionResource(URI resource) throws OBOResourceException {
        validate(resource);
        relationshipsURL = resource;
    }

    public void setSourceAbbreviationsResource(URI resource) throws OBOResourceException {
        validate(resource);
        abbreviationsURL = resource;
    }

    public URI getResourceLocation() {
        return inputResourceURL;
    }

    public URI getRelationshipsDefinitionResource() {
        return relationshipsURL;
    }

    public URI getSourceAbbreviationsResource() {
        return abbreviationsURL;
    }

    public boolean isValidManifest() {
        return validate(inputResourceURL) && validate(relationshipsURL) && validate(abbreviationsURL);
    }

    private boolean validate(URI uri) {
        boolean isValid = true;

        try {
            if (uri.getScheme().equals("file")) {
                new FileReader(new File(uri));
            } else {
                new InputStreamReader(uri.toURL().openConnection().getInputStream());
            }
        }

        catch (Exception e) {
            logger.fatal("ResourceLocation [" + ((uri != null) ? uri.toString() : "NULL") + "] not found.");
            isValid = false;
        }

        return isValid;
    }

    private URI getURI(String fileName) {
        // Get current classloader
        ClassLoader cl = this.getClass().getClassLoader();
        URL url = null;
        try {
            url = cl.getResource(fileName);
            return url.toURI();
        } catch (Exception e) {
            logger.fatal("problem in getURI", e);
            return null;
        }
    }

    public String toString() {
        return "inputResourceURL= " + inputResourceURL + " relationshipsURL= " + relationshipsURL
                + " abbreviationsURL= " + abbreviationsURL + "\n";

    }
}