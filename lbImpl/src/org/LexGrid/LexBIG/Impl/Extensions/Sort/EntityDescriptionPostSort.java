/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Impl.Extensions.Sort;

import java.util.Comparator;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Sort;

/**
 * Entity description sort which implements the Sort interface.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a> 
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class EntityDescriptionPostSort extends EntityDescriptionSort {
  
    private static final long serialVersionUID = -8356411011570608432L;
    final static String description_ = "Sort the results of a List Post-Resolve, according to the entityDescription";
    final static String name_ = "entityDescriptionPost";
    final static String provider_ = "Mayo Clinic";
    final static String version_ = "1.0";
    
    public EntityDescriptionPostSort() throws LBParameterException, LBException {
        super();
    }
    
    protected SortDescription buildSortDescription() {
        SortDescription sd = new SortDescription();
        sd.setExtensionBaseClass(Sort.class.getName());
        sd.setExtensionClass(EntityDescriptionPostSort.class.getName());
        sd.setDescription(description_);
        sd.setName(name_);
        sd.setVersion(version_);
        sd.setRestrictToContext(new SortContext[] { SortContext.SETLISTPOSTRESOLVE });

        return sd;
    }
    
    @Override
    public void registerComparators(Map<Class, Comparator> classToComparatorsMap) {
        classToComparatorsMap.put(ResolvedConceptReference.class, new EntityDescriptionPostSort.ResolvedConceptReferenceEntityDescritpionSort()); 
    }  
}