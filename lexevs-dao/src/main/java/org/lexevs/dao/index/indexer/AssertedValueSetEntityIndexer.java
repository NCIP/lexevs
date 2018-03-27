package org.lexevs.dao.index.indexer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.lexevs.dao.database.ibatis.entity.model.IdableEntity;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

public class AssertedValueSetEntityIndexer extends LuceneLoaderCodeIndexer implements EntityIndexer {
	

	public List<Document> indexEntity(String codingSchemeName, String codingSchemeUri,
			String codingSchemeVersion, String vsURI, String vsName, Entity entity) {
List<Document> returnList = new ArrayList<Document>();
		
		try {
			
			String entityUid = null;
			if(entity instanceof IdableEntity) {
				entityUid = ((IdableEntity)entity).getId();
			}
			
			Document parentDoc = createParentDocument(
					codingSchemeName, 
					codingSchemeUri, 
					vsURI,
					vsName,
					codingSchemeVersion, 
					entity,
					entityUid);
			
			if(entity.getAllProperties().length == 0) {
				entity.addPresentation(
						getDefaultPresentation(entity));
			}
			for(Property prop : entity.getAllProperties()) {
				returnList.add(
						indexProperty(codingSchemeName, codingSchemeUri, codingSchemeVersion, entity, prop));
			}
			
			returnList.add(parentDoc);
			
			return returnList;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



	@Override
	public LexEvsIndexFormatVersion getIndexerFormatVersion() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected Document createParentDocument(String codingSchemeName, String codingSchemeUri, String vsURI, String vsName, String codingSchemeVersion,
			Entity entity, String entityUid) {

		generator_.startNewDocument(codingSchemeName + "-" + entity.getEntityCode());
		generator_.addTextField("codingSchemeName", vsName, true, true, false);
		generator_.addTextField("codingSchemeUri", vsURI, true, true, false);
		for(int i = 0; i < 5; i++) {
		generator_.addTextField("codingSchemeVersion", codingSchemeVersion + i, true, true, false);
		}
		generator_.addTextField("entityCode", entity.getEntityCode(), true, true, false);
		generator_.addTextField("entityCodeNamespace", entity.getEntityCodeNamespace(), true, true, false);
		generator_.addTextField("entityDescription", entity.getEntityDescription() != null &&
				entity.getEntityDescription().getContent() != null ? entity.getEntityDescription().getContent() : "ENTITY DESCRIPTION ABSENT", true, true,
				false);

		generator_.addTextField("isParentDoc", "true", true, true, false);

		generator_.addTextField(CODING_SCHEME_URI_VERSION_KEY_FIELD,
				createCodingSchemeUriVersionKey(codingSchemeUri, codingSchemeVersion), false, true, false);
		generator_.addTextField(CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD,
				createCodingSchemeUriVersionCodeNamespaceKey(codingSchemeUri, codingSchemeVersion, entity.getEntityCode(),
						entity.getEntityCodeNamespace()),
				false, true, false);

		return generator_.getDocument();
	}
	
	private Document indexProperty(String codingSchemeName, String codingSchemeUri, String codingSchemeVersion, Entity entity, Property prop) {
		
		if(prop instanceof Presentation) {
			Presentation pres = (Presentation)prop;
			
			if(pres.isIsPreferred() == null) {
				pres.setIsPreferred (false);
			} 
		}
		
		return this.addProperty(
				codingSchemeName, 
				codingSchemeUri, 
				codingSchemeVersion,
				entity,
				prop);
	}



	protected Document addProperty(String codingSchemeName, String codingSchemeUri, String codingSchemeVersion,
			Entity entity,
			Property prop) {
		 if(entity.getEntityCode() == null || entity.getEntityCodeNamespace() == null) {throw new RuntimeException("Entity code or namespace cannot be null for " + entity.getEntityCode());}
		 if(prop.getPropertyName() == null || prop.getPropertyType() == null || prop.getValue() == null || prop.getValue().getContent() == null) {
			 throw new RuntimeException("Property Name or Value cannot be null for entity " + entity.getEntityCode() + ":" + prop.getPropertyId());}

		 String  propertyFieldName = SQLTableConstants.TBLCOL_PROPERTYNAME;
	       
	        generator_.startNewDocument(codingSchemeName + "-" + entity.getEntityCode());
	        generator_.addTextField(UNIQUE_ID + "Tokenized", entity.getEntityCode(), false, true, true);
	        generator_.addTextField(UNIQUE_ID, entity.getEntityCode(), false, true, false);// must be analyzed with KeywordAnalyzer
	        generator_.addTextField(UNIQUE_ID + "LC", entity.getEntityCode().toLowerCase(), false, true, false);
	        
	        
	        generator_.addTextField(CODING_SCHEME_URI_VERSION_KEY_FIELD, 
	        		createCodingSchemeUriVersionKey(codingSchemeUri, codingSchemeVersion), false, true, false);
	        generator_.addTextField(CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, 
	        		createCodingSchemeUriVersionCodeNamespaceKey(codingSchemeUri, codingSchemeVersion, 
	        				entity.getEntityCode(), entity.getEntityCodeNamespace()), false, true, false);
	     // must be analyzed with KeywordAnalyzer
	        generator_.addTextField(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE, entity.getEntityCodeNamespace(), false, true, false);

	        String tempPropertyType;
	        if (prop.getPropertyType() == null || prop.getPropertyType().length() == 0) {
	            if (prop.getPropertyName().equalsIgnoreCase("textualPresentation")) {
	                tempPropertyType = "presentation";
	            } else if (prop.getPropertyName().equals("definition")) {
	                tempPropertyType = "definition";
	            } else if (prop.getPropertyName().equals("comment")) {
	                tempPropertyType = "comment";
	            } else if (prop.getPropertyName().equals("instruction")) {
	                tempPropertyType = "instruction";
	            } else {
	                tempPropertyType = propertyFieldName;
	            }
	        } else {
	            tempPropertyType = prop.getPropertyType();
	        }
	        generator_.addTextField("propertyType", tempPropertyType, false, true, false);

	        generator_.addTextField(propertyFieldName, prop.getPropertyName(), false, true, false);

	        if (StringUtils.isNotBlank(prop.getValue().getContent())) {
	            generator_.addTextField(PROPERTY_VALUE_FIELD, prop.getValue().getContent(), false, true, true);
	            
	            generator_.addTextField(REVERSE_PROPERTY_VALUE_FIELD, 
	                    reverseTermsInPropertyValue(prop.getValue().getContent()), false, true, true);
	            
	            generator_.addTextField(LITERAL_PROPERTY_VALUE_FIELD, prop.getValue().getContent(), false, true, true);
	            
	            generator_.addTextField(LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD, 
	                    reverseTermsInPropertyValue(prop.getValue().getContent()), false, true, true);

	            // This copy of the content is required for making "startsWith" or
	            // "exactMatch" types of queries
	            generator_.addTextField(UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD, prop.getValue().getContent().getBytes().length > 32000? prop.getValue().getContent().substring(0, 1000) : prop.getValue().getContent().toLowerCase(), false, true, false);
	            if(prop.getValue().getContent().getBytes().length > 32000){
	            	logger.warn("Term is of a size exceeding 32k bytes.  Truncating term that starts with: \"" + prop.getValue().getContent().substring(0, 100) + "\"");
	            }
	        }

	        boolean isPreferred = false;
	        if(prop instanceof Presentation) {
	        isPreferred = ((Presentation) prop).isIsPreferred();
	        }
	        if (isPreferred) {
	                generator_.addTextField("isPreferred", "T", false, true, false);
	            } else {
	                generator_.addTextField("isPreferred", "F", false, true, false);
	            }
	        
	        Source[] sources = prop.getSource();
	        if (sources != null && sources.length > 0) {
	            StringBuffer temp = new StringBuffer();
	            for (int i = 0; i < sources.length; i++) {
	                temp.append(sources[i]);
	                if (i + 1 < sources.length) {
	                    temp.append(STRING_TOKENIZER_TOKEN);
	                }
	            }
	            generator_.addTextField("sources", temp.toString(), false, true, true);
	        }

	        PropertyQualifier[] qualifiers = prop.getPropertyQualifier();
	        if (qualifiers != null && qualifiers.length > 0) {
	            StringBuffer temp = new StringBuffer();
	            for (int i = 0; i < qualifiers.length; i++) {
	                temp.append(qualifiers[i].getPropertyQualifierName() + QUALIFIER_NAME_VALUE_SPLIT_TOKEN
	                        + qualifiers[i].getValue().getContent());
	                if (i + 1 < qualifiers.length) {
	                    temp.append(STRING_TOKENIZER_TOKEN);
	                }
	            }
	            generator_.addTextField("qualifiers", temp.toString(), false, true, true);
	        }

	        return generator_.getDocument();
	}

}
