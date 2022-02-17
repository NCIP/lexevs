
package org.lexgrid.loader.writer;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.springframework.batch.item.ItemWriter;

public class CodingSchemeWriter extends AbstractDatabaseServiceWriter implements ItemWriter<CodingScheme>{

	public void write(List<? extends CodingScheme> schemes) throws Exception {
		
		for(CodingScheme scheme : schemes ){
			this.getDatabaseServiceManager().getAuthoringService().loadRevision(scheme, null, null);
		}
	}
}