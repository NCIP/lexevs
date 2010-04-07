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
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.castor.xml.UnmarshalListenerAdapter;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.mayo.edu.lgModel.LexGridBase;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */
public class LexGridUnmarshalListener extends UnmarshalListenerAdapter {
	
	int nentities = 0;
	int nassociations = 0;
	boolean isCodingSchemeLoaded = false;
	boolean isConceptLoaded = false;
	boolean isAssociationLoaded = false;
	boolean isPropertiesPresent = false;
	Entity currentEntity = new Entity();
	AssociationPredicate currentPredicate = new AssociationPredicate();
	
	XMLDaoServiceAdaptor serviceAdaptor = null;

	public LexGridUnmarshalListener() {
		super();
		serviceAdaptor = new XMLDaoServiceAdaptor();
	}

	int getNentities() {
		return nentities;
	}
	
	int getNassociations() {
		return nassociations;
	}
	
    public boolean isPropertiesPresent() {
		return isPropertiesPresent;
	}

	public void setPropertiesPresent(boolean isPropertiesPresent) {
		this.isPropertiesPresent = isPropertiesPresent;
	}

	private boolean isPredicateLoaded(AssociationPredicate e){
		if( currentPredicate.equals(e))
			return true;
		else
		{
			currentPredicate = e;
			return false;
		}
	}
	@Override
	/**
	 * First event: An instance of type target was just created, pending addition to parent
	 */
	public void initialized(Object target, Object parent) {
		if(target != null && target instanceof LexGridBase)
			((LexGridBase)target).setParent(parent);
		else
			System.out.println(target.getClass().getName() + " is not an instance of LexGridBase");
	}
	
	@Override
	/**
	 * Second event: all of the attributes have been processed for entity target, destined for parent
	 */
	public void attributesProcessed(Object target, Object parent) {

	}
	
	@Override
	/**
	 * Third event: the entity target is completely unmarshalled
	 */
	public void unmarshalled(Object target, Object parent) {
		System.out.println("Unmarshalled target: " + (target != null? target.getClass().getSimpleName(): "target is null"));
		System.out.println("parent of Unmarshalled target: " + (parent != null? parent.getClass().getSimpleName(): "parent is null"));
	}
	
	
	@Override
	/**
	 * An instance of type child was just added to parent as field name fieldname
	 */
	public void fieldAdded(String fieldName, Object parent, Object child) {
		
		System.out.println("fieldName:" + fieldName);
		System.out.println("parent: " + parent.getClass().getSimpleName());
		System.out.println("child: " + child.getClass().getSimpleName());
		if(!isPropertiesPresent && parent instanceof CodingScheme && child instanceof Mappings){
		 	CodingScheme scheme = (CodingScheme)parent;
		    try {
				serviceAdaptor.storeCodingScheme(scheme);
			} catch (CodingSchemeAlreadyLoadedException e) {
				e.printStackTrace();
			}
			isCodingSchemeLoaded = true;}
		if(!isCodingSchemeLoaded && parent instanceof CodingScheme && child instanceof Properties){
		 	CodingScheme scheme = (CodingScheme)parent;
		    try {
				serviceAdaptor.storeCodingScheme(scheme);
			} catch (CodingSchemeAlreadyLoadedException e) {
				e.printStackTrace();
			}
			isCodingSchemeLoaded = true;}
		if(child instanceof Entity && parent instanceof Entities){
			Entity e = (Entity)child;
			Entities entities = (Entities)parent;
			CodingScheme c = (CodingScheme)entities.getParent();
			Property[] properties = e.getAllProperties();
			serviceAdaptor.storeEntity(e,c);
			nentities++;
		}
		else if(child instanceof Entities && parent instanceof CodingScheme)
		{	((CodingScheme)parent).setEntities(null);
		}
		else if(child instanceof AssociationSource && parent instanceof AssociationPredicate) {
			AssociationPredicate a = (AssociationPredicate)parent;
			Relations relations = (Relations)a.getParent();
			CodingScheme cs = (CodingScheme)relations.getParent();
			serviceAdaptor.storeRelation(cs.getCodingSchemeURI(),cs.getRepresentsVersion(), relations);
			if(!isPredicateLoaded(a)){
			serviceAdaptor.storeAssociationPredicate(cs.getCodingSchemeURI(), cs.getRepresentsVersion(),relations.getContainerName(), (AssociationPredicate)parent);
			}
			else {
				serviceAdaptor.storeAssociation(cs.getCodingSchemeURI(), cs.getRepresentsVersion(),relations.getContainerName(), a.getAssociationName(), (AssociationSource)child);
			}
			a.removeSource((AssociationSource)child);
			nassociations++;
		}
	}


}
