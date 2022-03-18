
package org.lexevs.dao.database.operation.root;

import java.util.List;

import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.RootOrTail;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;

public interface RootBuilder {

	public void addRootRelationNode(
			String codingSchemeUri, 
			String codingSchemeVersion, 
			List<String> associationNames, 
			String relationContainerName, 
			RootOrTail rootOrTail, 
			TraverseAssociations traverse);
}