package org.lexevs.dao.database.service.listener;
import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;


public class DefaultServiceEventListener implements DatabaseServiceEventListener {

	public boolean onCodingSchemeUpdate(CodingSchemeUpdateEvent event) {
		return true;
	}

	public boolean onCodingSchemeInsert(CodingSchemeInsertEvent event) throws CodingSchemeAlreadyLoadedException {
		return true;
	}

}
