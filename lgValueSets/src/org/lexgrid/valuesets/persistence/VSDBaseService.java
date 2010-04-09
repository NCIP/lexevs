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

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.emf.base.LgUnsupportedConstraintException;
import org.LexGrid.emf.commonTypes.CommontypesFactory;
import org.LexGrid.emf.commonTypes.Properties;
import org.LexGrid.emf.commonTypes.Property;
import org.LexGrid.emf.commonTypes.impl.PropertyImpl;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.naming.impl.MappingsImpl;
import org.LexGrid.emf.valueDomains.PickListDefinition;
import org.LexGrid.emf.valueDomains.PickListEntryNode;
import org.LexGrid.emf.valueDomains.ValueDomainDefinition;
import org.LexGrid.emf.valueDomains.impl.DefinitionEntryImpl;
import org.LexGrid.emf.valueDomains.impl.PickListDefinitionImpl;
import org.LexGrid.emf.valueDomains.impl.PickListEntryImpl;
import org.LexGrid.emf.valueDomains.impl.PickListEntryNodeImpl;
import org.LexGrid.emf.valueDomains.impl.ValueDomainDefinitionImpl;
import org.LexGrid.managedobj.HomeServiceBroker;
import org.LexGrid.managedobj.ManagedObjIF;
import org.LexGrid.managedobj.ServiceInitException;
import org.LexGrid.managedobj.jdbc.JDBCBaseService;
import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.LexGrid.persistence.sql.LGBaseService;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lexgrid.valuesets.helper.VSDConstants;

import edu.mayo.informatics.lexgrid.convert.utility.MessageRedirector;

