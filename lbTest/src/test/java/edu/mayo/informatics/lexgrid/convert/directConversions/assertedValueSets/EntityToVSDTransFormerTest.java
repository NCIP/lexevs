package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.*;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.locator.LexEvsServiceLocator;

public class EntityToVSDTransFormerTest {
	CodedNodeGraphService service;
	EntityToVSDTransformer transformer;
	
	@Before
	public void setUp(){
		service = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().
				getCodedNodeGraphService();
		transformer = new EntityToVSDTransformer(null);
	}

	@Test
	public void getPredicateGuidForValueSetRelationTest() {
		transformer = new EntityToVSDTransformer(null);
		String guid = transformer.getPredicateGuidForValueSetRelation("Concept_In_Subset", "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "17.02d");
		assertEquals(guid, "37456035");
	}
	
	@Test
	public void getgetEntityByCodeAndNamespaceTest(){
		transformer = new EntityToVSDTransformer(null);
		Entity entity = transformer.getEntityByCodeAndNamespace(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "17.02d", "C12434", "Thesaurus");
		assertNotNull(entity);
	}
	
	@Test
	public void entityToValueSetTransformTest(){
		transformer = new EntityToVSDTransformer(null);
		Entity entity = new Entity();
		Property prop = new Property();
		prop.setPropertyName("Contributing_Source");
		Text text = new Text();
		text.setContent("CDISC");
		prop.setValue(text);
		entity.getPropertyAsReference().add(prop);
		List<ValueSetDefinition> list = transformer.transformEntityToValueSetDefinition(entity, null);
		assertNotNull(list);
	}
	
	

}
