
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.net.URI;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon.LexGridXMLProcessor;

/**
 * Wrapper for updated xml loader
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 * 
 */
public class StreamingXMLToSQL {
    
    private LgMessageDirectorIF messages_;
    private Object[] loadedObject;

    /**
     * @param fileLocation
     * @param messageDirector
     * @param isXMLValid
     * @return
     * @throws CodingSchemeAlreadyLoadedException
     */
    public Object[] load(URI fileLocation, LgMessageDirectorIF messageDirector,
            boolean isXMLValid, CodingSchemeManifest manifest) throws CodingSchemeAlreadyLoadedException {
        messages_ = messageDirector;
        LexGridXMLProcessor processor = new LexGridXMLProcessor();
        int entryPoint = processor.getEntryPointType(fileLocation,  messageDirector);

        switch (entryPoint) {
            case 1:  loadedObject = processor.loadCodingScheme(fileLocation, messages_, isXMLValid, manifest); break;
            case 2:  loadedObject = processor.loadRevision(fileLocation, messages_, isXMLValid); break;
            case 3:  loadedObject = processor.loadSystemRelease(fileLocation, messages_, isXMLValid); break;
            case 4:  loadedObject = processor.loadValueSetDefinition(fileLocation, messages_, isXMLValid); break;
            case 5:  loadedObject = processor.loadPickListDefinition(fileLocation, messages_, isXMLValid); break;
            default: messageDirector.info("No Valid LexGrid XML entry point found at " + fileLocation); break;
        }
       
        return loadedObject;
    }

}