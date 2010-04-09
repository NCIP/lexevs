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
package org.lexgrid.valuesets.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.naming.NamingFactory;
import org.LexGrid.emf.naming.SupportedAssociation;
import org.LexGrid.emf.naming.SupportedAssociationQualifier;
import org.LexGrid.emf.naming.SupportedCodingScheme;
import org.LexGrid.emf.naming.SupportedContainerName;
import org.LexGrid.emf.naming.SupportedContext;
import org.LexGrid.emf.naming.SupportedDataType;
import org.LexGrid.emf.naming.SupportedDegreeOfFidelity;
import org.LexGrid.emf.naming.SupportedEntityType;
import org.LexGrid.emf.naming.SupportedHierarchy;
import org.LexGrid.emf.naming.SupportedLanguage;
import org.LexGrid.emf.naming.SupportedNamespace;
import org.LexGrid.emf.naming.SupportedProperty;
import org.LexGrid.emf.naming.SupportedPropertyLink;
import org.LexGrid.emf.naming.SupportedPropertyQualifier;
import org.LexGrid.emf.naming.SupportedPropertyQualifierType;
import org.LexGrid.emf.naming.SupportedPropertyType;
import org.LexGrid.emf.naming.SupportedRepresentationalForm;
import org.LexGrid.emf.naming.SupportedSortOrder;
import org.LexGrid.emf.naming.SupportedSource;
import org.LexGrid.emf.naming.SupportedSourceRole;
import org.LexGrid.emf.naming.SupportedStatus;
import org.LexGrid.emf.naming.URIMap;
import org.LexGrid.managedobj.FindException;
import org.LexGrid.managedobj.InsertException;
import org.LexGrid.managedobj.ManagedObjIF;
import org.LexGrid.managedobj.ObjectAlreadyExistsException;
import org.LexGrid.managedobj.ObjectNotFoundException;
import org.LexGrid.managedobj.ServiceInitException;
import org.LexGrid.managedobj.UpdateException;
import org.LexGrid.managedobj.jdbc.JDBCBaseService;
import org.LexGrid.persistence.sql.SingletonVariables;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.lexgrid.valuesets.helper.VSDConstants;

