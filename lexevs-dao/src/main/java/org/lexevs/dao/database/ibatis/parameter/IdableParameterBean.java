package org.lexevs.dao.database.ibatis.parameter;

public class IdableParameterBean extends PrefixedTableParameterBean {

	private String id;
	private String entryStateId;
	
	public IdableParameterBean() {
		super();
	}
	
	public IdableParameterBean(String prefix, String id, String entryStateId) {
		super(prefix);
		this.id = id;
		this.entryStateId = entryStateId;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEntryStateId() {
		return entryStateId;
	}
	public void setEntryStateId(String entryStateId) {
		this.entryStateId = entryStateId;
	}
}
