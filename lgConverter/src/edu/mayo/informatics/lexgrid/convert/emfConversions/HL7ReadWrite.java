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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Iterator;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;
import org.apache.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFWrite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.hl7.HL72EMFMain;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.HL7SQL;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * This is currently designed to work with a single coding scheme in the RIM
 * database
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto:stancl.craig@mayo.edu">Craig Stancl</A>
 * 
 */
public class HL7ReadWrite extends EMFReadImpl implements EMFRead, EMFWrite {
    private static Logger log = Logger.getLogger("convert.HL7ReadWrite");
    URI accessDBLocation;
    LgMessageDirectorIF messages;
    boolean failOnAllErrors;
    boolean overwrite = false;
    String dbPathName;
    String accessConnectionString;
    URNVersionPair currentCodingScheme;

    public HL7ReadWrite(String dbPathName, URNVersionPair currentCodingScheme, LoaderPreferences loaderPrefs,
            boolean failOnAllErrors, boolean overwrite, LgMessageDirectorIF messages) throws URISyntaxException {
        this.messages = messages;
        this.overwrite = overwrite;
        this.failOnAllErrors = failOnAllErrors;
        this.dbPathName = dbPathName;
        this.accessConnectionString = HL7SQL.MSACCESS_SERVER + dbPathName;
        this.currentCodingScheme = currentCodingScheme;
        this.loaderPreferences = loaderPrefs;
    }

    public URNVersionPair[] getUrnVersionPairs() throws Exception {
        return new URNVersionPair[0];
    }

    public CodingScheme readCodingScheme(String registeredName) throws Exception {

        // try {
        //			
        // HL72EMFMain hl7_main = new HL72EMFMain();
        // CodingScheme scheme =
        // hl7_main.map("http://www.hl7.org/Library/data-model/RIM", dbPathName,
        // this.failOnAllErrors, messages);
        // return scheme;
        // } catch (Exception e) {
        // log.error("Failed...", e);
        // messages.fatalAndThrowException(
        // "Failed - " + e.toString() + " see log file.", e);
        //	
        // return null;
        // }
        throw new UnsupportedOperationException();

    }

    public CodingScheme readCodingScheme() throws Exception {
        try {
            // Get the source scheme (complete representation) from original
            // context
            if (dbPathName.contains("%20")) {
                dbPathName = URLDecoder.decode(dbPathName, "UTF-8");
            }
            HL72EMFMain hl7_main = new HL72EMFMain();
            hl7_main.setLoaderPrefs(loaderPreferences); // CRS
            CodingScheme scheme = hl7_main.map(null, dbPathName, this.failOnAllErrors, messages);
            // Apply manifest changes
            ManifestUtil manifestUtil = new ManifestUtil(null, messages);
            manifestUtil.applyManifest(codingSchemeManifest, scheme);
            return scheme;
        } catch (Exception e) {
            log.error("Failed...", e);
            messages.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
            return null;
        }
    }

    //
    // Currently unsupported operations ...
    //

    // General
    public void clearCodingScheme(String codingScheme) throws Exception {
        throw new UnsupportedOperationException();
    }

    public void writeCodingScheme(CodingScheme codingScheme) throws Exception {
        throw new UnsupportedOperationException();
    }

    // Full read (from EMFRead interface) ...
    public CodingScheme[] readAllCodingSchemes() throws Exception {
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

    public void setStreamingOn(boolean streamOn) {
        // TODO Auto-generated method stub

    }

    public boolean getStreamingOn() {
        // TODO Auto-generated method stub
        return false;
    }

}