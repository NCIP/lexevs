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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.emf.base.LgModelObj;
import org.LexGrid.emf.base.LgStagedObj;
import org.LexGrid.emf.commonTypes.Property;
import org.LexGrid.emf.valueDomains.PickListDefinition;
import org.LexGrid.emf.valueDomains.PickListEntry;
import org.LexGrid.emf.valueDomains.PickListEntryExclusion;
import org.LexGrid.emf.valueDomains.PickListEntryNode;
import org.LexGrid.emf.valueDomains.ValuedomainsFactory;
import org.LexGrid.emf.valueDomains.impl.PickListEntryNodeImpl;
import org.LexGrid.managedobj.FindException;
import org.LexGrid.managedobj.HomeServiceBroker;
import org.LexGrid.managedobj.InsertException;
import org.LexGrid.managedobj.ManagedObjException;
import org.LexGrid.managedobj.ManagedObjIF;
import org.LexGrid.managedobj.ObjectAlreadyExistsException;
import org.LexGrid.managedobj.ObjectNotFoundException;
import org.LexGrid.managedobj.ResolveException;
import org.LexGrid.managedobj.ServiceInitException;
import org.LexGrid.managedobj.UpdateException;
import org.LexGrid.managedobj.jdbc.JDBCBaseService;
import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.LexGrid.persistence.sql.SingletonVariables;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lexgrid.valuesets.helper.VSDConstants;

import edu.mayo.informatics.lexgrid.convert.utility.MessageRedirector;

