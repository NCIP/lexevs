/**
 * 
 */
package org.lexevs.dao.database.service.valuesets;

import java.util.List;
import java.util.Map;

import org.LexGrid.valueSets.PickListDefinition;
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

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#getReferencedPLDefinitions(java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public Map<String, String> getReferencedPLDefinitions(String entityCode,
			String entityCodeNameSpace, String propertyId,
			Boolean extractPickListName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#getReferencedPLDefinitions(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public Map<String, String> getReferencedPLDefinitions(String valueSet,
			Boolean extractPickListName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#insertPickListDefinition(org.LexGrid.valueSets.PickListDefinition)
	 */
	@Transactional
//	@DatabaseErrorIdentifier(errorCode=INSERT_PICKLIST_ERROR)
	public void insertPickListDefinition(String systemReleaseUri, PickListDefinition definition) {
		PickListDao plDao = this.getDaoManager().getCurrentPickListDefinitionDao();
	
		plDao.insertPickListDefinition(systemReleaseUri, definition);
		
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
