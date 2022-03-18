
package edu.mayo.informatics.resourcereader.obo;

import java.net.URI;
import java.net.URL;
import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.IF.ResourceHeader;
import edu.mayo.informatics.resourcereader.core.IF.ResourceManifest;
import edu.mayo.informatics.resourcereader.core.IF.ResourceReader;

/**
 * The class is used to read a resource
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOResourceReader extends OBO implements ResourceReader {
    private OBOResourceReaderHelper oboHelper = null;

    private OBOResourceManifest resInfo = null;
    private OBOHeader header = null;
    private OBOContents contents = null;

    // Vector or OBOHeader Objects, mapped to external ontologies
    private Vector<OBOHeader> importedOBOOntologies = new Vector<OBOHeader>();
    private Vector<URI> importedURLs = new Vector<URI>();

    public OBOResourceReader(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public void initResourceManifest(ResourceManifest manifest, LgMessageDirectorIF rLogger) {
        if (rLogger != null)
            setLogger(rLogger);

        try {
            if ((manifest != null) && (manifest instanceof OBOResourceManifest)) {
                if (((OBOResourceManifest) manifest).isValidManifest())
                    resInfo = (OBOResourceManifest) manifest;
            } else {
                String msg = "Manifest object is either null or incompatible.[Expected type: OBOResourceManifest]";
                logger.fatal(msg);
            }
        } catch (Exception e) {
            try {
                String msg = "Failed to initialize Manifest. " + e.getMessage();
                logger.fatal(msg, e);
            } catch (Exception e1) {
                e1.printStackTrace();
                logger.fatal(e1.getMessage());
            }
        }
    }

    private void loadHeader(boolean createNew) {
        if (isReady()) {
            try {
                if ((header == null) || (!header.isHeaderFilled()) || (createNew == true)) {
                    OBOResourceManifest oboResManf = (OBOResourceManifest) resInfo;
                    oboHelper = new OBOResourceReaderHelper(logger);
                    oboHelper.setStream(oboResManf.getResourceLocation());
                    ResourceHeader rh = oboHelper.readHeader();

                    if ((rh != null) && (rh instanceof OBOHeader))
                        header = (OBOHeader) rh;
                }
            } catch (Exception e) {
                String msg = "Failed to read OBO Resource Header!" + e.getMessage();
                logger.warn(msg, e);
            }
        }
    }

    public OBOHeader getLoadedHeader(boolean createNew) {
        loadHeader(createNew);
        return header;
    }

    public OBOHeader getResourceHeader(boolean createNew) {
        return getLoadedHeader(createNew);
    }

    private void importContents(URI importURI, Vector<URI> myLoadedURLs, Vector<OBOHeader> oboImportedHeaders,
            OBOContents contents) {
        if (importURI == null)
            return;

        // Load the contents of the OBO Ontology denoted by importURL
        // and add it to the current one.
        boolean toLoadURL = true;

        if (myLoadedURLs != null) {
            // Do not import a URL if it already exists in the myLoadedURLs
            for (int i = 0; i < myLoadedURLs.size(); i++) {
                Object loadedUrlObj = myLoadedURLs.elementAt(i);

                if ((loadedUrlObj != null) && (loadedUrlObj instanceof URL)) {
                    if (importURI.getPath().equalsIgnoreCase(((URL) loadedUrlObj).getPath())) {
                        String msg = "The URL " + importURI.getPath() + " is already loaded.";
                        logger.info(msg);
                        toLoadURL = false;
                    }
                }
            }
        } else
            myLoadedURLs = new Vector<URI>();

        if (oboImportedHeaders == null)
            oboImportedHeaders = new Vector<OBOHeader>();

        if (toLoadURL == true) {
            try {
                myLoadedURLs.addElement(importURI);

                OBOResourceReader reader = new OBOResourceReader(logger);
                ResourceManifest rmf = new OBOResourceManifest(importURI, null, null, logger);
                reader.initResourceManifest(rmf, logger);
                OBOHeader oH = (OBOHeader) reader.getLoadedHeader(false);

                if (oH != null) {
                    // Add the ResourceHeader to the Vector of OBO Header
                    oboImportedHeaders.addElement(oH);

                    // importedOBOOntologies
                    loadImportedOntologies(oH, myLoadedURLs, oboImportedHeaders, contents);
                    logger.debug("Loading URL = " + importURI.getPath());

                    if (oboHelper == null)
                        oboHelper = new OBOResourceReaderHelper(importURI, logger);
                    else
                        oboHelper.setStream(importURI);

                    oboHelper.readAndMergeContents(contents, oH);
                }
            } catch (Exception e) {
                String msg = "Failed to import URL=" + importURI + "; " + e.getMessage();
                logger.warn(msg, e);
            }
        }
    }

    private void loadImportedOntologies(OBOHeader oboHeader, Vector<URI> myLoadedURLs,
            Vector<OBOHeader> oboImportedHeaders, OBOContents contents) {
        Vector<URI> imports = oboHeader.getImportedOntologies();
        if (imports != null) {
            for (int i = 0; i < imports.size(); i++) {
                try {
                    importContents(imports.elementAt(i), myLoadedURLs, oboImportedHeaders, contents);
                } catch (Exception e) {
                    logger.fatal("Could not import from " + imports.elementAt(i).toString());
                }
            }

        }
    }

    private void loadContents(boolean isLazy, OBOContents contents) {
        if (isReady()) {
            // See if header is loaded or not, refresh it.
            loadHeader(false);

            // Read Realtionships from relationship resource in manifest.
            importContents(resInfo.getRelationshipsDefinitionResource(), importedURLs, importedOBOOntologies, contents);

            // Read Source Abbreviations from relationship resource in manifest.
            importContents(resInfo.getSourceAbbreviationsResource(), importedURLs, importedOBOOntologies, contents);

            // load current Files content
            // Read Source Abbreviations from relationship resource in manifest.
            importContents(resInfo.getResourceLocation(), importedURLs, importedOBOOntologies, contents);
        }
    }

    private boolean isReady() {
        if (resInfo == null) {
            logger.warn("Resource Manifest not initialized!");
            return false;
        }

        return true;
    }

    public OBOContents getContents(boolean isLazy, boolean createNew) {
        if ((contents == null) || (createNew)) {
            contents = new OBOContents(logger);

            loadContents(false, contents);

            if ((logger.getLog(LogLevel.WARN).length <= 0)
                    && (logger.getLog(LogLevel.ERROR).length <= 0))
                logger.info("OBO Resource contents loaded successfully!");
            else
                logger.warn("There were problems loading OBO Resource contents; Please see messages.");
        }

        return contents;
    }

    /**
     * @return Returns the importedOBOOntologies.
     */
    public Vector<OBOHeader> getImportedOBOOntologies() {
        return importedOBOOntologies;
    }

    /**
     * @param importedOBOOntologies
     *            The importedOBOOntologies to set.
     */
    public void setImportedOBOOntologies(Vector<OBOHeader> importedOBOOntologies) {
        this.importedOBOOntologies = importedOBOOntologies;
    }
}