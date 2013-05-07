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