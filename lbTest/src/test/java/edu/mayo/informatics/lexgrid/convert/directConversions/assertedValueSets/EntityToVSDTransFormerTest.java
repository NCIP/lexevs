package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.*;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.commonTypes.Property;
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
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;

public class EntityToVSDTransFormerTest {
	CodedNodeGraphService service;
	EntityToVSDTransformer transformer;
	public static final String CODING_SCHEME_URI = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
	public static final String CODING_SCHEME = "NCI_Thesaurus";
	public static final String VERSION = "17.02d";
	public static final String ASSOCIATION_NAME = "Concept_In_Subset";
	
	@Before
	public void setUp(){
		service = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().
				getCodedNodeGraphService();
		transformer = new EntityToVSDTransformer(null, null, null, null, ASSOCIATION_NAME);
	}

//	@Test
//	public void getPredicateGuidForValueSetRelationTest() {
//		
//		String guid = transformer.getPredicateGuidForValueSetRelation(ASSOCIATION_NAME, CODING_SCHEME_URI, VERSION);
//		assertEquals(guid, "37456035");
//	}
	
//	@Test
//	public void getgetEntityByCodeAndNamespaceTest(){
//		
//		Entity entity = transformer.getEntityByCodeAndNamespace(
//				CODING_SCHEME_URI, VERSION, "C12434", "Thesaurus");
//		assertNotNull(entity);
//	}
	
	@Test
	public void entityToValueSetTransformTest() throws LBParameterException{
		
		Entity entity = new Entity();
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
	public void getProductionVersionForCodingSchemeURI() throws LBParameterException{
		
		String version = transformer.getProductionVersionForCodingSchemeURI(CODING_SCHEME_URI);
		assertNotNull(version);
	}
	
	@Test
    public void createSupportedCodingSchemeTest(){
		SupportedCodingScheme scheme = transformer.createSupportedCodingScheme(CODING_SCHEME, CODING_SCHEME_URI);
		assertTrue(scheme.getLocalId().equals(CODING_SCHEME));
		assertTrue(scheme.getContent().equals(CODING_SCHEME));
		assertTrue(scheme.getUri().equals(CODING_SCHEME_URI));
    }

	@Test
    public void createSupportedNamespaceTest(){
		SupportedNamespace namespace = transformer.createSupportedNamespace(CODING_SCHEME, CODING_SCHEME_URI);
		assertTrue(namespace.getLocalId().equals(CODING_SCHEME));
		assertTrue(namespace.getContent().equals(CODING_SCHEME));
		assertTrue(namespace.getUri().equals(CODING_SCHEME_URI));
    }

	@Test
    public void createSupportedConceptDomainTest(){
		SupportedConceptDomain cd = transformer.createSupportedConceptDomain("Intellectual Product", CODING_SCHEME_URI);
		assertTrue(cd.getLocalId().equals("Intellectual Product"));
		assertTrue(cd.getContent().equals("Intellectual Product"));
		assertTrue(cd.getUri().equals(CODING_SCHEME_URI));
    }
	
	@Test
    public void createSupportedSourceTest(){
		SupportedConceptDomain cd = transformer.createSupportedConceptDomain("CDISC", CODING_SCHEME_URI);
		assertTrue(cd.getLocalId().equals("CDISC"));
		assertTrue(cd.getContent().equals("CDISC"));
		assertTrue(cd.getUri().equals(CODING_SCHEME_URI));
    }
 
	@Test
    public void getDefaultSourceIfNullTest(){
		String source = transformer.getDefaultSourceIfNull(null);
		assertTrue(source.equals("Contributing_Source"));
		String test = transformer.getDefaultSourceIfNull("testSource");
		assertTrue(test.equals("testSource"));
    }
    
	@Test
    public void createUriTest(){
		String uri = transformer.createUri("http://evs.nci.nih.gov/valueset/", "CDISC", "C12345");
		assertEquals(uri,"http://evs.nci.nih.gov/valueset/CDISC/C12345");
		String test = transformer.createUri("http://evs.nci.nih.gov/valueset/", null, "C12345");
		assertTrue(test.equals("http://evs.nci.nih.gov/valueset/C12345"));
    }
    
	@Test
    public void initValueSetDefintionTest(){
		ValueSetDefinition def = transformer.initValueSetDefintion(CODING_SCHEME, true, "1", "NCI");
		assertTrue(def.getDefaultCodingScheme().equals(CODING_SCHEME));
		assertTrue(def.getIsActive());
		assertTrue(def.getStatus().equals("1"));
		assertTrue(def.getOwner().equals("NCI"));
    }
    
	@Test
    public void initDefinitionEntryTest(){
		DefinitionEntry entry = transformer.initDefinitionEntry(0, DefinitionOperator.OR);
		assertTrue(entry.getRuleOrder() == 0);
		assertTrue(entry.getOperator().equals(DefinitionOperator.OR));
    }
    
	@Test
    public void initEntityReferenceTest(){
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

}
