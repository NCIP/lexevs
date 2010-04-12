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
package org.lexevs.dao.database.access;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.valuesets.PickListDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.registry.service.Registry;

/**
 * The Class DaoManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DaoManager {
	
	/** The CURREN t_ version. */
	public static LexGridSchemaVersion CURRENT_VERSION = LexGridSchemaVersion.parseStringToVersion("2.0");

	/** The coding scheme daos. */
	private List<CodingSchemeDao> codingSchemeDaos;
	
	/** The entity daos. */
	private List<EntityDao> entityDaos;
	
	/** The property daos. */
	private List<PropertyDao> propertyDaos;
	
	/** The association daos. */
	private List<AssociationDao> associationDaos;
	
	/** The pick list definition daos. */
	private List<PickListDao> pickListDaos;
	
	/** The value set definition daos. */
	private List<ValueSetDefinitionDao> valueSetDefinitionDaos;
	
	/** The vsProperty daos. */
	private List<VSPropertyDao> vsPropertyDaos;
	
	/** The versions daos. */
	private List<VersionsDao> versionsDaos;
	
	/** The versions daos. */
	private List<CodedNodeGraphDao> codedNodeGraphDaos;
	
	/** The registry. */
	private Registry registry;
	
	/**
	 * Gets the versions dao.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the versions dao
	 */
	public VersionsDao getVersionsDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getVersionsDaos());
	}
	
	/**
	 * Gets the current entity dao.
	 * 
	 * @return the current entity dao
	 */
	public EntityDao getCurrentEntityDao(){
		return this.getCorrectDaoForSchemaVersion(this.getEntityDaos(), CURRENT_VERSION);
	}
	
	/**
	 * Gets the current association dao.
	 * 
	 * @return the current association dao
	 */
	public AssociationDao getCurrentAssociationDao(){
		return this.getCorrectDaoForSchemaVersion(this.getAssociationDaos(), CURRENT_VERSION);
	}
	
	/**
	 * Gets the entity dao.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the entity dao
	 */
	public EntityDao getEntityDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getEntityDaos());
	}
	
	public CodedNodeGraphDao getCodedNodeGraphDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getCodedNodeGraphDaos());
	}
	
	/**
	 * Gets the coding scheme dao.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the coding scheme dao
	 */
	public CodingSchemeDao getCodingSchemeDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getCodingSchemeDaos());
	}
	
	/**
	 * Gets the current coding scheme dao.
	 * 
	 * @return the current coding scheme dao
	 */
	public CodingSchemeDao getCurrentCodingSchemeDao(){
		return this.getCorrectDaoForSchemaVersion(this.codingSchemeDaos, CURRENT_VERSION);
	}
	
	/**
	 * Gets the property dao.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the property dao
	 */
	public PropertyDao getPropertyDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getPropertyDaos());
	}
	
	/**
	 * Gets the association dao.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the association dao
	 */
	public AssociationDao getAssociationDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getAssociationDaos());
	}
	
	/**
	 * Do get dao.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param daos the daos
	 * 
	 * @return the t
	 */
	protected <T extends LexGridSchemaVersionAwareDao> T doGetDao(String codingSchemeUri, String version, List<T> daos){
		Assert.assertNotNull("No DAOs have been registered for the requested type.", daos);	
		return getCorrectDaoForSchemaVersion(daos, 
				getLexGridSchemaVersion(codingSchemeUri, version));
	}
	
	/**
	 * Gets the lex grid schema version.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the lex grid schema version
	 */
	protected LexGridSchemaVersion getLexGridSchemaVersion(String uri, String version){
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(uri);
		ref.setCodingSchemeVersion(version);

		try {
			return LexGridSchemaVersion.parseStringToVersion(
					registry.getCodingSchemeEntry(ref).getDbSchemaVersion()
			);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the correct dao for schema version.
	 * 
	 * @param possibleDaos the possible daos
	 * @param schemaVersion the schema version
	 * 
	 * @return the correct dao for schema version
	 */
	protected <T extends LexGridSchemaVersionAwareDao> T getCorrectDaoForSchemaVersion(List<T> possibleDaos, LexGridSchemaVersion schemaVersion) {
		List<T> foundDaos = new ArrayList<T>();
		
		for(T dao : possibleDaos){
			if(dao.supportsLgSchemaVersion(schemaVersion)){
				foundDaos.add(dao);
			}
		}
		
		Assert.assertTrue("No matching DAO for Database Version: " +
				schemaVersion, foundDaos.size() > 0);	
		
		Assert.assertTrue("More than one matching DAO for: " +
				foundDaos.get(0).getClass().getName(), foundDaos.size() < 2);
		
		return foundDaos.get(0);
	}

	/**
	 * Gets the coding scheme daos.
	 * 
	 * @return the coding scheme daos
	 */
	public List<CodingSchemeDao> getCodingSchemeDaos() {
		return codingSchemeDaos;
	}

	/**
	 * Sets the coding scheme daos.
	 * 
	 * @param codingSchemeDaos the new coding scheme daos
	 */
	public void setCodingSchemeDaos(List<CodingSchemeDao> codingSchemeDaos) {
		this.codingSchemeDaos = codingSchemeDaos;
	}

	/**
	 * Gets the entity daos.
	 * 
	 * @return the entity daos
	 */
	public List<EntityDao> getEntityDaos() {
		return entityDaos;
	}

	/**
	 * Sets the entity daos.
	 * 
	 * @param entityDaos the new entity daos
	 */
	public void setEntityDaos(List<EntityDao> entityDaos) {
		this.entityDaos = entityDaos;
	}

	/**
	 * Gets the property daos.
	 * 
	 * @return the property daos
	 */
	public List<PropertyDao> getPropertyDaos() {
		return propertyDaos;
	}

	/**
	 * Sets the property daos.
	 * 
	 * @param propertyDaos the new property daos
	 */
	public void setPropertyDaos(List<PropertyDao> propertyDaos) {
		this.propertyDaos = propertyDaos;
	}

	/**
	 * Gets the versions daos.
	 * 
	 * @return the versions daos
	 */
	public List<VersionsDao> getVersionsDaos() {
		return versionsDaos;
	}

	/**
	 * Sets the versions daos.
	 * 
	 * @param versionsDaos the new versions daos
	 */
	public void setVersionsDaos(List<VersionsDao> versionsDaos) {
		this.versionsDaos = versionsDaos;
	}

	/**
	 * Gets the registry.
	 * 
	 * @return the registry
	 */
	public Registry getRegistry() {
		return registry;
	}

	/**
	 * Sets the registry.
	 * 
	 * @param registry the new registry
	 */
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	/**
	 * Sets the association daos.
	 * 
	 * @param associationDaos the new association daos
	 */
	public void setAssociationDaos(List<AssociationDao> associationDaos) {
		this.associationDaos = associationDaos;
	}

	/**
	 * Gets the association daos.
	 * 
	 * @return the association daos
	 */
	public List<AssociationDao> getAssociationDaos() {
		return associationDaos;
	}

	/**
	 * Gets the pick list definition daos.
	 * @return the pickListDaos
	 */
	public List<PickListDao> getPickListDaos() {
		return pickListDaos;
	}

	/**
	 * Sets the pick list definition daos.
	 * @param pickListDaos the pickListDaos to set
	 */
	public void setPickListDaos(List<PickListDao> pickListDaos) {
		this.pickListDaos = pickListDaos;
	}
	
	/**
	 * Gets the current pick list dao.
	 * 
	 * @return the current pick list dao
	 */
	public PickListDao getCurrentPickListDefinitionDao(){
		return this.getCorrectDaoForSchemaVersion(this.getPickListDaos(), CURRENT_VERSION);
	}

	/**
	 * @return the valueSetDefinitionDaos
	 */
	public List<ValueSetDefinitionDao> getValueSetDefinitionDaos() {
		return valueSetDefinitionDaos;
	}

	/**
	 * @param valueSetDefinitionDaos the valueSetDefinitionDaos to set
	 */
	public void setValueSetDefinitionDaos(
			List<ValueSetDefinitionDao> valueSetDefinitionDaos) {
		this.valueSetDefinitionDaos = valueSetDefinitionDaos;
	}
	
	/**
	 * Gets the current value set definition dao.
	 * 
	 * @return the current value set definition dao
	 */
	public ValueSetDefinitionDao getCurrentValueSetDefinitionDao(){
		return this.getCorrectDaoForSchemaVersion(this.getValueSetDefinitionDaos(), CURRENT_VERSION);
	}

	public List<CodedNodeGraphDao> getCodedNodeGraphDaos() {
		return codedNodeGraphDaos;
	}

	public void setCodedNodeGraphDaos(List<CodedNodeGraphDao> codedNodeGraphDaos) {
		this.codedNodeGraphDaos = codedNodeGraphDaos;
	}

	/**
	 * @return the vsPropertyDaos
	 */
	public List<VSPropertyDao> getVsPropertyDaos() {
		return vsPropertyDaos;
	}

	/**
	 * @param vsPropertyDaos the vsPropertyDaos to set
	 */
	public void setVsPropertyDaos(List<VSPropertyDao> vsPropertyDaos) {
		this.vsPropertyDaos = vsPropertyDaos;
	}
	
	/**
	 * Gets the current value set property dao.
	 * 
	 * @return the current value set property dao
	 */
	public VSPropertyDao getCurrentVsPropertyDao(){
		return this.getCorrectDaoForSchemaVersion(this.getVsPropertyDaos(), CURRENT_VERSION);
	}
}
