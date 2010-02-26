package org.lexevs.dao.database.service.listener;

import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertEvent;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.utility.RegistryUtility;

public class CodingSchemeRegisteringListener extends DefaultServiceEventListener {

	private Registry registry;
	
	@Override
	public boolean onCodingSchemeInsert(CodingSchemeInsertEvent event) throws CodingSchemeAlreadyLoadedException {
		CodingScheme scheme = event.getCodingScheme();
		
		boolean exists = registry.containsCodingSchemeEntry(

				DaoUtility.createAbsoluteCodingSchemeVersionReference(
						scheme.getCodingSchemeURI(), 
						scheme.getRepresentsVersion())

		);

		if(exists) {
			throw new CodingSchemeAlreadyLoadedException(
					scheme.getCodingSchemeURI(), 
					scheme.getRepresentsVersion());
		}

		RegistryEntry entry = RegistryUtility.codingSchemeToRegistryEntry(scheme);
		entry.setStatus(CodingSchemeVersionStatus.PENDING.toString());
		try {
			registry.addNewItem(entry);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		return true;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public Registry getRegistry() {
		return registry;
	}

}
