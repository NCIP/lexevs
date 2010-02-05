package org.lexevs.dao.index.compass;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.compass.core.Compass;
import org.compass.core.CompassQueryBuilder.CompassBooleanQueryBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.lexevs.dao.index.model.compass.v20.IndexedEntityBuilder;
import org.lexevs.dao.index.model.compass.v20.IndexedProperty;
import org.lexevs.dao.test.LexEvsDaoTestBase;
import org.springframework.beans.factory.annotation.Autowired;

public class CompassEntityDaoTest extends LexEvsDaoTestBase {

	@Autowired
	private CompassEntityDao compassEntityDao;
	
	@Autowired
	private Compass compass;
	
	private IndexedEntityBuilder builder = new IndexedEntityBuilder();
	

	@Test
	@Ignore
	public void searchForEntity(){

		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		
		Presentation pres = new Presentation();
		Text text = new Text();
		text.setContent("testContent");
		pres.setValue(text);
		
		entity.addPresentation(pres);
		
		compassEntityDao.insertResource(builder.buildIndexableResource(entity));
		
		List<IndexedProperty> results = compassEntityDao.query("IndexedProperty.value:testContent");
		
		assertTrue("Size: " + results.size(), results.size() == 1);
		
		compassEntityDao.deleteResource(results.get(0));
	}
	
	@Test
	public void searchHasPropertyAndMatchesText(){

		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		
		Presentation pres = new Presentation();
		pres.setPropertyName("propName");
		pres.setPropertyId("1");
		Text text = new Text();
		text.setContent("testContent");
		pres.setValue(text);
		
		Presentation pres2 = new Presentation();
		pres2.setPropertyName("propName2");
		pres2.setPropertyId("2");
		Text text2 = new Text();
		text2.setContent("tstContent");
		pres2.setValue(text2);
		
		entity.addPresentation(pres);
		entity.addPresentation(pres2);
		
		compassEntityDao.insertResource(builder.buildIndexableResource(entity));
		
		CompassBooleanQueryBuilder builder = compass.queryBuilder().bool();
		builder.addMust(this.compass.queryBuilder().fuzzy("IndexedProperty.value", "testContent"));
		builder.addMust(this.compass.queryBuilder().fuzzy("IndexedProperty.allPropertyNames", "propName"));
		
		List<IndexedProperty> results = compassEntityDao.query(builder.toQuery());
		
		for(IndexedProperty prop : results){
		
		System.out.println(prop.getValue());
		System.out.println(" -- " + prop.getScore());
		}
	}
}
