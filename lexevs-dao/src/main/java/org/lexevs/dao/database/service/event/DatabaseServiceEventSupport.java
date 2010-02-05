package org.lexevs.dao.database.service.event;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent;

public class DatabaseServiceEventSupport {

	private List<DatabaseServiceEventListener> databaseServiceEventListener = new ArrayList<DatabaseServiceEventListener>();

	protected void fireCodingSchemeUpdateEvent(
			String revisionId,
			String entryStateId,
			CodingScheme originalCodingScheme,
			CodingScheme updatedCodingScheme){
		if(databaseServiceEventListener != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListener){
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
			CodingScheme codingScheme){
		if(databaseServiceEventListener != null){
			for(DatabaseServiceEventListener listener : this.databaseServiceEventListener){
				listener.onCodingSchemeInsert(
						new CodingSchemeInsertEvent(
								codingScheme));
			}
		}
	}

	public List<DatabaseServiceEventListener> getDatabaseServiceEventListener() {
		return databaseServiceEventListener;
	}

	public void setDatabaseServiceEventListener(
			List<DatabaseServiceEventListener> databaseServiceEventListener) {
		this.databaseServiceEventListener = databaseServiceEventListener;
	}

}
