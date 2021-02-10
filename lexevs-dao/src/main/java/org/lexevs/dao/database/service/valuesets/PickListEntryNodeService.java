
package org.lexevs.dao.database.service.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.PickListEntryNode;

/**
 * The Interface PickListEntryNodeService.
 */
public interface PickListEntryNodeService {

	/**
	 * Insert pick list entry node.
	 * 
	 * @param pickListId the pick list id
	 * @param pickListEntryNode the pick list entry node
	 */
	public void insertPickListEntryNode(String pickListId, PickListEntryNode pickListEntryNode);
	
	/**
	 * Removes the pick list entry node.
	 * 
	 * @param pickListId the pick list id
	 * @param pickListEntryNode the pick list entry node
	 */
	public void removePickListEntryNode(String pickListId, PickListEntryNode pickListEntryNode);
	
	/**
	 * Update pick list entry node.
	 * 
	 * @param pickListId the pick list id
	 * @param pickListEntryNode the pick list entry node
	 * 
	 * @throws LBException the LB exception
	 */
	public void updatePickListEntryNode(String pickListId, PickListEntryNode pickListEntryNode) throws LBException;
	
	/**
	 * Insert dependent changes.
	 * 
	 * @param pickListId the pick list id
	 * @param pickListEntryNode the pick list entry node
	 * 
	 * @throws LBException the LB exception
	 */
	public void insertDependentChanges(String pickListId, PickListEntryNode pickListEntryNode) throws LBException;
	
	/**
	 * Update versionable attributes.
	 * 
	 * @param pickListId the pick list id
	 * @param pickListEntryNode the pick list entry node
	 * 
	 * @throws LBException the LB exception
	 */
	public void updateVersionableAttributes(String pickListId, PickListEntryNode pickListEntryNode) throws LBException;
	
	/**
	 * Revise.
	 * 
	 * @param pickListId the pick list id
	 * @param pickListEntryNode the pick list entry node
	 * 
	 * @throws LBException the LB exception
	 */
	public void revise(String pickListId, PickListEntryNode pickListEntryNode) throws LBException;

	/**
	 * Resolve pick list entry node by revision.
	 * 
	 * @param pickListId the pick list id
	 * @param plEntryId the pl entry id
	 * @param revisionId the revision id
	 * 
	 * @return the pick list entry node
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public PickListEntryNode resolvePickListEntryNodeByRevision(String pickListId,
			String plEntryId, String revisionId) throws LBRevisionException;

}