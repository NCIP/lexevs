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
import java.net.URI;
import java.util.Iterator;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;
import org.apache.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFWrite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.obo1_2.EMF2OBO;
import edu.mayo.informatics.lexgrid.convert.emfConversions.obo1_2.OBO2EMFMain;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;
import edu.mayo.informatics.resourcereader.core.IF.ResourceManifest;

/**
 * Reads OBO -> EMF
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala </A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          11:42:24 +0000 (Mon, 30 Jan 2006) $
 */
public class OboReadWrite extends EMFReadImpl implements EMFRead, EMFWrite {
    private static Logger log = Logger.getLogger("convert.OboReadWrite");

    URI inputFileLocation_;
    boolean failOnAllErrors_;
    boolean overwrite_ = false;
    CachingMessageDirectorIF messages_;
    private ResourceManifest oboManifest_ = null;

    public OboReadWrite(URI oboFileLocation, boolean failOnAllErrors, boolean overwrite, CachingMessageDirectorIF messages) {
        this.inputFileLocation_ = oboFileLocation;
        this.messages_ = messages;
        this.overwrite_ = overwrite;
        this.failOnAllErrors_ = failOnAllErrors;
    }

    public OboReadWrite(URI oboFileLocation, LoaderPreferences loaderPrefs, boolean failOnAllErrors, boolean overwrite,
            CachingMessageDirectorIF messages) {
        this.inputFileLocation_ = oboFileLocation;
        this.messages_ = messages;
        this.overwrite_ = overwrite;
        this.failOnAllErrors_ = failOnAllErrors;
        this.loaderPreferences = loaderPrefs;
    }

    public OboReadWrite(URI oboFileLocation, LoaderPreferences loaderPrefs, ResourceManifest manifest,
            boolean failOnAllErrors, boolean overwrite, CachingMessageDirectorIF messages) {
        this.inputFileLocation_ = oboFileLocation;
        this.messages_ = messages;
        this.overwrite_ = overwrite;
        this.failOnAllErrors_ = failOnAllErrors;
        this.oboManifest_ = manifest;
        this.loaderPreferences = loaderPrefs;
    }

    public CodingScheme readCodingScheme() throws Exception {
        try {
            // Get the source scheme (complete representation) from original
            // context

            // TODO this loader should pay attention to the failOnAllErrors
            // flag.
            OBO2EMFMain mainTxfm = new OBO2EMFMain();
            CodingScheme scheme = mainTxfm.map(inputFileLocation_, oboManifest_, messages_);
            // Apply manifest changes
            ManifestUtil manifestUtil = new ManifestUtil(null, messages_);
            manifestUtil.applyManifest(codingSchemeManifest, scheme);

            return scheme;
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
            return null;
        }
    }

    public void writeCodingScheme(CodingScheme codingScheme) throws Exception {
        EMF2OBO emf2obo = null;
        try {

            File temp = new File(inputFileLocation_);
            File writeFile = new File(temp, codingScheme.getCodingSchemeName() + ".obo");

            messages_.info("Writing to the file '" + writeFile.getCanonicalPath() + "'");

            if (writeFile.exists() && !overwrite_) {
                messages_
                        .fatalAndThrowException("The output file already exists, and you didn't select the overwrite option.");
            }

            emf2obo = new EMF2OBO(codingScheme, messages_);

            // Perform the save ...
            emf2obo.save(writeFile);
        }

        catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        }

    }

    public void clearCodingScheme(String codingScheme) throws Exception {
        try {
            File temp = new File(inputFileLocation_ + System.getProperty("file.separator") + codingScheme + ".obo");
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

    public URNVersionPair[] getUrnVersionPairs() throws Exception {
        return new URNVersionPair[0];
    }

    //
    // Currently unsupported operations ...
    //

    // Full read (from EMFRead interface) ...
    public CodingScheme[] readAllCodingSchemes() throws Exception {
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