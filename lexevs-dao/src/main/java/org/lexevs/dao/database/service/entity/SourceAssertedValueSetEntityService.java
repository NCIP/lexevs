package org.lexevs.dao.database.service.entity;

import java.sql.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationEntity;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId;

public class SourceAssertedValueSetEntityService extends RevisableAbstractDatabaseService<Entity,CodingSchemeUriVersionBasedEntryId> implements EntityService {

	@Override
	public void insertEntity(String codingSchemeUri, String version, Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getEntityCount(String codingSchemeUri, String version) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Entity getEntity(String codingSchemeUri, String version, String entityCode, String entityCodeNamespace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity getEntity(String codingSchemeUri, String version, String entityCode, String entityCodeNamespace,
			List<String> propertyNames, List<String> propertyTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationEntity getAssociationEntity(String codingSchemeUri, String version, String entityCode,
			String entityCodeNamespace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends Entity> getEntities(String codingSchemeUri, String version, int start, int pageSize) {
		String codingSchemeId = this.getDaoManager().
				getCodingSchemeDao(codingSchemeUri, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
			
			List<? extends Entity> entities = this.getDaoManager().getEntityDao(codingSchemeUri, version).
				getAllEntitiesOfCodingScheme(codingSchemeId, start, pageSize);
			
			return entities;
	}

	@Override
	public ResolvedConceptReference getResolvedCodedNodeReference(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace, boolean resolve, List<String> propertyNames,
			List<String> propertyTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertBatchEntities(String codingSchemeUri, String version, List<? extends Entity> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateEntity(String codingSchemeUri, String version, Entity entity) throws LBException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEntity(String codingSchemeUri, String version, Entity entity) throws LBException {
		// TODO Auto-generated method stub

	}

	@Override
	public void revise(String codingSchemeUri, String version, Entity revisedEntity) throws LBException {
		// TODO Auto-generated method stub

	}

	@Override
	public Entity resolveEntityByRevision(String codingSchemeURI, String version, String entityCode,
			String entityCodeNamespace, String revisionId) throws LBRevisionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity resolveEntityByDate(String codingSchemeURI, String version, String entityCode,
			String entityCodeNamespace, Date date) throws LBRevisionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityDescription getEntityDescription(String codingSchemeURI, String version, String code,
			String codeNamespace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Entity addDependentAttributesByRevisionId(CodingSchemeUriVersionBasedEntryId id, String entryUid,
			Entity entry, String revisionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void insertIntoHistory(CodingSchemeUriVersionBasedEntryId id, Entity currentEntry, String entryUId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doInsertDependentChanges(CodingSchemeUriVersionBasedEntryId id, Entity revisedEntry)
			throws LBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String updateEntryVersionableAttributes(CodingSchemeUriVersionBasedEntryId id, String entryUId,
			Entity revisedEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Entity getCurrentEntry(CodingSchemeUriVersionBasedEntryId id, String entryUId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Entity getHistoryEntryByRevisionId(CodingSchemeUriVersionBasedEntryId id, String entryUid,
			String revisionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getLatestRevisionId(CodingSchemeUriVersionBasedEntryId id, String entryUId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEntryUid(CodingSchemeUriVersionBasedEntryId id, Entity entry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean entryStateExists(CodingSchemeUriVersionBasedEntryId id, String entryStateUid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String getCurrentEntryStateUid(CodingSchemeUriVersionBasedEntryId id, String entryUid) {
		// TODO Auto-generated method stub
		return null;
	}

}
