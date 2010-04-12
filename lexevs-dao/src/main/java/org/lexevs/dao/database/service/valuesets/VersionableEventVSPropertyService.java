package org.lexevs.dao.database.service.valuesets;

import org.LexGrid.commonTypes.Property;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.service.AbstractDatabaseService;

public class VersionableEventVSPropertyService extends AbstractDatabaseService implements VSPropertyService{

	@Override
	public void insertPickListDefinitionProperty(String pickListId,
			Property property) {
		String pickListGuid = this.getDaoManager().getCurrentPickListDefinitionDao().getPickListGuidFromPickListId(pickListId);
		
		this.getDaoManager().getCurrentVsPropertyDao().insertProperty(pickListGuid, ReferenceType.PICKLISTDEFINITION, property);
		
	}

	@Override
	public void insertPickListEntryNodeProperty(String pickListId,
			String pickListEntryNodeId, Property property) {
		String pickListEntryNodeGuid = this.getDaoManager().getCurrentPickListDefinitionDao().getPickListEntryNodeGuidByPickListIdAndPLEntryId(pickListId, pickListEntryNodeId);
		
		this.getDaoManager().getCurrentVsPropertyDao().insertProperty(pickListEntryNodeGuid, ReferenceType.PICKLISTENTRY, property);
		
	}

	@Override
	public void insertValueSetDefinitionProperty(String valueSetDefinitionUri,
			Property property) {
		String valueSetDefGuid = this.getDaoManager().getCurrentValueSetDefinitionDao().getGuidFromvalueSetDefinitionURI(valueSetDefinitionUri);
		
		this.getDaoManager().getCurrentVsPropertyDao().insertProperty(valueSetDefGuid, ReferenceType.VALUESETDEFINITION, property);
	}

	@Override
	public void updatePickListDefinitionProperty(String pickListId,
			String propertyId, Property property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePickListEntryNodeProperty(String pickListId,
			String pickListEntryNodeId, String propertyId, Property property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateValueSetDefinitionProperty(String valueSetDefinitionUri,
			String propertyId, Property property) {
		// TODO Auto-generated method stub
		
	}

}
