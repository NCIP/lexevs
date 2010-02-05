package org.lexevs.dao.index.model.compass.v20;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.compass.annotations.Reverse;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableMetaData;
import org.compass.annotations.SearchableMetaDatas;
import org.compass.annotations.SearchableProperty;
import org.lexevs.dao.index.model.IndexableResource;

@Searchable
public class IndexedProperty implements IndexableResource<CodeToReturn>{
	
	public static String ALIAS = "IndexedProperty";
	
	@SearchableId
	private String id;
	
	@SearchableProperty 
	private String entityCode;
	
	@SearchableProperty 
	private String entityCodeNamespace;
	
	@SearchableProperty 
	private String entityDescription;
	
	@SearchableProperty
	private String propertyType;
	
	@SearchableProperty
	private String propertyName;
	
	@SearchableProperty
	@SearchableMetaDatas(value={
			@SearchableMetaData(name = "literalValue"), 
			@SearchableMetaData(name = "reverseLiteralValue", reverse=Reverse.STRING)
	})
	private String value;
	
	private List<String> allPropertyQualifiers = new ArrayList<String>();
	
	private List<String> allSources = new ArrayList<String>();

	private List<String> allUsageContexts = new ArrayList<String>();
	
	@SearchableProperty
	private List<String> allPropertyNames = new ArrayList<String>();
	
	private List<String> allPropertyTypes = new ArrayList<String>();
	
	private List<String> propertyQualifiers = new ArrayList<String>();
	
	private List<String> sources = new ArrayList<String>();

	private List<String> usageContexts = new ArrayList<String>();
	
	private float score;
	
	public CodeToReturn getResultValue() {
		CodeToReturn codeToReturn = new CodeToReturn();
		codeToReturn.setCode(this.getEntityCode());
		codeToReturn.setEntityDescription(this.getEntityDescription());
		codeToReturn.setNamespace(this.getEntityCodeNamespace());
		
		return codeToReturn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getEntityCodeNamespace() {
		return entityCodeNamespace;
	}

	public void setEntityCodeNamespace(String entityCodeNamespace) {
		this.entityCodeNamespace = entityCodeNamespace;
	}

	public String getEntityDescription() {
		return entityDescription;
	}

	public void setEntityDescription(String entityDescription) {
		this.entityDescription = entityDescription;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getAllPropertyQualifiers() {
		return allPropertyQualifiers;
	}

	public void setAllPropertyQualifiers(List<String> allPropertyQualifiers) {
		this.allPropertyQualifiers = allPropertyQualifiers;
	}

	public List<String> getAllSources() {
		return allSources;
	}

	public void setAllSources(List<String> allSources) {
		this.allSources = allSources;
	}

	public List<String> getAllUsageContexts() {
		return allUsageContexts;
	}

	public void setAllUsageContexts(List<String> allUsageContexts) {
		this.allUsageContexts = allUsageContexts;
	}

	public List<String> getAllPropertyNames() {
		return allPropertyNames;
	}

	public void setAllPropertyNames(List<String> allPropertyNames) {
		this.allPropertyNames = allPropertyNames;
	}

	public List<String> getAllPropertyTypes() {
		return allPropertyTypes;
	}

	public void setAllPropertyTypes(List<String> allPropertyTypes) {
		this.allPropertyTypes = allPropertyTypes;
	}

	public List<String> getPropertyQualifiers() {
		return propertyQualifiers;
	}

	public void setPropertyQualifiers(List<String> propertyQualifiers) {
		this.propertyQualifiers = propertyQualifiers;
	}

	public List<String> getSources() {
		return sources;
	}

	public void setSources(List<String> sources) {
		this.sources = sources;
	}

	public List<String> getUsageContexts() {
		return usageContexts;
	}

	public void setUsageContexts(List<String> usageContexts) {
		this.usageContexts = usageContexts;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getScore() {
		return score;
	}
}
