package org.lexevs.dao.database.service.entity;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;

public interface SourceAssertedValueSetEntityService extends EntityService {


	/**
	 * @param codingSchemeUri
	 * @param version
	 * @param start
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public List<? extends Entity> getEntities(String codingSchemeUri, String version, int start, int pageSize, AssertedValueSetParameters params);

	/**
	 * @param codingSchemeUri
	 * @param version
	 * @param start
	 * @param pageSize
	 * @return
	 */
	List<String> getEntityUids(String codingSchemeUri, String version, int start, int pageSize);

}
