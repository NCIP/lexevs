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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.directConversions;

import java.net.URI;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon.LexGridXMLProcessor;
import org.LexGrid.codingSchemes.CodingScheme;

/**
 * Wrapper for updated xml loader
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 * 
 */
public class StreamingXMLToSQL {

    private LgMessageDirectorIF messages_;
    private CodingScheme[] codingScheme;

    /**
     * @param fileLocation
     * @param messageDirector
     * @param isXMLValid
     * @return
     * @throws CodingSchemeAlreadyLoadedException
     */
    public org.LexGrid.codingSchemes.CodingScheme[] load(URI fileLocation, LgMessageDirectorIF messageDirector,
            boolean isXMLValid) throws CodingSchemeAlreadyLoadedException {
        messages_ = messageDirector;
        LexGridXMLProcessor processor = new LexGridXMLProcessor();
        int entryPoint = processor.getEntryPointType(fileLocation.getPath());

        switch (entryPoint) {
            case 1:  codingScheme = processor.loadCodingScheme(fileLocation.getPath(), messages_, isXMLValid); break;
            case 2:  codingScheme = processor.loadRevision(fileLocation.getPath(), messages_, isXMLValid); break;
            case 3:  codingScheme = processor.loadSystemRelease(fileLocation.getPath(), messages_, isXMLValid); break;
            default: messageDirector.info("No Valid LexGrid XML entry point found at " + fileLocation.getPath()); break;
        }

        return codingScheme;
    }

}
