package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class EntityToVSDTransFormerTest extends TestCase{
	EntityToVSDTransformer transformer;
	public static final String CODING_SCHEME_URI = "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl";
	public static final String CODING_SCHEME = "owl2lexevs";
	public static final String VERSION = "0.1.5";
	public static final String ASSOCIATION_NAME = "Concept_In_Subset";
	
	@Before
	public void setUp(){
		transformer = new EntityToVSDTransformer(null, null, null, null, null, ASSOCIATION_NAME, null);
	}

	
	@Test
	public void testentityToValueSetTransform() throws LBParameterException{
		
		Entity entity = new Entity();
		EntityDescription ed = Constructors.createEntityDescription("TestDescription");
		entity.setEntityDescription(ed);
		Property prop = new Property();
		prop.setPropertyName("Contributing_Source");
		Text text = new Text();
		text.setContent("CDISC");
		prop.setValue(text);
		entity.getPropertyAsReference().add(prop);
		List<ValueSetDefinition> list = transformer.transformEntityToValueSetDefinitions(entity, null);
		assertNotNull(list);
	}
	
	@Test
	public void testGetProductionVersionForCodingSchemeURI() throws LBParameterException{		
		String version = transformer.getProductionVersionForCodingSchemeURI(CODING_SCHEME_URI);
		assertNotNull(version);
	}
	
	@Test
    public void testInitValueSetDefintion(){
		ValueSetDefinition def = transformer.initValueSetDefintion(CODING_SCHEME, true, "1", "NCI");
		assertTrue(def.getDefaultCodingScheme().equals(CODING_SCHEME));
		assertTrue(def.getIsActive());
		assertTrue(def.getStatus().equals("1"));
		assertTrue(def.getOwner().equals("NCI"));
    }
    
	@Test
    public void testInitDefinitionEntry(){
		DefinitionEntry entry = transformer.initDefinitionEntry(0, DefinitionOperator.OR);
		assertTrue(entry.getRuleOrder() == 0);
		assertTrue(entry.getOperator().equals(DefinitionOperator.OR));
		long l = entry.getRuleOrder();
		assertEquals(l, 0L);
		assertEquals(entry.getStatus(), "1");
    }
    
	@Test
    public void testInitEntityReference(){
		Entity entity = new Entity();
		entity.setEntityCode("C123");
		entity.setEntityCodeNamespace(CODING_SCHEME);
		EntityReference ref = transformer.initEntityReference(entity, ASSOCIATION_NAME);
		assertTrue(ref.getEntityCode().equals("C123"));
		assertTrue(ref.getEntityCodeNamespace().equals(CODING_SCHEME));
		assertTrue(ref.getLeafOnly());
		assertTrue(ref.getReferenceAssociation().equals(ASSOCIATION_NAME));
		assertTrue(ref.getTargetToSource());
		assertTrue(ref.getTransitiveClosure());
    }
	
	@Test
	   public void testGetPropertyQualifierValueForSource(){
			PropertyQualifier q = new PropertyQualifier();
			q.setPropertyQualifierName("source");
			q.setValue(Constructors.createText("CDISC"));
			List<PropertyQualifier> list = new ArrayList<PropertyQualifier>();
			list.add(q);
		    assertEquals(transformer.getPropertyQualifierValueForSource(list), "CDISC");
    }

}
