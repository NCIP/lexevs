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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;
import org.apache.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.fma.FMA2EMFMain;
import edu.mayo.informatics.lexgrid.convert.emfConversions.radlex.RadLex2EMFMain;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;
import edu.stanford.smi.protege.model.Project;

/**
 * Reads ProtegeFrames -> EMF
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: 3711 $ checked in on $Date: 2006-11-01
 *          21:19:33 +0000 (Wed, 01 Nov 2006) $
 */
public class ProtegeFramesRead extends EMFReadImpl implements EMFRead {
    private static Logger log = Logger.getLogger("convert.FMARead");

    URI protegeFileLocation_;
    LgMessageDirectorIF messages_;
    boolean failOnAllErrors_;
    boolean overwrite_ = false;

    public ProtegeFramesRead(URI protegeFileLocation_, LoaderPreferences loaderPrefs, boolean failOnAllErrors,
            boolean overwrite, LgMessageDirectorIF messages) {
        this.protegeFileLocation_ = protegeFileLocation_;
        this.messages_ = messages;
        this.overwrite_ = overwrite;
        this.failOnAllErrors_ = failOnAllErrors;
        this.loaderPreferences = loaderPrefs;
    }

    public CodingScheme readCodingScheme() throws Exception {
        try {
            // Get the source scheme (complete representation) from original
            // context
            Collection errors = new ArrayList();
            CodingScheme scheme = null;

            Project proj = Project.loadProjectFromURI(protegeFileLocation_, errors);
            String project_name = proj.getName();

            ManifestUtil manifestUtil = new ManifestUtil(null, messages_);
            CodingSchemeManifest manifest = this.getCodingSchemeManifest();

            if ("FMA".equalsIgnoreCase(project_name)) {
                FMA2EMFMain mainTxfm = new FMA2EMFMain();
                scheme = null;//TODO: We are moving away from the EMF read/write techniques.
            } else if ((project_name != null) && (project_name.toLowerCase().startsWith("radlex"))) {
                RadLex2EMFMain mainTxfm = new RadLex2EMFMain();
                scheme = null;//TODO: We are moving away from the EMF read/write techniques.
            } else {
                throw new Exception("Currently the Protege Frames loader only supports loading of FMA and RadLex.");
            }

            if (manifest != null) {
                manifestUtil.applyManifest(manifest, scheme);
            }
            return scheme;
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
            return null;
        }
    }

    public URNVersionPair[] getUrnVersionPairs() throws Exception {
        Collection errors = new ArrayList();
        Project proj = Project.loadProjectFromURI(protegeFileLocation_, errors);
        return new URNVersionPair[] { new URNVersionPair(proj.getName(), null) };
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

    public void setStreamingOn(boolean streamOn) {
        // TODO Auto-generated method stub

    }

    public boolean getStreamingOn() {
        // TODO Auto-generated method stub
        return false;
    }
}