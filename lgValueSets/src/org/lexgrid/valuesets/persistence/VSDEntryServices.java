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
import org.LexGrid.emf.valueDomains.CodingSchemeReference;
import org.LexGrid.emf.valueDomains.DefinitionEntry;
import org.LexGrid.emf.valueDomains.EntityReference;
import org.LexGrid.emf.valueDomains.ValueDomainDefinition;
import org.LexGrid.emf.valueDomains.ValueDomainReference;
import org.LexGrid.emf.valueDomains.ValuedomainsFactory;
import org.LexGrid.emf.valueDomains.impl.DefinitionEntryImpl;
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

import edu.mayo.informatics.lexgrid.convert.utility.MessageRedirector;

/**
 * <pre>
 *        Title:        VDEntryService.java
 *        Description:  Class that handles Value Domain DefinitionEntry to the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VSDEntryServices extends VSDBaseService{
	
	private VSDEntryTypeServices entryTypeServices_;
	
	protected VSDEntryServices(JDBCBaseService anchorService)
			throws ServiceInitException {
		super(anchorService);
	}
	
	public VSDEntryServices(HomeServiceBroker broker, JDBCConnectionDescriptor desc,
			boolean b, String tablePrefix, boolean failOnAllErrors,
			MessageRedirector messageRedirector, boolean createCSTables) throws ServiceInitException {
		super(broker, desc, b, tablePrefix, failOnAllErrors, messageRedirector, createCSTables);
		SingletonVariables.instance(getConnectionDescriptor().toString()).messages = messageRedirector;
		SingletonVariables.instance(getConnectionDescriptor().toString()).failOnAllErrors = failOnAllErrors;
	}
	
	public void resolve(Object key) throws FindException
	{
		PreparedStatement selectFromVDEntry = null;
		DefinitionEntry vdEntry = null;
		
		try
		{
			Integer foreignEntryId = (Integer) key;

			selectFromVDEntry = checkOutPreparedStatement(
					" SELECT * " +
					" FROM " + VSDConstants.TBL_VD_ENTRY + 
					" WHERE " + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? " +
					" ORDER BY " + SQLTableConstants.TBLCOL_RULEORDER);
			
			selectFromVDEntry.setInt(1, foreignEntryId);

			ResultSet results = selectFromVDEntry.executeQuery();
			
			while (results.next())
			{
				int entryId = results.getInt(SQLTableConstants.TBLCOL_ENTRYID);
				String entityCode = results.getString(SQLTableConstants.TBLCOL_ENTITYCODE);
				String entityCodeNamespace = results.getString(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE);				
				int ruleOrder = results.getInt(SQLTableConstants.TBLCOL_RULEORDER);
				String operator = results.getString(SQLTableConstants.TBLCOL_OPERATOR);
				String csRef = results.getString(SQLTableConstants.TBLCOL_CODINGSCHEMEREFERENCE);
				String vdRef = results.getString(SQLTableConstants.TBLCOL_VALUEDOMAINREFERENCE);
				Boolean leafOnly = getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_LEAFONLY);
				String refAssns = results.getString(SQLTableConstants.TBLCOL_REFERENCEASSOCIATION);
				Boolean targetToSource = getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_TARGETTOSOURCE);
				Boolean transitiveClosure = getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_TRANSITIVECLOSURE);				
				
				vdEntry = ValuedomainsFactory.eINSTANCE.createDefinitionEntry();
				
				vdEntry.setRuleOrder(ruleOrder);
				
				if (!StringUtils.isEmpty(csRef))
				{
					CodingSchemeReference temp = ValuedomainsFactory.eINSTANCE.createCodingSchemeReference();
					temp.setCodingScheme(csRef);
					vdEntry.setCodingSchemeReference(temp);
				}
				
				if (!StringUtils.isEmpty(vdRef))
				{
					ValueDomainReference temp = ValuedomainsFactory.eINSTANCE.createValueDomainReference();
					temp.setValueDomainURI(vdRef);
					vdEntry.setValueDomainReference(temp);
				}
				
				if (!StringUtils.isEmpty(operator))
				{
					vdEntry.setOperator(org.LexGrid.emf.valueDomains.DefinitionOperator.get(operator));
				}
				
				EntityReference entityRef = ValuedomainsFactory.eINSTANCE.createEntityReference();
				entityRef.setEntityCode(entityCode);
				entityRef.setEntityCodeNamespace(entityCodeNamespace);
				if (leafOnly != null)
					entityRef.setLeafOnly(leafOnly);
				entityRef.setReferenceAssociation(refAssns);
				if (targetToSource != null)
					entityRef.setTargetToSource(targetToSource);
				if (transitiveClosure != null)
					entityRef.setTransitiveClosure(transitiveClosure);
				
				vdEntry.setEntityReference(entityRef);	
				
				((ValueDomainDefinition) getEContainer()).getDefinitionEntry().add(vdEntry);
			}
			results.close();
		}
		catch (Exception e)
		{
			throw new FindException("Problem", e);
		}
		finally
		{
			checkInPreparedStatement(selectFromVDEntry);
		}
	}

	public void insert(ManagedObjIF obj, int foreignEntryId) throws LBException, Exception
	{
		DefinitionEntry de = (DefinitionEntry) (obj);
		int entryId = getNextEntryId();
		insertEntryType(de, entryId);

		PreparedStatement insertIntoVDEntryn = null;
		try
		{
			String entityCode = "";
			String entityCodeNamespace = null;
			Boolean isLeafOnly = null;
			Boolean isTargetToSource = null;
			Boolean isTransitiveClosure = null;
			String refAssns = null;
			
			insertIntoVDEntryn = getKeyedInsertStatement(VSDConstants.TBL_VD_ENTRY);
			
			if (de.getEntityReference() != null)
			{
				entityCode = de.getEntityReference() == null ? null : de.getEntityReference().getEntityCode();
				entityCodeNamespace = de.getEntityReference() == null ? null : de.getEntityReference().getEntityCodeNamespace();				
				isLeafOnly = de.getEntityReference() == null ? null : de.getEntityReference().isLeafOnly();
				refAssns = de.getEntityReference() == null ? null : de.getEntityReference().getReferenceAssociation();
				isTargetToSource = de.getEntityReference() == null ? null : de.getEntityReference().isTargetToSource();
				isTransitiveClosure = de.getEntityReference() == null ? null : de.getEntityReference().isTransitiveClosure();
			}
			
			int ruleOrder = StringUtils.isEmpty(String.valueOf(de.getRuleOrder())) ? 1 : de.getRuleOrder();
			String operator = de.getOperator() == null ? org.LexGrid.valueSets.types.DefinitionOperator.OR.toString() : de.getOperator().getLiteral();
			String csRef = de.getCodingSchemeReference() == null ? null : de.getCodingSchemeReference().getCodingScheme();
			String vdRef = de.getValueDomainReference() == null ? null : de.getValueDomainReference().getValueDomainURI();
			
			
			int i = 1;
			insertIntoVDEntryn.setInt(i++, entryId);
			insertIntoVDEntryn.setInt(i++, foreignEntryId);
			insertIntoVDEntryn.setString(i++, entityCode);            
			insertIntoVDEntryn.setString(i++, entityCodeNamespace);
			insertIntoVDEntryn.setInt(i++, ruleOrder);			
			insertIntoVDEntryn.setString(i++, operator);
			insertIntoVDEntryn.setString(i++, csRef);
			insertIntoVDEntryn.setString(i++, vdRef);           
			setBooleanOnPreparedStatment(insertIntoVDEntryn, i++, isLeafOnly);
			insertIntoVDEntryn.setString(i++, refAssns);
			setBooleanOnPreparedStatment(insertIntoVDEntryn, i++, isTargetToSource);
			setBooleanOnPreparedStatment(insertIntoVDEntryn, i++, isTransitiveClosure);
			
			insertIntoVDEntryn.executeUpdate();
			insertIntoVDEntryn.clearParameters();
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
				if (insertIntoVDEntryn != null)
					insertIntoVDEntryn.close();
			}
			catch (Exception e)
			{
				// do nothing
			}
		}
	}

	protected Class getInstanceClass()
	{
		return DefinitionEntryImpl.class;
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
			if (obj instanceof ValueDomainDefinition)
			{
				//mark everything as complete
				Iterator iterator = ((ValueDomainDefinition)obj).eClass().getEAllStructuralFeatures().iterator();
				while (iterator.hasNext())
				{
					EStructuralFeature temp = (EStructuralFeature)iterator.next();
					obj.stageComplete(temp);
				}

				obj.stageComplete(feature);

				//I have to call this after setting everything to feature complete.
				//this loads all other missing features.
//				getSubProperties((ValueDomainDefinition)obj);
			}
		}
		catch (Exception e)
		{
			throw new ResolveException("Problem getting the sub-details for the coding scheme", e);
		}
	}

	protected void initNestedServices() throws ServiceInitException
    {
        entryTypeServices_ = new VSDEntryTypeServices(this);
    }
    
	public void update(ManagedObjIF obj) throws UpdateException, ObjectNotFoundException
	{
		// TODO: Implement update
		throw new java.lang.UnsupportedOperationException("Method update not yet implemented.");
	}
	
	public void remove(int foreignEntryId) throws ObjectNotFoundException, SQLException
	{
		PreparedStatement deleteFromVDEntry = null;
		PreparedStatement selectFromVDEntry = null;
		ArrayList<Integer> entryIds = new ArrayList<Integer>();
		
		try
		{
			selectFromVDEntry = checkOutPreparedStatement(
					" SELECT " + SQLTableConstants.TBLCOL_ENTRYID +
					" FROM " + VSDConstants.TBL_VD_ENTRY + 
					" WHERE " + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? ");
			
			selectFromVDEntry.setInt(1, foreignEntryId);
	
			ResultSet results = selectFromVDEntry.executeQuery();
			while (results.next())
			{
				entryIds.add(results.getInt(SQLTableConstants.TBLCOL_ENTRYID));
			}
			results.close();
		} finally
		{
			checkInPreparedStatement(selectFromVDEntry);
		}
		
		try
		{
			deleteFromVDEntry = checkOutPreparedStatement(
					" DELETE FROM " + VSDConstants.TBL_VD_ENTRY + 
					" WHERE " + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? ");
			
			deleteFromVDEntry.setInt(1, foreignEntryId);

			deleteFromVDEntry.executeUpdate();
		}
		finally
		{
			checkInPreparedStatement(deleteFromVDEntry);
		}
		
		entryTypeServices_.remove(entryIds, SQLTableConstants.ENTRY_STATE_TYPE_VALUEDOMAINENTRY);
		
		entryIds.clear();
	}
	
    protected void insertEntryType(DefinitionEntry de, int entryId) throws InsertException, ObjectAlreadyExistsException
	{
    	entryTypeServices_.insert(de, entryId);
	}
    
    protected String resolveEntryType(int entryId) throws FindException
	{
    	return entryTypeServices_.resolveEntryTypeFor(entryId);
	}	
}