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
package edu.mayo.informatics.lexgrid.convert.emfConversions;

import java.io.File;
import java.util.Iterator;

import org.LexGrid.emf.base.xml.LgXMLResourceImpl;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;
import org.LexGrid.emf.valueDomains.ValueDomainDefinition;
import org.LexGrid.messaging.LgMessageDirectorIF;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;

import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFWrite;

/**
 * Writes XML -> EMF
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7237 $ checked in on $Date: 2008-02-19
 *          18:52:29 +0000 (Tue, 19 Feb 2008) $
 */
public class XMLWrite implements EMFWrite {
    private String xmlFolderLocation;
    private static Logger log = Logger.getLogger("convert.XMLReadWrite");
    private LgMessageDirectorIF messages_;
    private boolean overwrite_;

    public XMLWrite(String xmlFolderLocation, boolean overwrite, boolean failOnAllErrors, LgMessageDirectorIF messages) {
        this.xmlFolderLocation = xmlFolderLocation;
        this.messages_ = messages;
        this.overwrite_ = overwrite;
        // TODO do something useful with the failOnAllErrors flag.
    }

    public void writeCodingScheme(CodingScheme codingScheme) throws Exception {
        LgXMLResourceImpl xml = null;
        try {
            String fileLocation = xmlFolderLocation + System.getProperty("file.separator")
                    + codingScheme.getCodingSchemeName() + ".xml";

            messages_.info("Writing to the file '" + fileLocation + "'");

            File temp = new File(fileLocation);
            if (temp.exists() && !overwrite_) {
                messages_
                        .fatalAndThrowException("The output file already exists, and you didn't select the overwrite option.");
            }

            xml = new LgXMLResourceImpl(URI.createFileURI(fileLocation));
            xml.getContents().add(codingScheme);

            // Perform the save ...
            xml.save();
        }

        catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        } finally {
            if (xml != null) {
                xml.unload();
            }
        }
    }

    public void clearCodingScheme(String codingScheme) throws Exception {
        try {
            File temp = new File(xmlFolderLocation + System.getProperty("file.separator") + codingScheme + ".xml");
            if (temp.exists()) {
                boolean success = temp.delete();
                if (!success) {
                    throw new Exception("Problem deleting file");
                }
            }
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        }
    }
    
    public void writeValueDomainDefinition(ValueDomainDefinition vdDef) throws Exception {
        LgXMLResourceImpl xml = null;
        try {
            String fileName = vdDef.getValueDomainURI().replace(":", "_");
            String fileLocation = xmlFolderLocation + System.getProperty("file.separator")
                    + fileName + ".xml";

            messages_.info("Writing to the file '" + fileLocation + "'");

            File temp = new File(fileLocation);
            if (temp.exists() && !overwrite_) {
                messages_
                        .fatalAndThrowException("The output file already exists, and you didn't select the overwrite option.");
            }

            xml = new LgXMLResourceImpl(URI.createFileURI(fileLocation));
            xml.getContents().add(vdDef);

            // Perform the save ...
            xml.save();
        }

        catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        } finally {
            if (xml != null) {
                xml.unload();
            }
        }
    }
    //
    // Currently unsupported operations ...
    //

    // Incremental write (from EMFWrite interface) ...
    public void streamedWriteOnAssociation(CodingScheme codingScheme, Relations relationsContainer,
            Association association) throws Exception {
        throw new UnsupportedOperationException();
    }

    public void streamedWriteOnAssociations(CodingScheme codingScheme, Relations relationsContainer,
            Iterator associations) throws Exception {
        throw new UnsupportedOperationException();
    }

    public void streamedWriteOnAssociationInstances(CodingScheme codingScheme, Relations relationsContainer,
            Association associationContainer, Iterator associationInstances) throws Exception {
        throw new UnsupportedOperationException();
    }

    public void streamedWriteOnConcepts(CodingScheme codingScheme, Entities conceptsContainer, Iterator concepts)
            throws Exception {
        throw new UnsupportedOperationException();
    }

    public void closeStreamedWrite() {
        throw new UnsupportedOperationException();
    }
}