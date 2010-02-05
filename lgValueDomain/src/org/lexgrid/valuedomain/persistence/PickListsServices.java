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

import org.LexGrid.emf.base.LgModelObj;
import org.LexGrid.emf.base.LgStagedObj;
import org.LexGrid.emf.valueDomains.impl.PickListsImpl;
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
import org.eclipse.emf.ecore.EStructuralFeature;

import edu.mayo.informatics.lexgrid.convert.utility.MessageRedirector;

/**
 * <pre>
 *        Title:        PickListsService.java
 *        Description:  Class that handles Pick Lists to the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class PickListsServices extends VDBaseService {

	private PickListServices plServices_;
	private VDMappingServices mappingServices_;
	private SystemReleaseServices systemReleaseServices_;

	protected PickListsServices(JDBCBaseService anchorService)
			throws ServiceInitException {
		super(anchorService);
	}

	public PickListsServices(HomeServiceBroker broker, JDBCConnectionDescriptor desc,
			boolean b, String tablePrefix, boolean failOnAllErrors,
			MessageRedirector messageRedirector, boolean createCSTables)
			throws ServiceInitException {
		super(broker, desc, b, tablePrefix, failOnAllErrors, messageRedirector,
				createCSTables);
		SingletonVariables.instance(getConnectionDescriptor().toString()).messages = messageRedirector;
		SingletonVariables.instance(getConnectionDescriptor().toString()).failOnAllErrors = failOnAllErrors;
	}

	protected ManagedObjIF findByPrimaryKeyPrim(Object key)
			throws FindException {
		throw new java.lang.UnsupportedOperationException(
		"Method findByPrimaryKeyPrim not yet implemented.");
	}

	public void insert(ManagedObjIF obj) throws InsertException,
			ObjectAlreadyExistsException {
		throw new java.lang.UnsupportedOperationException(
		"Method insert not yet implemented.");
	}

	protected Class<PickListsImpl> getInstanceClass() {
		return PickListsImpl.class;
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
		plServices_ = new PickListServices(this);
		mappingServices_ = new VDMappingServices(this);
		systemReleaseServices_ = new SystemReleaseServices(this);
	}

	public void stageFeature(LgStagedObj obj, EStructuralFeature feature)
			throws ResolveException {
		
	}

	public void update(ManagedObjIF obj) throws UpdateException,
			ObjectNotFoundException {
		// TODO: Implement update
		throw new java.lang.UnsupportedOperationException(
				"Method update not yet implemented.");
	}

	public void remove(ManagedObjIF obj) throws ObjectNotFoundException {
		// TODO: Implement update
		throw new java.lang.UnsupportedOperationException(
				"Method remove not yet implemented.");
	}
}