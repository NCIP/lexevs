
package edu.mayo.informatics.lexgrid.convert.directConversions.protegeOwl;

import java.net.URI;
import java.util.Date;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedCodingScheme;
import org.lexevs.logging.messaging.impl.CommandLineMessageDirector;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;

/**
 * This is a generic OWL Loader for OWL to EMF transformation.
 * 
 * Last modified on: Apr 28, 2008 9:28:19 AM
 */
public class ProtegeOwl2LGMain {

    private ProtegeOwl2LG owl2lg = null;
    private LgMessageDirectorIF messages = null;

    public ProtegeOwl2LGMain(URI owlOntologyURI, CodingSchemeManifest manifest, LoaderPreferences loadPrefs,
            boolean failOnAllErrors, int memorySafe, LgMessageDirectorIF messages)
            throws Exception {
        this.messages = messages;

        messages.info("Loading From URI: " + owlOntologyURI);
        
        try {
            if (owlOntologyURI == null) {
                String msg = "Input URI of ontology is required.";
                messages.fatalAndThrowException(msg);
                throw new LgConvertException(msg);
            }

            if (manifest != null && !manifest.isValid()) {
                String msg = "The manifest is not valid.";
                messages.error(msg);
                if (failOnAllErrors) {
                    throw new LgConvertException(msg);
                }

            }

            owl2lg = new ProtegeOwl2LG(owlOntologyURI, manifest, loadPrefs, memorySafe, messages);
        } catch (Exception e) {
            messages.fatalAndThrowException("Conversion failed", e);
        }
    }

    /**
     * Main entry point for conversion from NCI Thesaurus OWL format to the
     * LexGrid model.
     * 
     * @param inFileName
     *            The source file.
     * @param messages
     *            Responsible for handling display of program messages to the
     *            user.
     * @return The resulting coding scheme (EMF representation).
     * @throws Exception
     *             If an error occurs during conversion.
     */

    public CodingScheme map() throws Exception {
        CodingScheme cs = null;
        try {
            messages.info("Started at: " + new Date());

            // Validate parameters ...
            if (messages == null) {
                throw new LgConvertException("Message handler must be provided.");
            }

            // Perform the conversion from OWL to LexGrid EMF model.
            cs = owl2lg.run();

        } catch (Exception e) {
            messages.fatalAndThrowException("Conversion failed", e);
        } finally {
            messages.info("Ended at: " + new Date());
        }

        return cs;
    }

    void writeLexGridXML(CodingScheme codingScheme, URI output_filename) {
        throw new UnsupportedOperationException(); //TODO  Need to implement new 6.0 exporter 
//        LgXMLResourceImpl xml = null;
//        try {
//
//            xml = new LgXMLResourceImpl(org.eclipse.lg.common.util.URI.createURI(output_filename.toURL().toString()));
//            xml.getContents().add(codingScheme);
//
//            // Perform the save ...
//            xml.save();
//        } catch (Exception e) {
//
//            messages.fatal("Failed - " + e.toString() + " see log file.");
//
//        }
    }

    public static void main(String args[]) {
        try {

             URI physicalURI =
             URI.create("http://www.co-ode.org/ontologies/pizza/2007/02/12/pizza.owl");
            // URI physicalURI = URI
            // .create("http://protege.cim3.net/file/pub/ontologies/wine/wine.owl");
            // URI physicalURI = URI.create("file:///c:/camera.owl");
            // URI physicalURI = URI
            // .create("file:///c:/pizza-original.owl");
            // URI physicalURI = URI
            // .create("file:///c:/pizza-modified.owl");
            // URI physicalURI = URI
            // .create("file:///c:/AugmentedFood.owl");
            // URI physicalURI = URI
            // .create("file:///c:/amino-acid.owl");
            // URI physicalURI = URI
            // .create("file:///c:/birnlex.owl");
            // URI physicalURI = URI
            // .create("file:///C:/My-Home/Ontologies/fmaOwlDlComponent_2_0.owl");
            //URI physicalURI = URI.create("file:///c:/koala-good.owl");
            //URI physicalURI = URI.create("file:///c:/zebrafish.owl");
            //URI physicalURI = URI.create("file:///c:/pizza-original.owl");

            // URI physicalURI = URI
            // .create("file:///c:/generations.owl");
            // URI physicalURI =
            // URI.create("http://www.co-ode.org/ontologies/amino-acid/2006/05/18/amino-acid.owl");
            // URI physicalURI = URI.create("file:///c:/Gene-Ontology.owl");
            ProtegeOwl2LGMain moem = new ProtegeOwl2LGMain(physicalURI, null, null, false, ProtegeOwl2LGConstants.MEMOPT_ALL_IN_MEMORY,
                    new CommandLineMessageDirector());

            CodingScheme cst = moem.map();
            SupportedCodingScheme scs = cst.getMappings().getSupportedCodingScheme()[0];
            scs.setIsImported(false);
            // URI output_filename = URI
            // .create("file:///c:/fma-owl-dl.xml");

            URI output_filename = URI.create("file:///c:/pizza.xml");
           // URI output_filename = URI.create("file:///c:/koala-good.xml");
           // URI output_filename = URI.create("file:///c:/zebrafish.xml");
            // URI output_filename =
            // URI.create("file:///c:/pizza-modified.xml");
            // URI output_filename = URI.create("file:///c:/AugmentedFood.xml");
            // URI output_filename = URI.create("file:///c:/birnlex.xml");
            // URI output_filename = URI.create("file:///c:/pizza.xml");
            // URI output_filename = URI.create("file:///c:/amino-acid.xml");
            // URI output_filename = URI.create("file:///c:/Gene-Ontology.xml");
            // URI output_filename = URI.create("file:///c:/amino.xml");
            // URI output_filename = URI.create("file:///c:/camera.xml");
            // URI output_filename = URI.create("file:///c:/wine.xml");
            // URI output_filename = URI
            // .create("file:///c:/generations.xml");
            moem.writeLexGridXML(cst, output_filename);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}