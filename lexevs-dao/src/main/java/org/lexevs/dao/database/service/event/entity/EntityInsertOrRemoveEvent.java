package org.lexevs.dao.database.service.event.entity;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.concepts.Entity;

public class EntityInsertOrRemoveEvent {
	private String codingSchemeUri;
	private String version;
	private List<Entity> entityList;
	
	public EntityInsertOrRemoveEvent() {
		super();
	}
	
	public EntityInsertOrRemoveEvent(String codingSchemeUri, String version, Entity entity) {
		this.codingSchemeUri = codingSchemeUri;
		this.version = version;
		entityList = new ArrayList<Entity>();
		this.entityList.add(entity);
	}
	
	@SuppressWarnings("unchecked")
	public EntityInsertOrRemoveEvent(String codingSchemeUri, String version, List<? extends Entity> entityList) {
		this.codingSchemeUri = codingSchemeUri;
		this.version = version;
		this.entityList = (List<Entity>) entityList;
	}
	
	public String getCodingSchemeUri() {
		return this.codingSchemeUri;
	}
	
	public void setCodingSchemeUri(String codingSchemeUri) {
		this.codingSchemeUri = codingSchemeUri;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public List<Entity> getEntityList() {
		return this.entityList;
	}
	
	public void setEntity(List<Entity> entityList) {
		this.entityList = entityList;
	}
}
