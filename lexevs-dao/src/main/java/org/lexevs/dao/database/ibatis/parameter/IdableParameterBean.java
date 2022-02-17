
package org.lexevs.dao.database.ibatis.parameter;

/**
 * The Class IdableParameterBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IdableParameterBean extends PrefixedTableParameterBean {

	/** The id. */
	private String uid;
	
	/** The entry state id. */
	private String entryStateUId;
	
	/**
	 * Instantiates a new idable parameter bean.
	 */
	public IdableParameterBean() {
		super();
	}
	
	/**
	 * Instantiates a new idable parameter bean.
	 * 
	 * @param prefix the prefix
	 * @param uid the id
	 * @param entryStateUId the entry state id
	 */
	public IdableParameterBean(String prefix, String uid, String entryStateUId) {
		super(prefix);
		this.uid = uid;
		this.entryStateUId = entryStateUId;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getUId() {
		return uid;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setUId(String id) {
		this.uid = id;
	}
	
	/**
	 * Gets the entry state id.
	 * 
	 * @return the entry state id
	 */
	public String getEntryStateUId() {
		return entryStateUId;
	}
	
	/**
	 * Sets the entry state id.
	 * 
	 * @param entryStateUId the new entry state id
	 */
	public void setEntryStateUId(String entryStateUId) {
		this.entryStateUId = entryStateUId;
	}
}