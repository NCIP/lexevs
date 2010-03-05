package org.lexevs.dao.database.service.codingscheme;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.service.Registry;
import org.springframework.jdbc.core.JdbcTemplate;

public class VersionableEventCodingSchemeServiceTest extends LexEvsDbUnitTestBase {

	@Resource
	private VersionableEventCodingSchemeService service;
	
	@Before
	public void clearListeners() {
		//service.setDatabaseServiceEventListeners(null);
	}
	
	@Test
	public void insertCodingScheme() throws Exception{
		
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		service.insertCodingScheme(scheme);
	}
	
	@Test
	public void insertCodingSchemeWithLocalName() throws Exception{

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		scheme.addLocalName("localName");
		
		service.insertCodingScheme(scheme);
	}
	
	@Test
	public void testInsertCodingSchemeWithEntityAndProperty() throws Exception{

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		scheme.setEntities(new Entities());
		
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("ns");
		
		Property prop = new Property();
		prop.setPropertyName("name");
		prop.setPropertyId("id");
		prop.setValue(DaoUtility.createText("value"));
		
		entity.addProperty(prop);
		
		scheme.getEntities().addEntity(entity);
		
		service.insertCodingScheme(scheme);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		assertEquals(1, template.queryForInt("Select count(*) from codingScheme"));
		assertEquals(1, template.queryForInt("Select count(*) from entity"));
		assertEquals(1, template.queryForInt("Select count(*) from property"));
			
	}
}
