package org.lexgrid.loader.writer;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.junit.Test;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.springframework.beans.factory.annotation.Autowired;

public class CodingSchemeWriterTest extends LoaderFrameworkCoreTestBase {

	@Autowired
	private CodingSchemeWriter codingSchemeWriter;
	
	@Test
	public void testInsertCodingScheme() throws Exception{
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("csUri");
		cs.setRepresentsVersion("v1");
		
		List<CodingScheme> csList = DaoUtility.createList(cs, CodingScheme.class);
		
		codingSchemeWriter.write(csList);
	}
}
