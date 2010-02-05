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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.emf.base.xml.LgXMLResourceImpl;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.codingSchemes.impl.CodingSchemeImpl;
import org.LexGrid.emf.codingSchemes.impl.CodingSchemesImpl;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;
import org.LexGrid.messaging.LgMessageDirectorIF;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;

import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFRead;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Reads XML -> EMF
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 8814 $ checked in on $Date: 2008-06-13
 *          16:09:43 +0000 (Fri, 13 Jun 2008) $
 */
public class XMLRead extends EMFReadImpl implements EMFRead {
    private String xmlFileLocation;
    private InputStream inputStream_;
    private static Logger log = Logger.getLogger("convert.XMLReadWrite");
    private LgMessageDirectorIF messages_;
    private URNVersionPair[] codingSchemes_ = new URNVersionPair[0];

    public XMLRead(String xmlFileLocation, CodingSchemeManifest codingSchemeManifest, LgMessageDirectorIF messages,
            boolean failOnAllErrors) {
        // TODO failOnAllErrors should be used.
        this.xmlFileLocation = xmlFileLocation;
        this.messages_ = messages;
        super.codingSchemeManifest = codingSchemeManifest;
    }

    public XMLRead(InputStream inputStream, LgMessageDirectorIF messages, boolean failOnAllErrors) {
        // TODO failOnAllErrors should be used.
        this.inputStream_ = inputStream;
        ;
        this.messages_ = messages;
    }

    public CodingScheme[] readAllCodingSchemes() throws Exception {
        LgXMLResourceImpl xml = null;
        try {
            // Get the source scheme (complete representation) from original
            // context

            if (xmlFileLocation != null) {
                xml = new LgXMLResourceImpl(URI.createFileURI(xmlFileLocation));
                xml.load();
            } else if (inputStream_ != null) {
                xml = new LgXMLResourceImpl();
                xml.doLoad(inputStream_, xml.getDefaultLoadOptions());
            } else {
                throw new Exception("User error");
            }

            if (codingSchemeManifest != null) {
                new ManifestUtil(null, messages_).applyManifest(codingSchemeManifest, (CodingSchemeImpl) xml
                        .getContents().get(0));
            }

            if (xml.getContents().get(0) instanceof CodingSchemeImpl) {
                return new CodingScheme[] { (CodingSchemeImpl) xml.getContents().get(0) };
            } else if (xml.getContents().get(0) instanceof CodingSchemesImpl) {
                CodingSchemesImpl temp = (CodingSchemesImpl) xml.getContents().get(0);

                ArrayList allCodingSchemes = new ArrayList();

                Iterator codingSchemes = temp.getCodingScheme().iterator();
                while (codingSchemes.hasNext()) {
                    CodingSchemeImpl csti = (CodingSchemeImpl) codingSchemes.next();
                    URNVersionPair[] tmpCodingSchemes = new URNVersionPair[codingSchemes_.length + 1];
                    for (int i = 0; i < codingSchemes_.length; i++) {
                        tmpCodingSchemes[i] = codingSchemes_[i];
                    }
                    tmpCodingSchemes[codingSchemes_.length] = new URNVersionPair(csti.getCodingSchemeName(), null);
                    codingSchemes_ = tmpCodingSchemes;
                    allCodingSchemes.add((CodingSchemeImpl) csti);
                }

                return (CodingScheme[]) allCodingSchemes.toArray(new CodingScheme[allCodingSchemes.size()]);

            } else {
                return new CodingScheme[] {};
            }
        }

        catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
            return null;
        }
    }

    public URNVersionPair[] getUrnVersionPairs() throws Exception {
        return codingSchemes_;
    }

    //
    // Currently unsupported operations ...
    //

    // Full read (from EMFRead interface) ...
    public CodingScheme readCodingScheme() throws Exception {
        throw new UnsupportedOperationException();
    }

    public CodingScheme readCodingScheme(String registeredName) throws Exception {
        // Coding scheme for this converter is not selected by name.
        // By throwing this exception, the conversion launcher will
        // fall through to the readCodingScheme() method.
        throw new UnsupportedOperationException();
    }

    // Incremental read (from EMFRead interface) ...
    public Iterator streamedReadOnAssociations(CodingScheme codingScheme, Relations relationsContainer)
            throws Exception {
        throw new UnsupportedOperationException();
    }

    public Iterator streamedReadOnAssociationInstances(CodingScheme codingScheme, Relations relationsContainer,
            Association associationContainer) throws Exception {
        throw new UnsupportedOperationException();
    }

    public Iterator streamedReadOnConcepts(CodingScheme codingScheme, Entities conceptsContainer) throws Exception {
        throw new UnsupportedOperationException();
    }

    public boolean supportsStreamedRead(CodingScheme codingScheme) {
        return false;
    }

    public void closeStreamedRead() {
    }

    public void setStreamingOn(boolean streamOn) {
        // TODO Auto-generated method stub

    }

    public boolean getStreamingOn() {
        // TODO Auto-generated method stub
        return false;
    }
}