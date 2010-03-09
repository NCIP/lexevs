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
package edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces;

import java.util.Iterator;

import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Interface for classes supporting conversion from a non-LexGrid source format
 * to an EMF-based representation of the LexGrid model.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
public interface EMFRead {
    /**
     * Return the coding scheme with the given name.
     * 
     * @param registeredName
     *            Coding scheme registered name (typically a URN).
     * @return The requested coding scheme.
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public CodingScheme readCodingScheme(String registeredName) throws Exception;

    /**
     * Return the default or primary coding scheme.
     * 
     * @return The default or primary coding scheme available.
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public CodingScheme readCodingScheme() throws Exception;

    /**
     * Return all known or currently available coding schemes.
     * 
     * @return The array of available coding schemes.
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public CodingScheme[] readAllCodingSchemes() throws Exception;

    /**
     * Return all known or currently available urn/version pairs identifying the
     * available coding schemes.
     * 
     * @return The array of identifying urn/version pairs.
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public URNVersionPair[] getUrnVersionPairs() throws Exception;

    /**
     * Indicates if the given coding scheme (returned through readCodingScheme
     * or readCodingSchemes methods) was provided as partial information. If
     * true, the coding scheme as originally returned needs to provide only the
     * CodingScheme, Concepts, and Relations portions of the model. Requests to
     * fill in content of the Concepts container and Relations container(s) is
     * decoupled and carried out through subsequent calls to the
     * streamAssociations() and streamConcepts() methods.
     * 
     * @return true if content for the coding scheme is to be streamed; false
     *         otherwise.
     */
    public boolean supportsStreamedRead(CodingScheme codingScheme);

    /**
     * Invoked to request an iterator over associations contained by the given
     * coding scheme and relations container.
     * <p>
     * Each item in the returned Iterator is expected to be an EMF Association
     * representing a branch of available relationships (e.g. 'hasSubtype'). The
     * Association is not expected to be populated. The contained source and
     * target items (AssociationInstances and AssociationTargets) are requested
     * via the streamedReadOnAssociationInstances() method.
     * 
     * @return An iterator over all matching Association objects.
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public Iterator streamedReadOnAssociations(CodingScheme codingScheme, Relations relationsContainer)
            throws Exception;

    /**
     * Invoked to request an iterator over association instances (source and
     * related targets) contained by the given coding scheme, relations
     * container, and association container.
     * <p>
     * Each item in the returned Iterator is expected to be an EMF
     * AssociationInstance. The AssociationInstance is expected to be fully
     * populated (source and all target items).
     * 
     * @return An iterator over all matching AssociationInstance objects.
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public Iterator streamedReadOnAssociationInstances(CodingScheme codingScheme, Relations relationsContainer,
            Association associationContainer) throws Exception;

    /**
     * Invoked to request an iterator over concepts contained by the given
     * coding scheme and concept container.
     * <p>
     * Each item in the returned Iterator is expected to be an EMF CodedEntry
     * representing an available concept. The CodedEntry is expected to be fully
     * populated (including all properties and property links).
     * 
     * @return An iterator over all matching CodedEntry objects.
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public Iterator streamedReadOnConcepts(CodingScheme codingScheme, Entities conceptsContainer) throws Exception;

    /**
     * Invoked to allow implementers a chance to clean up resources on normal
     * process completion or if the operation ended prematurely due to error.
     */
    public void closeStreamedRead();

    /**
     * Method sets a flag to indicate whether any ontology data (concepts,
     * associations, etc) will be streamed directly into database or not.
     */
    public void setStreamingOn(boolean streamOn);

    /**
     * Method returns a flag to indicate whether any ontology data (concepts,
     * associations, etc) will be streamed directly into database or not.
     */
    public boolean getStreamingOn();

    /**
     * Returns the manifest object which is used to adjust the content of the
     * codingScheme.
     */
    public CodingSchemeManifest getCodingSchemeManifest();
}