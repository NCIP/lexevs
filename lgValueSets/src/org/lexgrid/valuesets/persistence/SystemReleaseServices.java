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
import java.sql.Timestamp;
import java.util.Iterator;

import org.LexGrid.emf.base.LgModelObj;
import org.LexGrid.emf.base.LgStagedObj;
import org.LexGrid.emf.versions.SystemRelease;
import org.LexGrid.emf.versions.VersionsFactory;
import org.LexGrid.emf.versions.impl.SystemReleaseImpl;
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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lexgrid.valuesets.helper.VSDConstants;

import edu.mayo.informatics.lexgrid.convert.utility.MessageRedirector;

/**
 * <pre>
 *        Title:        SystemReleaseServices.java
 *        Description:  Class that handles SystemRelease to the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class SystemReleaseServices extends VSDBaseService {

//	private VDServices vdServices_;
//	private PickListServices plServices_;
	
	public SystemReleaseServices(JDBCBaseService anchorService)
			throws ServiceInitException {
		super(anchorService);
	}

	public SystemReleaseServices(HomeServiceBroker broker, JDBCConnectionDescriptor desc,
			boolean b, String tablePrefix, boolean failOnAllErrors,
			MessageRedirector messageRedirector, boolean createCSTables)
			throws ServiceInitException {
		super(broker, desc, b, tablePrefix, failOnAllErrors, messageRedirector,
				createCSTables);
		SingletonVariables.instance(getConnectionDescriptor().toString()).messages = messageRedirector;
		SingletonVariables.instance(getConnectionDescriptor().toString()).failOnAllErrors = failOnAllErrors;
	}

	public ManagedObjIF findByPrimaryKey(Object key) throws FindException {
		SystemRelease systemRelease = VersionsFactory.eINSTANCE.createSystemRelease();
		systemRelease.setContainer(getEContainer(), getEContainmentFeature(), true);

		PreparedStatement selectFromSystemRelease = null;

		try {
			URI sysRelURI = (URI) key;

			selectFromSystemRelease = checkOutPreparedStatement(" SELECT * "
					+ " FROM " + VSDConstants.TBL_SYSTEM_RELEASE + " WHERE "
					+ SQLTableConstants.TBLCOL_RELEASEURI + " = ?");

			selectFromSystemRelease.setString(1, sysRelURI.toString());

			ResultSet results = selectFromSystemRelease.executeQuery();

			if (results.next()) {
				systemRelease.setReleaseURI(sysRelURI.toString());
				systemRelease.setReleaseDate(results.getDate(SQLTableConstants.TBLCOL_RELEASEDATE));
				systemRelease.setReleaseId(results.getString(SQLTableConstants.TBLCOL_RELEASEID));
				systemRelease.setReleaseAgency(results.getString(SQLTableConstants.TBLCOL_RELEASEAGENCY));
				systemRelease.setBasedOnRelease(results.getString(SQLTableConstants.TBLCOL_BASEDONRELEASE));
				systemRelease.setEntityDescription(results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));

				results.close();
			} else {
				throw new FindException("Could not find system release for uri : " + sysRelURI);
			}
			
			//TODO following code to use when using SystemRelease as an entrypoint
//			vdServices_.setEContext(systemRelease, getEContainmentFeature());
//			vdServices_.resolveForReleaseURI(sysRelURI);
//			
//			plServices_.setEContext(systemRelease, getEContainmentFeature());
//			plServices_.resolveForReleaseURI(sysRelURI);

			return systemRelease;
		} catch (FindException e) {
			throw e;
		} catch (Exception e) {
			throw new FindException("Problem", e);
		} finally {
			checkInPreparedStatement(selectFromSystemRelease);
		}
	}

	public void insert(ManagedObjIF obj) throws InsertException,
			ObjectAlreadyExistsException {
		SystemRelease systemRelease = (SystemRelease) (obj);
		
		boolean ignoreRelease = false;
		
		try {
			PreparedStatement insertIntoSystemRelease = null;
			try {
				insertIntoSystemRelease = getKeyedInsertStatement(VSDConstants.TBL_SYSTEM_RELEASE);
				int i = 1;
				insertIntoSystemRelease.setString(i++, systemRelease.getReleaseURI());
				Timestamp relDate = null;
				if (systemRelease.getReleaseDate() != null)
					relDate = new Timestamp(systemRelease.getReleaseDate().getTime());
				insertIntoSystemRelease.setTimestamp(i++, relDate);
				insertIntoSystemRelease.setString(i++, systemRelease.getReleaseId());
				insertIntoSystemRelease.setString(i++, systemRelease.getReleaseAgency());
				insertIntoSystemRelease.setString(i++, systemRelease.getBasedOnRelease());
				insertIntoSystemRelease.setString(i++, systemRelease.getEntityDescription());

				insertIntoSystemRelease.executeUpdate();
				insertIntoSystemRelease.clearParameters();
			} catch (SQLException e) {
				if ((e.getMessage().indexOf("Duplicate entry") != -1)
						|| (e.getMessage().indexOf("unique constraint") != -1)
						|| (e.getMessage().indexOf("SQLCODE: -803") != -1)) {
					// print error message and continue
					String msg = "SystemRelease already exists with URI "
							+ systemRelease.getReleaseURI() + ". Error message="
							+ e.getMessage();
					System.out.println(msg);
					if (SingletonVariables.instance(getConnectionDescriptor()
							.toString()).messages != null) {
						SingletonVariables.instance(getConnectionDescriptor()
								.toString()).messages.message(msg);
					}
					ignoreRelease = true;
				}
			} catch (Exception e) {
				throw new InsertException(e);
			} finally {
				try {
					if (insertIntoSystemRelease != null)
						insertIntoSystemRelease.close();
				} catch (Exception e) {
					// do nothing
				}
			}
			
			//Following code to use when using SystemRelease as an entry point.
//			if (!ignoreRelease)
//			{
//				if (systemRelease.getValueDomains() != null && systemRelease.getValueDomains().getValueDomainDefinition() != null) {
//					Iterator<ValueDomainDefinition> iter = systemRelease.getValueDomains().getValueDomainDefinition().iterator();
//					while (iter.hasNext()) {
//						vdServices_.insert((ValueDomainDefinition) iter.next(), systemReleaseURI);
//					}
//				}
//				
//				if (systemRelease.getPickLists() != null && systemRelease.getPickLists().getPickListDefinition() != null) {
//					Iterator<PickListDefinition> iter = systemRelease.getPickLists().getPickListDefinition().iterator();
//					while (iter.hasNext()) {
//						plServices_.insert((PickListDefinition) iter.next(), systemReleaseURI);
//					}
//				}
//			}
		} catch (InsertException e) {
			throw e;
		} catch (Exception e) {
			throw new InsertException(e);
		}
	}

	protected Class<SystemReleaseImpl> getInstanceClass() {
		return SystemReleaseImpl.class;
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
//		vdServices_ = new VDServices(this);
//		plServices_ = new PickListServices(this);
	}

	public void stageFeature(LgStagedObj obj, EStructuralFeature feature)
			throws ResolveException {
		try {
			if (obj instanceof SystemRelease) {
				// mark everything as complete
				Iterator iterator = ((SystemRelease) obj).eClass()
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
					"Problem getting the sub-details for the system release", e);
		}
	}

	public void update(ManagedObjIF obj) throws UpdateException,
			ObjectNotFoundException {
		// TODO: Implement update
		throw new java.lang.UnsupportedOperationException(
				"Method update not yet implemented.");
	}

	public void remove(Object key) throws RemoveException, FindException {
		
		PreparedStatement deleteFromSystemRelease = null;
		URI systemReleaseURI = (URI) key;
		
		try {
			
			deleteFromSystemRelease = checkOutPreparedStatement(" DELETE FROM  "
					+ VSDConstants.TBL_SYSTEM_RELEASE + " WHERE "
					+ SQLTableConstants.TBLCOL_RELEASEURI + " = ?");

			deleteFromSystemRelease.setString(1, systemReleaseURI.toString());

			deleteFromSystemRelease.executeUpdate();
			
		} catch (SQLException e) {
			throw new RemoveException(e.getMessage());
		} finally {
			checkInPreparedStatement(deleteFromSystemRelease);
		}
		
		// remove value domains and pickLists
//		try {
//			vdServices_.removeBasedOnReleaseURI(systemReleaseURI);
//			plServices_.removeBasedOnReleaseURI(systemReleaseURI);			
//		} catch (SQLException e) {
//			throw new RemoveException(e.getMessage());
//		}
	}	
}