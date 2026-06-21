package org.lexevs.dao.database.ibatis.mybatis.association.batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.LexGrid.relations.AssociationSource;

public class AssociationSourceAccumulator {
	
	private Boolean isCompleteList;
	
	private List<MetaSource> sources = new ArrayList<MetaSource>();
	

	public List<MetaSource> getSources() {
		return sources;
	}

	public void setSources(List<MetaSource> sources) {
		this.sources = sources;
	}
	
	public Boolean getIsCompleteList() {
		return isCompleteList;
	}

	public void setIsCompleteList(Boolean isCompleteList) {
		this.isCompleteList = isCompleteList;
	}

	public void addAllToList(List<MetaSource> processedSources) {
		sources.addAll(processedSources);
	}
	
	public void addToList(MetaSource processedSource) {
		sources.add(processedSource);
	}
	
	private class MetaSource {
		
		public MetaSource(AssociationSource source,  String predicateId,
				String codingSchemeId) {
			this.source = source;
			this.predicateId = predicateId;
			this.codingSchemeId = codingSchemeId;
		}
		
		private AssociationSource source;
		private String predicateId;
		private String codingSchemeId;

	
	}
	
	public void addMetaSource(AssociationSource source, String predicateId, String codingSchemeId) {
		addToList(new MetaSource(source, predicateId, codingSchemeId));
	}
	


}
