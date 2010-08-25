package org.lexevs.dao.database.service.event.association;

import java.util.List;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;

public class AssociationBatchInsertEvent {

	private String codingSchemeUri;
	private String version;
	private Relations relation;
	private AssociationSource source;
	private List<? extends AssociationSource> sources;

	public AssociationBatchInsertEvent(String codingSchemeUri, String version,
			Relations relations, List<? extends AssociationSource> sources) {
		super();
		this.codingSchemeUri = codingSchemeUri;
		this.version = version;
		this.relation = relations;
		this.sources = sources;
	}

	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}

	public void setCodingSchemeUri(String codingSchemeUri) {
		this.codingSchemeUri = codingSchemeUri;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Relations getRelation() {
		return relation;
	}

	public void setRelation(Relations relation) {
		this.relation = relation;
	}

	public List<? extends AssociationSource> getSources() {
		return sources;
	}

	public void setSources(List<? extends AssociationSource> sources) {
		this.sources = sources;
	}

	public AssociationSource getSource() {
		return source;
	}

	public void setSource(AssociationSource source) {
		this.source = source;
	}
}
