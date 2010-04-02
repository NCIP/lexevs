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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.LexGrid.emf.codingSchemes.impl.CodingSchemeImpl;
import org.LexGrid.emf.commonTypes.CommontypesFactory;
import org.LexGrid.emf.commonTypes.Source;
import org.LexGrid.emf.commonTypes.impl.PropertyImpl;
import org.LexGrid.emf.concepts.impl.EntityImpl;
import org.LexGrid.emf.relations.impl.AssociationDataImpl;
import org.LexGrid.emf.relations.impl.AssociationImpl;
import org.LexGrid.emf.relations.impl.AssociationTargetImpl;
import org.LexGrid.emf.valueDomains.impl.PickListDefinitionImpl;
import org.LexGrid.emf.valueDomains.impl.PickListEntryNodeImpl;
import org.LexGrid.emf.valueDomains.impl.ValueDomainDefinitionImpl;
import org.LexGrid.emf.versions.ChangeType;
import org.LexGrid.emf.versions.EntryState;
import org.LexGrid.emf.versions.VersionsFactory;
import org.LexGrid.managedobj.FindException;
import org.LexGrid.managedobj.InsertException;
import org.LexGrid.managedobj.ManagedObjIF;
import org.LexGrid.managedobj.ObjectAlreadyExistsException;
import org.LexGrid.managedobj.ObjectNotFoundException;
import org.LexGrid.managedobj.ServiceInitException;
import org.LexGrid.managedobj.jdbc.JDBCBaseService;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;

