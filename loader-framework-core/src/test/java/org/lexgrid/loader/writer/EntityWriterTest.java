
package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.lexgrid.loader.test.util.SupportHelpers.TestCodingSchemeIdSetter;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityWriterTest extends LoaderFrameworkCoreTestBase {

	@Autowired
	private EntityWriter entityWriter;
	
	@Autowired
    private AuthoringService authoringService;
	
	@Autowired
	private LexEvsServiceLocator lexEvsServiceLocator;
	
	@Autowired
	private EntityService entityService;
	
	@Test
	public void testEntity() throws Exception{
		TestCodingSchemeIdSetter test = new TestCodingSchemeIdSetter();
		
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri(test.getCodingSchemeUri());
		entry.setResourceVersion(test.getCodingSchemeVersion());
		entry.setDbSchemaVersion("2.0");
		lexEvsServiceLocator.getRegistry().addNewItem(entry);
		
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeName(test.getCodingSchemeUri());
		cs.setCodingSchemeURI(test.getCodingSchemeUri());
		cs.setRepresentsVersion(test.getCodingSchemeVersion());
		
		authoringService.loadRevision(cs, null, true);
		
		CodingSchemeIdHolder<Entity> holder = new CodingSchemeIdHolder<Entity>();
		holder.setCodingSchemeIdSetter(new TestCodingSchemeIdSetter());
		
		Entity e = new Entity();
		e.setEntityCode("code");
		e.setEntityCodeNamespace("ns");
		
		holder.setItem(e);
		
		List<CodingSchemeIdHolder<Entity>> list = new ArrayList<CodingSchemeIdHolder<Entity>>();
		list.add(holder);
		
		entityWriter.write(list);
		
		
	}
}