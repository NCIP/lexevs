
package edu.mayo.informatics.lexgrid.convert.directConversions.obo1_2;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.Collection;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.logging.messaging.impl.CommandLineMessageDirector;

import edu.mayo.informatics.resourcereader.core.IF.ResourceContents;
import edu.mayo.informatics.resourcereader.core.IF.ResourceManifest;
import edu.mayo.informatics.resourcereader.obo.OBOContents;
import edu.mayo.informatics.resourcereader.obo.OBORelations;
import edu.mayo.informatics.resourcereader.obo.OBOResourceManifest;
import edu.mayo.informatics.resourcereader.obo.OBOResourceReader;
import edu.mayo.informatics.resourcereader.obo.OBOTerms;

/**
 * OBO To EMF Implementation.
 * 
 * @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */
public class OBO2LGMain {
    private CachingMessageDirectorIF messages_;

    public CodingScheme map(URI inFileName, ResourceManifest manifestObj, CachingMessageDirectorIF messages)
            throws Exception {
        messages_ = messages;

        if (inFileName == null) {
            messages_.fatalAndThrowException("Error! Input file name is null.");
        }

        CachingMessageDirectorIF logger = new CachingMessageDirectorImpl(messages_);

        OBOResourceManifest omf = null;

        if ((manifestObj != null) && (manifestObj instanceof OBOResourceManifest))
            omf = (OBOResourceManifest) manifestObj;
        else
            omf = new OBOResourceManifest(inFileName, null, null, logger);

        logger.debug("OBOResourceManifest=" + omf);

        logger.debug("OBOResourceManifest is valid=" + omf.isValidManifest());
        OBOResourceReader oboReader = new OBOResourceReader(logger);

        try {
            oboReader.initResourceManifest(omf, logger);

        } catch (Exception e) {
            messages_.fatalAndThrowException("Failed to load OBO Content from: " + inFileName + "", e);
        }

        if (oboReader != null) {

            ResourceContents rContents = oboReader.getContents(false, false);
            // logger.debug(rContents.toString());
            Collection termList = null, relList = null;
            if (rContents != null && rContents instanceof OBOContents) {
                OBOTerms terms = ((OBOContents) rContents).getOBOTerms();
                termList = terms.getAllMembers();
                OBORelations relations = ((OBOContents) rContents).getOBORelations();
                relList = relations.getAllMembers();
            }
            if (((termList != null) && (!termList.isEmpty())) || ((relList != null) && (!relList.isEmpty()))) {
                try {
                    int indexOfFileNameWithoutExt = inFileName.toString().lastIndexOf(".");
                    int indexOfLastFileSeparator = inFileName.toString().lastIndexOf(
                            System.getProperty("file.separator"));
                    // If the obo file does not have a .obo extension, we use
                    // the whole filename
                    if (indexOfFileNameWithoutExt == -1) {
                        indexOfFileNameWithoutExt = inFileName.toString().length();
                    }
                    // We need to take care of the case where the path may have
                    // a "." in it....for example
                    // path= /bmir.app/ncbo/mouse
                    if (indexOfLastFileSeparator > indexOfFileNameWithoutExt) {
                        indexOfFileNameWithoutExt = inFileName.toString().length();
                    }
                    String fileNameWithoutExt = inFileName.toString().substring(indexOfLastFileSeparator + 1,
                            indexOfFileNameWithoutExt);
                    OBO2LGStaticMapHolders oesmh = new OBO2LGStaticMapHolders(messages);
                    CodingScheme csclass = oesmh.getOBOCodingScheme(oboReader, fileNameWithoutExt);

                    if (csclass != null) {
                        OBO2LGDynamicMapHolders oboDynamicMap = new OBO2LGDynamicMapHolders(messages);
                        boolean processed = oboDynamicMap.prepareCSClass(oboReader, csclass);

                        if (processed) {
                            oboDynamicMap.populateSupportedProperties(csclass);
                            oboDynamicMap.populateSupportedSources(csclass);
                            oboDynamicMap.populateSupportedAssociations(csclass);
                            // requires call to populateSupportedAssociations
                            // first
                            // so that it can adjust it's association list
                            // properly
                            oboDynamicMap.populateSupportedHierarchy(csclass);
                            oboDynamicMap.setSupportedHierarchyAssociationsTransitive(csclass);
                            messages_.info("Processing DONE!!");
                            return csclass;
                        }
                    }
                } catch (Exception e) {
                    messages_.fatalAndThrowException("Failed in OBO Mapping...", e);
                }
            }
        }
        // if it didn't return a coding scheme above, it didn't work...
        messages_.fatalAndThrowException("Failed to read the OBO file.", new Exception("Problem reading the OBO file"));
        // it is impossible to get to the next line, because the above line
        // throws an exception
        return null;
    }

    public static void main(String args[]) {
        try {

            // URI.create("http://www.co-ode.org/ontologies/amino-acid/2006/05/18/amino-acid.owl");
             URI physicalURI = URI.create("file:///s:/ontologies/obo/cell.obo");
             //URI physicalURI = URI.create("file:///C:/lexevs_v6/lbTest/resources/testData/cell.obo");
             OBO2LGMain moem = new OBO2LGMain();
             CachingMessageDirectorIF message= new CachingMessageDirectorImpl(new CommandLineMessageDirector());
            CodingScheme codingScheme = moem.map(physicalURI, null, message);

            URI output_filename = URI.create("file:///c:/temp/cell.xml");
            File file = new File(output_filename);
            // file.createNewFile();
            FileWriter out = new FileWriter(file);            
            codingScheme.marshal(out);
//            XmlContentWriter xmlContentWriter = new XmlContentWriter();
//            xmlContentWriter.marshalToXml(codingScheme, output_filename);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }    
    
    
}