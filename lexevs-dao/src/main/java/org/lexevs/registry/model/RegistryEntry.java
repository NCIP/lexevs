package org.lexevs.registry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.hibernate.annotations.GenericGenerator;
import org.lexevs.dao.database.constants.DatabaseConstants;

@Entity
@Table(name=DatabaseConstants.PREFIX_PLACEHOLDER + "codingschemeentry")
public class RegistryEntry extends AbstractRegistryEntry {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="system-uuid")
 	@GenericGenerator(name="system-uuid", strategy = "uuid")
	public String id;
	
	@Column(name="status")
	private String status;
	
	@Column(name="tag")
	private String tag;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
