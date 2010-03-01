package org.lexevs.dao.index.service;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;

public interface IndexService {

	public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity);
	
	public void removeIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity);
	
	public void createIndex(AbsoluteCodingSchemeVersionReference reference);
}
