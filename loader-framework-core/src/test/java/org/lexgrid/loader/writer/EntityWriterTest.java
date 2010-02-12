package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.lexgrid.loader.test.util.SupportHelpers.TestCodingSchemeNameSetter;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityWriterTest extends LoaderFrameworkCoreTestBase {

	@Autowired
	private EntityWriter entityWriter;
	
	@Test
	public void testEntity() throws Exception{
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
