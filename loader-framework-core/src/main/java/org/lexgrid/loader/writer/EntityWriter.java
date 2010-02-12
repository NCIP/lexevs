package org.lexgrid.loader.writer;

import java.util.List;

import org.LexGrid.concepts.Entity;

public class EntityWriter extends AbstractCodingSchemeIdHolderWriter<Entity>{

	@Override
	public void doWrite(CodingSchemeUriVersionPair codingSchemeId, List<Entity> items) {
		this.getDatabaseServiceManager().
			getEntityService().
			insertBatchEntities(codingSchemeId.getUri(),
					codingSchemeId.getVersion(), items);	
	}
}