/**
 * <pre>
 * 
 *  Title:        BaseService.java
 *  Description:  Base service class to handles all ValueDomain and pick list related objects to and from the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VSDBaseService extends LGBaseService
{
	private static Logger log = Logger.getLogger("convert.SQL");
	
	private VSDBaseSQLServices baseSQLServ_;
	
	private static int highEntryId_ = -999;
	private static int entryId_ = 1000;
	
	public VSDBaseService(JDBCBaseService anchorService) throws ServiceInitException
    {
        super(anchorService);
        try {
			baseSQLServ_ = new VSDBaseSQLServices(getConnectionPool(), "");
			if (!baseSQLServ_.doValueDomainTablesExist())
			{
				baseSQLServ_.createValueDomainTables();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public VSDBaseService(HomeServiceBroker broker, JDBCConnectionDescriptor desc,
			boolean stagedRetrievalEnabled, String tablePrefix, boolean failOnAllErrors,
			MessageRedirector messageRedirector, boolean createCSTables) throws ServiceInitException {
		super(broker, desc, stagedRetrievalEnabled, tablePrefix, createCSTables);
		try {
			baseSQLServ_ = new VSDBaseSQLServices(getConnectionPool(), "");
			if (!baseSQLServ_.doValueDomainTablesExist())
			{
				baseSQLServ_.createValueDomainTables();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setConnectionDescriptor(JDBCConnectionDescriptor desc){
		desc.setAutoCommit(true);
		super.setConnectionDescriptor(desc);
	}
	
	public void setBroker(HomeServiceBroker broker){
		super.setBroker(broker);
	}
	
	/**
	 * Sets the EMF container to be applied to resolved objects, optionally
	 * adding the model to the contents of the container when resolved.
	 * @param container EObject
	 * @param containerFeature EStructuralFeature
	 */
	protected void setEContext(EObject container, EStructuralFeature containerFeature) {
		super.setEContext(container, containerFeature);
	}
	
	@Override
	protected PreparedStatement getKeyedInsertStatement(String key) throws Exception
	{
		String statement = baseSQLServ_.getInsertStatementSQL(key);
		Connection insertConn = getWriteConnection();
		return insertConn.prepareStatement(statement);
	}
	
	public int getNextEntryId() throws LBException, Exception{
		synchronized (VSDBaseService.class) {			
			if (entryId_ >= highEntryId_)
			{
				entryId_ = baseSQLServ_.getCurrentEntryId();
				if (entryId_ == 0)
				{
					entryId_ = VSDConstants.ENTRYID_INCREMENT;
					highEntryId_ = entryId_ + VSDConstants.ENTRYID_INCREMENT;
					baseSQLServ_.insertEntryId(highEntryId_);
				}
				else
				{
					highEntryId_ = entryId_ + VSDConstants.ENTRYID_INCREMENT;
					baseSQLServ_.incrementEntryId(highEntryId_);
				}
			}
		}
		return ++entryId_;
	}
	
	public String getEntryType(ManagedObjIF obj){
    	if (obj instanceof ValueDomainDefinitionImpl)
		{
        	return SQLTableConstants.ENTRY_STATE_TYPE_VALUEDOMAIN;
		}
        else if (obj instanceof DefinitionEntryImpl)
		{
        	return SQLTableConstants.ENTRY_STATE_TYPE_VALUEDOMAINENTRY;
		}
        else if (obj instanceof PickListDefinitionImpl)
		{
        	return SQLTableConstants.ENTRY_STATE_TYPE_PICKLIST;
		}
        else if (obj instanceof PickListEntryNodeImpl)
		{
        	return SQLTableConstants.ENTRY_STATE_TYPE_PICKLISTENTRY;
		}
        else if (obj instanceof PickListEntryImpl)
		{
        	return SQLTableConstants.ENTRY_STATE_TYPE_PICKLISTENTRY;
		}
        else if (obj instanceof PropertyImpl)
		{
        	return SQLTableConstants.ENTRY_STATE_TYPE_PROPERTY;
		}
        else if (obj instanceof MappingsImpl)
		{
        	return SQLTableConstants.ENTRY_STATE_TYPE_MAPPING;
		}
        else
        {
        	throw new LgUnsupportedConstraintException("Unsupported object passed.");
        }        
    }
	
	public void addPropertyToObject(ManagedObjIF obj, Property currProperty){
    	if (obj instanceof ValueDomainDefinitionImpl)
		{
    		if (((ValueDomainDefinition) obj).getProperties() == null)
    		{
    			Properties props = CommontypesFactory.eINSTANCE.createProperties();
    			((ValueDomainDefinition) getEContainer()).setProperties(props);
    		}
    		((ValueDomainDefinition) getEContainer()).getProperties().getProperty().add(currProperty);
		}
        else if (obj instanceof PickListDefinitionImpl)
		{
        	if (((PickListDefinitionImpl) obj).getProperties() == null)
    		{
    			Properties props = CommontypesFactory.eINSTANCE.createProperties();
    			((PickListDefinition) getEContainer()).setProperties(props);
    		}
        	((PickListDefinition) getEContainer()).getProperties().getProperty().add(currProperty);
		}
        else if (obj instanceof PickListEntryNodeImpl)
		{
        	if (((PickListEntryNodeImpl) obj).getProperties() == null)
    		{
    			Properties props = CommontypesFactory.eINSTANCE.createProperties();
    			((PickListEntryNode) getEContainer()).setProperties(props);
    		}
        	((PickListEntryNode) getEContainer()).getProperties().getProperty().add(currProperty);
		}
        else
        {
        	throw new LgUnsupportedConstraintException("Unsupported object passed.");
        }        
    }
	
	public void addMappingsToObject(ManagedObjIF obj, Mappings maps){
    	if (obj instanceof ValueDomainDefinitionImpl)
		{
    		((ValueDomainDefinition) getEContainer()).setMappings(maps);
		}
        else if (obj instanceof PickListDefinitionImpl)
		{
        	((PickListDefinition) getEContainer()).setMappings(maps);
		}
        else
        {
        	throw new LgUnsupportedConstraintException("Unsupported object passed.");
        }        
    }
    
    VSDBaseSQLServices getVDBaseSQLService(){
    	return baseSQLServ_;
    }
}