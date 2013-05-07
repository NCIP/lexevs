/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.constants.classifier.property;

import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.springframework.batch.classify.Classifier;

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