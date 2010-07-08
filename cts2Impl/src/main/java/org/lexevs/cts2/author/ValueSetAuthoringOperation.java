/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and Research
 * (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo
 * logo are trademarks and service marks of MFMER.
 * 
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
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

public interface ValueSetAuthoringOperation {
	
	/**
	 * Create a new value set definition.
	 * 
	 * @param valueSetURI URI of value set definition 
	 * @param valueSetName Name of value set definition
	 * @param defaultCodeSystem local name of default code system
	 * @param conceptDomainId local name of concept domain
	 * @param sourceList list of source
	 * @param usageContextList list of usage context
	 * @param properties collection of properties
	 * @param ruleSetList list of definition entries (rule sets)
	 * @param versionable versionable entries (status, isActive, effective date etc)
	 * @param revision revision information
	 * @return value set definition URI
	 * @throws LBException
	 */
	public URI createValueSet(URI valueSetURI, String valueSetName, String defaultCodeSystem, String conceptDomainId, 
			List<Source> sourceList, List<String> usageContextList, Properties properties, List<DefinitionEntry> ruleSetList,
			Versionable versionable, RevisionInfo revision) throws LBException;
	
	public URI createValueSet(ValueSetDefinition valueSetDefininition, RevisionInfo revision) throws LBException;
	
	public boolean updateValueSetMetaData(URI valueSetURI, String valueSetName, String defaultCodeSystem, 
			String conceptDomainId, List<Source> sourceList, List<String> usageContext, RevisionInfo revision) throws LBException;

	public boolean updateValueSetVersionable(URI valueSetURI, Versionable changedVersionable, 
			RevisionInfo revision) throws LBException;	
	
	public boolean addValueSetProperty(URI valueSetURI, Property newProperty, RevisionInfo revision) throws LBException;
	
	public boolean updateValueSetProperty(URI valueSetURI, Property changedProperty, RevisionInfo revision) throws LBException;
	
	public boolean addDefinitionEntry(URI valueSetURI, DefinitionEntry newDefinitionEntry, RevisionInfo revision) throws LBException;
	
	public boolean updateDefinitionEntry(URI valueSetURI, DefinitionEntry changedDefinitionEntry, RevisionInfo revision) throws LBException;
	
	public boolean updateValueSetStatus(URI valueSetURI, String status, RevisionInfo revision) throws LBException;
	
	public boolean removeValueSet(URI valueSetURI, RevisionInfo revision) throws LBException;
	
	public boolean removeDefinitionEntry(URI valueSetURI, Long ruleOrder, RevisionInfo revision) throws LBException;
	
	public boolean removeValueSetProperty(URI valueSetURI, String propertyId, RevisionInfo revision) throws LBException;
}
