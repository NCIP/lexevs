/**
 * 
 */
package org.lexevs.dao.database.service.valuesets;

import java.util.List;

import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListDefinitions;
import org.lexevs.dao.database.access.valuesets.PickListDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventPickListDefinitionService.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 *
 */
public class VersionableEventPickListDefinitionService extends AbstractDatabaseService implements
		PickListDefinitionService {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#getPickListDefinitionByPickListId(java.lang.String)
	 */
	@Override
	public PickListDefinition getPickListDefinitionByPickListId(
			String pickListId) {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListDefinitionById(pickListId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#getPickListDefinitionIdForValueSetDefinitionUri(java.lang.String)
	 */
	@Override
	public List<String> getPickListDefinitionIdForValueSetDefinitionUri(
			String valueSetDefURI) {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListDefinitionIdForValueSetDefinitionURI(valueSetDefURI);
	}

	@Override
	public List<String> getPickListDefinitionIdForEntityReference(
			String entityCode, String entityCodeNameSpace, String propertyId) {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListDefinitionIdForEntityReference(entityCode, entityCodeNameSpace, propertyId);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#insertPickListDefinition(org.LexGrid.valueSets.PickListDefinition)
	 */
	@Transactional
//	@DatabaseErrorIdentifier(errorCode=INSERT_PICKLIST_ERROR)
	public void insertPickListDefinition(PickListDefinition definition, String systemReleaseUri, Mappings mappings) {
		PickListDao plDao = this.getDaoManager().getCurrentPickListDefinitionDao();
	
		plDao.insertPickListDefinition(definition, systemReleaseUri, mappings);
		
//		this.fireCodingSchemeInsertEvent(definition);
	}
	
	@Transactional
//	@DatabaseErrorIdentifier(errorCode=INSERT_PICKLIST_ERROR)
	public void insertPickListDefinitions(PickListDefinitions definitions, String systemReleaseUri) {
		PickListDao plDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		
		Mappings mappings = definitions.getMappings();
		
		for (PickListDefinition definition : definitions.getPickListDefinitionAsReference())
		{
			plDao.insertPickListDefinition(definition, systemReleaseUri, mappings);
		}
		
//		this.fireCodingSchemeInsertEvent(definition);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#listPickListIds()
	 */
	@Override
	public List<String> listPickListIds() {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListIds();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#removePickListDefinitionByPickListId(java.lang.String)
	 */
	@Override
	public void removePickListDefinitionByPickListId(String pickListId) {
		PickListDao plDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		
		plDao.removePickListDefinitionByPickListId(pickListId);
	}
	
}
