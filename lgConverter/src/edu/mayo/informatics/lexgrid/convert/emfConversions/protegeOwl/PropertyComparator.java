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
package edu.mayo.informatics.lexgrid.convert.emfConversions.protegeOwl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.LexGrid.emf.commonTypes.CommontypesPackage;
import org.LexGrid.emf.commonTypes.Property;
import org.LexGrid.emf.concepts.ConceptsPackage;
import org.LexGrid.emf.concepts.Definition;
import org.LexGrid.emf.concepts.Presentation;
import org.eclipse.emf.ecore.EClass;
/**
 *  Comparator used to sort concept properties by type and priority ...
 * 
 * @author Pradip Kanjamala (kanjamala.pradip@mayo.edu)
 */

public class PropertyComparator implements Comparator<Property>{
    
    List<EClass> prioritizedEClass;
    List<String> prioritizedPresentationNames;
    List<String> prioritizedDefinitionNames;
    PreferenceManager prefManager;
    
    public PropertyComparator(PreferenceManager prefManager) {
        this.prefManager= prefManager;
        prioritizedPresentationNames = prefManager.getPrioritized_presentation_names();
        prioritizedDefinitionNames = prefManager.getPrioritized_definition_names();
        ConceptsPackage emfPackage = ConceptsPackage.eINSTANCE;
        prioritizedEClass = Arrays.asList(new EClass[] { emfPackage.getPresentation(), emfPackage.getDefinition(),
                emfPackage.getComment(), CommontypesPackage.eINSTANCE.getProperty() });
    }

    public int compare(Property o1, Property o2) {
        if (o1 instanceof Property && o2 instanceof Property) {
            Property p1 = (Property) o1;
            Property p2 = (Property) o2;
            int i = prioritizedEClass.indexOf(p1.eClass()) - prioritizedEClass.indexOf(p2.eClass());
            if (i != 0)
                return i;
            if (p1 instanceof Presentation && p2 instanceof Presentation) {
                i = prioritizedPresentationNames.indexOf(p1.getPropertyName())
                        - prioritizedPresentationNames.indexOf(p2.getPropertyName());
            } else if (p1 instanceof Definition && p2 instanceof Definition) {
                i = prioritizedDefinitionNames.indexOf(p1.getPropertyName())
                        - prioritizedDefinitionNames.indexOf(p2.getPropertyName());
            }
            return (i != 0) ? i : p1.getPropertyId().compareTo(p2.getPropertyId());
        }
        // different types - just put one ahead of the other - otherwise,
        // when the treeMap uses this comparator, it throws everything
        // away...
        return 1;
    }


}