/**
 * <pre>
 *        Title:        MappingServices.java
 *        Description:  Class that handles Supported Attributes to the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VSDMappingServices extends VSDBaseService {
	
	private VSDEntryTypeServices entryTypeServices_;
	
	protected VSDMappingServices(JDBCBaseService anchorService)
			throws ServiceInitException {
		super(anchorService);
	}
	
	public void insert(ManagedObjIF obj, int foreignEntryId) throws LBException, Exception {
		if (obj != null)
		{
			PreparedStatement insertIntoMappings = null;
			try {
				insertIntoMappings = getKeyedInsertStatement(VSDConstants.TBL_MAPPING);
	
				Mappings mappings = (Mappings) obj;
				
				Iterator supportedAttribute = mappings.getSupportedAssociation().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedAssociationQualifier().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATIONQUALIFIER,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedCodingScheme().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedContainer().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTAINERNAME,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedContext().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedDataType().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_DATATYPE,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedDegreeOfFidelity().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_DEGREEOFFIDELITY,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedEntityType().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_ENTITYTYPE,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedHierarchy().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_HIERARCHY,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedLanguage().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_LANGUAGE,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedNamespace().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_NAMESPACE,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedProperty().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedPropertyLink().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYLINK,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedPropertyQualifier().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIER,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedPropertyQualifierType().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIERTYPE,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedPropertyType().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYTYPE,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedRepresentationalForm().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedSortOrder().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_SORTORDER,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedSource().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedSourceRole().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_ROLEGROUP,
						insertIntoMappings);
	
				supportedAttribute = mappings.getSupportedStatus().iterator();
				insertSupportedAttribute(mappings, supportedAttribute, foreignEntryId,
						SQLTableConstants.TBLCOLVAL_SUPPTAG_STATUS,
						insertIntoMappings);
	
			} catch (SQLException e) {
				if ((e.getMessage().indexOf("Duplicate entry") != -1) || (e.getMessage().indexOf("unique constraint") != -1)
						|| (e.getMessage().indexOf("SQLCODE: -803") != -1)){
					// print error message and continue
					String msg = "Mapping already exists : " + e.getMessage();
					System.out.println(msg);
					if (SingletonVariables.instance(getConnectionDescriptor().toString()).messages != null) {
						SingletonVariables.instance(getConnectionDescriptor().toString()).messages.message(msg);
					}
				}
				else
					throw new ObjectAlreadyExistsException(e);
			} catch (Exception e) {
				throw new InsertException(e);
			} finally {
				try {
					if (insertIntoMappings != null)
						insertIntoMappings.close();
				} catch (Exception e) {
					// do nothing
				}
			}
		}
	}

	private void insertSupportedAttribute(Mappings mappings, Iterator supportedAttribute,
			int foreignEntryId, String attribute,
			PreparedStatement insertIntoMappings)
			throws LBException, Exception {
		while (supportedAttribute.hasNext()) {
			int entryId = getNextEntryId();
			insertEntryType(mappings, entryId);
			try {
				URIMap urimap = (URIMap) supportedAttribute.next();
				int k = 1;
				insertIntoMappings.setInt(k++, entryId);
				insertIntoMappings.setInt(k++, foreignEntryId);
				insertIntoMappings.setString(k++, attribute);
				insertIntoMappings.setString(k++, urimap.getLocalId());
				insertIntoMappings.setString(k++, setBlankStringsNull(urimap.getUri()));
				insertIntoMappings.setString(k++, StringUtils.isNotBlank(urimap.getValue()) ? urimap.getValue() : " ");
				insertIntoMappings.setString(k++, " ");
				insertIntoMappings.setString(k++, null);
				if (urimap instanceof SupportedSource) {
					SupportedSource currSupportedVal = (SupportedSource) urimap;
					insertIntoMappings.setString(7, StringUtils.isNotBlank(currSupportedVal
									.getAssemblyRule()) ? currSupportedVal
									.getAssemblyRule() : " ");
				} else if (urimap instanceof SupportedHierarchy) {
					SupportedHierarchy currSupportedVal = (SupportedHierarchy) urimap;
					// Set the local ID
					insertIntoMappings.setString(4, StringUtils.isNotBlank(currSupportedVal
									.getLocalId()) ? currSupportedVal
									.getLocalId() : " ");
					// Set the associations, converting list to comma-delimited
					// string
					String associations = null;
					List associationList = currSupportedVal.getAssociationNames();
					if (associationList != null) {
						for (int i = 0; i < associationList.size(); i++) {
							String assoc = (String) associationList.get(i);
							associations = i == 0 ? assoc
									: (associations += ("," + assoc));
						}
					}
					insertIntoMappings.setString(6,
							StringUtils.isNotBlank(associations) ? associations
									: " ");
					insertIntoMappings.setString(7,
							StringUtils.isNotBlank(currSupportedVal
									.getRootCode()) ? currSupportedVal
									.getRootCode() : " ");
					insertIntoMappings.setString(8,
							setBlankStringsNull(String.valueOf(currSupportedVal
									.isIsForwardNavigable())));
				} else if (urimap instanceof SupportedCodingScheme) {
					SupportedCodingScheme currSupportedVal = (SupportedCodingScheme) urimap;
					insertIntoMappings.setString(7,
							String.valueOf(currSupportedVal.isIsImported()));
				} else if (urimap instanceof SupportedNamespace) {
					SupportedNamespace currSupportedVal = (SupportedNamespace) urimap;
					String nameSpaceCS = currSupportedVal
							.getEquivalentCodingScheme();
					if (StringUtils.isEmpty(nameSpaceCS))
						nameSpaceCS = urimap.getValue();
					insertIntoMappings.setString(7,
							StringUtils.isEmpty(nameSpaceCS) ? " "
									: nameSpaceCS);
				}

				insertIntoMappings.executeUpdate();
			}
			// This is to allow us to optionally continue even if there is a
			// duplicate, or some other sql insert error.
			catch (SQLException e) {
				if (e.getMessage().indexOf("Duplicate entry") != -1){
					// print error message and continue
					if (SingletonVariables.instance(getConnectionDescriptor().toString()).messages != null) {
						SingletonVariables.instance(getConnectionDescriptor().toString()).messages
						.message("There was a problem while executing an insert for supported" + attribute
								+ ". Error message=" + e.getMessage());
					}
				}
				else if (SingletonVariables.instance(getConnectionDescriptor()
						.toString()).failOnAllErrors) {
					throw e;
				} else {
					if (SingletonVariables.instance(getConnectionDescriptor()
							.toString()).messages != null) {
						SingletonVariables.instance(getConnectionDescriptor()
								.toString()).messages
								.message("There was a problem while executing an insert for supported"
										+ attribute
										+ ". Error message="
										+ e.getMessage());
					}
				}
			}
		}
	}
	
	public void resolveAll() throws FindException, SQLException{
		findByPrimaryKey(-1);
	}
	public void findByPrimaryKey(ManagedObjIF obj, int foreignEntryId) throws SQLException, FindException
	{
		Mappings mappings = NamingFactory.eINSTANCE.createMappings();
		mappings.setContainer(getEContainer(), getEContainmentFeature(), true);
		
		PreparedStatement selectFromMappings = null;

		try
		{
			selectFromMappings = checkOutPreparedStatement(
					" SELECT * " +
					" FROM " + VSDConstants.TBL_MAPPING + 
					(foreignEntryId > 0 ? 
							" WHERE " + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ?"
							: ""));
			
			if (foreignEntryId > 0)
				selectFromMappings.setInt(1, foreignEntryId);

			ResultSet results = selectFromMappings.executeQuery();

			while (results.next())
			{
				int mapEntryId = results.getInt(SQLTableConstants.TBLCOL_ENTRYID);
				String attributeName = results.getString(SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG);
				String localId = results.getString(SQLTableConstants.TBLCOL_LOCALID);
				String uri = results.getString(SQLTableConstants.TBLCOL_URI);
				String value = results.getString(SQLTableConstants.TBLCOL_DEPENDENTVALUE);
				String val1 = results.getString(SQLTableConstants.TBLCOL_VAL1);
				String val2 = results.getString(SQLTableConstants.TBLCOL_VAL2);

				if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION))
				{
					SupportedAssociation temp = NamingFactory.eINSTANCE.createSupportedAssociation();
					temp.setUri(uri);
					temp.setLocalId(localId);
					temp.setValue(value);
					mappings.getSupportedAssociation().add(temp);
				}
				else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATIONQUALIFIER))
				{
					SupportedAssociationQualifier temp = NamingFactory.eINSTANCE.createSupportedAssociationQualifier();
					temp.setUri(uri);
					temp.setLocalId(localId);
					temp.setValue(value);
					mappings.getSupportedAssociationQualifier().add(temp);
				}
				else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME))
				{
					SupportedCodingScheme temp = NamingFactory.eINSTANCE.createSupportedCodingScheme();
					temp.setUri(uri);
					temp.setLocalId(localId);
					temp.setValue(value);
					if (val1 != null)
						temp.setIsImported(Boolean.parseBoolean(val1));
					mappings.getSupportedCodingScheme().add(temp);
				}
				else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONCEPTSTATUS))
				{
					SupportedStatus temp = NamingFactory.eINSTANCE.createSupportedStatus();
					temp.setUri(uri);
					temp.setLocalId(localId);
					temp.setValue(value);
					mappings.getSupportedStatus().add(temp);
				}
				else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTAINERNAME))
				{
					SupportedContainerName temp = NamingFactory.eINSTANCE.createSupportedContainerName();
					temp.setUri(uri);
					temp.setLocalId(localId);
					temp.setValue(value);
					mappings.getSupportedContainer().add(temp);
				}
				else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT))
				{
					SupportedContext temp = NamingFactory.eINSTANCE.createSupportedContext();
					temp.setUri(uri);
					temp.setLocalId(localId);
					temp.setValue(value);
					mappings.getSupportedContext().add(temp);
				}
				else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_DATATYPE))
				{
					SupportedDataType temp = NamingFactory.eINSTANCE.createSupportedDataType();
					temp.setUri(uri);
					temp.setLocalId(localId);
					temp.setValue(value);
					mappings.getSupportedDataType().add(temp);
				}
				else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_DEGREEOFFIDELITY))
				{
					SupportedDegreeOfFidelity temp = NamingFactory.eINSTANCE.createSupportedDegreeOfFidelity();
					temp.setUri(uri);
					temp.setLocalId(localId);
					temp.setValue(value);
					mappings.getSupportedDegreeOfFidelity().add(temp);
				}
				else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_ENTITYTYPE))
				{
					SupportedEntityType temp = NamingFactory.eINSTANCE.createSupportedEntityType();
					temp.setUri(uri);
					temp.setLocalId(localId);
					temp.setValue(value);
					mappings.getSupportedEntityType().add(temp);
				}
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_FORMAT))
                {
                    SupportedDataType temp = NamingFactory.eINSTANCE.createSupportedDataType();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    mappings.getSupportedDataType().add(temp);
                }
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_HIERARCHY))
                {
                    SupportedHierarchy temp = NamingFactory.eINSTANCE.createSupportedHierarchy();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    temp.setIsForwardNavigable(Boolean.parseBoolean(val2));
                    temp.setRootCode(val1);
                    String tempString = value;
                    String[] stringarray = tempString.split(",");
                    if (temp.getAssociationNames() == null)
                        temp.setAssociationNames(new ArrayList());
                    for(int i = 0; i < stringarray.length; i++){
                        temp.getAssociationNames().add(stringarray[i]);
                    }

                    mappings.getSupportedHierarchy().add(temp);
                }
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_LANGUAGE))
                {
                    SupportedLanguage temp = NamingFactory.eINSTANCE.createSupportedLanguage();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    mappings.getSupportedLanguage().add(temp);
                }
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_NAMESPACE))
                {
                    SupportedNamespace temp = NamingFactory.eINSTANCE.createSupportedNamespace();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    temp.setEquivalentCodingScheme(val1);
                    mappings.getSupportedNamespace().add(temp);
                }
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY))
                {
                    SupportedProperty temp = NamingFactory.eINSTANCE.createSupportedProperty();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    mappings.getSupportedProperty().add(temp);
                }
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYLINK))
                {
                    SupportedPropertyLink temp = NamingFactory.eINSTANCE.createSupportedPropertyLink();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    mappings.getSupportedPropertyLink().add(temp);
                }
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIER))
                {
                    SupportedPropertyQualifier temp = NamingFactory.eINSTANCE.createSupportedPropertyQualifier();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    mappings.getSupportedPropertyQualifier().add(temp);
                }
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIERTYPE))
                {
                    SupportedPropertyQualifierType temp = NamingFactory.eINSTANCE.createSupportedPropertyQualifierType();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    mappings.getSupportedPropertyQualifierType().add(temp);
                }
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYTYPE))
                {
                    SupportedPropertyType temp = NamingFactory.eINSTANCE.createSupportedPropertyType();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    mappings.getSupportedPropertyType().add(temp);
                }
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM))
                {
                    SupportedRepresentationalForm temp = NamingFactory.eINSTANCE.createSupportedRepresentationalForm();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    mappings.getSupportedRepresentationalForm().add(temp);
                }
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_ROLEGROUP))
                {
                    SupportedSourceRole temp = NamingFactory.eINSTANCE.createSupportedSourceRole();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    mappings.getSupportedSourceRole().add(temp);
                } 
                else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_SORTORDER))
                {
                    SupportedSortOrder temp = NamingFactory.eINSTANCE.createSupportedSortOrder();
                    temp.setUri(uri);
                    temp.setLocalId(localId);
                    temp.setValue(value);
                    mappings.getSupportedSortOrder().add(temp);
                } 
				else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE))
				{
					SupportedSource temp = NamingFactory.eINSTANCE.createSupportedSource();
					temp.setUri(uri);
					temp.setLocalId(localId);
					temp.setValue(value);
					temp.setAssemblyRule(val1);
//					if (val1 == null)
//						temp.setAssemblyRule(" ");
					mappings.getSupportedSource().add(temp);
				} 
				else if (attributeName.equals(SQLTableConstants.TBLCOLVAL_SUPPTAG_STATUS))
				{
					SupportedStatus temp = NamingFactory.eINSTANCE.createSupportedStatus();
					temp.setUri(uri);
					temp.setLocalId(localId);
					temp.setValue(value);
					mappings.getSupportedStatus().add(temp);
				}
				else
				{
					System.out.println("INVALID DATA '" + attributeName
							+ "' IN CODING-SCHEME-SUPPORTED-ATTRIBUTES-TABLE");
				}
			}
			
			addMappingsToObject(obj, mappings);

			results.close();
		}
		finally
		{
			checkInPreparedStatement(selectFromMappings);
		}
	}


	protected Class getInstanceClass()
	{
		return VSDMappingServices.class;
	}
	
	public void update(ManagedObjIF obj) throws UpdateException, ObjectNotFoundException
	{
		// TODO: Implement update
		throw new java.lang.UnsupportedOperationException("Method update not yet implemented.");
	}
	
	public void remove(int foreignEntryId, String foreignEntryType) throws SQLException, FindException
	{
		PreparedStatement selectFromMappings = null;
        ArrayList<Integer> entryIds = new ArrayList<Integer>();
        
        try
        {
            selectFromMappings = checkOutPreparedStatement(modifySql(
            		" SELECT P1. " + SQLTableConstants.TBLCOL_ENTRYID +
            		" FROM " + VSDConstants.TBL_MAPPING + " {AS} P1 " +
            		" , " + VSDConstants.TBL_ENTRY_TYPE + " {AS} P2 " +
            		" WHERE P1." + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? " +
            		" AND P1." + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = P2." + SQLTableConstants.TBLCOL_REFERENCEENTRYID +
            		" AND P2." + SQLTableConstants.TBLCOL_ENTRYTYPE + " = ?"));
            
            selectFromMappings.setInt(1, foreignEntryId);
            selectFromMappings.setString(2, foreignEntryType);
            
            ResultSet results = selectFromMappings.executeQuery();
            while (results.next())
            {
                entryIds.add(results.getInt(SQLTableConstants.TBLCOL_ENTRYID));
            }
            results.close();
        }
        finally
        {
            checkInPreparedStatement(selectFromMappings);
        }
            
        for (int entryId : entryIds)
        {
        	// remove property
            removeMapping(entryId);
        }
        
        if (entryIds.size() > 0)
        {
        	// remove from entryType table
          	entryTypeServices_.remove(entryIds, SQLTableConstants.ENTRY_STATE_TYPE_MAPPING);
        }
        
	}
	
	private void removeMapping(int entryId) throws FindException, SQLException{
    	PreparedStatement deleteMapping = null;
        
        try
        {
            deleteMapping = checkOutPreparedStatement(modifySql(
            		" DELETE FROM " + VSDConstants.TBL_MAPPING + 
            		" WHERE " + SQLTableConstants.TBLCOL_ENTRYID + " = ? "));
            
            deleteMapping.setInt(1, entryId);
            
            deleteMapping.executeUpdate();            
        }
       finally
        {
            checkInPreparedStatement(deleteMapping);
        }
    }
	
	protected void initNestedServices() throws ServiceInitException
    {
        entryTypeServices_ = new VSDEntryTypeServices(this);
    }
	
	protected void insertEntryType(Mappings mappings, int entryId) throws InsertException, ObjectAlreadyExistsException
	{
    	entryTypeServices_.insert(mappings, entryId);
	}
    
    protected String resolveEntryType(int entryId) throws FindException
	{
    	return entryTypeServices_.resolveEntryTypeFor(entryId);
	}
}