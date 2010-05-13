package org.lexevs.dao.database.operation.root;

import java.util.List;

import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.RootOrTail;

public interface RootBuilder {

	public void addRootRelationNode(String codingSchemeUri, String codingSchemeVersion, 
			List<String> associationNames, String relationContainerName, RootOrTail rootOrTail);
}
