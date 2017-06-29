package org.lexevs.dao.database.access.association.model;

public class VSHierarchyNode implements Comparable<VSHierarchyNode>{
	private String entityCode;
	private String namespace;
	private String description;
	private String source;

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public int compareTo(VSHierarchyNode o) {
		return this.getDescription().compareTo(o.getDescription());
	}

}
