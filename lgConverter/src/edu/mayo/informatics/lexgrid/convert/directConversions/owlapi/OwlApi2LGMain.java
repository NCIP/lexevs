
package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.Date;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.lexevs.logging.messaging.impl.CommandLineMessageDirector;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.formatters.XmlContentWriter;

/**
 * This is a generic OWL Loader for OWL to LexEVS transformation.
 * 
 * Last modified on: May 14, 2013 
 */
public class OwlApi2LGMain {

    private OwlApi2LG owl2lg = null;
    private LgMessageDirectorIF messages = null;

    public OwlApi2LGMain(URI owlOntologyURI, CodingSchemeManifest manifest, LoaderPreferences loadPrefs,
            boolean failOnAllErrors,  int memoryUsage, LgMessageDirectorIF messages)
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

            owl2lg = new OwlApi2LG(owlOntologyURI, manifest, loadPrefs, memoryUsage, messages);
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

    void writeLexGridXML(CodingScheme codingScheme, URI output_filename) throws Exception{
        File file = new File(output_filename.toString());
        
        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        XmlContentWriter contentWriter= new XmlContentWriter();
        preProcessCodingSchemeNotSureWhy(codingScheme);
        
        CodedNodeSet cns=null;
        contentWriter.marshalToXml(codingScheme, cns, bw, 1000);

    }

    public static void main(String args[]) {
        try {

             //URI physicalURI = URI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
             URI physicalURI = URI.create("file:///Users/m029451/Documents/nci/owl2-snippet-data.owl");
            // URI physicalURI = URI
            // .create("http://protege.cim3.net/file/pub/ontologies/wine/wine.owl");
            // URI physicalURI = URI.create("file:///c:/camera.owl");

            OwlApi2LGMain moem = new OwlApi2LGMain(physicalURI, null, null, false, OwlApi2LGConstants.MEMOPT_ALL_IN_MEMORY,
                    new CommandLineMessageDirector());

            CodingScheme cst = moem.map();
            //SupportedCodingScheme scs = cst.getMappings().getSupportedCodingScheme()[0];
            //scs.setIsImported(false);
            // URI output_filename = URI
            // .create("file:///c:/fma-owl-dl.xml");

            URI output_filename = URI.create("/tmp/owl2-snippet-data.xml");
        
            moem.writeLexGridXML(cst, output_filename);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    public void preProcessCodingSchemeNotSureWhy(CodingScheme codingScheme) {

        
        addStopFlagsToAssociationPredicates(codingScheme);
        
    }
    
    private void addStopFlagsToAssociationPredicates(CodingScheme cs) {
        if(cs == null) return;
        
        Relations[] relationsList = cs.getRelations();
        if(relationsList == null || relationsList.length == 0) return;
        
        for(int i=0; i<relationsList.length; ++i) {
            Relations relations = relationsList[i];
            processRelationsObject(relations);
        }
    }
    
    private void processRelationsObject(Relations relations) {
        if(relations == null) return;
        
        AssociationPredicate[] apList = relations.getAssociationPredicate();
        
        for(int i=0; i<apList.length; ++i) {
            AssociationPredicate ap = apList[i];
            processAssociationPredicateObject(ap);
        }
    }
    
    private void processAssociationPredicateObject(AssociationPredicate ap) {
        if(ap == null) return;
        AssociationSource as = new AssociationSource();
        as.setSourceEntityCode(LexGridConstants.MR_FLAG);
        ap.addSource(as);
    }    
    
}