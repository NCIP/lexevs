
package org.lexevs.dao.database.constants.classifier.property;

import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.springframework.classify.Classifier;

public class EntryStateTypeClassifier implements Classifier<EntryStateType,String>{

	//TODO: Figure out which constants to use here...
	/* (non-Javadoc)
	 * @see org.springframework.batch.classify.Classifier#classify(java.lang.Object)
	 */
	public String classify(EntryStateType type) {
		
		if(type.equals(EntryStateType.CODINGSCHEME)){
			return "codingScheme";
		}
		if(type.equals(EntryStateType.ENTITY)){
			return "entity";
		}
		if(type.equals(EntryStateType.PROPERTY)){
			return "property";
		}
		if(type.equals(EntryStateType.RELATION)){
			return "relation";
		}
		if(type.equals(EntryStateType.ENTITYASSNSTOENTITY)){
			return "entityAssnsToEntity";
		}
		if(type.equals(EntryStateType.ENTITYASSNSTODATA)){
			return "entityAssnsToData";
		}
		if(type.equals(EntryStateType.VALUESETDEFINITION)){
			return "valueSetDefinition";
		}
		if(type.equals(EntryStateType.VALUESETDEFINITIONENTRY)){
			return "vsdEntry";
		}
		if(type.equals(EntryStateType.PICKLISTDEFINITION)){
			return "vsPickList";
		}
		if(type.equals(EntryStateType.PICKLISTENTRYNODE)){
			return "vsPLEntry";
		}
		else throw new RuntimeException("Class:" + type + " is not Classifiable.");
	}
}