/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.AssociationDataDao;
import org.lexevs.dao.database.access.association.AssociationTargetDao;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.ncihistory.NciHistoryDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.revision.RevisionDao;
import org.lexevs.dao.database.access.systemRelease.SystemReleaseDao;
import org.lexevs.dao.database.access.valuesets.PickListDao;
import org.lexevs.dao.database.access.valuesets.PickListEntryNodeDao;
import org.lexevs.dao.database.access.valuesets.VSDefinitionEntryDao;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao;
import org.lexevs.dao.database.access.valuesets.ValueSetHierarchyDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.registry.service.Registry;
import org.springframework.util.Assert;

/**
 * The Class DaoManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DaoManager {
	
	public LexGridSchemaVersion currentVersion;

	/** The coding scheme daos. */
	private List<CodingSchemeDao> codingSchemeDaos;
	
	/** The entity daos. */
	private List<EntityDao> entityDaos;
	
	/** The property daos. */
	private List<PropertyDao> propertyDaos;
	
	/** The association daos. */
	private List<AssociationDao> associationDaos;
	
	/** The association entity daos. */
	private List<AssociationTargetDao> associationTargetDaos;
	
	/** The association data daos. */
	private List<AssociationDataDao> associationDataDaos;
	
	/** The pick list definition daos. */
	private List<PickListDao> pickListDaos;
	
	/** The pick list entry node daos. */
	private List<PickListEntryNodeDao> pickListEntryNodeDaos;
	
	/** The value set definition daos. */
	private List<ValueSetDefinitionDao> valueSetDefinitionDaos;
	
	/** The VSDefinitionEntry daos. */
	private List<VSDefinitionEntryDao> vsDefinitionEntryDaos;
	
	/** The Value Set Hierarchy daos. */
	private List<ValueSetHierarchyDao> valueSetHierarchyDaos;

	/** The vsProperty daos. */
	private List<VSPropertyDao> vsPropertyDaos;
	
	/** The vsEntryState daos. */
	private List<VSEntryStateDao> vsEntryStateDaos;
	
	/** The versions daos. */
	private List<VersionsDao> versionsDaos;

	/** the system release daos */
	private List<SystemReleaseDao> systemReleaseDaos = null;

	/** the revision daos*/
	private List<RevisionDao> revisionDaos = null;
	
	/** The versions daos. */
	private List<CodedNodeGraphDao> codedNodeGraphDaos;
	
	/** The versions daos. */
	private List<NciHistoryDao> nciHistoryDaos;
	
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
		return this.getCorrectDaoForSchemaVersion(this.getEntityDaos(), currentVersion);
	}
	
	/**
	 * Gets the current association dao.
	 * 
	 * @return the current association dao
	 */
	public AssociationDao getCurrentAssociationDao(){
		return this.getCorrectDaoForSchemaVersion(this.getAssociationDaos(), currentVersion);
	}
	
	/**
	 * Gets the current association target dao.
	 * 
	 * @return the current association target dao
	 */
	public AssociationTargetDao getCurrentAssociationTargetDao() {
		return this.getCorrectDaoForSchemaVersion(this
				.getAssociationTargetDaos(), currentVersion);
	}
	
	/**
	 * Gets the current association data dao.
	 * 
	 * @return the current association data dao
	 */
	public AssociationDataDao getCurrentAssociationDataDao() {
		return this.getCorrectDaoForSchemaVersion(
				this.getAssociationDataDaos(), currentVersion);
	}
	
	/**
	 * Gets the current codednodegraph dao.
	 * 
	 * @return the current association dao
	 */
	public CodedNodeGraphDao getCurrentCodedNodeGraphDao(){
		return this.getCorrectDaoForSchemaVersion(this.codedNodeGraphDaos, currentVersion);
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
		return this.getCorrectDaoForSchemaVersion(this.codingSchemeDaos, currentVersion);
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
	 * Gets the association Target dao.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the association entity dao
	 */
	public AssociationTargetDao getAssociationTargetDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getAssociationTargetDaos());
	}
	
	/**
	 * Gets the association data dao.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the association data dao
	 */
	public AssociationDataDao getAssociationDataDao(String codingSchemeUri, String version){
		return this.doGetDao(codingSchemeUri, version, this.getAssociationDataDaos());
	}
	
	public SystemReleaseDao getSystemReleaseDao(){
		return this.getSystemReleaseDaos().get(0);
	}
	
	public RevisionDao getRevisionDao(){
		return this.getRevisionDaos().get(0);
	}
	
	public NciHistoryDao getNciHistoryDao(String codingSchemeUri){
		return this.doGetDao(codingSchemeUri, this.getNciHistoryDaos());
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
		Assert.notNull(daos, "No DAOs have been registered for the requested type.");	
		return getCorrectDaoForSchemaVersion(daos, 
				getLexGridSchemaVersion(codingSchemeUri, version));
	}
	
	protected <T extends LexGridSchemaVersionAwareDao> T doGetDao(String codingSchemeUri, List<T> daos){
		Assert.notNull(daos, "No DAOs have been registered for the requested type.");	
		return getCorrectDaoForSchemaVersion(daos, 
				getLexGridSchemaVersion(codingSchemeUri));
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
			throw new RuntimeException("There was a problem connecting to the persistence store for the resource: " +
					"URI: " + uri + ", Version: " + version + ".\nPlease make sure this resource exists in the system.", e);
		}
	}
	
	protected LexGridSchemaVersion getLexGridSchemaVersion(String uri){
		
		try {
			return LexGridSchemaVersion.parseStringToVersion(
					registry.getNonCodingSchemeEntry(uri).getDbSchemaVersion());
		
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
		
		Assert.isTrue(foundDaos.size() > 0, "No matching DAO for Database Version: " +
				schemaVersion);	
		
		Assert.isTrue(foundDaos.size() < 2, "More than one matching DAO for: " +
				foundDaos.get(0).getClass().getName());
		
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
		return this.getCorrectDaoForSchemaVersion(this.getPickListDaos(), currentVersion);
	}

	/**
	 * Gets the current pick list entry node dao.
	 * 
	 * @return the current pick list entry node dao
	 */
	public PickListEntryNodeDao getCurrentPickListEntryNodeDao(){
		return this.getCorrectDaoForSchemaVersion(this.getPickListEntryNodeDaos(), currentVersion);
	}
	
	public void setSystemReleaseDaos(List<SystemReleaseDao> systemReleaseDaos) {
		this.systemReleaseDaos = systemReleaseDaos;
	}

	/**
	 * Gets the systemRelease daos.
	 * 
	 * @return the systemRelease daos
	 */
	public List<SystemReleaseDao> getSystemReleaseDaos() {
		return systemReleaseDaos;
	}

	/**
	 * Gets the revision daos.
	 * 
	 * @return the revision daos
	 */
	public List<RevisionDao> getRevisionDaos() {
		return revisionDaos;
	}

	public void setRevisionDaos(List<RevisionDao> revisionDaos) {
		this.revisionDaos = revisionDaos;
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
	
	public List<ValueSetHierarchyDao> getvalueSetHierarchyDaos() {
		return valueSetHierarchyDaos;
	}

	public void setvalueSetHierarchyDaos(List<ValueSetHierarchyDao> valueSetHierarchyDaos) {
		this.valueSetHierarchyDaos = valueSetHierarchyDaos;
	}

	/**
	 * Gets the current value set definition dao.
	 * 
	 * @return the current value set definition dao.
	 */
	public ValueSetDefinitionDao getCurrentValueSetDefinitionDao(){
		return this.getCorrectDaoForSchemaVersion(this.getValueSetDefinitionDaos(), currentVersion);
	}

	/**
	 * Gets the current value set hierarchy dao.
	 * 
	 * @return the current value set hierarchy dao.
	 */
	public ValueSetHierarchyDao getCurrentValueSetHiearchyDao(){
		return this.getCorrectDaoForSchemaVersion(this.getvalueSetHierarchyDaos(), currentVersion);
	}
	
	
	/**
	 * Gets the current value set definition entry dao.
	 * 
	 * @return the current value set definition entry dao.
	 */
	public VSDefinitionEntryDao getCurrentVSDefinitionEntryDao(){
		return this.getCorrectDaoForSchemaVersion(this.getVsDefinitionEntryDaos(), currentVersion);
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
		return this.getCorrectDaoForSchemaVersion(this.getVsPropertyDaos(), currentVersion);
	}
	
	/**
	 * @return the associationEntityDaos
	 */
	public List<AssociationTargetDao> getAssociationTargetDaos() {
		return associationTargetDaos;
	}

	/**
	 * @param associationTargetDaos the associationEntityDaos to set
	 */
	public void setAssociationTargetDaos(
			List<AssociationTargetDao> associationTargetDaos) {
		this.associationTargetDaos = associationTargetDaos;
	}

	/**
	 * @return the associationDataDaos
	 */
	public List<AssociationDataDao> getAssociationDataDaos() {
		return associationDataDaos;
	}

	/**
	 * @param associationDataDaos the associationDataDaos to set
	 */
	public void setAssociationDataDaos(List<AssociationDataDao> associationDataDaos) {
		this.associationDataDaos = associationDataDaos;
	}

	/**
	 * @return the vsDefinitionEntryDaos
	 */
	public List<VSDefinitionEntryDao> getVsDefinitionEntryDaos() {
		return vsDefinitionEntryDaos;
	}

	/**
	 * @param vsDefinitionEntryDaos the vsDefinitionEntryDaos to set
	 */
	public void setVsDefinitionEntryDaos(
			List<VSDefinitionEntryDao> vsDefinitionEntryDaos) {
		this.vsDefinitionEntryDaos = vsDefinitionEntryDaos;
	}

	/**
	 * @return the pickListEntryNodeDaos
	 */
	public List<PickListEntryNodeDao> getPickListEntryNodeDaos() {
		return pickListEntryNodeDaos;
	}

	/**
	 * @param pickListEntryNodeDaos the pickListEntryNodeDaos to set
	 */
	public void setPickListEntryNodeDaos(
			List<PickListEntryNodeDao> pickListEntryNodeDaos) {
		this.pickListEntryNodeDaos = pickListEntryNodeDaos;
	}
	
	/**
	 * @return the vsEntryStateDaos
	 */
	public List<VSEntryStateDao> getVsEntryStateDaos() {
		return vsEntryStateDaos;
	}

	/**
	 * @param vsEntryStateDaos the vsEntryStateDaos to set
	 */
	public void setVsEntryStateDaos(List<VSEntryStateDao> vsEntryStateDaos) {
		this.vsEntryStateDaos = vsEntryStateDaos;
	}
	
	/**
	 * Gets the current value set EntryState dao.
	 * 
	 * @return the current value set EntryState dao
	 */
	public VSEntryStateDao getCurrentVsEntryStateDao(){
		return this.getCorrectDaoForSchemaVersion(this.getVsEntryStateDaos(), currentVersion);
	}

	public void setNciHistoryDaos(List<NciHistoryDao> nciHistoryDaos) {
		this.nciHistoryDaos = nciHistoryDaos;
	}

	public List<NciHistoryDao> getNciHistoryDaos() {
		return nciHistoryDaos;
	}

	public LexGridSchemaVersion getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(LexGridSchemaVersion currentVersion) {
		this.currentVersion = currentVersion;
	}
}