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

import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.persistence.model.CodingSchemeSupportedAttrib;
import org.LexGrid.persistence.model.CodingSchemeSupportedAttribId;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lexgrid.loader.logging.LoggingBean;

/**
 * The Class AbstractSupportedAttributeTemplate.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSupportedAttributeTemplate extends LoggingBean implements SupportedAttributeTemplate{
	
	/** The lex evs dao. */
	private LexEvsDao lexEvsDao;
	
	private int idValueMaxSize = 200;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedAssociation(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedAssociation(String codingSchemeName, String localId, String uri,
			String content) {	
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION, 
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedAssociationQualifier(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedAssociationQualifier(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATIONQUALIFIER, 
				localId, uri, content, null, null));		
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedCodingScheme(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public void addSupportedCodingScheme(String codingSchemeName, String localId, String uri,
			String content, boolean isImported) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME, 
				localId, uri, content, Boolean.toString(isImported), null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedContainerName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedContainerName(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTAINERNAME, 
				localId, uri, content, null, null));		
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedContext(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedContext(String codingSchemeName, String localId, String uri, String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT, 
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedDataType(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedDataType(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_DATATYPE, 
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedDegreeOfFidelity(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedDegreeOfFidelity(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_DEGREEOFFIDELITY, 
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedEntityType(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedEntityType(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_ENTITYTYPE, 
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedHierarchy(java.lang.String, java.lang.String, java.lang.String, java.util.List, boolean, java.lang.String)
	 */
	public void addSupportedHierarchy(String codingSchemeName, String localId, String uri,
			List<String> associationNames,
			boolean isForwardNavigable, String rootCode) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_HIERARCHY, 
				localId, uri, listToCSV(associationNames), rootCode, Boolean.toString(isForwardNavigable)));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedLanguage(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedLanguage(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_LANGUAGE, 
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedNamespace(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedNamespace(String codingSchemeName, String localId, String uri,
			String content, String equivalentCodingScheme) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_NAMESPACE, 
				localId, uri, content, equivalentCodingScheme, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedProperty(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedProperty(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY, 
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedPropertyQualifier(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedPropertyQualifier(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIER, 
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedPropertyType(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedPropertyType(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYTYPE, 
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedRepresentationalForm(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedRepresentationalForm(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM,
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedSortOrder(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedSortOrder(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_SORTORDER, 
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedSource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedSource(String codingSchemeName, String localId, String uri, String content,
			String assemblyRule) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE, 
				localId, uri, content, assemblyRule, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedSourceRole(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedSourceRole(String codingSchemeName, String localId, String uri,
			String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_ROLEGROUP, 
				localId, uri, content, null, null));	
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedStatus(String codingSchemeName, String localId, String uri, String content) {
		insert(buildCodingSchemeSupportedAttrib(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_STATUS, 
				localId, uri, content, null, null));	
	}
	
	 /**
 	 * Builds the coding scheme supported attrib.
 	 * 
 	 * @param codingSchemeName the coding scheme name
 	 * @param supportedAttribute the supported attribute
 	 * @param localId the local id
 	 * @param uri the uri
 	 * @param content the content
 	 * @param val1 the val1
 	 * @param val2 the val2
 	 * 
 	 * @return the coding scheme supported attrib
 	 */
 	private CodingSchemeSupportedAttrib buildCodingSchemeSupportedAttrib(
			 String codingSchemeName, String supportedAttribute, String localId, String uri,
			 String content, String val1, String val2) {
		CodingSchemeSupportedAttrib attrib = new CodingSchemeSupportedAttrib();
		CodingSchemeSupportedAttribId attribId = new CodingSchemeSupportedAttribId();
		attribId.setCodingSchemeName(codingSchemeName);
		attribId.setId(localId);
		attribId.setIdValue(truncateIfNecessary(
				changeNullToBlankString(content), idValueMaxSize));
		attribId.setSupportedAttributeTag(supportedAttribute);
		attribId.setVal1(changeNullToBlankString(val1));
		attrib.setVal2(val2);
		attrib.setUri(uri);
		attrib.setId(attribId);
		return attrib;
	}
	
	/**
	 * Insert.
	 * 
	 * @param attrib the attrib
	 */
	protected abstract void insert(CodingSchemeSupportedAttrib attrib);

	/**
	 * Change null to blank string.
	 * 
	 * @param value the value
	 * 
	 * @return the string
	 */
	private String changeNullToBlankString(String value){
		if(value == null){
			return " ";
		} else {
			return value;
		}
	}
	
	private String truncateIfNecessary(String value, int maxLength){
		if(value.length() > maxLength){
			getLogger().info("Truncating: " + value);
			return value.substring(0, maxLength);
		} else {
			return value;
		}
	}

	/**
	 * List to csv.
	 * 
	 * @param list the list
	 * 
	 * @return the string
	 */
	private String listToCSV(List<String> list){
		String returnString = null;
		for(String value : list){
			if(returnString == null){
				returnString = value;
			} else {
				returnString = returnString + "," + value;
			}
		}
		return returnString;
	}

	/**
	 * Gets the lex evs dao.
	 * 
	 * @return the lex evs dao
	 */
	public LexEvsDao getLexEvsDao() {
		return lexEvsDao;
	}

	/**
	 * Sets the lex evs dao.
	 * 
	 * @param lexEvsDao the new lex evs dao
	 */
	public void setLexEvsDao(LexEvsDao lexEvsDao) {
		this.lexEvsDao = lexEvsDao;
	}

	public int getIdValueMaxSize() {
		return idValueMaxSize;
	}

	public void setIdValueMaxSize(int idValueMaxSize) {
		this.idValueMaxSize = idValueMaxSize;
	}	
}
