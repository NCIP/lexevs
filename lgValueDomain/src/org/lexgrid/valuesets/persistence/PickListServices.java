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

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.emf.base.LgModelObj;
import org.LexGrid.emf.base.LgStagedObj;
import org.LexGrid.emf.commonTypes.CommontypesFactory;
import org.LexGrid.emf.commonTypes.Property;
import org.LexGrid.emf.commonTypes.Source;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.valueDomains.PickListDefinition;
import org.LexGrid.emf.valueDomains.PickListEntryNode;
import org.LexGrid.emf.valueDomains.ValuedomainsFactory;
import org.LexGrid.emf.valueDomains.impl.PickListDefinitionImpl;
import org.LexGrid.managedobj.FindException;
import org.LexGrid.managedobj.HomeServiceBroker;
import org.LexGrid.managedobj.InsertException;
import org.LexGrid.managedobj.ManagedObjException;
import org.LexGrid.managedobj.ManagedObjIF;
import org.LexGrid.managedobj.ObjectAlreadyExistsException;
import org.LexGrid.managedobj.ObjectNotFoundException;
import org.LexGrid.managedobj.RemoveException;
import org.LexGrid.managedobj.ResolveException;
import org.LexGrid.managedobj.ServiceInitException;
import org.LexGrid.managedobj.UpdateException;
import org.LexGrid.managedobj.jdbc.JDBCBaseService;
import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.LexGrid.persistence.sql.SingletonVariables;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EStructuralFeature;

import edu.mayo.informatics.lexgrid.convert.utility.MessageRedirector;

