package org.lexevs.dao.index.indexer;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.SystemResourceService;

public class LuceneLoaderCodeIndexer extends LuceneLoaderCode implements Indexer {

	private SystemResourceService systemResourceService;
	private SystemVariables systemVariables;
	
	private String indexName = "commonIndex";
	
	public LuceneLoaderCodeIndexer(){
		this.normEnabled_ = false;
	}
	
	public void indexEntity(String codingSchemeUri, String codingSchemeVersion,
			Entity entity) {
		for(Property prop : entity.getAllProperties()) {
			try {
				this.indexEntity(codingSchemeUri, codingSchemeVersion, entity, prop);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected void indexEntity(String codingSchemeUri, String codingSchemeVersion,
			Entity entity, Property prop) throws Exception {
		
		boolean isPreferred = false;
		String degreeOfFidelity = "";
		boolean matchIfNoContext = false;
		String repForm = "";
		if(prop instanceof Presentation) {
			Presentation pres = (Presentation)prop;
			isPreferred = pres.getIsPreferred();
			degreeOfFidelity = pres.getDegreeOfFidelity();
			matchIfNoContext = pres.getMatchIfNoContext();
			repForm = pres.getRepresentationalForm();
		}
		
		this.addEntity(
				systemResourceService.
					getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, codingSchemeVersion), 
				codingSchemeUri, 
				entity.getEntityCode(), 
				entity.getEntityCodeNamespace(), 
				entity.getEntityType(0), //TODO: Allow multple Entity Types
				entity.getEntityDescription().getContent(), 
				prop.getPropertyType(), 
				prop.getPropertyName(), 
				prop.getValue().getContent(), 
				prop.getIsActive(), 
				prop.getValue().getDataType(), 
				prop.getLanguage(),
				isPreferred,
				entity.getStatus(),
				prop.getPropertyId(), 
				degreeOfFidelity,
				matchIfNoContext,
				repForm,
				sourceToString(prop.getSource()), 
				prop.getUsageContext(), 
				propertyQualifiersToQualifiers(prop.getPropertyQualifier()), 
				null);
	}
	
	private Qualifier[] propertyQualifiersToQualifiers(PropertyQualifier[] qualifiers) {
		Qualifier[] quals = new Qualifier[qualifiers.length];
		for(int i=0;i<qualifiers.length;i++) {
			quals[i] = new Qualifier(qualifiers[i].getPropertyQualifierName(), qualifiers[i].getValue().getContent());
		}
		return quals;
	}
	
	private String[] sourceToString(Source[] sources) {
		String[] stringSource = new String[sources.length];
		for(int i=0;i<sources.length;i++) {
			stringSource[i] = sources[i].getContent();
		}
		return stringSource;
	}

	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	public void closeIndex() {
		try {
			this.closeIndexes();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void openIndex() {
		try {
			this.initIndexes(indexName, this.systemVariables.getAutoLoadIndexLocation());
			this.createIndex();
			this.openIndexes();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

}
