package org.lexevs.dao.database.service.event;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

public class DatabaseServiceEventSupport {

	private List<DatabaseServiceEventListener> databaseServiceEventListeners = new ArrayList<DatabaseServiceEventListener>();

	protected void fireCodingSchemeUpdateEvent(
			String revisionId,
			String entryStateId,
			CodingScheme originalCodingScheme,
			CodingScheme updatedCodingScheme){
		if(databaseServiceEventListeners != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListeners){
				listener.onCodingSchemeUpdate(
						new CodingSchemeUpdateEvent(
								revisionId,
								entryStateId,
								originalCodingScheme,
								updatedCodingScheme));
			}
		}
	}
	
	protected void fireCodingSchemeInsertEvent(
			CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException{
		if(databaseServiceEventListeners != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListeners){
				listener.onCodingSchemeInsert(
						new CodingSchemeInsertEvent(
								codingScheme));
			}
		}
	}

	public List<DatabaseServiceEventListener> getDatabaseServiceEventListeners() {
		return databaseServiceEventListeners;
	}

	public void setDatabaseServiceEventListeners(
			List<DatabaseServiceEventListener> databaseServiceEventListeners) {
		this.databaseServiceEventListeners = databaseServiceEventListeners;
	}

}
