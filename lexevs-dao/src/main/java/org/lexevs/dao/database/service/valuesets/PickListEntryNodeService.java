package org.lexevs.dao.database.service.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.valueSets.PickListEntryNode;

public interface PickListEntryNodeService {

	public void insertPickListEntryNode(String pickListId, PickListEntryNode pickListEntryNode);
	
	public void removePickListEntryNode(String pickListId, PickListEntryNode pickListEntryNode);
	
	public void updatePickListEntryNode(String pickListId, PickListEntryNode pickListEntryNode) throws LBException;
	
	public void insertDependentChanges(String pickListId, PickListEntryNode pickListEntryNode) throws LBException;
	
	public void updateVersionableAttributes(String pickListId, PickListEntryNode pickListEntryNode) throws LBException;
	
	public void revise(String pickListId, PickListEntryNode pickListEntryNode) throws LBException;

}
