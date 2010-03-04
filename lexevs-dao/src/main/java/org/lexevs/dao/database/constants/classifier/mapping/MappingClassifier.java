package org.lexevs.dao.database.constants.classifier.mapping;

import org.LexGrid.naming.*;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.springframework.batch.classify.Classifier;

public class MappingClassifier implements Classifier<Class<? extends URIMap>,String>{

	public String classify(Class<? extends URIMap> clazz) {
		if(clazz == SupportedAssociation.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION; 
		}
		if(clazz == SupportedAssociationQualifier.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATIONQUALIFIER; 
		}
		if(clazz == SupportedCodingScheme.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME; 
		}
		if(clazz == SupportedConceptDomain.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_CONCEPTDOMAIN; 
		}
		if(clazz == SupportedContainerName.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTAINERNAME; 
		}
		if(clazz == SupportedContext.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT; 
		}
		if(clazz == SupportedDataType.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_DATATYPE; 
		}
		if(clazz == SupportedDegreeOfFidelity.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_DEGREEOFFIDELITY; 
		}
		if(clazz == SupportedEntityType.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_ENTITYTYPE; 
		}
		if(clazz == SupportedHierarchy.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_HIERARCHY; 
		}
		if(clazz == SupportedLanguage.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_LANGUAGE; 
		}
		if(clazz == SupportedNamespace.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_NAMESPACE; 
		}
		if(clazz == SupportedProperty.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY; 
		}
		if(clazz == SupportedPropertyLink.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYLINK; 
		}
		if(clazz == SupportedPropertyQualifier.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIER; 
		}
		if(clazz == SupportedPropertyQualifierType.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIERTYPE; 
		}
		if(clazz == SupportedPropertyType.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYTYPE; 
		}
		if(clazz == SupportedRepresentationalForm.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM; 
		}
		if(clazz == SupportedSortOrder.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_SORTORDER; 
		}
		if(clazz == SupportedSource.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE; 
		}
		if(clazz == SupportedSourceRole.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCEROLE; 
		}
		if(clazz == SupportedStatus.class){ 
		    return SQLTableConstants.TBLCOLVAL_SUPPTAG_STATUS; 
		}
		else throw new RuntimeException("Class:" + clazz.getName() + " is not Classifiable.");
	}
}
