
package org.lexevs.dao.database.service.listener;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertErrorEvent;
import org.lexevs.dao.database.service.event.codingscheme.PreCodingSchemeInsertEvent;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.service.SystemResourceService;

/**
 * The listener interface for receiving codingSchemeRegistering events.
 * The class that is interested in processing a codingSchemeRegistering
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCodingSchemeRegisteringListener<code> method. When
 * the codingSchemeRegistering event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see CodingSchemeRegisteringEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeRegisteringListener extends DefaultServiceEventListener {
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onCodingSchemeInsert(org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertEvent)
	 */
	@Override
	public boolean onPreCodingSchemeInsert(PreCodingSchemeInsertEvent event) throws CodingSchemeAlreadyLoadedException {
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

		systemResourceService.addCodingSchemeResourceToSystem(
					scheme.getCodingSchemeURI(),
					scheme.getRepresentsVersion());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onCodingSchemeInsertError(org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertErrorEvent)
	 */
	@Override
	public <T extends Exception> void onCodingSchemeInsertError(
			CodingSchemeInsertErrorEvent<T> codingSchemeInsertErrorEvent){
		SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
		try {
			systemResourceService.removeCodingSchemeResourceFromSystem(
					codingSchemeInsertErrorEvent.getCodingScheme().getCodingSchemeURI(),
					codingSchemeInsertErrorEvent.getCodingScheme().getRepresentsVersion());
		} catch (LBParameterException e) {
			LoggerFactory.getLogger().warn("Error removing coding scheme");
		}
	}
}