/**
 * <pre>
 *        Title:        EntryStateService.java
 *        Description:  Class that handles EntryState to the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VSDEntryStateService extends VSDBaseService
{
	
    public VSDEntryStateService(JDBCBaseService anchorService) throws ServiceInitException
    {
        super(anchorService);
    }
	
	
    public void insert(ManagedObjIF obj, Integer entryStateId, Integer prevEntryStateId) throws InsertException, ObjectAlreadyExistsException
	{
    	String entryType = null;
    	String owner = null;
    	String status = null;
    	Date effectiveDate = null;
    	Date expirationDate = null;
    	String revisionId = null;
    	String prevRevisionId = null;
    	String changeType = null;
    	int relativeOrder = 0;
    	
		if (obj instanceof EntityImpl)
    	{
			EntityImpl temp = (EntityImpl) obj;
			entryType = SQLTableConstants.ENTRY_STATE_TYPE_ENTITY;
			if (temp.getOwner() != null)
				owner = temp.getOwner().getValue();
			status = temp.getStatus();
			effectiveDate = temp.getEffectiveDate();
			expirationDate = temp.getExpirationDate();
			
			EntryState es = temp.getEntryState();
			if (es != null)
			{
				revisionId = es.getContainingRevision();
				prevRevisionId = es.getPrevRevision();
				changeType = es.getChangeType().getLiteral();
				relativeOrder = es.getRelativeOrder();				
			}
    	}
		else if (obj instanceof AssociationImpl)
    	{
			AssociationImpl temp = (AssociationImpl) obj;
			entryType = SQLTableConstants.ENTRY_STATE_TYPE_ASSOCIATION;
			if (temp.getOwner() != null)
				owner = temp.getOwner().getValue();
			status = temp.getStatus();
			effectiveDate = temp.getEffectiveDate();
			expirationDate = temp.getExpirationDate();
			
			EntryState es = temp.getEntryState();
			if (es != null)
			{
				revisionId = es.getContainingRevision();
				prevRevisionId = es.getPrevRevision();
				changeType = es.getChangeType().getLiteral();
				relativeOrder = es.getRelativeOrder();				
			}
    	}
		else if (obj instanceof CodingSchemeImpl)
    	{
			CodingSchemeImpl temp = (CodingSchemeImpl) obj;
			entryType = SQLTableConstants.ENTRY_STATE_TYPE_CODINGSCHEME;
			if (temp.getOwner() != null)
				owner = temp.getOwner().getValue();
			status = temp.getStatus();
			effectiveDate = temp.getEffectiveDate();
			expirationDate = temp.getExpirationDate();
			
			EntryState es = temp.getEntryState();
			if (es != null)
			{
				revisionId = es.getContainingRevision();
				prevRevisionId = es.getPrevRevision();
				changeType = es.getChangeType().getLiteral();
				relativeOrder = es.getRelativeOrder();				
			}
    	}
		else if (obj instanceof PropertyImpl)
    	{
			PropertyImpl temp = (PropertyImpl) obj;
			entryType = SQLTableConstants.ENTRY_STATE_TYPE_PROPERTY;
			if (temp.getOwner() != null)
				owner = temp.getOwner().getValue();
			status = temp.getStatus();
			effectiveDate = temp.getEffectiveDate();
			expirationDate = temp.getExpirationDate();
			
			EntryState es = temp.getEntryState();
			if (es != null)
			{
				revisionId = es.getContainingRevision();
				prevRevisionId = es.getPrevRevision();
				changeType = es.getChangeType().getLiteral();
				relativeOrder = es.getRelativeOrder();				
			}
    	}
		else if (obj instanceof AssociationTargetImpl)
    	{
			AssociationTargetImpl temp = (AssociationTargetImpl) obj;
			entryType = SQLTableConstants.ENTRY_STATE_TYPE_ENTITYASSNSTOENTITY;
			if (temp.getOwner() != null)
				owner = temp.getOwner().getValue();
			status = temp.getStatus();
			effectiveDate = temp.getEffectiveDate();
			expirationDate = temp.getExpirationDate();
			
			EntryState es = temp.getEntryState();
			if (es != null)
			{
				revisionId = es.getContainingRevision();
				prevRevisionId = es.getPrevRevision();
				changeType = es.getChangeType().getLiteral();
				relativeOrder = es.getRelativeOrder();				
			}
    	}
		else if (obj instanceof AssociationDataImpl)
    	{
			AssociationDataImpl temp = (AssociationDataImpl) obj;
			entryType = SQLTableConstants.ENTRY_STATE_TYPE_ENTITYASSNSTODATA;
			if (temp.getOwner() != null)
				owner = temp.getOwner().getValue();
			status = temp.getStatus();
			effectiveDate = temp.getEffectiveDate();
			expirationDate = temp.getExpirationDate();
			
			EntryState es = temp.getEntryState();
			if (es != null)
			{
				revisionId = es.getContainingRevision();
				prevRevisionId = es.getPrevRevision();
				changeType = es.getChangeType().getLiteral();
				relativeOrder = es.getRelativeOrder();				
			}
    	}
		else if (obj instanceof ValueDomainDefinitionImpl)
    	{
			ValueDomainDefinitionImpl temp = (ValueDomainDefinitionImpl) obj;
			entryType = SQLTableConstants.ENTRY_STATE_TYPE_VALUEDOMAIN;
			if (temp.getOwner() != null)
				owner = temp.getOwner().getValue();
			status = temp.getStatus();
			effectiveDate = temp.getEffectiveDate();
			expirationDate = temp.getExpirationDate();
			
			EntryState es = temp.getEntryState();
			if (es != null)
			{
				revisionId = es.getContainingRevision();
				prevRevisionId = es.getPrevRevision();
				changeType = es.getChangeType().getLiteral();
				relativeOrder = es.getRelativeOrder();				
			}
    	}
		else if (obj instanceof PickListDefinitionImpl)
    	{
			PickListDefinitionImpl temp = (PickListDefinitionImpl) obj;
			entryType = SQLTableConstants.ENTRY_STATE_TYPE_PICKLIST;
			if (temp.getOwner() != null)
				owner = temp.getOwner().getValue();
			status = temp.getStatus();
			effectiveDate = temp.getEffectiveDate();
			expirationDate = temp.getExpirationDate();
			
			EntryState es = temp.getEntryState();
			if (es != null)
			{
				revisionId = es.getContainingRevision();
				prevRevisionId = es.getPrevRevision();
				changeType = es.getChangeType().getLiteral();
				relativeOrder = es.getRelativeOrder();				
			}
    	}
		else if (obj instanceof PickListEntryNodeImpl)
    	{
			PickListEntryNodeImpl temp = (PickListEntryNodeImpl) obj;
			entryType = SQLTableConstants.ENTRY_STATE_TYPE_PICKLISTENTRY;
			if (temp.getOwner() != null)
				owner = temp.getOwner().getValue();
			status = temp.getStatus();
			effectiveDate = temp.getEffectiveDate();
			expirationDate = temp.getExpirationDate();
			
			EntryState es = temp.getEntryState();
			if (es != null)
			{
				revisionId = es.getContainingRevision();
				prevRevisionId = es.getPrevRevision();
				changeType = es.getChangeType().getLiteral();
				relativeOrder = es.getRelativeOrder();				
			}
    	}
		
		PreparedStatement insertIntoEntryState = null;
		try
		{
			// Insert only if there is any data.
			if (!StringUtils.isBlank(owner) || !StringUtils.isBlank(status) 
					|| effectiveDate != null || expirationDate != null
					|| !StringUtils.isBlank(revisionId) || !StringUtils.isBlank(prevRevisionId)
					|| !StringUtils.isBlank(changeType))
			{
				insertIntoEntryState = getKeyedInsertStatement(VSDConstants.TBL_ENTRY_STATE);
				
				int k = 1;
				insertIntoEntryState.setInt(k++, entryStateId);
				insertIntoEntryState.setString(k++, entryType);
				
				insertIntoEntryState.setString(k++, owner);
				insertIntoEntryState.setString(k++, status);			
				insertIntoEntryState.setTimestamp(k++, (effectiveDate != null ? new Timestamp(effectiveDate.getTime()) : null));			
				insertIntoEntryState.setTimestamp(k++, (expirationDate != null ? new Timestamp(expirationDate.getTime()) : null));
				
				insertIntoEntryState.setString(k++, revisionId);
				insertIntoEntryState.setString(k++, prevRevisionId);
				
				insertIntoEntryState.setString(k++, (changeType != null ? changeType : " "));
				insertIntoEntryState.setInt(k++, relativeOrder);
				if( prevEntryStateId != null ) {
					insertIntoEntryState.setInt(k++, prevEntryStateId);
				} else {
					if(this.getDatabaseName().equals("ACCESS")){
						insertIntoEntryState.setString(k++, null);
					} else {
						insertIntoEntryState.setObject(k++, null, java.sql.Types.BIGINT);
					}
				}
				
				insertIntoEntryState.executeUpdate();
				insertIntoEntryState.clearParameters();
				insertIntoEntryState.close();
				insertIntoEntryState = null;
			}
		}
		catch (SQLException e)
		{
			throw new ObjectAlreadyExistsException("Problem loading EntryState table ",  e);
		}
		catch (Exception e)
		{
			throw new InsertException("Problem loading EntryState table ",  e);
		}
		finally
		{
			try
			{
				checkInPreparedStatement(insertIntoEntryState);
			}
			catch (Exception e)
			{
				// do nothing
			}
		}
	}
    
    public void findbyPrimaryKey(ManagedObjIF obj, int entryStateId) throws FindException
	{
		PreparedStatement selectFromEntryState = null;

		try
		{
			selectFromEntryState = checkOutPreparedStatement(modifySql(
					" SELECT * " +
					" FROM " + VSDConstants.TBL_ENTRY_STATE + 
					" WHERE " + SQLTableConstants.TBLCOL_ENTRYSTATEID + " = ?"));
			
			selectFromEntryState.setInt(1, entryStateId);
			ResultSet results = selectFromEntryState.executeQuery();

			if (results.next())
			{
				String revisionId = results.getString(SQLTableConstants.TBLCOL_REVISIONID);
				String prevRevisionId = results.getString(SQLTableConstants.TBLCOL_PREVREVISIONID);
				int relativeOrder = results.getInt(SQLTableConstants.TBLCOL_RELATIVEORDER);
				String changeType = results.getString(SQLTableConstants.TBLCOL_CHANGETYPE);
				
				if (revisionId == null && prevRevisionId == null && relativeOrder == 1 && changeType == null)
					return;
				
				if (obj instanceof EntityImpl)
				{
					EntityImpl temp = (EntityImpl) obj;
					
					String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
					if (owner != null)
					{
						Source src = CommontypesFactory.eINSTANCE.createSource();
						src.setValue(owner);
						temp.setOwner(src);
					}
					
					temp.setStatus(results.getString(SQLTableConstants.TBLCOL_STATUS));
					temp.setEffectiveDate(results.getDate(SQLTableConstants.TBLCOL_EFFECTIVEDATE));
					temp.setExpirationDate(results.getDate(SQLTableConstants.TBLCOL_EXPIRATIONDATE));
					
					EntryState es = VersionsFactory.eINSTANCE.createEntryState();
					
					es.setContainingRevision(revisionId);
					es.setPrevRevision(prevRevisionId);
					es.setRelativeOrder(relativeOrder);
					es.setChangeType(ChangeType.get(changeType));
					temp.setEntryState(es);
		    	}
				else if (obj instanceof AssociationImpl)
		    	{
					AssociationImpl temp = (AssociationImpl) obj;
					String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
					if (owner != null)
					{
						Source src = CommontypesFactory.eINSTANCE.createSource();
						src.setValue(owner);
						temp.setOwner(src);
					}
					
					temp.setStatus(results.getString(SQLTableConstants.TBLCOL_STATUS));
					temp.setEffectiveDate(results.getDate(SQLTableConstants.TBLCOL_EFFECTIVEDATE));
					temp.setExpirationDate(results.getDate(SQLTableConstants.TBLCOL_EXPIRATIONDATE));
					
					EntryState es = VersionsFactory.eINSTANCE.createEntryState();
					
					es.setContainingRevision(revisionId);
					es.setPrevRevision(prevRevisionId);
					es.setRelativeOrder(relativeOrder);
					es.setChangeType(ChangeType.get(changeType));				
					temp.setEntryState(es);
		    	}
				else if (obj instanceof CodingSchemeImpl)
		    	{
					CodingSchemeImpl temp = (CodingSchemeImpl) obj;
					String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
					if (owner != null)
					{
						Source src = CommontypesFactory.eINSTANCE.createSource();
						src.setValue(owner);
						temp.setOwner(src);
					}
					
					temp.setStatus(results.getString(SQLTableConstants.TBLCOL_STATUS));
					temp.setEffectiveDate(results.getDate(SQLTableConstants.TBLCOL_EFFECTIVEDATE));
					temp.setExpirationDate(results.getDate(SQLTableConstants.TBLCOL_EXPIRATIONDATE));
					
					EntryState es = VersionsFactory.eINSTANCE.createEntryState();
					
					es.setContainingRevision(revisionId);
					es.setPrevRevision(prevRevisionId);
					es.setRelativeOrder(relativeOrder);
					es.setChangeType(ChangeType.get(changeType));			
					temp.setEntryState(es);	
		    	}
				else if (obj instanceof PropertyImpl)
		    	{
					PropertyImpl temp = (PropertyImpl) obj;
					String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
					if (owner != null)
					{
						Source src = CommontypesFactory.eINSTANCE.createSource();
						src.setValue(owner);
						temp.setOwner(src);
					}
					
					temp.setStatus(results.getString(SQLTableConstants.TBLCOL_STATUS));
					temp.setEffectiveDate(results.getDate(SQLTableConstants.TBLCOL_EFFECTIVEDATE));
					temp.setExpirationDate(results.getDate(SQLTableConstants.TBLCOL_EXPIRATIONDATE));
					
					EntryState es = VersionsFactory.eINSTANCE.createEntryState();
					
					es.setContainingRevision(revisionId);
					es.setPrevRevision(prevRevisionId);
					es.setRelativeOrder(relativeOrder);
					es.setChangeType(ChangeType.get(changeType));				
					temp.setEntryState(es);	
		    	}
				else if (obj instanceof AssociationTargetImpl)
		    	{
					AssociationTargetImpl temp = (AssociationTargetImpl) obj;
					String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
					if (owner != null)
					{
						Source src = CommontypesFactory.eINSTANCE.createSource();
						src.setValue(owner);
						temp.setOwner(src);
					}
					
					temp.setStatus(results.getString(SQLTableConstants.TBLCOL_STATUS));
					temp.setEffectiveDate(results.getDate(SQLTableConstants.TBLCOL_EFFECTIVEDATE));
					temp.setExpirationDate(results.getDate(SQLTableConstants.TBLCOL_EXPIRATIONDATE));
					
					EntryState es = VersionsFactory.eINSTANCE.createEntryState();
					
					es.setContainingRevision(revisionId);
					es.setPrevRevision(prevRevisionId);
					es.setRelativeOrder(relativeOrder);
					es.setChangeType(ChangeType.get(changeType));				
					temp.setEntryState(es);	
		    	}
				else if (obj instanceof AssociationDataImpl)
		    	{
					AssociationDataImpl temp = (AssociationDataImpl) obj;
					String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
					if (owner != null)
					{
						Source src = CommontypesFactory.eINSTANCE.createSource();
						src.setValue(owner);
						temp.setOwner(src);
					}
					
					temp.setStatus(results.getString(SQLTableConstants.TBLCOL_STATUS));
					temp.setEffectiveDate(results.getDate(SQLTableConstants.TBLCOL_EFFECTIVEDATE));
					temp.setExpirationDate(results.getDate(SQLTableConstants.TBLCOL_EXPIRATIONDATE));
					
					EntryState es = VersionsFactory.eINSTANCE.createEntryState();
					
					es.setContainingRevision(revisionId);
					es.setPrevRevision(prevRevisionId);
					es.setRelativeOrder(relativeOrder);
					es.setChangeType(ChangeType.get(changeType));				
					temp.setEntryState(es);
		    	}
				else if (obj instanceof ValueDomainDefinitionImpl)
		    	{
					ValueDomainDefinitionImpl temp = (ValueDomainDefinitionImpl) obj;
					String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
					if (owner != null)
					{
						Source src = CommontypesFactory.eINSTANCE.createSource();
						src.setValue(owner);
						temp.setOwner(src);
					}
					
					temp.setStatus(results.getString(SQLTableConstants.TBLCOL_STATUS));
					temp.setEffectiveDate(results.getDate(SQLTableConstants.TBLCOL_EFFECTIVEDATE));
					temp.setExpirationDate(results.getDate(SQLTableConstants.TBLCOL_EXPIRATIONDATE));
					
					EntryState es = VersionsFactory.eINSTANCE.createEntryState();
					
					es.setContainingRevision(revisionId);
					es.setPrevRevision(prevRevisionId);
					es.setRelativeOrder(relativeOrder);
					es.setChangeType(ChangeType.get(changeType));				
					temp.setEntryState(es);
		    	}
				else if (obj instanceof PickListDefinitionImpl)
		    	{
					PickListDefinitionImpl temp = (PickListDefinitionImpl) obj;
					String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
					if (owner != null)
					{
						Source src = CommontypesFactory.eINSTANCE.createSource();
						src.setValue(owner);
						temp.setOwner(src);
					}
					
					temp.setStatus(results.getString(SQLTableConstants.TBLCOL_STATUS));
					temp.setEffectiveDate(results.getDate(SQLTableConstants.TBLCOL_EFFECTIVEDATE));
					temp.setExpirationDate(results.getDate(SQLTableConstants.TBLCOL_EXPIRATIONDATE));
					
					EntryState es = VersionsFactory.eINSTANCE.createEntryState();
					
					es.setContainingRevision(revisionId);
					es.setPrevRevision(prevRevisionId);
					es.setRelativeOrder(relativeOrder);
					es.setChangeType(ChangeType.get(changeType));				
					temp.setEntryState(es);
		    	}
				else if (obj instanceof PickListEntryNodeImpl)
		    	{
					PickListEntryNodeImpl temp = (PickListEntryNodeImpl) obj;
					String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
					if (owner != null)
					{
						Source src = CommontypesFactory.eINSTANCE.createSource();
						src.setValue(owner);
						temp.setOwner(src);
					}
					
					temp.setStatus(results.getString(SQLTableConstants.TBLCOL_STATUS));
					temp.setEffectiveDate(results.getDate(SQLTableConstants.TBLCOL_EFFECTIVEDATE));
					temp.setExpirationDate(results.getDate(SQLTableConstants.TBLCOL_EXPIRATIONDATE));
					
					EntryState es = VersionsFactory.eINSTANCE.createEntryState();
					
					es.setContainingRevision(revisionId);
					es.setPrevRevision(prevRevisionId);
					es.setRelativeOrder(relativeOrder);
					es.setChangeType(ChangeType.get(changeType));				
					temp.setEntryState(es);
		    	}
			}
			
			results.close();
			// don't need to hold these open while we do the expensive part
			checkInPreparedStatement(selectFromEntryState);
			
		}
		catch (SQLException e)
		{
			throw new FindException(e);
		}
		finally
		{
			checkInPreparedStatement(selectFromEntryState);
		}
	}
    
    public void remove(ArrayList<Integer> entryStateIds, String entryType) throws ObjectNotFoundException, SQLException
	{
    	if (entryStateIds.size() == 0)
    		return;
    	
		PreparedStatement deleteFromEntryState = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("DELETE FROM " + VSDConstants.TBL_ENTRY_STATE);
		sb.append(" WHERE " + SQLTableConstants.TBLCOL_ENTRYTYPE + " = ? ");
		sb.append(" AND " + SQLTableConstants.TBLCOL_ENTRYSTATEID + " IN (");
		
		Iterator<Integer> itr = entryStateIds.iterator();
		for (;itr.hasNext();itr.next())
		{
			sb.append("?,");
		}
		
		sb.delete(sb.lastIndexOf(","), sb.length());
		sb.append(")");
		
		try
		{
			deleteFromEntryState = checkOutPreparedStatement(modifySql(sb.toString()));
			
			int i = 1;
			deleteFromEntryState.setString(i++, entryType);
			for(int entryId : entryStateIds)
			{
				deleteFromEntryState.setInt(i++, entryId);
			}
			
			deleteFromEntryState.executeUpdate();
			
		}
		finally
		{
			checkInPreparedStatement(deleteFromEntryState);
		}
	}
    
    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.managedobj.service.BaseService#getInstanceClass()
     */
    protected Class getInstanceClass()
    {
        return VSDEntryStateService.class;
    }
}