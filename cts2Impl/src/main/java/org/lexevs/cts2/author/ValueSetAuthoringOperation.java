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
package org.lexevs.cts2.author;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.cts2.core.update.RevisionInfo;

/**
 * LexEVS CTS 2 Value Set Authoring Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface ValueSetAuthoringOperation {
	
	/**
	 * Creates a new value set definition and loads into repository.
	 * 
	 * @param valueSetURI URI of value set definition 
	 * @param valueSetName Name of value set definition
	 * @param defaultCodeSystem local name of default code system
	 * @param conceptDomainId (Optional)local name of concept domain
	 * @param sourceList (Optional)list of source
	 * @param usageContextList (Optional)list of usage context
	 * @param properties (Optional)collection of properties
	 * @param ruleSetList list of definition entries (rule sets)
	 * @param versionable versionable entries (status, isActive, effective date etc)
	 * @param revision revision information
	 * @return value set definition URI
	 * @throws LBException
	 */
	public URI createValueSet(URI valueSetURI, String valueSetName, String defaultCodeSystem, String conceptDomainId, 
			List<Source> sourceList, List<String> usageContextList, Properties properties, List<DefinitionEntry> ruleSetList,
			Versionable versionable, RevisionInfo revision) throws LBException;
	
	/**
	 * Creates new value set definition and loads into repository.
	 * 
	 * @param valueSetDefininition Value Set Definition object to be loaded into repository
	 * @param revision revision information
	 * @return value set definition URI
	 * @throws LBException
	 */
	public URI createValueSet(ValueSetDefinition valueSetDefininition, RevisionInfo revision) throws LBException;
	
	/**
	 * Updates value set definition meta data.
	 * 
	 * @param valueSetURI URI of value set definition that will be modified
	 * @param valueSetName modified value set definition name
	 * @param defaultCodeSystem modified default code system name
	 * @param conceptDomainId modified concept domain id
	 * @param sourceList modified complete list of source
	 * @param usageContext modified complete list of context
	 * @param revision revision information
	 * @return True if update was successful
	 * @throws LBException
	 */
	public boolean updateValueSetMetaData(URI valueSetURI, String valueSetName, String defaultCodeSystem, 
			String conceptDomainId, List<Source> sourceList, List<String> usageContext, RevisionInfo revision) throws LBException;

	/**
	 * Updates versionable attributes of value set definition. 
	 * Versionable attributes are : Status, isActive, Effective Date, Expiration Date, and owner. 
	 * @param valueSetURI URI of value set definition that will be modified
	 * @param changedVersionable modified versionable object
	 * @param revision revision information
	 * @return True if update was successful
	 * @throws LBException
	 */
	public boolean updateValueSetVersionable(URI valueSetURI, Versionable changedVersionable, 
			RevisionInfo revision) throws LBException;	
	
	/**
	 * Add new Property to existing value set definition.
	 * 
	 * @param valueSetURI URI of value set definition this new property will be added
	 * @param newProperty new Property object
	 * @param revision revision information
	 * @return True if update was successful
	 * @throws LBException
	 */
	public boolean addValueSetProperty(URI valueSetURI, Property newProperty, RevisionInfo revision) throws LBException;
	
	/**
	 * Updates existing property of a value set definition.
	 * 
	 * @param valueSetURI URI of value set definition
	 * @param changedProperty modified property object
	 * @param revision revision information
	 * @return True if update was successful
	 * @throws LBException
	 */
	public boolean updateValueSetProperty(URI valueSetURI, Property changedProperty, RevisionInfo revision) throws LBException;
	
	/**
	 * Add new definition entry (rule set) to a value set definition.
	 * 
	 * @param valueSetURI URI of value set definition 
	 * @param newDefinitionEntry Definition Entry object to be added
	 * @param revision revision information
	 * @return True if update was successful
	 * @throws LBException
	 */
	public boolean addDefinitionEntry(URI valueSetURI, DefinitionEntry newDefinitionEntry, RevisionInfo revision) throws LBException;
	
	/**
	 * Update existing definition entry (rule set) of a value set definition.
	 * 
	 * @param valueSetURI URI of value set definition	   
	 * @param changedDefinitionEntry modified definition entry object
	 * @param revision revision information
	 * @return True if update was successful
	 * @throws LBException
	 */
	public boolean updateDefinitionEntry(URI valueSetURI, DefinitionEntry changedDefinitionEntry, RevisionInfo revision) throws LBException;
	
	/**
	 * Updates value set definition status.
	 * 
	 * @param valueSetURI URI of value set definition
	 * @param status modified status
	 * @param revision revision information
	 * @return True if update was successful
	 * @throws LBException
	 */
	public boolean updateValueSetStatus(URI valueSetURI, String status, RevisionInfo revision) throws LBException;
	
	/**
	 * Removes value set definition from the system. This operation is permanent removal of value set definition.
	 * 
	 * @param valueSetURI URI of value set definition to be removed
	 * @param revision revision information
	 * @return True if removal was successful
	 * @throws LBException
	 */
	public boolean removeValueSet(URI valueSetURI, RevisionInfo revision) throws LBException;
	
	/**
	 * Removes definition entry (rule set) of a value set definition.
	 * 
	 * @param valueSetURI URI of a value set definition
	 * @param ruleOrder rule set order id to be removed
	 * @param revision revision information
	 * @return True if removal was successful
	 * @throws LBException
	 */
	public boolean removeDefinitionEntry(URI valueSetURI, Long ruleOrder, RevisionInfo revision) throws LBException;
	
	/**
	 * Removes property of a value set definition.
	 * 
	 * @param valueSetURI URI of a value set definition
	 * @param propertyId id of a property that needs to be removed
	 * @param revision revision information
	 * @return True if removal was successful
	 * @throws LBException
	 */
	public boolean removeValueSetProperty(URI valueSetURI, String propertyId, RevisionInfo revision) throws LBException;
}