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
package org.lexgrid.valuedomain.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.LexGrid.managedobj.FindException;
import org.LexGrid.managedobj.InsertException;
import org.LexGrid.managedobj.ManagedObjIF;
import org.LexGrid.managedobj.ObjectAlreadyExistsException;
import org.LexGrid.managedobj.ObjectNotFoundException;
import org.LexGrid.managedobj.ServiceInitException;
import org.LexGrid.managedobj.UpdateException;
import org.LexGrid.managedobj.jdbc.JDBCBaseService;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;

/**
 * <pre>
 *        Title:        EntryTypeService.java
 *        Description:  Class that handles EntryType to the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VDEntryTypeServices extends VDBaseService
{
	
    public VDEntryTypeServices(JDBCBaseService anchorService) throws ServiceInitException
    {
        super(anchorService);
    }
	
	
    public void insert(ManagedObjIF obj, int entryId) throws InsertException, ObjectAlreadyExistsException
	{
    	insert(getEntryType(obj), entryId);
	}
    
    public void insert(String entryType, int entryId) throws InsertException, ObjectAlreadyExistsException
	{
    	PreparedStatement insertIntoEntryState = null;
		try
		{
			insertIntoEntryState = getKeyedInsertStatement(VDConstants.TBL_ENTRY_TYPE);
				
			int k = 1;
			insertIntoEntryState.setInt(k++, entryId);
			insertIntoEntryState.setString(k++, entryType);	
			
			insertIntoEntryState.executeUpdate();
			insertIntoEntryState.clearParameters();
		}
		catch (SQLException e)
		{
			throw new ObjectAlreadyExistsException("Problem loading EntryType table ",  e);
		}
		catch (Exception e)
		{
			throw new InsertException("Problem loading EntryType table ",  e);
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
    
    public String resolveEntryTypeFor(int entryId) throws FindException
	{
    	String entryType = null;
    	
		PreparedStatement selectFromEntryType = null;

		try
		{
			selectFromEntryType = checkOutPreparedStatement(modifySql(
					" SELECT " + SQLTableConstants.TBLCOL_ENTRYTYPE +
					" FROM " + VDConstants.TBL_ENTRY_TYPE + 
					" WHERE " + SQLTableConstants.TBLCOL_REFERENCEENTRYID + " = ?"));
			
			selectFromEntryType.setInt(1, entryId);
			ResultSet results = selectFromEntryType.executeQuery();

			if (results.next())
			{
				entryType = results.getString(SQLTableConstants.TBLCOL_REVISIONID);
			}
			
			results.close();
			// Maybe we need to open at the beginning of the load and close at the end TODO
			checkInPreparedStatement(selectFromEntryType);
			
			return entryType;
		}
		catch (SQLException e)
		{
			throw new FindException(e);
		}
		finally
		{
			checkInPreparedStatement(selectFromEntryType);
		}
	}
    
    public void update(ManagedObjIF obj) throws UpdateException, ObjectNotFoundException
	{
		// TODO: Implement update
		throw new java.lang.UnsupportedOperationException("Method update not yet implemented.");
	}
	
	public void remove(ArrayList<Integer> entryIds, String entryType) throws ObjectNotFoundException
	{
		if (entryIds.size() == 0)
			return;
		
		PreparedStatement deleteFromEntryType = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("DELETE FROM " + VDConstants.TBL_ENTRY_TYPE);
		sb.append(" WHERE " + SQLTableConstants.TBLCOL_ENTRYTYPE + " = ? ");
		sb.append(" AND " + SQLTableConstants.TBLCOL_REFERENCEENTRYID + " IN (");
		
		Iterator<Integer> itr = entryIds.iterator();
		for (;itr.hasNext();itr.next())
		{
			sb.append("?,");
		}
		
		sb.delete(sb.lastIndexOf(","), sb.length());
		sb.append(")");
		
		try
		{
			deleteFromEntryType = checkOutPreparedStatement(modifySql(sb.toString()));
			
			int i = 1;
			deleteFromEntryType.setString(i++, entryType);
			for(int entryId : entryIds)
			{
				deleteFromEntryType.setInt(i++, entryId);
			}
			
			deleteFromEntryType.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			checkInPreparedStatement(deleteFromEntryType);
		}
	}
	
    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.managedobj.service.BaseService#getInstanceClass()
     */
    protected Class getInstanceClass()
    {
        return VDEntryTypeServices.class;
    }
}