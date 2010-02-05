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
package org.lexgrid.loader.dao.template;

import java.util.List;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;

/**
 * The Interface SupportedAttributeTemplate.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SupportedAttributeTemplate {
	
	/**
	 * Adds the supported association.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedAssociation(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported association qualifier.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedAssociationQualifier(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported coding scheme.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 * @param isImported the is imported
	 */
	public void addSupportedCodingScheme(String codingSchemeName, String localId, String uri, String content, boolean isImported);
	
	/**
	 * Adds the supported container name.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedContainerName(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported context.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedContext(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported data type.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedDataType(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported degree of fidelity.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedDegreeOfFidelity(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported entity type.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedEntityType(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported hierarchy.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param associationNames the association names
	 * @param isForwardNavigable the is forward navigable
	 * @param rootCode the root code
	 */
	public void addSupportedHierarchy(String codingSchemeName, String localId, String uri, List<String> associationNames, boolean isForwardNavigable, String rootCode);
	
	/**
	 * Adds the supported language.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedLanguage(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported namespace.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 * @param equivalentCodingScheme the equivalent coding scheme
	 */
	public void addSupportedNamespace(String codingSchemeName, String localId, String uri, String content, String equivalentCodingScheme);
	
	/**
	 * Adds the supported property.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedProperty(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported property qualifier.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedPropertyQualifier(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported property type.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedPropertyType(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported representational form.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedRepresentationalForm(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported sort order.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedSortOrder(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported source.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 * @param assemblyRule the assembly rule
	 */
	public void addSupportedSource(String codingSchemeName, String localId, String uri, String content, String assemblyRule);
	
	/**
	 * Adds the supported source role.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedSourceRole(String codingSchemeName, String localId, String uri, String content);
	
	/**
	 * Adds the supported status.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param localId the local id
	 * @param uri the uri
	 * @param content the content
	 */
	public void addSupportedStatus(String codingSchemeName, String localId, String uri, String content);
}
