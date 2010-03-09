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
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;
import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.apache.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFWrite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.protegeOwl.ProtegeOwl2EMFConstants;
import edu.mayo.informatics.lexgrid.convert.emfConversions.protegeOwl.ProtegeOwl2EMFMain;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Reads OWL -> EMF Writes EMF -> OWL
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala </A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          11:42:24 +0000 (Mon, 30 Jan 2006) $
 */
public class OwlReadWrite extends EMFReadImpl implements EMFRead, EMFWrite {
    private static Logger log = Logger.getLogger("convert.OwlReadWrite");

    URI owlOntologyURI;

    LgMessageDirectorIF messages;
    boolean failOnAllErrors;
    int memorySafe = 0;
    boolean overwrite = false;
    private LoaderPreferences loadPrefs = null;

    private SQLReadWrite emfOut_ = null;
    private boolean streamingOn_ = false;

    public OwlReadWrite(URI owlOntologyURI, boolean failOnAllErrors, int memorySafe, boolean overwrite,
            LgMessageDirectorIF messages) {
        this(owlOntologyURI, null, null, failOnAllErrors, memorySafe, overwrite, messages);
    }

    public OwlReadWrite(URI owlOntologyURI, CodingSchemeManifest codingSchemeManifest,
            LoaderPreferences loaderPreferences, boolean failOnAllErrors, int memorySafe, boolean overwrite,
            LgMessageDirectorIF messages) {
        this.owlOntologyURI = owlOntologyURI;
        this.codingSchemeManifest = codingSchemeManifest;
        this.failOnAllErrors = failOnAllErrors;
        this.memorySafe = memorySafe;
        this.overwrite = overwrite;
        this.messages = messages;
        this.loadPrefs = loaderPreferences;
    }

    public CodingScheme readCodingScheme() throws Exception {
        try {
            // Get the source scheme (complete representation) from original
            // context

            if (memorySafe != ProtegeOwl2EMFConstants.MEMOPT_ALL_IN_MEMORY) {
                setStreamingOn(true);
            }

            ProtegeOwl2EMFMain owl_main = new ProtegeOwl2EMFMain(owlOntologyURI, codingSchemeManifest, loadPrefs,
                    failOnAllErrors, memorySafe, messages, emfOut_);
            CodingScheme scheme = owl_main.map();

            // Apply manifest changes
            if (codingSchemeManifest != null) {
                ManifestUtil manifestUtil = new ManifestUtil(null, messages);

                if (memorySafe == ProtegeOwl2EMFConstants.MEMOPT_ALL_IN_MEMORY) {

                    manifestUtil.applyManifest(codingSchemeManifest, scheme);
                } else {
                    URNVersionPair csURNVersion = new URNVersionPair(scheme.getCodingSchemeURI(), scheme
                            .getRepresentsVersion());
                    JDBCConnectionDescriptor sqlConfig = new JDBCConnectionDescriptor();

                    sqlConfig.setDbUid(emfOut_.getDBUsername());
                    sqlConfig.setDbPwd(emfOut_.getDBPassword());
                    sqlConfig.setDbUrl(emfOut_.getDBServer());
                    sqlConfig.setDbDriver(emfOut_.getDBDriver());
                    sqlConfig.setUseUTF8(true);
                    sqlConfig.setAutoRetryFailedConnections(true);

                    manifestUtil.applyManifest(codingSchemeManifest, sqlConfig, emfOut_.getDBTablePrefix(),
                            csURNVersion);
                }
            }
            return scheme;
        } catch (Exception e) {
            log.error("Failed...", e);
            messages.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
            return null;
        }
    }

    public void clearCodingScheme(String codingScheme) throws Exception {
        try {
            File temp = new File(owlOntologyURI + System.getProperty("file.separator") + codingScheme + ".owl");
            if (temp.exists()) {
                boolean success = temp.delete();
                if (!success) {
                    throw new Exception("Problem deleting file");
                }
            }
        } catch (Exception e) {
            log.error("Failed...", e);
            messages.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        }
    }

    public URNVersionPair[] getUrnVersionPairs() throws Exception {
        return new URNVersionPair[0];

    }

    //
    // Currently unsupported operations ...
    //
    public void writeCodingScheme(CodingScheme codingScheme) throws Exception {
        throw new UnsupportedOperationException();
    }

    public CodingScheme readCodingScheme(String registeredName) throws Exception {
        // Coding scheme for this converter is not selected by name.
        // By throwing this exception, the conversion launcher will
        // fall through to the readCodingScheme() method.
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

    public void setEmfOut(SQLReadWrite emfOut) {
        emfOut_ = emfOut;

    }

    public void setStreamingOn(boolean streamOn) {
        streamingOn_ = streamOn;
    }

    public boolean getStreamingOn() {
        return streamingOn_;
    }
}