package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import java.util.List;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class EntityToRVSTransformerTest extends TestCase {
	EntityToRVSTransformer transformer;
	Entity entity;
	@Before
	public void setUp() throws Exception {
		 transformer = new EntityToRVSTransformer(null, "http://Coding.Scheme.uri", null, null, null, null, null, null, null, null);
		 entity = new Entity();
		 Property prop = new Property();
		 prop.setPropertyName("ImaProp");
		 prop.setValue(Constructors.createText("ImaValue"));
		 PropertyQualifier pq = new PropertyQualifier();
		 pq.setPropertyQualifierName(AssertedValueSetServices.SOURCE);
		 pq.setValue(Constructors.createText("CDISC"));
		 PropertyQualifier pq1 = new PropertyQualifier();
		 pq1.setPropertyQualifierName(AssertedValueSetServices.SOURCE);
		 pq1.setValue(Constructors.createText("FDA"));
		 prop.setPropertyQualifier(new PropertyQualifier[]{pq, pq1});
		 entity.setProperty(new Property[]{prop});

	}

	@Test
	public void testTruncateDefName() {
		String fiftyPlus = "asdfjkl;neoimcfsha dkflajfd;l aldkj;asdfaljfdlasfdlaflaflafladsfladlfldfjasdlfdsalfadslf";
		String target = fiftyPlus.substring(0, 49);
		
		assertEquals(transformer.truncateDefNameforCodingSchemeName(fiftyPlus), target);
	}
	
	@Test
	public void testGetSupportedSources(){
		List<SupportedSource> list = transformer.getSupportedSources(entity);
		assertTrue(list.get(0).getContent().equals("CDISC") || list.get(0).getContent().equals("FDA"));
		assertTrue(list.get(1).getContent().equals("CDISC") || list.get(1).getContent().equals("FDA"));
	}

}
