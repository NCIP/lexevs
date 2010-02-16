package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.system.ResourceManager;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.lexgrid.loader.test.util.SupportHelpers.TestCodingSchemeNameSetter;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityWriterTest extends LoaderFrameworkCoreTestBase {

	@Autowired
	private EntityWriter entityWriter;
	
	@Autowired
	private ResourceManager resourceManager;
	
	@Autowired
	private CodingSchemeService codingSchemeService;
	
	@Test
	public void testEntity() throws Exception{
		TestCodingSchemeNameSetter test = new TestCodingSchemeNameSetter();
		
		resourceManager.getLexEvsDatabaseOperations().createTables("");
		resourceManager.getRegistry().addNewItem(test.getCodingSchemeUri(), test.getCodingSchemeVersion(), "active", "url", null, null, null);
		
		
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeName(test.getCodingSchemeUri());
		cs.setCodingSchemeURI(test.getCodingSchemeUri());
		cs.setRepresentsVersion(test.getCodingSchemeVersion());
		
		codingSchemeService.insertCodingScheme(cs);
		
		CodingSchemeIdHolder<Entity> holder = new CodingSchemeIdHolder<Entity>();
		holder.setCodingSchemeIdSetter(new TestCodingSchemeNameSetter());
		
		Entity e = new Entity();
		e.setEntityCode("code");
		e.setEntityCodeNamespace("ns");
		
		holder.setItem(e);
		
		
		List<CodingSchemeIdHolder<Entity>> list = new ArrayList<CodingSchemeIdHolder<Entity>>();
		list.add(holder);
		
		entityWriter.write(list);
	}
}
