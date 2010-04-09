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

import org.LexGrid.emf.base.LgModelObj;
import org.LexGrid.emf.base.LgStagedObj;
import org.LexGrid.emf.valueDomains.ValueDomainDefinition;
import org.LexGrid.emf.valueDomains.ValueDomains;
import org.LexGrid.emf.valueDomains.ValuedomainsFactory;
import org.LexGrid.emf.valueDomains.ValuedomainsPackage;
import org.LexGrid.emf.valueDomains.impl.ValueDomainsImpl;
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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lexgrid.valuesets.helper.VSDConstants;

import edu.mayo.informatics.lexgrid.convert.utility.MessageRedirector;

/**
 * <pre>
 *        Title:        VDSServices.java
 *        Description:  Class that handles ValueDomains to the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VSDSServices extends VSDBaseService {

	private VSDServices vdServices_;
	private VSDMappingServices mappingServices_;

	protected VSDSServices(JDBCBaseService anchorService)
			throws ServiceInitException {
		super(anchorService);
	}

	public VSDSServices(HomeServiceBroker broker, JDBCConnectionDescriptor desc,
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
		ValueDomains valueDomains = ValuedomainsFactory.eINSTANCE
				.createValueDomains();

		PreparedStatement getValueDomains = null;
		try {
			getValueDomains = checkOutPreparedStatement(" SELECT "
					+ SQLTableConstants.TBLCOL_VALUEDOMAINURI + " FROM "
					+ VSDConstants.TBL_VALUE_DOMAIN);

			ResultSet results = getValueDomains.executeQuery();

			// put the results in an array list so we don't have to hold this
			// result set
			// open for so long.
			ArrayList valueDomainURIs = new ArrayList();
			while (results.next()) {
				valueDomainURIs.add(results
						.getString(SQLTableConstants.TBLCOL_VALUEDOMAINURI));
			}
			checkInPreparedStatement(getValueDomains);

			for (int i = 0; i < valueDomainURIs.size(); i++) {
				ValueDomainDefinition valueDomain = (ValueDomainDefinition) vdServices_
						.findByPrimaryKey(valueDomainURIs.get(i));
				valueDomains.getValueDomainDefinition().add(valueDomain);
			}
			
			mappingServices_.setEContext(valueDomains, null);
			mappingServices_.resolveAll();
			
		} catch (SQLException e) {
			throw new FindException(e);
		} finally {
			checkInPreparedStatement(getValueDomains);
		}
		return valueDomains;
	}

	public void insert(ManagedObjIF obj, String systemReleaseURI) throws InsertException,
			ObjectAlreadyExistsException {
		try {
			ValueDomains vds = (ValueDomains) (obj);
			vdServices_.setEContext(vds, ValuedomainsPackage.eINSTANCE.getValueDomains_ValueDomainDefinition());

			Iterator<ValueDomainDefinition> vdIter = vds.getValueDomainDefinition().iterator();
			while (vdIter.hasNext()) {
				ValueDomainDefinition temp = (ValueDomainDefinition) vdIter
						.next();
				vdServices_.insert(temp, systemReleaseURI, null);
			}
			mappingServices_.insert(obj, -1);
			
		} catch (InsertException e) {
			throw e;
		} catch (ObjectAlreadyExistsException e) {
			throw e;
		} catch (Exception e) {
			throw new InsertException(e);
		}
	}

	protected Class<ValueDomainsImpl> getInstanceClass() {
		return ValueDomainsImpl.class;
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
		vdServices_ = new VSDServices(this);
		mappingServices_ = new VSDMappingServices(this);
	}

	public void stageFeature(LgStagedObj obj, EStructuralFeature feature)
			throws ResolveException {
		try {
			if (obj instanceof ValueDomainDefinition) {
				// mark everything as complete
				Iterator iterator = ((ValueDomainDefinition) obj).eClass()
						.getEAllStructuralFeatures().iterator();
				while (iterator.hasNext()) {
					EStructuralFeature temp = (EStructuralFeature) iterator
							.next();
					obj.stageComplete(temp);
				}

				obj.stageComplete(feature);

				// I have to call this after setting everything to feature
				// complete.
				// this loads all other missing features.
				// getSubProperties((ValueDomainDefinition)obj);
			}
		} catch (Exception e) {
			throw new ResolveException(
					"Problem getting the sub-details for the coding scheme", e);
		}
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