package org.lexevs.dao.database.service.event;

import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

public interface DatabaseServiceEventListener {

	public boolean onCodingSchemeUpdate(CodingSchemeUpdateEvent event);
	
	public boolean onCodingSchemeInsert(CodingSchemeInsertEvent event) throws CodingSchemeAlreadyLoadedException;
}
