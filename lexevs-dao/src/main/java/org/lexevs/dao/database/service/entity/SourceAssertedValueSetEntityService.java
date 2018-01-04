package org.lexevs.dao.database.service.entity;

import java.sql.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId;

public interface SourceAssertedValueSetEntityService extends EntityService {


	public List<? extends Entity> getEntities(String codingSchemeUri, String version, int start, int pageSize, AssertedValueSetParameters params);

	List<String> getEntityUids(String codingSchemeUri, String version, int start, int pageSize);

}
