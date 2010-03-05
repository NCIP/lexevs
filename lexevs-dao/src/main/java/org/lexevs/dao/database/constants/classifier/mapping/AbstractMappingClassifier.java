package org.lexevs.dao.database.constants.classifier.mapping;

import java.util.HashMap;
import java.util.Map;

import org.LexGrid.naming.*;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.springframework.batch.classify.Classifier;

public abstract class AbstractMappingClassifier<I,O> implements Classifier<I,O>{
	
	private Map<Class<? extends URIMap>,String> mappings = buildMap();
	
	@Override
	public O classify(I item) {
		try {
			return doClassify(item, mappings);
		} catch (Exception e) {
			throw getException(item);
		}
	}
	
	protected RuntimeException getException(Object item) {
		return new RuntimeException("Cound not classify: " + item.toString());
	}

	protected abstract O doClassify(I item, Map<Class<? extends URIMap>,String> mappngs);

	protected Map<Class<? extends URIMap>,String> buildMap() {
		Map<Class<? extends URIMap>,String> mappings = new HashMap<Class<? extends URIMap>,String>();
		
		mappings.put(
				SupportedAssociation.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION); 
		mappings.put(
				SupportedAssociationQualifier.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATIONQUALIFIER); 
		mappings.put(
				SupportedCodingScheme.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME); 
		mappings.put(
				SupportedConceptDomain.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_CONCEPTDOMAIN); 
		mappings.put(
				SupportedContainerName.class ,SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTAINERNAME); 
		mappings.put(
				SupportedContext.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT); 
		mappings.put(
				SupportedDataType.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_DATATYPE); 
		mappings.put(
				SupportedDegreeOfFidelity.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_DEGREEOFFIDELITY); 
		mappings.put(
				SupportedEntityType.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_ENTITYTYPE); 
		mappings.put(
				SupportedHierarchy.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_HIERARCHY); 
		mappings.put(
				SupportedLanguage.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_LANGUAGE); 
		mappings.put(
				SupportedNamespace.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_NAMESPACE); 
		mappings.put(
				SupportedProperty.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY); 
		mappings.put(
				SupportedPropertyLink.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYLINK); 
		mappings.put(
				SupportedPropertyQualifier.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIER); 
		mappings.put(
				SupportedPropertyQualifierType.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIERTYPE); 
		mappings.put(
				SupportedPropertyType.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYTYPE); 
		mappings.put(
				SupportedRepresentationalForm.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM); 
		mappings.put(
				SupportedSortOrder.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_SORTORDER); 
		mappings.put(
				SupportedSource.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE); 
		mappings.put(
				SupportedSourceRole.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCEROLE); 
		mappings.put(
				SupportedStatus.class, SQLTableConstants.TBLCOLVAL_SUPPTAG_STATUS); 
		
		return mappings;
	}
}
