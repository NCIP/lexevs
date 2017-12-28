package org.lexevs.dao.index.indexer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.lexevs.dao.database.ibatis.entity.model.IdableEntity;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

public class AssertedValueSetEntityIndexer extends LuceneLoaderCodeIndexer implements EntityIndexer {

	public List<Document> indexEntity(String codingSchemeName, String codingSchemeUri, String codingSchemeVersion, Entity entity) {
List<Document> returnList = new ArrayList<Document>();
		
		try {
			
			String entityUid = null;
			if(entity instanceof IdableEntity) {
				entityUid = ((IdableEntity)entity).getId();
			}
			
			Document parentDoc = this.createParentDocument(
					codingSchemeName, 
					codingSchemeUri, 
					codingSchemeVersion, 
					entity.getEntityCode(),
					entity.getEntityCodeNamespace(),
					entity.getEntityDescription(),
					entity.getIsActive(),
					entity.getIsAnonymous(),
					entity.getIsDefined(),
					entity.getEntityType(),
					entity.getStatus(),
					entityUid,
					isParent);
			
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
	
	protected Document createParentDocument(String codingSchemeName, String codingSchemeUri, String codingSchemeVersion,
			String entityCode, String entityCodeNamespace, EntityDescription entityDescription) {

		generator_.startNewDocument(codingSchemeName + "-" + entityCode);
		generator_.addTextField("codingSchemeName", codingSchemeName, true, true, false);
		generator_.addTextField("codingSchemeUri", codingSchemeUri, true, true, false);
		generator_.addTextField("codingSchemeVersion", codingSchemeVersion, true, true, false);
		generator_.addTextField("entityCode", entityCode, true, true, false);
		generator_.addTextField("entityCodeNamespace", entityCodeNamespace, true, true, false);
		generator_.addTextField("entityDescription",
				entityDescription != null ? entityDescription.getContent() : "ENTITY DESCRIPTION ABSENT", true, true,
				false);

		generator_.addTextField("isParentDoc", "true", true, true, false);

		generator_.addTextField(CODING_SCHEME_URI_VERSION_KEY_FIELD,
				createCodingSchemeUriVersionKey(codingSchemeUri, codingSchemeVersion), false, true, false);
		generator_.addTextField(CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD,
				createCodingSchemeUriVersionCodeNamespaceKey(codingSchemeUri, codingSchemeVersion, entityCode,
						entityCodeNamespace),
				false, true, false);

		return generator_.getDocument();
	}
	
	private Document indexProperty(String codingSchemeName, String codingSchemeUri, String codingSchemeVersion, Entity entity, Property prop) {
		
		boolean isPreferred = false;
		if(prop instanceof Presentation) {
			Presentation pres = (Presentation)prop;
			
			if(pres.isIsPreferred() == null) {
				isPreferred = false;
			} else {
				isPreferred = pres.isIsPreferred();
			}
		}
		
		return this.addProperty(
				codingSchemeName, 
				codingSchemeUri, 
				codingSchemeVersion,
				entity.getEntityCode(), 
				entity.getEntityCodeNamespace(), 
				entity.getEntityType(),
				DaoUtility.getEntityDescriptionText(entity.getEntityDescription()),
				prop.getPropertyType(), 
				prop.getPropertyName(), 
				prop.getValue().getContent(),
				isPreferred,
				sourceToString(prop.getSource()), 
				propertyQualifiersToQualifiers(prop.getPropertyQualifier()));
	}



	private Document addProperty(String codingSchemeName, String codingSchemeUri, String codingSchemeVersion,
			String entityCode, String entityCodeNamespace, String[] entityType, String entityDescriptionText,
			String propertyType, String propertyName, String propertyValue, Boolean isPreferred, String[] sources,
			Qualifier[] qualifiers) {
		 String  propertyFieldName = SQLTableConstants.TBLCOL_PROPERTYNAME;
	       
	        generator_.startNewDocument(codingSchemeName + "-" + entityCode);
	        generator_.addTextField(UNIQUE_ID + "Tokenized", entityCode, false, true, true);
	        generator_.addTextField(UNIQUE_ID, entityCode, false, true, false);// must be anyalyzed with KeywordAnalyzer
	        generator_.addTextField(UNIQUE_ID + "LC", entityCode.toLowerCase(), false, true, false);
	        
	        
	        generator_.addTextField(CODING_SCHEME_URI_VERSION_KEY_FIELD, 
	        		createCodingSchemeUriVersionKey(codingSchemeUri, codingSchemeVersion), false, true, false);
	        generator_.addTextField(CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, 
	        		createCodingSchemeUriVersionCodeNamespaceKey(codingSchemeUri, codingSchemeVersion, 
	        				entityCode, entityCodeNamespace), false, true, false);
	     // must be analyzed with KeywordAnalyzer
	        generator_.addTextField(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE, entityCodeNamespace, false, true, false);

	        String tempPropertyType;
	        if (propertyType == null || propertyType.length() == 0) {
	            if (propertyName.equalsIgnoreCase("textualPresentation")) {
	                tempPropertyType = "presentation";
	            } else if (propertyName.equals("definition")) {
	                tempPropertyType = "definition";
	            } else if (propertyName.equals("comment")) {
	                tempPropertyType = "comment";
	            } else if (propertyName.equals("instruction")) {
	                tempPropertyType = "instruction";
	            } else {
	                tempPropertyType = propertyFieldName;
	            }
	        } else {
	            tempPropertyType = propertyType;
	        }
	        generator_.addTextField("propertyType", tempPropertyType, false, true, false);

	        generator_.addTextField(propertyFieldName, propertyName, false, true, false);

	        if (StringUtils.isNotBlank(propertyValue)) {
	            generator_.addTextField(PROPERTY_VALUE_FIELD, propertyValue, false, true, true);
	            
	            generator_.addTextField(REVERSE_PROPERTY_VALUE_FIELD, 
	                    reverseTermsInPropertyValue(propertyValue), false, true, true);
	            
	            generator_.addTextField(LITERAL_PROPERTY_VALUE_FIELD, propertyValue, false, true, true);
	            
	            generator_.addTextField(LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD, 
	                    reverseTermsInPropertyValue(propertyValue), false, true, true);

	            // This copy of the content is required for making "startsWith" or
	            // "exactMatch" types of queries
	            generator_.addTextField(UNTOKENIZED_LOWERCASE_PROPERTY_VALUE_FIELD, propertyValue.getBytes().length > 32000? propertyValue.substring(0, 1000) : propertyValue.toLowerCase(), false, true, false);
	            if(propertyValue.getBytes().length > 32000){
	            	logger.warn("Term is of a size exceeding 32k bytes.  Truncating term that starts with: \"" + propertyValue.substring(0, 100) + "\"");
	            }
	        }


	        if (isPreferred != null) {
	            if (isPreferred.booleanValue()) {
	                generator_.addTextField("isPreferred", "T", false, true, false);
	            } else {
	                generator_.addTextField("isPreferred", "F", false, true, false);
	            }
	        }

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

	        if (qualifiers != null && qualifiers.length > 0) {
	            StringBuffer temp = new StringBuffer();
	            for (int i = 0; i < qualifiers.length; i++) {
	                temp.append(qualifiers[i].qualifierName + QUALIFIER_NAME_VALUE_SPLIT_TOKEN
	                        + qualifiers[i].qualifierValue);
	                if (i + 1 < qualifiers.length) {
	                    temp.append(STRING_TOKENIZER_TOKEN);
	                }
	            }
	            generator_.addTextField("qualifiers", temp.toString(), false, true, true);
	        }

	        return generator_.getDocument();
	}

}
