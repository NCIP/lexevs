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

import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;

/**
 * Interface for classes supporting conversion to a non-LexGrid source format
 * from an EMF-based representation of the LexGrid model.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
public interface EMFWrite {
    /**
     * Release any storage allocated for the given coding scheme from the target
     * environment.
     * 
     * @param codingScheme
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public void clearCodingScheme(String codingScheme) throws Exception;

    /**
     * Store the given coding scheme in the target environment.
     * 
     * @param codingScheme
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public void writeCodingScheme(CodingScheme codingScheme) throws Exception;

    /**
     * Provides an association to be stored that is contained by the given
     * coding scheme and relations container.
     * <p>
     * The Association is not expected to be populated. The contained source and
     * target items (AssociationInstances and AssociationTargets) are requested
     * via the streamedReadOnAssociationInstances() method.
     * 
     * @param association
     *            An Association object.
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public void streamedWriteOnAssociation(CodingScheme codingScheme, Relations relationsContainer,
            Association association) throws Exception;

    /**
     * Provides an iterator over associations to be stored that are contained by
     * the given coding scheme and relations container.
     * <p>
     * Each item in the Iterator is expected to be an EMF Association
     * representing a branch of available relationships (e.g. 'hasSubtype'). The
     * Association is not expected to be populated. The contained source and
     * target items (AssociationInstances and AssociationTargets) are requested
     * via the streamedReadOnAssociationInstances() method.
     * 
     * @param associations
     *            An iterator over all matching Association objects.
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public void streamedWriteOnAssociations(CodingScheme codingScheme, Relations relationsContainer,
            Iterator associations) throws Exception;

    /**
     * Provides an iterator over association instances (source and related
     * targets) contained by the given coding scheme, relations container, and
     * association container.
     * <p>
     * Each item in the Iterator is expected to be an EMF AssociationInstance.
     * The AssociationInstance is expected to be fully populated (source and all
     * target items).
     * 
     * @return An iterator over all matching AssociationInstance objects.
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public void streamedWriteOnAssociationInstances(CodingScheme codingScheme, Relations relationsContainer,
            Association associationContainer, Iterator associationInstances) throws Exception;

    /**
     * Provides an iterator over coded entries to be stored that are contained
     * by the given coding scheme and concepts container.
     * <p>
     * Each item in the SIterator is expected to be an EMF CodedEntry
     * representing a concept and embedded Properties and PropertyLinks.
     * 
     * @return An iterator over all matching Association objects.
     * @throws UnsupportedOperationException
     *             If not supported.
     * @throws Exception
     *             If a problem occurs.
     */
    public void streamedWriteOnConcepts(CodingScheme codingScheme, Entities conceptsContainer, Iterator concepts)
            throws Exception;

    /**
     * Invoked to allow implementers a chance to clean up resources on normal
     * process completion or if the operation ended prematurely due to error.
     */
    public void closeStreamedWrite();
}