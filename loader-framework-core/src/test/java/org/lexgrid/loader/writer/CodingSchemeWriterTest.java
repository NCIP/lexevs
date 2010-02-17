package org.lexgrid.loader.writer;

import static org.junit.Assert.*;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.system.ResourceManager;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.springframework.beans.factory.annotation.Autowired;

public class CodingSchemeWriterTest extends LoaderFrameworkCoreTestBase {

	@Autowired
	private CodingSchemeWriter codingSchemeWriter;
	
	@Autowired
	private ResourceManager resourceManager;
	
	@Autowired
	private CodingSchemeService codingSchemeService;
	
	@Test
	public void testInsertCodingScheme() throws Exception{
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("csUri");
		entry.setResourceVersion("v1");
		entry.setDbSchemaVersion("2.0");
		resourceManager.getRegistry().addNewItem(entry);
		
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("csUri");
		cs.setRepresentsVersion("v1");
		
		List<CodingScheme> csList = DaoUtility.createList(cs, CodingScheme.class);
		
		codingSchemeWriter.write(csList);
		
		CodingScheme foundCs = 
			codingSchemeService.getCodingSchemeByUriAndVersion("csUri", "v1");
		
		assertEquals("csUri", foundCs.getCodingSchemeURI());
		assertEquals("v1", foundCs.getRepresentsVersion());
		
	}
}