/**
 * <pre>
 *        Title:        PickListServices.java
 *        Description:  Class that handles Pick Lists to the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class PickListServices extends VSDBaseService {

	private PLEntryServices plEntryServices_;
	private VSDPropertyServices propertyServices_;
	private VSDEntryTypeServices entryTypeServices_;
	private VSDEntryStateService entryStateService_;
	private VSDMappingServices mappingServices_;
	private SystemReleaseServices systemReleaseServices_;
	
	public PickListServices(JDBCBaseService anchorService)
			throws ServiceInitException {
		super(anchorService);
	}

	public PickListServices(HomeServiceBroker broker, JDBCConnectionDescriptor desc,
			boolean b, String tablePrefix, boolean failOnAllErrors,
			MessageRedirector messageRedirector, boolean createCSTables)
			throws ServiceInitException {
		super(broker, desc, b, tablePrefix, failOnAllErrors, messageRedirector,
				createCSTables);
		SingletonVariables.instance(getConnectionDescriptor().toString()).messages = messageRedirector;
		SingletonVariables.instance(getConnectionDescriptor().toString()).failOnAllErrors = failOnAllErrors;
	}

	public ManagedObjIF findByPrimaryKey(Object key) throws FindException {
		PickListDefinition pickList = ValuedomainsFactory.eINSTANCE.createPickListDefinition();
		pickList.setContainer(getEContainer(), getEContainmentFeature(), true);

		PreparedStatement selectFromPickList = null;

		try {
			String pickListId = (String) key;

			selectFromPickList = checkOutPreparedStatement(" SELECT * "
					+ " FROM " + VSDConstants.TBL_PICK_LIST + " WHERE "
					+ SQLTableConstants.TBLCOL_PICKLISTID + " = ?");

			selectFromPickList.setString(1, pickListId);

			ResultSet results = selectFromPickList.executeQuery();

			int entryId;
			
			String entryStateIdStr = null;
			if (results.next()) {
				entryId = results.getInt(SQLTableConstants.TBLCOL_ENTRYID);
				pickList.setPickListId(pickListId);
				pickList.setRepresentsValueDomain(results.getString(SQLTableConstants.TBLCOL_REPRESENTSVALUEDOMAIN));
				pickList.setCompleteDomain(getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_COMPLETEDOMAIN));
				pickList.setDefaultEntityCodeNamespace(results.getString(SQLTableConstants.TBLCOL_DEFAULTENTITYCODENAMESPACE));
				pickList.setDefaultLanguage(results.getString(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE));
				pickList.setDefaultSortOrder(results.getString(SQLTableConstants.TBLCOL_DEFAULTSORTORDER));
				pickList.setIsActive(getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISACTIVE));
				entryStateIdStr = results.getString(SQLTableConstants.TBLCOL_ENTRYSTATEID);
				pickList.setEntityDescription(results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
				
				results.close();
			} else {
				throw new FindException(
						"Could not find pickList for id : " + pickListId);
			}
			
			// get pickList attributes (Source and Context)
			getAttributes(pickList, entryId);
			
			plEntryServices_.setEContext(pickList, getEContainmentFeature());
			plEntryServices_.resolve(entryId);

			propertyServices_.setEContext(pickList, getEContainmentFeature());
			propertyServices_.findByPrimaryKey(pickList, entryId);
			
			mappingServices_.setEContext(pickList, null);
			mappingServices_.findByPrimaryKey(pickList, entryId);
			
			if (entryStateIdStr != null && !entryStateIdStr.equals("-1")) {
				resolveEntryState(pickList, Integer.valueOf(entryStateIdStr));
			}

			return pickList;
		} catch (FindException e) {
			throw e;
		} catch (Exception e) {
			throw new FindException("Problem", e);
		} finally {
			checkInPreparedStatement(selectFromPickList);
		}
	}
	
	public PickListDefinition[] findByValueDomainURI(Object key) throws FindException {
		List<PickListDefinition> pickLists = new ArrayList<PickListDefinition>();
		
		PreparedStatement selectFromPickList = null;

		try {
			URI valueDomainURI = (URI) key;

			selectFromPickList = checkOutPreparedStatement(" SELECT * "
					+ " FROM " + VSDConstants.TBL_PICK_LIST + " WHERE "
					+ SQLTableConstants.TBLCOL_REPRESENTSVALUEDOMAIN + " = ?");

			selectFromPickList.setString(1, valueDomainURI.toString());

			ResultSet results = selectFromPickList.executeQuery();

			int entryId;
			String pickListId;
			String entryStateIdStr = null;
			while (results.next()) {
				PickListDefinition pickList = ValuedomainsFactory.eINSTANCE.createPickListDefinition();
				pickList.setContainer(getEContainer(), getEContainmentFeature(), true);

				entryId = results.getInt(SQLTableConstants.TBLCOL_ENTRYID);
				pickListId = results.getString(SQLTableConstants.TBLCOL_PICKLISTID);
				pickList.setPickListId(pickListId);
				pickList.setRepresentsValueDomain(results.getString(SQLTableConstants.TBLCOL_REPRESENTSVALUEDOMAIN));
				pickList.setCompleteDomain(getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_COMPLETEDOMAIN));
				pickList.setDefaultEntityCodeNamespace(results.getString(SQLTableConstants.TBLCOL_DEFAULTENTITYCODENAMESPACE));
				pickList.setDefaultLanguage(results.getString(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE));
				pickList.setDefaultSortOrder(results.getString(SQLTableConstants.TBLCOL_DEFAULTSORTORDER));
				pickList.setIsActive(getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISACTIVE));
				entryStateIdStr = results.getString(SQLTableConstants.TBLCOL_ENTRYSTATEID);
				pickList.setEntityDescription(results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
				
				// get pickList attributes (Source and Context)
				getAttributes(pickList, entryId);
				
				plEntryServices_.setEContext(pickList, getEContainmentFeature());
				plEntryServices_.resolve(entryId);
	
				propertyServices_.setEContext(pickList, getEContainmentFeature());
				propertyServices_.findByPrimaryKey(pickList, entryId);
				
				mappingServices_.setEContext(pickList, getEContainmentFeature());
				mappingServices_.findByPrimaryKey(pickList, entryId);
				
				if (entryStateIdStr != null && !entryStateIdStr.equals("-1")) {
					resolveEntryState(pickList, Integer.valueOf(entryStateIdStr));
				}
	
				pickLists.add(pickList);
			} 
			
			results.close();
			
			return pickLists.toArray(new PickListDefinition[pickLists.size()]);
			
		} catch (FindException e) {
			throw e;
		} catch (Exception e) {
			throw new FindException("Problem", e);
		} finally {
			checkInPreparedStatement(selectFromPickList);
		}
	}
	
	public List<String> listPickListIds() throws FindException {
		PreparedStatement selectFromPickList = null;
		List<String> pickListIds = new ArrayList<String>();
		
		try {
			selectFromPickList = checkOutPreparedStatement(" SELECT " + SQLTableConstants.TBLCOL_PICKLISTID
					+ " FROM " + VSDConstants.TBL_PICK_LIST);

			ResultSet results = selectFromPickList.executeQuery();

			while (results.next()) {
				pickListIds.add(results.getString(SQLTableConstants.TBLCOL_PICKLISTID));
			} 
			
			results.close();
			
			return pickListIds;
		} catch (SQLException e) {
			throw new FindException("Problem", e);
		} finally {
			checkInPreparedStatement(selectFromPickList);
		}
	}
	
	public Map<String, String> getReferencedPLDefinitions(String entityCode,
			String entityCodeNameSpace, String propertyId,
			Boolean extractPickListName) throws FindException {

		StringBuffer query = new StringBuffer();
		PreparedStatement prepStmt = null;
		Map<String, String> refPLDef = new HashMap<String, String>();

		query.append("SELECT ");
		query.append("pl." + SQLTableConstants.TBLCOL_PICKLISTID);
		query.append(" FROM ");
		query.append(VSDConstants.TBL_PICK_LIST + " {AS} pl,");
		query.append(VSDConstants.TBL_PL_ENTRY + " {AS} en ");
		query.append(" WHERE ");
		query.append("en." + SQLTableConstants.TBLCOL_ENTITYCODE + " = ? AND ");
		query.append("en." + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + " = ? AND ");
		query.append("en." + SQLTableConstants.TBLCOL_PROPERTYID + " = ? AND ");
		query.append("en." + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = " + "pl." + SQLTableConstants.TBLCOL_ENTRYID);

		try {

			prepStmt = checkOutPreparedStatement(modifySql(query.toString()));

			prepStmt.setString(1, entityCode);
			prepStmt.setString(2, entityCodeNameSpace);
			prepStmt.setString(3, propertyId);

			ResultSet result = prepStmt.executeQuery();

			while (result.next()) {

				String pickListId = result
						.getString(SQLTableConstants.TBLCOL_PICKLISTID);
				String pickListName = null;

				if (pickListId != null) {
					
					if( extractPickListName )
						pickListName = getPickListName(pickListId);
					
					refPLDef.put(pickListId, pickListName);
				}
			}
		} catch (SQLException e) {
			throw new FindException("Exception : " + e.getMessage());
		} finally {
			checkInPreparedStatement(prepStmt);
		}

		return refPLDef;
	}

	public Map<String, String> getReferencedPLDefinitions(
			String valueSet, Boolean extractPickListName)
			throws FindException {

		StringBuffer query = new StringBuffer();
		PreparedStatement prepStmt = null;
		Map<String, String> refPLDef = new HashMap<String, String>();

		query.append("SELECT ");
		query.append(SQLTableConstants.TBLCOL_PICKLISTID);
		query.append(" FROM ");
		query.append(VSDConstants.TBL_PICK_LIST);
		query.append(" WHERE ");
		query.append(SQLTableConstants.TBLCOL_REPRESENTSVALUEDOMAIN + " = ?");

		try {

			prepStmt = checkOutPreparedStatement(query.toString());

			prepStmt.setString(1, valueSet);

			ResultSet result = prepStmt.executeQuery();

			while (result.next()) {

				String pickListId = result
						.getString(SQLTableConstants.TBLCOL_PICKLISTID);
				String pickListName = null;

				if (pickListId != null) {
					
					if( extractPickListName )
						pickListName = getPickListName(pickListId);
					
					refPLDef.put(pickListId, pickListName);
				}
			}
		} catch (SQLException e) {
			throw new FindException("Exception : " + e.getMessage());
		} finally {
			checkInPreparedStatement(prepStmt);
		}

		return refPLDef;
	}
	
	private String getPickListName(String pickListId) throws FindException {
		
		String pickListName = null;
		LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
		
		try {
			CodingSchemeRenderingList suppCodingSchemes = lbSvc
					.getSupportedCodingSchemes();

			CodingSchemeRendering[] csRendering = suppCodingSchemes
					.getCodingSchemeRendering();

			for (int i = 0; i < csRendering.length; i++) {
				CodingSchemeSummary csSummary = csRendering[i]
						.getCodingSchemeSummary();

				String csName = csSummary.getLocalName();
				String version = csSummary.getRepresentsVersion();

				CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
				versionOrTag.setVersion(version);

				CodedNodeSet codeSet = lbSvc.getCodingSchemeConcepts(csName,
						versionOrTag);

				codeSet.restrictToCodes(Constructors
						.createConceptReferenceList(pickListId));

				ResolvedConceptReferenceList conceptRef = codeSet
						.resolveToList(null, null, null, -1);

				if (conceptRef.getResolvedConceptReferenceCount() > 0) {
					Entity entity = conceptRef.getResolvedConceptReference(0)
							.getEntity();

					if (entity != null) {
						if (entity.getEntityDescription() != null)
							pickListName = entity.getEntityDescription()
									.getContent();

						if (pickListName == null) {
							Presentation[] allProps = entity.getPresentation();

							for (int j = 0; j < allProps.length; j++) {
								if (allProps[j].getIsPreferred()) {

									Text value = allProps[j].getValue();
									if (value != null)
										pickListName = value.getContent();
								}
							}
						}
					}
					break;
				}
			}
		} catch (LBInvocationException e) {
			throw new FindException("Exception : " + e.getMessage());
		} catch (LBException e) {
			throw new FindException("Exception : " + e.getMessage());
		}

		return pickListName;
	}
	
	private void getAttributes(PickListDefinition pickList, int entryId)
			throws SQLException {
		PreparedStatement selectVDSubProperties = null;

		try {
			selectVDSubProperties = checkOutPreparedStatement(modifySql(" SELECT P1.* "
					+ " FROM " + VSDConstants.TBL_PROPERTY + " {AS} P1 " 
					+ " , " + VSDConstants.TBL_ENTRY_TYPE + " {AS} P2 "
					+ " WHERE P1." + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? "
					+ " AND P1." + SQLTableConstants.TBLCOL_ENTRYID
					+ " = P2." + SQLTableConstants.TBLCOL_REFERENCEENTRYID
					+ " AND P2." + SQLTableConstants.TBLCOL_ENTRYTYPE + " = ?"));

			selectVDSubProperties.setInt(1, entryId);
			selectVDSubProperties.setString(2, VSDConstants.ENTRY_STATE_TYPE_ATTRIBUTE);

			ResultSet results = selectVDSubProperties.executeQuery();

			while (results.next()) {
				String attributeName = results.getString(SQLTableConstants.TBLCOL_PROPERTYNAME);
				if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SOURCE)) {
					Source src = CommontypesFactory.eINSTANCE.createSource();
					src.setValue(results.getString(SQLTableConstants.TBLCOL_PROPERTYVALUE));
					src.setRole(results.getString(SQLTableConstants.TBLCOL_VAL2));
					src.setSubRef(results.getString(SQLTableConstants.TBLCOL_VAL1));
					pickList.getSource().add(src);
				} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT)) {
					pickList.getDefaultPickContext().add(results.getString(SQLTableConstants.TBLCOL_PROPERTYVALUE));
				}
			}
			results.close();
		} finally {
			checkInPreparedStatement(selectVDSubProperties);
		}
	}

	public void insert(ManagedObjIF obj) throws InsertException, ObjectAlreadyExistsException {
		insert(obj, null, null);
	}
	
	public void insert(ManagedObjIF obj, URI systemReleaseURI, Mappings mappings) throws InsertException,
			ObjectAlreadyExistsException {
		PickListDefinition pickList = (PickListDefinition) (obj);
		boolean ignorePL = false;
		try {
			int entryId = getNextEntryId();
			insertEntryType(pickList, entryId);

			int entryStateId = getNextEntryId();

			PreparedStatement insertIntoPickList = null;
			try {
				insertIntoPickList = getKeyedInsertStatement(VSDConstants.TBL_PICK_LIST);
				int i = 1;
				insertIntoPickList.setInt(i++, entryId);
				insertIntoPickList.setString(i++, pickList.getPickListId());
				insertIntoPickList.setString(i++, pickList.getRepresentsValueDomain());
				insertIntoPickList.setBoolean(i++, pickList.isCompleteDomain());
				insertIntoPickList.setString(i++, pickList.getDefaultEntityCodeNamespace());
				insertIntoPickList.setString(i++, pickList.getDefaultLanguage());
				insertIntoPickList.setString(i++, pickList.getDefaultSortOrder());
				insertIntoPickList.setBoolean(i++, pickList.getIsActive());
				insertIntoPickList.setString(i++, systemReleaseURI != null ? systemReleaseURI.toString() : null); 
				
				if (pickList.getEntryState() != null) {
					insertIntoPickList.setInt(i++, entryStateId);
				} else {
					if(this.getDatabaseName().equals("ACCESS")){
						insertIntoPickList.setString(i++, null);
	            	} else {
	            		insertIntoPickList.setObject(i++, null, java.sql.Types.BIGINT);
	            	}
				}
				
				insertIntoPickList.setString(i++, pickList.getEntityDescription());

				insertIntoPickList.executeUpdate();
				insertIntoPickList.clearParameters();
			} catch (SQLException e) {
				if ((e.getMessage().indexOf("Duplicate entry") != -1)
						|| (e.getMessage().indexOf("unique constraint") != -1)
						|| (e.getMessage().indexOf("SQLCODE: -803") != -1)) {
					// print error message and continue
					String msg = "PickList already exists with PickListId " + pickList.getPickListId() + ". Error message=" + e.getMessage();
					System.out.println(msg);
					if (SingletonVariables.instance(getConnectionDescriptor().toString()).messages != null) {
						SingletonVariables.instance(getConnectionDescriptor().toString()).messages.message(msg);
					}
					ignorePL = true;
				}
			} catch (Exception e) {
				throw new InsertException(e);
			} finally {
				try {
					if (insertIntoPickList != null)
						insertIntoPickList.close();
				} catch (Exception e) {
					// do nothing
				}
			}

			if (!ignorePL)
			{
				// insert Source and Context
				insertAttributes(pickList, entryId);
				
				if (pickList.getPickListEntryNode() != null) {
					Iterator<PickListEntryNode> iter = pickList.getPickListEntryNode().iterator();
					while (iter.hasNext()) {
						plEntryServices_.insert((PickListEntryNode) iter.next(), entryId);
					}
				}

				if (pickList.getProperties() != null) {
					Iterator<Property> propIter = pickList.getProperties().getProperty()
							.iterator();
					while (propIter.hasNext()) {
						propertyServices_.insert(propIter.next(),
								entryId);
					}
				}
				
				if (pickList.getMappings() != null)
				{
					mappingServices_.insert(pickList.getMappings(), entryId);
				}
				
				if (pickList.getEntryState() != null)
					insertEntryState(pickList, entryStateId);
			}
		} catch (InsertException e) {
			throw e;
		} catch (ObjectAlreadyExistsException e) {
			throw e;
		} catch (Exception e) {
			throw new InsertException(e);
		}
	}

	public void insertAttributes(PickListDefinition pickList, int vdEntryId) throws InsertException, ObjectAlreadyExistsException {
		PreparedStatement insertIntoProperty = null;
		try {
			insertIntoProperty = getKeyedInsertStatement(VSDConstants.TBL_PROPERTY);
			
			Iterator<Source> sourceIter = pickList.getSource().iterator();
			
			while (sourceIter.hasNext()) {
				Source source = sourceIter.next();
				
				int k = 1;
				
				int entryId = getNextEntryId();
				insertEntryType(VSDConstants.ENTRY_STATE_TYPE_ATTRIBUTE, entryId);

				insertIntoProperty.setInt(k++, entryId);
				insertIntoProperty.setInt(k++, vdEntryId);
				insertIntoProperty.setString(k++, SQLTableConstants.TBLCOLVAL_SOURCE);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, true);
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, null);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, null);
				if(this.getDatabaseName().equals("ACCESS")){
					insertIntoProperty.setString(k++, null);
            	} else {
            		insertIntoProperty.setObject(k++, null, java.sql.Types.BIGINT);
            	}
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, null);
				String valueString = source.getValue();
				insertIntoProperty.setString(k++, valueString);
				String subRef = setBlankStringsNull(source.getSubRef());
				insertIntoProperty.setString(k++, subRef == null ? " " : subRef);
				insertIntoProperty.setString(k++, setBlankStringsNull(source.getRole()));
				
				insertIntoProperty.executeUpdate();
			}
			
			Iterator<String> contextIter = pickList.getDefaultPickContext().iterator();
			while (contextIter.hasNext()) {
				String context = contextIter.next();
				
				int k = 1;
				
				int entryId = getNextEntryId();
				insertEntryType(VSDConstants.ENTRY_STATE_TYPE_ATTRIBUTE, entryId);

				insertIntoProperty.setInt(k++, entryId);
				insertIntoProperty.setInt(k++, vdEntryId);
				insertIntoProperty.setString(k++, SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, true);
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, null);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, null);
				if(this.getDatabaseName().equals("ACCESS")){
					insertIntoProperty.setString(k++, null);
            	} else {
            		insertIntoProperty.setObject(k++, null, java.sql.Types.BIGINT);
            	}
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, context);
				insertIntoProperty.setString(k++, " ");
				insertIntoProperty.setString(k++, null);
				
				insertIntoProperty.executeUpdate();
			}
            insertIntoProperty.close();
		} catch (SQLException e) {
			throw new ObjectAlreadyExistsException(e);
		} catch (Exception e) {
			throw new InsertException(e);
		} finally {
			try {
				if (insertIntoProperty != null)
					insertIntoProperty.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	protected Class<PickListDefinitionImpl> getInstanceClass() {
		return PickListDefinitionImpl.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.LexGrid.plugin.base.service.LgModelPersistence#synchronize(org.LexGrid
	 * .plugin.base.LgModelObj, boolean)
	 */
	public void synchronize(LgModelObj obj, boolean recurse)
			throws ManagedObjException {
		// TODO: Implement synchronize
		throw new java.lang.UnsupportedOperationException(
				"Method synchronize not yet implemented.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.org.LexGrid.commons.managedobj.service.BaseService#initNestedServices
	 * ()
	 */
	protected void initNestedServices() throws ServiceInitException {
		plEntryServices_ = new PLEntryServices(this);
		propertyServices_ = new VSDPropertyServices(this);
		entryTypeServices_ = new VSDEntryTypeServices(this);
		entryStateService_ = new VSDEntryStateService(this);
		mappingServices_ = new VSDMappingServices(this);
		systemReleaseServices_ = new SystemReleaseServices(this);
	}

	public void stageFeature(LgStagedObj obj, EStructuralFeature feature)
			throws ResolveException {
		try {
			if (obj instanceof PickListDefinition) {
				// mark everything as complete
				Iterator iterator = ((PickListDefinition) obj).eClass()
						.getEAllStructuralFeatures().iterator();
				while (iterator.hasNext()) {
					EStructuralFeature temp = (EStructuralFeature) iterator
							.next();
					obj.stageComplete(temp);
				}

				obj.stageComplete(feature);
			}
		} catch (Exception e) {
			throw new ResolveException(
					"Problem getting the sub-details for the pick list", e);
		}
	}

	public void update(ManagedObjIF obj) throws UpdateException,
			ObjectNotFoundException {
		// TODO: Implement update
		throw new java.lang.UnsupportedOperationException(
				"Method update not yet implemented.");
	}

	public void remove(Object key) throws RemoveException, FindException {
		
		PreparedStatement selectFromPickList = null;
		String pickListId = (String) key;
		int plEntryId;
		String plEntryStateIdStr = null;
		
		try {
			
			selectFromPickList = checkOutPreparedStatement(" SELECT  "
					+ SQLTableConstants.TBLCOL_ENTRYID 
					+ ", " + SQLTableConstants.TBLCOL_ENTRYSTATEID
					+ " FROM " + VSDConstants.TBL_PICK_LIST + " WHERE "
					+ SQLTableConstants.TBLCOL_PICKLISTID + " = ?");

			selectFromPickList.setString(1, pickListId.toString());

			ResultSet results = selectFromPickList.executeQuery();

			if (results.next()) {
				plEntryId = results.getInt(SQLTableConstants.TBLCOL_ENTRYID);
				plEntryStateIdStr = results.getString(SQLTableConstants.TBLCOL_ENTRYSTATEID);
				
				results.close();
			} else {
				throw new FindException(
						"Could not find pick list for pickListId : " + pickListId);
			}
		} catch (SQLException e) {
			throw new RemoveException(e.getMessage());
		} finally {
			checkInPreparedStatement(selectFromPickList);
		}
		
		// remove pickList attributes (Source and Context)
		try {
			propertyServices_.removePropQualsAndAttribs(plEntryId);
			// remove valueDomain property
			propertyServices_.remove(plEntryId, SQLTableConstants.ENTRY_STATE_TYPE_PICKLIST);			
			
			// remove all pickListEntryNode associated to this pickList from plEntry table
			plEntryServices_.remove(plEntryId);
			
			mappingServices_.remove(plEntryId, SQLTableConstants.ENTRY_STATE_TYPE_PICKLIST);
			
			if (StringUtils.isNotEmpty(plEntryStateIdStr))
			{
				ArrayList<Integer> entryStateIds = new ArrayList<Integer>();
				entryStateIds.add(Integer.valueOf(plEntryStateIdStr));
				entryStateService_.remove(entryStateIds, SQLTableConstants.ENTRY_STATE_TYPE_PICKLIST);
			}
			
			// now remove this pickList
			removePickList(plEntryId);
			
			// remove from entryType table
			ArrayList<Integer> entryIds = new ArrayList<Integer>();
			entryIds.add(plEntryId);
			entryTypeServices_.remove(entryIds, SQLTableConstants.ENTRY_STATE_TYPE_PICKLIST);
		} catch (SQLException e) {
			throw new RemoveException(e.getMessage());
		}
	}
	
	private void removePickList(int entryId) throws SQLException, FindException {
		
		PreparedStatement deleteFromPickList = null;
		
		try {
			
			deleteFromPickList = checkOutPreparedStatement("DELETE FROM " 
					+ VSDConstants.TBL_PICK_LIST + " WHERE "
					+ SQLTableConstants.TBLCOL_ENTRYID + " = ?");

			deleteFromPickList.setInt(1, entryId);

			deleteFromPickList.executeUpdate();
		} finally {
			checkInPreparedStatement(deleteFromPickList);
		}
	}
	
	protected void insertEntryType(PickListDefinition pickList, int entryId)
			throws InsertException, ObjectAlreadyExistsException {
		entryTypeServices_.insert(pickList, entryId);
	}

	protected void insertEntryType(String entryType, int entryId)
			throws InsertException, ObjectAlreadyExistsException {
		entryTypeServices_.insert(entryType, entryId);
	}

	protected String resolveEntryType(int entryId) throws FindException {
		return entryTypeServices_.resolveEntryTypeFor(entryId);
	}

	protected void insertEntryState(PickListDefinition pickList, int entryStateId)
			throws InsertException, ObjectAlreadyExistsException {
		entryStateService_.insert(pickList, entryStateId, null);
	}

	protected void resolveEntryState(PickListDefinition pickList, int entryStateId)
			throws FindException {
		entryStateService_.findbyPrimaryKey(pickList, entryStateId);
	}
}