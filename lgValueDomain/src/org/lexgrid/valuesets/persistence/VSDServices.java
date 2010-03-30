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
import java.util.Iterator;

import org.LexGrid.emf.base.LgModelObj;
import org.LexGrid.emf.base.LgStagedObj;
import org.LexGrid.emf.commonTypes.CommontypesFactory;
import org.LexGrid.emf.commonTypes.Property;
import org.LexGrid.emf.commonTypes.Source;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.valueDomains.DefinitionEntry;
import org.LexGrid.emf.valueDomains.ValueDomainDefinition;
import org.LexGrid.emf.valueDomains.ValuedomainsFactory;
import org.LexGrid.emf.valueDomains.impl.ValueDomainDefinitionImpl;
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
 *        Title:        VDServices.java
 *        Description:  Class that handles Value Domain to the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VSDServices extends VSDBaseService {

	private VSDEntryServices vdEntryServices_;
	private VSDPropertyServices propertyServices_;
	private VSDEntryTypeServices entryTypeServices_;
	private VSDEntryStateService entryStateService_;
	private VSDMappingServices mappingServices_;
	private SystemReleaseServices systemReleaseServices_;

	protected VSDServices(JDBCBaseService anchorService)
			throws ServiceInitException {
		super(anchorService);
	}

	public VSDServices(HomeServiceBroker broker, JDBCConnectionDescriptor desc,
			boolean b, String tablePrefix, boolean failOnAllErrors,
			MessageRedirector messageRedirector, boolean createCSTables)
			throws ServiceInitException {
		super(broker, desc, b, tablePrefix, failOnAllErrors, messageRedirector,
				createCSTables);
		SingletonVariables.instance(getConnectionDescriptor().toString()).messages = messageRedirector;
		SingletonVariables.instance(getConnectionDescriptor().toString()).failOnAllErrors = failOnAllErrors;
	}

	public ManagedObjIF findByPrimaryKey(Object key) throws FindException {
		ValueDomainDefinition valueDomain = ValuedomainsFactory.eINSTANCE
				.createValueDomainDefinition();
		valueDomain.setContainer(getEContainer(), getEContainmentFeature(),
				true);

		PreparedStatement selectFromValueDomain = null;

		try {
			URI vdURI = (URI) key;

			selectFromValueDomain = checkOutPreparedStatement(" SELECT * "
					+ " FROM " + VSDConstants.TBL_VALUE_DOMAIN + " WHERE "
					+ SQLTableConstants.TBLCOL_VALUEDOMAINURI + " = ?");

			selectFromValueDomain.setString(1, vdURI.toString());

			ResultSet results = selectFromValueDomain.executeQuery();

			int entryId;
			String entryStateIdStr = null;
			if (results.next()) {
				entryId = results.getInt(SQLTableConstants.TBLCOL_ENTRYID);
				valueDomain.setValueDomainURI(results
						.getString(SQLTableConstants.TBLCOL_VALUEDOMAINURI));
				valueDomain.setValueDomainName(results
						.getString(SQLTableConstants.TBLCOL_VALUEDOMAINNAME));
				valueDomain
						.setDefaultCodingScheme(results
								.getString(SQLTableConstants.TBLCOL_DEFAULTCODINGSCHEME));
				entryStateIdStr = results
						.getString(SQLTableConstants.TBLCOL_ENTRYSTATEID);
				valueDomain.setEntityDescription(results
						.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));

				results.close();
			} else {
				throw new FindException(
						"Could not find value domain for uri : " + vdURI);
			}
			
			// get value domain attributes (Source and Context)
			getAttributes(valueDomain, entryId);
			
			vdEntryServices_.setEContext(valueDomain, getEContainmentFeature());
			vdEntryServices_.resolve(entryId);

			propertyServices_
					.setEContext(valueDomain, getEContainmentFeature());
			propertyServices_.findByPrimaryKey(valueDomain, entryId);
			
			mappingServices_.setEContext(valueDomain, getEContainmentFeature());
			mappingServices_.findByPrimaryKey(valueDomain, entryId);
			
			if (entryStateIdStr != null) {
				resolveEntryState(valueDomain, Integer.valueOf(entryStateIdStr));
			}

			return valueDomain;
		} catch (FindException e) {
			throw e;
		} catch (Exception e) {
			throw new FindException("Problem", e);
		} finally {
			checkInPreparedStatement(selectFromValueDomain);
		}
	}

	/**
	 * Return all value domain URIs that match the supplied key
	 * @param key - null: return all value domain URI's
	 *            - " ":  return all value domain URI's with no name
	 *            - otherwise return all URIs that match the key
	 * @return list of matching URIs
	 * @throws FindException
	 */
	public URI[] findByValueDomainName(Object key) throws FindException {
		ArrayList<URI> uris = new ArrayList<URI>();
		PreparedStatement selectFromValueDomain = null;

		try {
			String name = (String) key;

			String sql = "SELECT " + SQLTableConstants.TBLCOL_VALUEDOMAINURI
					+ " FROM " + VSDConstants.TBL_VALUE_DOMAIN;

			if (name != null)
				sql += " WHERE " + SQLTableConstants.TBLCOL_VALUEDOMAINNAME
						+ " = ?";

			selectFromValueDomain = checkOutPreparedStatement(sql);

			if (name != null) {
				if (StringUtils.isEmpty(name))
					name = " ";
				selectFromValueDomain.setString(1, name);
			}

			ResultSet results = selectFromValueDomain.executeQuery();

			while (results.next()) {
				uris.add(new URI(results
						.getString(SQLTableConstants.TBLCOL_VALUEDOMAINURI)));
			}

			results.close();

			return uris.toArray(new URI[uris.size()]);
		} catch (Exception e) {
			throw new FindException("Problem", e);
		} finally {
			checkInPreparedStatement(selectFromValueDomain);
		}
	}

	private void getAttributes(ValueDomainDefinition valueDomain, int entryId)
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
			selectVDSubProperties.setString(2,
					VSDConstants.ENTRY_STATE_TYPE_ATTRIBUTE);

			ResultSet results = selectVDSubProperties.executeQuery();

			while (results.next()) {
				String attributeName = results
						.getString(SQLTableConstants.TBLCOL_PROPERTYNAME);
				if (attributeName
						.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SOURCE)) {
					Source src = CommontypesFactory.eINSTANCE.createSource();
					src.setValue(results
							.getString(SQLTableConstants.TBLCOL_PROPERTYVALUE));
					src.setRole(results
							.getString(SQLTableConstants.TBLCOL_VAL2));
					src.setSubRef(results
							.getString(SQLTableConstants.TBLCOL_VAL1));
					valueDomain.getSource().add(src);
				} else if (attributeName
						.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT)) {
					valueDomain
							.getRepresentsRealmOrContext()
							.add(
									results
											.getString(SQLTableConstants.TBLCOL_PROPERTYVALUE));
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
	
	public void insert(ManagedObjIF obj, String systemReleaseURI, Mappings mappings) throws InsertException,
			ObjectAlreadyExistsException {
		ValueDomainDefinition vdd = (ValueDomainDefinition) (obj);
		boolean ignoreVD = false;
		try {
			int entryId = getNextEntryId();
			insertEntryType(vdd, entryId);

			int entryStateId = getNextEntryId();

			PreparedStatement insertIntoValueDomain = null;
			try {
				String vdName = vdd.getValueDomainName();
				insertIntoValueDomain = getKeyedInsertStatement(VSDConstants.TBL_VALUE_DOMAIN);
				int i = 1;
				insertIntoValueDomain.setInt(i++, entryId);
				insertIntoValueDomain.setString(i++, vdd.getValueDomainURI());
				insertIntoValueDomain.setString(i++, StringUtils.isEmpty(vdName) ? " " : vdName);
				insertIntoValueDomain.setString(i++, vdd.getDefaultCodingScheme());
				insertIntoValueDomain.setString(i++, systemReleaseURI);
				if (vdd.getEntryState() != null) {
					insertIntoValueDomain.setInt(i++, entryStateId);
				} else {
					if(this.getDatabaseName().equals("ACCESS")){
						insertIntoValueDomain.setString(i++, null);
                	} else {
                		insertIntoValueDomain.setObject(i++, null, java.sql.Types.BIGINT);
                	}
				}
				insertIntoValueDomain
						.setString(i++, vdd.getEntityDescription());

				insertIntoValueDomain.executeUpdate();
				insertIntoValueDomain.clearParameters();
			} catch (SQLException e) {
				if ((e.getMessage().indexOf("Duplicate entry") != -1)
						|| (e.getMessage().indexOf("unique constraint") != -1)
						|| (e.getMessage().indexOf("SQLCODE: -803") != -1)) {
					// print error message and continue
					String msg = "ValueDomain already exists with URI "
							+ vdd.getValueDomainURI() + ". Error message="
							+ e.getMessage();
					System.out.println(msg);
					if (SingletonVariables.instance(getConnectionDescriptor()
							.toString()).messages != null) {
						SingletonVariables.instance(getConnectionDescriptor()
								.toString()).messages.message(msg);
					}
					ignoreVD = true;
				}
			} catch (Exception e) {
				throw new InsertException(e);
			} finally {
				try {
					if (insertIntoValueDomain != null)
						insertIntoValueDomain.close();
				} catch (Exception e) {
					// do nothing
				}
			}

			if (!ignoreVD)
			{
				// insert Source and Context
				insertAttributes(vdd, entryId);
				
				if (vdd.getDefinitionEntry() != null) {
					Iterator<DefinitionEntry> iter = vdd.getDefinitionEntry()
							.iterator();
					while (iter.hasNext()) {
						vdEntryServices_.insert((DefinitionEntry) iter.next(),
								entryId);
					}
				}

				if (vdd.getProperties() != null) {
					Iterator<Property> propIter = vdd.getProperties().getProperty()
							.iterator();
					while (propIter.hasNext()) {
						propertyServices_.insert((Property) propIter.next(),
								entryId);
					}
				}
				
				if (vdd.getMappings() != null)
				{
					mappingServices_.insert(vdd.getMappings(), entryId);
				}
				if (mappings != null) {
				    mappingServices_.insert(mappings, entryId);
				}
				
				if (vdd.getEntryState() != null)
					insertEntryState(vdd, entryStateId);
			}
		} catch (InsertException e) {
			throw e;
		} catch (ObjectAlreadyExistsException e) {
			throw e;
		} catch (Exception e) {
			throw new InsertException(e);
		}
	}

	public void insertAttributes(ValueDomainDefinition valueDomain,
			int vdEntryId) throws InsertException, ObjectAlreadyExistsException {
		PreparedStatement insertIntoProperty = null;
		try {
			insertIntoProperty = getKeyedInsertStatement(VSDConstants.TBL_PROPERTY);

			
			Iterator<Source> sourceIter = valueDomain.getSource().iterator();
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
			
			Iterator contextIter = valueDomain.getRepresentsRealmOrContext().iterator();
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

	protected Class<ValueDomainDefinitionImpl> getInstanceClass() {
		return ValueDomainDefinitionImpl.class;
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
		vdEntryServices_ = new VSDEntryServices(this);
		propertyServices_ = new VSDPropertyServices(this);
		entryTypeServices_ = new VSDEntryTypeServices(this);
		entryStateService_ = new VSDEntryStateService(this);
		mappingServices_ = new VSDMappingServices(this);
		systemReleaseServices_ = new SystemReleaseServices(this);
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
			}
		} catch (Exception e) {
			throw new ResolveException(
					"Problem getting the sub-details for the value domain", e);
		}
	}

	public void update(ManagedObjIF obj) throws UpdateException,
			ObjectNotFoundException {
		// TODO: Implement update
		throw new java.lang.UnsupportedOperationException(
				"Method update not yet implemented.");
	}

	public void remove(Object key) throws RemoveException, FindException {
		
		PreparedStatement selectFromValueDomain = null;
		URI vdURI = (URI) key;
		int vdEntryId;
		String vdEntryStateIdStr = null;
		
		try {
			
			selectFromValueDomain = checkOutPreparedStatement(" SELECT  "
					+ SQLTableConstants.TBLCOL_ENTRYID 
					+ ", " + SQLTableConstants.TBLCOL_ENTRYSTATEID
					+ " FROM " + VSDConstants.TBL_VALUE_DOMAIN + " WHERE "
					+ SQLTableConstants.TBLCOL_VALUEDOMAINURI + " = ?");

			selectFromValueDomain.setString(1, vdURI.toString());

			ResultSet results = selectFromValueDomain.executeQuery();

			if (results.next()) {
				vdEntryId = results.getInt(SQLTableConstants.TBLCOL_ENTRYID);
				vdEntryStateIdStr = results.getString(SQLTableConstants.TBLCOL_ENTRYSTATEID);
				
				results.close();
			} else {
				throw new FindException(
						"Could not find value domain for uri : " + vdURI);
			}
		} catch (SQLException e) {
			throw new RemoveException(e.getMessage());
		} finally {
			checkInPreparedStatement(selectFromValueDomain);
		}
		
		// remove value domain attributes (Source and Context)
		try {
			propertyServices_.removePropQualsAndAttribs(vdEntryId);
			// remove valueDomain property
			propertyServices_.remove(vdEntryId, SQLTableConstants.ENTRY_STATE_TYPE_VALUEDOMAIN);			
			
			mappingServices_.remove(vdEntryId, SQLTableConstants.ENTRY_STATE_TYPE_VALUEDOMAIN);
			
			// remove all definitionEntry associated to this valueDomain from vdEntry table
			vdEntryServices_.remove(vdEntryId);
			
			
			if (StringUtils.isNotEmpty(vdEntryStateIdStr))
			{
				ArrayList<Integer> entryStateIds = new ArrayList<Integer>();
				entryStateIds.add(Integer.valueOf(vdEntryStateIdStr));
				entryStateService_.remove(entryStateIds, SQLTableConstants.ENTRY_STATE_TYPE_VALUEDOMAIN);
			}
			
			// now remove this valuDomain
			removeValueDomain(vdEntryId);
			
			// remove from entryType table
			ArrayList<Integer> entryIds = new ArrayList<Integer>();
			entryIds.add(vdEntryId);
			entryTypeServices_.remove(entryIds, SQLTableConstants.ENTRY_STATE_TYPE_VALUEDOMAIN);
		} catch (SQLException e) {
			throw new RemoveException(e.getMessage());
		}
	}
	
	public void dropValueDomainTables(boolean forceDelete) throws FindException, SQLException, RemoveException{
		URI[] uris = null;
		
		if (!forceDelete)
			uris = findByValueDomainName(null);
		
		if ((uris == null || uris.length == 0) && (!pickListHasEntries()))
			getVDBaseSQLService().dropValueDomainTables();
		else
			throw new RemoveException("Can not remove Value Domain Tables when there are entries in value domain or pick list tables.");
	}
	
	private void removeValueDomain(int entryId) throws SQLException, FindException {
		
		PreparedStatement deleteFromValueDomain = null;
		
		try {
			
			deleteFromValueDomain = checkOutPreparedStatement("DELETE FROM " 
					+ VSDConstants.TBL_VALUE_DOMAIN + " WHERE "
					+ SQLTableConstants.TBLCOL_ENTRYID + " = ?");

			deleteFromValueDomain.setInt(1, entryId);

			deleteFromValueDomain.executeUpdate();
		} finally {
			checkInPreparedStatement(deleteFromValueDomain);
		}
	}
	
	private boolean pickListHasEntries() throws SQLException, FindException {
		
		PreparedStatement selectFromPickList = null;
		
		try {
			
			selectFromPickList = checkOutPreparedStatement("SELECT COUNT(*) AS CNT FROM " 
					+ VSDConstants.TBL_PICK_LIST);

			ResultSet rs = selectFromPickList.executeQuery();
			
			rs.next();
			int total = rs.getInt("CNT");
			
			rs.close();
			
			if (total > 0)
				return true;
			else 
				return false;
			
		} finally {
			checkInPreparedStatement(selectFromPickList);
		}
	}
	
	protected void insertEntryType(ValueDomainDefinition vdd, int entryId)
			throws InsertException, ObjectAlreadyExistsException {
		entryTypeServices_.insert(vdd, entryId);
	}

	protected void insertEntryType(String entryType, int entryId)
			throws InsertException, ObjectAlreadyExistsException {
		entryTypeServices_.insert(entryType, entryId);
	}

	protected String resolveEntryType(int entryId) throws FindException {
		return entryTypeServices_.resolveEntryTypeFor(entryId);
	}

	protected void insertEntryState(ValueDomainDefinition vdd, int entryStateId)
			throws InsertException, ObjectAlreadyExistsException {
		entryStateService_.insert(vdd, entryStateId, null);
	}

	protected void resolveEntryState(ValueDomainDefinition vdd, int entryStateId)
			throws FindException {
		entryStateService_.findbyPrimaryKey(vdd, entryStateId);
	}
}