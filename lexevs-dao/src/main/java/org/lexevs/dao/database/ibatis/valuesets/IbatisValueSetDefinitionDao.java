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
package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;

import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertOrUpdateDefinitionEntryBean;
import org.lexevs.dao.database.ibatis.valuesets.parameter.InsertValueSetDefinitionBean;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

/**
 * The Class IbatisValueSetDefinitionDao.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class IbatisValueSetDefinitionDao extends AbstractIbatisDao implements ValueSetDefinitionDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	public static String VALUESETDEFINITION_NAMESPACE = "ValueSetDefinition.";
	
	public static String INSERT_VALUESET_DEFINITION_SQL = VALUESETDEFINITION_NAMESPACE + "insertValueSetDefinition";
	
	public static String INSERT_DEFINITION_ENTRY_SQL = VALUESETDEFINITION_NAMESPACE + "insertDefinitionEntry";
	
	public static String GET_VALUESET_DEFINITION_URIS_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionURIs";
	
	public static String GET_VALUESET_DEFINITION_GUID_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionGuidByValueSetDefinitionURI";
	
	public static String GET_VALUESET_DEFINITION_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "getValueSetDefinitionByValueSetURI";
	
	public static String REMOVE_VALUESET_DEFINITION_BY_VALUESET_DEFINITION_URI_SQL = VALUESETDEFINITION_NAMESPACE + "removevalueSetDefinitionByValueSetDefinitionURI";
	
	public static String REMOVE_DEFINITION_ENTRY_BY_VALUESET_DEFINITION_GUID_SQL = VALUESETDEFINITION_NAMESPACE + "removeDefinitionEntryByValueSetDefinitionGuid";
	
	/** The versions dao. */
	private VersionsDao versionsDao;
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#getValueSetDefinitionByURI(java.lang.String)
	 */
	@Override
	public ValueSetDefinition getValueSetDefinitionByURI(String pickListId) {
		ValueSetDefinition vdDef = (ValueSetDefinition) 
			this.getSqlMapClientTemplate().queryForObject(GET_VALUESET_DEFINITION_BY_VALUESET_DEFINITION_URI_SQL, 
				new PrefixedParameter(null, pickListId));
		return vdDef;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#getGuidFromvalueSetDefinitionURI(java.lang.String)
	 */
	@Override
	public String getGuidFromvalueSetDefinitionURI(String valueSetDefinitionURI) {
		return (String) 
		this.getSqlMapClientTemplate().queryForObject(GET_VALUESET_DEFINITION_GUID_BY_VALUESET_DEFINITION_URI_SQL, 
			new PrefixedParameter(null, valueSetDefinitionURI));
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#insertValueSetDefinition(java.lang.String, org.LexGrid.valueDomains.ValueSetDefinition)
	 */
	@Override
	public String insertValueSetDefinition(String systemReleaseUri,
			ValueSetDefinition definition) {
		String valueSetDefinitionGuid = this.createUniqueId();
		
		String systemReleaseId = this.versionsDao.getSystemReleaseIdByUri(systemReleaseUri);
		
		InsertValueSetDefinitionBean vsDefBean = new InsertValueSetDefinitionBean();
		vsDefBean.setId(valueSetDefinitionGuid);
		vsDefBean.setValueSetDefinition(definition);
		vsDefBean.setPrefix(getPrefix());
		vsDefBean.setSystemReleaseId(systemReleaseId);
		
		// insert into value set definition table
		this.getSqlMapClientTemplate().insert(INSERT_VALUESET_DEFINITION_SQL, vsDefBean);
		
		InsertOrUpdateDefinitionEntryBean vsdEntryBean = null;
		for (DefinitionEntry vsdEntry : definition.getDefinitionEntryAsReference()) 
		{
			String vsdEntryGuid = this.createUniqueId();
			vsdEntryBean = new InsertOrUpdateDefinitionEntryBean();
			vsdEntryBean.setId(vsdEntryGuid);
			vsdEntryBean.setDefinitionEntry(vsdEntry);
			vsdEntryBean.setValueSetDefGuid(valueSetDefinitionGuid);
			
			if (vsdEntry.getCodingSchemeReference() != null)
			{
				vsdEntryBean.setCodingSchemeReference(vsdEntry.getCodingSchemeReference().getCodingScheme());
			}
			
			else if (vsdEntry.getValueSetDefinitionReference() != null)
			{
				vsdEntryBean.setValueSetDefReference(vsdEntry.getValueSetDefinitionReference().getValueSetDefinitionURI());
			}
			else if (vsdEntry.getEntityReference() != null)
			{
				vsdEntryBean.setEntityCode(vsdEntry.getEntityReference().getEntityCode());
				vsdEntryBean.setEntityCodeNamespace(vsdEntry.getEntityReference().getEntityCodeNamespace());
				vsdEntryBean.setLeafOnly(vsdEntry.getEntityReference().getLeafOnly());
				vsdEntryBean.setReferenceAssociation(vsdEntry.getEntityReference().getReferenceAssociation());
				vsdEntryBean.setTargetToSource(vsdEntry.getEntityReference().getTargetToSource());
				vsdEntryBean.setTransitiveClosure(vsdEntry.getEntityReference().getTransitiveClosure());
			}
			else if (vsdEntry.getPropertyReference() != null)
			{
				vsdEntryBean.setPropertyName(vsdEntry.getPropertyReference().getPropertyName());
				vsdEntryBean.setPropertyRefCodingScheme(vsdEntry.getPropertyReference().getCodingScheme());
				vsdEntryBean.setPropertyMatchValue(vsdEntry.getPropertyReference().getPropertyMatchValue().getContent());
				vsdEntryBean.setFormat(vsdEntry.getPropertyReference().getPropertyMatchValue().getDataType());
			}			
			
			// insert into vsdEntry table
			this.getSqlMapClientTemplate().insert(INSERT_DEFINITION_ENTRY_SQL, vsdEntryBean);
		}
		
		return valueSetDefinitionGuid;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao#getValueSetDefinitionURIs()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValueSetDefinitionURIs() {
		return this.getSqlMapClientTemplate().queryForList(GET_VALUESET_DEFINITION_URIS_SQL);
	}	
	
	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	protected String getPrefix() {
		return this.getPrefixResolver().resolveDefaultPrefix();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
	}

	/**
	 * Gets the versions dao.
	 * 
	 * @return the versions dao
	 */
	public VersionsDao getVersionsDao() {
		return versionsDao;
	}

	/**
	 * Sets the versions dao.
	 * 
	 * @param versionsDao the new versions dao
	 */
	public void setVersionsDao(VersionsDao versionsDao) {
		this.versionsDao = versionsDao;
	}

	@ClearCache
	public void removeValueSetDefinitionByValueSetDefinitionURI(String valueSetDefinitionURI) {
		
		String valueSetDefGuid = (String) this.getSqlMapClientTemplate().queryForObject(GET_VALUESET_DEFINITION_GUID_BY_VALUESET_DEFINITION_URI_SQL, new PrefixedParameter(null, valueSetDefinitionURI));
		
		// remove definition entries
		this.getSqlMapClientTemplate().delete(REMOVE_DEFINITION_ENTRY_BY_VALUESET_DEFINITION_GUID_SQL, new PrefixedParameter(null, valueSetDefGuid));
		
		// remove value set definition
		this.getSqlMapClientTemplate().
			delete(REMOVE_VALUESET_DEFINITION_BY_VALUESET_DEFINITION_URI_SQL, new PrefixedParameter(null, valueSetDefinitionURI));	
	}

	@Override
	public String insertDefinitionEntry(String valueSetDefinitionGuid,
			ValueSetDefinition definition) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
