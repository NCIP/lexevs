package org.lexgrid.loader.writer;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;

public class CodingSchemeWriter extends AbstractDatabaseServiceWriter{

	public void write(List<? extends CodingScheme> schemes) throws Exception {
		
		for(CodingScheme scheme : schemes ){
			this.getDatabaseServiceManager().getCodingSchemeService().insertCodingScheme(scheme);
		}
	}
}
