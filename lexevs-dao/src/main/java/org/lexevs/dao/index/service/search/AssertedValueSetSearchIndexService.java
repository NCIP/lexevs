package org.lexevs.dao.index.service.search;

import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.concepts.Entity;

public interface AssertedValueSetSearchIndexService extends SearchIndexService {

	@LgAdminFunction
	public void updateIndexForEntity(String codingSchemeUri,String codingSchemeVersion,
			String vsURI, String vsName, Entity entity);
	
	@LgAdminFunction
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion,
			String vsURI, String vsName, Entity entity);
	
	@LgAdminFunction
	public void deleteEntityFromIndex(String codingSchemeUri, String codingSchemeVersion, 
			String vsURI, String vsName, Entity entity);
	

}