/**
 * <pre>
 *        Title:        PLEntryServices.java
 *        Description:  Class that handles PickListEntryNode to the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class PLEntryServices extends VSDBaseService{
	
	private VSDEntryTypeServices entryTypeServices_;
	private VSDEntryStateService entryStateService_;
	private VSDPropertyServices propertyServices_;
	
	public PLEntryServices(JDBCBaseService anchorService)
			throws ServiceInitException {
		super(anchorService);
	}
	
	public PLEntryServices(HomeServiceBroker broker, JDBCConnectionDescriptor desc,
			boolean b, String tablePrefix, boolean failOnAllErrors,
			MessageRedirector messageRedirector, boolean createCSTables) throws ServiceInitException {
		super(broker, desc, b, tablePrefix, failOnAllErrors, messageRedirector, createCSTables);
		SingletonVariables.instance(getConnectionDescriptor().toString()).messages = messageRedirector;
		SingletonVariables.instance(getConnectionDescriptor().toString()).failOnAllErrors = failOnAllErrors;
	}
	
	public void resolve(Object key) throws FindException
	{
		PreparedStatement selectFromPLEntry = null;
		
		try
		{
			Integer foreignEntryId = (Integer) key;

			selectFromPLEntry = checkOutPreparedStatement(
					" SELECT * " +
					" FROM " + VSDConstants.TBL_PL_ENTRY + 
					" WHERE " + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? ");
			
			selectFromPLEntry.setInt(1, foreignEntryId);

			ResultSet results = selectFromPLEntry.executeQuery();
			
			while (results.next())
			{
				PickListEntryNode plEntryNode = ValuedomainsFactory.eINSTANCE.createPickListEntryNode();
				PickListEntry plEntry = ValuedomainsFactory.eINSTANCE.createPickListEntry();
				
				int entryId = results.getInt(SQLTableConstants.TBLCOL_ENTRYID);
				plEntryNode.setPickListEntryId(results.getString(SQLTableConstants.TBLCOL_PLENTRYID));
				
				String entityCodeNamespace = results.getString(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE);
				plEntry.setEntityCodeNamespace(entityCodeNamespace);
				String entityCode = results.getString(SQLTableConstants.TBLCOL_ENTITYCODE);
				plEntry.setEntityCode(entityCode);
				plEntry.setEntryOrder(results.getInt(SQLTableConstants.TBLCOL_ENTRYORDER));
				Boolean isDefault = getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISDEFAULT);
				plEntry.setIsDefault(isDefault == null ? false : isDefault);
				Boolean matchIfNoContext = getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT);
				plEntry.setMatchIfNoContext(matchIfNoContext == null ? false : matchIfNoContext);
				plEntry.setPropertyId(results.getString(SQLTableConstants.TBLCOL_PROPERTYID));
				
				plEntryNode.setIsActive(getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISACTIVE));
				
				Boolean include = getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_INCLUDE);
				String entryStateIdStr = results.getString(SQLTableConstants.TBLCOL_ENTRYSTATEID);				
				plEntry.setPickText(results.getString(SQLTableConstants.TBLCOL_PICKTEXT));				
				
				
				// get pick list entry attributes (Context)
				getAttributes(plEntry, entryId);
				
				propertyServices_.setEContext(plEntryNode, getEContainmentFeature());
				propertyServices_.findByPrimaryKey(plEntryNode, entryId);

				if (entryStateIdStr != null) {
					resolveEntryState(plEntryNode, Integer.valueOf(entryStateIdStr));
				}
				
				if (include)
					plEntryNode.setInclusionEntry(plEntry);
				else
				{
					PickListEntryExclusion plEntryExclude = ValuedomainsFactory.eINSTANCE.createPickListEntryExclusion();
					plEntryExclude.setEntityCodeNamespace(entityCodeNamespace);
					plEntryExclude.setEntityCode(entityCode);
					plEntryNode.setExclusionEntry(plEntryExclude);
				}
				
				((PickListDefinition) getEContainer()).getPickListEntryNode().add(plEntryNode);
			}
			results.close();
		}
		catch (Exception e)
		{
			throw new FindException("Problem", e);
		}
		finally
		{
			checkInPreparedStatement(selectFromPLEntry);
		}
	}
	
	private void getAttributes(PickListEntry plEntry, int entryId)
			throws SQLException {
		PreparedStatement selectPLEntrySubProperties = null;

		try {
			selectPLEntrySubProperties = checkOutPreparedStatement(modifySql(" SELECT P1.* "
					+ " FROM "
					+ VSDConstants.TBL_PROPERTY + " {AS} P1 "
					+ " , " + VSDConstants.TBL_ENTRY_TYPE + " {AS} P2 "
					+ " WHERE P1." + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? "
					+ " AND P1." + SQLTableConstants.TBLCOL_ENTRYID 
					+ " = P2." + SQLTableConstants.TBLCOL_REFERENCEENTRYID
					+ " AND P2." + SQLTableConstants.TBLCOL_ENTRYTYPE + " = ?"));

			selectPLEntrySubProperties.setInt(1, entryId);
			selectPLEntrySubProperties.setString(2, VSDConstants.ENTRY_STATE_TYPE_ATTRIBUTE);

			ResultSet results = selectPLEntrySubProperties.executeQuery();

			while (results.next()) {
				String attributeName = results.getString(SQLTableConstants.TBLCOL_PROPERTYNAME);
				if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT)) {
					plEntry.getPickContext().add(results.getString(SQLTableConstants.TBLCOL_PROPERTYVALUE));
				}
			}
			results.close();
		} finally {
			checkInPreparedStatement(selectPLEntrySubProperties);
		}
	}
	
	@Override
	public void insert(ManagedObjIF obj) throws InsertException,
			ObjectAlreadyExistsException {
		
		PickListEntryNode plEntryNode = (PickListEntryNode) obj;
		
		PickListDefinition plDef = (PickListDefinition) plEntryNode.getContainer(PickListDefinition.class, 1);
		
		StringBuffer query = new StringBuffer();
		
		query.append("SELECT ");
		query.append(SQLTableConstants.TBLCOL_ENTRYID);
		query.append(" FROM ");
		query.append(VSDConstants.TBL_PICK_LIST);
		query.append(" WHERE ");
		query.append(SQLTableConstants.TBLCOL_PICKLISTID + " = ?");
		
		Integer entryId = null;
		try {
			PreparedStatement prepStmt = checkOutPreparedStatement(query.toString());
			
			prepStmt.setString(1, plDef.getPickListId());
			
			ResultSet result = prepStmt.executeQuery();
			
			if( result.next() ) {
				entryId = result.getInt(SQLTableConstants.TBLCOL_ENTRYID);
				
				insert(obj, entryId);
			} else {
				
				throw new InsertException(
						"Given pick list entry node doesn't belong to a valid pick list.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insert(ManagedObjIF obj, int foreignEntryId) throws LBException, Exception
	{
		PickListEntryNode plEntryNode = (PickListEntryNode) (obj);
		int entryId = getNextEntryId();
		insertEntryType(plEntryNode, entryId);

		int entryStateId = getNextEntryId();
		
		PreparedStatement insertIntoPLEntryn = null;
		try
		{
			insertIntoPLEntryn = getKeyedInsertStatement(VSDConstants.TBL_PL_ENTRY);			
			
			PickListEntry plEntry = plEntryNode.getInclusionEntry();
			PickListEntryExclusion plEntryExclude = plEntryNode.getExclusionEntry();
			
			if (plEntry != null)
			{
				int i = 1;
				insertIntoPLEntryn.setInt(i++, entryId);
				insertIntoPLEntryn.setInt(i++, foreignEntryId);
				insertIntoPLEntryn.setString(i++, plEntryNode.getPickListEntryId());
				insertIntoPLEntryn.setString(i++, plEntry.getEntityCodeNamespace());
				insertIntoPLEntryn.setString(i++, plEntry.getEntityCode());            
				insertIntoPLEntryn.setInt(i++, plEntry.getEntryOrder());			
				setBooleanOnPreparedStatment(insertIntoPLEntryn, i++, plEntry.isIsDefault());
				setBooleanOnPreparedStatment(insertIntoPLEntryn, i++, plEntry.isMatchIfNoContext());
				insertIntoPLEntryn.setString(i++, plEntry.getPropertyId());
				setBooleanOnPreparedStatment(insertIntoPLEntryn, i++, plEntryNode.getIsActive());
				setBooleanOnPreparedStatment(insertIntoPLEntryn, i++, true); // 'true' for include list
				if (plEntryNode.getEntryState() != null)					
					insertIntoPLEntryn.setInt(i++, entryStateId);
				else
				{
					if(this.getDatabaseName().equals("ACCESS")){
						insertIntoPLEntryn.setString(i++, null);
	            	} else {
	            		insertIntoPLEntryn.setObject(i++, null, java.sql.Types.BIGINT);
	            	}
				}
				insertIntoPLEntryn.setString(i++, plEntry.getPickText());           
				
				insertIntoPLEntryn.executeUpdate();
				insertIntoPLEntryn.clearParameters();
				
				if (plEntry.getPickContext() != null)
					insertAttributes(plEntry, entryId);
				
			}
			else if (plEntryExclude != null)
			{
				int i = 1;
				insertIntoPLEntryn.setInt(i++, entryId);
				insertIntoPLEntryn.setInt(i++, foreignEntryId);
				insertIntoPLEntryn.setString(i++, plEntryNode.getPickListEntryId());
				insertIntoPLEntryn.setString(i++, plEntryExclude.getEntityCodeNamespace());
				insertIntoPLEntryn.setString(i++, plEntryExclude.getEntityCode());          
				if(this.getDatabaseName().equals("ACCESS")){
					insertIntoPLEntryn.setString(i++, null);
				} else {
					insertIntoPLEntryn.setObject(i++, null, java.sql.Types.BIGINT);
				}
				setBooleanOnPreparedStatment(insertIntoPLEntryn, i++, null);
				setBooleanOnPreparedStatment(insertIntoPLEntryn, i++, null);
				insertIntoPLEntryn.setString(i++, null);
				setBooleanOnPreparedStatment(insertIntoPLEntryn, i++, null);
				setBooleanOnPreparedStatment(insertIntoPLEntryn, i++, false); // 'false' for exclude list
				if (plEntryNode.getEntryState() != null)					
					insertIntoPLEntryn.setInt(i++, entryStateId);
				else
				{
					if(this.getDatabaseName().equals("ACCESS")){
						insertIntoPLEntryn.setString(i++, null);
	            	} else {
	            		insertIntoPLEntryn.setObject(i++, null, java.sql.Types.BIGINT);
	            	}
				}
				insertIntoPLEntryn.setString(i++, null);           
				
				insertIntoPLEntryn.executeUpdate();
				insertIntoPLEntryn.clearParameters();
			}		
			
			if (plEntryNode.getEntryState() != null)
				insertEntryState(plEntryNode, entryStateId);
			
			if (plEntryNode.getProperties() != null) {
				Iterator<Property> propIter = plEntryNode.getProperties().getProperty().iterator();
				while (propIter.hasNext()) {
					propertyServices_.insert((Property) propIter.next(), entryId);
				}
			}
		}
		catch (SQLException e)
		{
			throw new ObjectAlreadyExistsException(e);
		}
		catch (Exception e)
		{
			throw new InsertException(e);
		}
		finally
		{
			try
			{
				if (insertIntoPLEntryn != null)
					insertIntoPLEntryn.close();
			}
			catch (Exception e)
			{
				// do nothing
			}
		}
	}
	
	public void insertAttributes(PickListEntry pickListEntry, int vdEntryId) throws InsertException, ObjectAlreadyExistsException {
		PreparedStatement insertIntoProperty = null;
		try {
			insertIntoProperty = getKeyedInsertStatement(VSDConstants.TBL_PROPERTY);

			
			Iterator contextIter = pickListEntry.getPickContext().iterator();
			while (contextIter.hasNext()) {
				String context = (String) contextIter.next();
				
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

	protected Class getInstanceClass()
	{
		return PickListEntryNodeImpl.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.LexGrid.plugin.base.service.LgModelPersistence#synchronize(org.LexGrid.plugin.base.LgModelObj,
	 *      boolean)
	 */
	public void synchronize(LgModelObj obj, boolean recurse) throws ManagedObjException
	{
		// TODO: Implement synchronize
		throw new java.lang.UnsupportedOperationException("Method synchronize not yet implemented.");
	}

	public void stageFeature(LgStagedObj obj, EStructuralFeature feature) throws ResolveException
	{
		try
		{
			if (obj instanceof PickListEntryNode)
			{
				//mark everything as complete
				Iterator iterator = ((PickListEntryNode)obj).eClass().getEAllStructuralFeatures().iterator();
				while (iterator.hasNext())
				{
					EStructuralFeature temp = (EStructuralFeature)iterator.next();
					obj.stageComplete(temp);
				}

				obj.stageComplete(feature);
			}
		}
		catch (Exception e)
		{
			throw new ResolveException("Problem getting the sub-details for the Pick List", e);
		}
	}

	protected void initNestedServices() throws ServiceInitException
    {
        entryTypeServices_ = new VSDEntryTypeServices(this);
        entryStateService_ = new VSDEntryStateService(this);
        propertyServices_ = new VSDPropertyServices(this);
    }
    
	public void update(ManagedObjIF obj) throws UpdateException, ObjectNotFoundException
	{
		throw new java.lang.UnsupportedOperationException("Method update not yet implemented.");
	}
	
	protected void remove(int foreignEntryId) throws SQLException, FindException
	{
		PreparedStatement deleteFromPLEntry = null;
		PreparedStatement selectFromPLEntry = null;
		ArrayList<Integer> entryIds = new ArrayList<Integer>();
		ArrayList<Integer> entryStateIds = new ArrayList<Integer>();
		
		try
		{
			selectFromPLEntry = checkOutPreparedStatement(
					" SELECT " + SQLTableConstants.TBLCOL_ENTRYID +
					" , " + SQLTableConstants.TBLCOL_ENTRYSTATEID +
					" FROM " + VSDConstants.TBL_PL_ENTRY + 
					" WHERE " + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? ");
			
			selectFromPLEntry.setInt(1, foreignEntryId);
	
			ResultSet results = selectFromPLEntry.executeQuery();
			String entryStateIdStr = null;
			while (results.next())
			{
				entryIds.add(results.getInt(SQLTableConstants.TBLCOL_ENTRYID));
				entryStateIdStr = results.getString(SQLTableConstants.TBLCOL_ENTRYSTATEID);
				if (StringUtils.isNotEmpty(entryStateIdStr))
					entryStateIds.add(Integer.valueOf(entryStateIdStr));
			}
			results.close();
		} finally
		{
			checkInPreparedStatement(selectFromPLEntry);
		}
		
		try
		{
			deleteFromPLEntry = checkOutPreparedStatement(
					" DELETE FROM " + VSDConstants.TBL_PL_ENTRY + 
					" WHERE " + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? ");
			
			deleteFromPLEntry.setInt(1, foreignEntryId);

			deleteFromPLEntry.executeUpdate();
		}
		finally
		{
			checkInPreparedStatement(deleteFromPLEntry);
		}
		
		for (int entryId : entryIds)
			propertyServices_.remove(entryId, SQLTableConstants.ENTRY_STATE_TYPE_PICKLISTENTRY);
		
		if (entryStateIds.size() > 0)
			entryStateService_.remove(entryStateIds, SQLTableConstants.ENTRY_STATE_TYPE_PICKLISTENTRY);
		
		entryTypeServices_.remove(entryIds, SQLTableConstants.ENTRY_STATE_TYPE_PICKLISTENTRY);
		
		entryIds.clear();
	}
	
    protected void insertEntryType(PickListEntryNode plEntryNode, int entryId) throws InsertException, ObjectAlreadyExistsException
	{
    	entryTypeServices_.insert(plEntryNode, entryId);
	}
    	
    protected void insertEntryType(String entryType, int entryId) throws InsertException, ObjectAlreadyExistsException {
    	entryTypeServices_.insert(entryType, entryId);
    }
    
    protected String resolveEntryType(int entryId) throws FindException
	{
    	return entryTypeServices_.resolveEntryTypeFor(entryId);
	}	
    
    protected void insertEntryState(PickListEntryNode plEntryNode, int entryStateId) throws InsertException, ObjectAlreadyExistsException {
    	entryStateService_.insert(plEntryNode, entryStateId, null);
    }

    protected void resolveEntryState(PickListEntryNode plEntryNode, int entryStateId) throws FindException {
    	entryStateService_.findbyPrimaryKey(plEntryNode, entryStateId);
    }
}