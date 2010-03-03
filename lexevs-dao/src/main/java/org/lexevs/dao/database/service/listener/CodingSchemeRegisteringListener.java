package org.lexevs.dao.database.service.listener;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertEvent;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;

public class CodingSchemeRegisteringListener extends DefaultServiceEventListener {
	
	@Override
	public boolean onCodingSchemeInsert(CodingSchemeInsertEvent event) throws CodingSchemeAlreadyLoadedException {
		SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
		CodingScheme scheme = event.getCodingScheme();
		try {
			
		boolean exists = systemResourceService.containsCodingSchemeResource(
				scheme.getCodingSchemeURI(), 
				scheme.getRepresentsVersion()
		);

		if(exists) {
			throw new CodingSchemeAlreadyLoadedException(
					scheme.getCodingSchemeURI(), 
					scheme.getRepresentsVersion());
		}

		
			systemResourceService.addCodingSchemeResourceFromSystem(
					scheme.getCodingSchemeURI(), 
				    scheme.getRepresentsVersion());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return true;
	}
}
