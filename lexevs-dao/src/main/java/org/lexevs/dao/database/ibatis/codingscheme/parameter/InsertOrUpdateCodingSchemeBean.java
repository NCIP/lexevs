
package org.lexevs.dao.database.ibatis.codingscheme.parameter;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertOrUpdateCodingSchemeBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateCodingSchemeBean extends IdableParameterBean{
	
	/** The coding scheme. */
	private CodingScheme codingScheme;
	
	private List<InsertOrUpdateCodingSchemeMultiAttribBean> csMultiAttribList = null;
	
	/** The system release uid*/
	private String releaseUId = null;
	/**
	 * Sets the coding scheme.
	 * 
	 * @param codingScheme the new coding scheme
	 */
	public void setCodingScheme(CodingScheme codingScheme) {
		this.codingScheme = codingScheme;
	}

	/**
	 * Gets the coding scheme.
	 * 
	 * @return the coding scheme
	 */
	public CodingScheme getCodingScheme() {
		return codingScheme;
	}

	public String getReleaseUId() {
		return releaseUId;
	}

	public void setReleaseUId(String releaseUId) {
		this.releaseUId = releaseUId;
	}

	/**
	 * @return the csMultiAttribList
	 */
	public List<InsertOrUpdateCodingSchemeMultiAttribBean> getCsMultiAttribList() {
		return csMultiAttribList;
	}

	/**
	 * @param csMultiAttribList the csMultiAttribList to set
	 */
	public void setCsMultiAttribList(
			List<InsertOrUpdateCodingSchemeMultiAttribBean> csMultiAttribList) {
		this.csMultiAttribList = csMultiAttribList;
	}
}