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
package edu.mayo.informatics.lexgrid.convert.directConversions.obo1_2;

import java.net.URI;
import java.util.Collection;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;

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

}