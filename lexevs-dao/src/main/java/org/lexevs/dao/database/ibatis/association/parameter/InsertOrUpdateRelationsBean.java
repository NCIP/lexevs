
package org.lexevs.dao.database.ibatis.association.parameter;

import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertRelationsBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateRelationsBean extends IdableParameterBean {
	
	/** The relations. */
	private Relations relations;
	
	/** The coding scheme id. */
	private String codingSchemeUId;

	/**
	 * Sets the relations.
	 * 
	 * @param relations the new relations
	 */
	public void setRelations(Relations relations) {
		this.relations = relations;
	}

	/**
	 * Gets the relations.
	 * 
	 * @return the relations
	 */
	public Relations getRelations() {
		return relations;
	}

	/**
	 * Sets the coding scheme id.
	 * 
	 * @param codingSchemeUId the new coding scheme id
	 */
	public void setCodingSchemeUId(String codingSchemeUId) {
		this.codingSchemeUId = codingSchemeUId;
	}

	/**
	 * Gets the coding scheme id.
	 * 
	 * @return the coding scheme id
	 */
	public String getCodingSchemeUId() {
		return codingSchemeUId;
	}
}