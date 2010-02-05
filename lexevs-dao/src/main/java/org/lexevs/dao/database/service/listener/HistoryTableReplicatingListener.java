package org.lexevs.dao.database.service.listener;

import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent;

public class HistoryTableReplicatingListener extends DefaultServiceEventListener {
	
	private DaoManager daoManager;

	@Override
	public boolean onCodingSchemeUpdate(CodingSchemeUpdateEvent event) {
		EntryState es = new EntryState();
		es.setChangeType(ChangeType.MODIFY);
		daoManager.getVersionsDao().updateEntryState(event.getEntryStateId(), es);
		daoManager.getCodingSchemeDao().insertHistoryCodingScheme(event.getOriginalCodingScheme());
		return true;
	}

	
}
