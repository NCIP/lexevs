package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal.Instant;
import junit.framework.TestCase;


public class AssertedValueSetServicesTest extends TestCase {
	
	Entity entityHasPubValue;
	Entity entityHasBrowserDef;
	Entity entityHasNoBrowserDef;
	Entity entityHasConceptDomain;
	
	private static final String CODING_SCHEME_URI = "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl";
	private static final String SEMANTIC_TYPE = "Semantic_Type";
	private static final String CODING_SCHEME = "owl2lexevs";
	private static final String EQUIV_CODING_SCHEME = "Thesaurus";
	
	@Before
	public void setUp(){
		entityHasPubValue = new Entity();
		entityHasPubValue.setProperty(new ArrayList<Property>());
		Property prop = new Property();
		prop.setPropertyName(AssertedValueSetServices.DEFAULT_DO_PUBLISH_NAME);
		prop.setValue(Constructors.createText(AssertedValueSetServices.DEFAULT_DO_PUBLISH_VALUE));
		entityHasPubValue.setProperty(new Property[]{prop});
		
		entityHasBrowserDef = new Entity();
		Property prop1 = new Property();
		prop1.setPropertyName(AssertedValueSetServices.BROWSER_VS_DEFINITION);
		prop1.setValue(Constructors.createText("&ltp&gt browser def &ltp&gt"));
		Property prop2 = new Property();
		prop2.setPropertyName("DEFINITION");
		prop2.setValue(Constructors.createText("default definition"));
		Property prop3 = new Property();
		prop3.setPropertyName("ALT_DEF");
		prop3.setValue(Constructors.createText("alternate definition"));
		entityHasBrowserDef.setProperty(new Property[]{prop1, prop2, prop3});
		
		entityHasNoBrowserDef = new Entity();
		entityHasNoBrowserDef.setProperty(new Property[]{prop2, prop3});
		
		entityHasConceptDomain = new Entity();
		Property cdProp = new Property();
		cdProp.setPropertyName(SEMANTIC_TYPE);
		cdProp.setValue(Constructors.createText("Intellectual Product"));
		entityHasConceptDomain.setProperty(new Property[]{cdProp});
	}
	
	@Test
	public void testIsPublishableValueSet(){
		assertTrue(AssertedValueSetServices.isPublishableValueSet(entityHasPubValue, false));
	}

	@Test
	public void testGetValueSetDefinition() {
		assertEquals(AssertedValueSetServices.getValueSetDefinition(entityHasBrowserDef), 
				"&ltp&gt browser def &ltp&gt");
		assertEquals(AssertedValueSetServices.getValueSetDefinition(entityHasNoBrowserDef), 
				"default definition");
	}


	@Test
    public void testCreateSupportedConceptDomain(){
		SupportedConceptDomain cd = AssertedValueSetServices.createSupportedConceptDomain("Intellectual Product", CODING_SCHEME_URI);
		assertTrue(cd.getLocalId().equals("Intellectual Product"));
		assertTrue(cd.getContent().equals("Intellectual Product"));
		assertTrue(cd.getUri().equals(CODING_SCHEME_URI));
    }
	
	@Test
	public void testGetSupporteConceptDomainValueFromName(){
		String conceptDomain = AssertedValueSetServices.getConceptDomainValueFromEntityProperty(entityHasConceptDomain, SEMANTIC_TYPE);
		assertEquals(conceptDomain, "Intellectual Product");
	}
	
	
	@Test
    public void testCreateSupportedCodingScheme(){
		SupportedCodingScheme scheme = AssertedValueSetServices.createSupportedCodingScheme(CODING_SCHEME, CODING_SCHEME_URI);
		assertTrue(scheme.getLocalId().equals(CODING_SCHEME));
		assertTrue(scheme.getContent().equals(CODING_SCHEME));
		assertTrue(scheme.getUri().equals(CODING_SCHEME_URI));
    }

	@Test
    public void testCreateSupportedNamespace(){
		SupportedNamespace namespace = AssertedValueSetServices.createSupportedNamespace(CODING_SCHEME, EQUIV_CODING_SCHEME, CODING_SCHEME_URI);
		assertTrue(namespace.getLocalId().equals(CODING_SCHEME));
		assertTrue(namespace.getContent().equals(CODING_SCHEME));
		assertEquals(namespace.getEquivalentCodingScheme(), EQUIV_CODING_SCHEME);
		assertTrue(namespace.getUri().equals(CODING_SCHEME_URI));
    }
	
	@Test
    public void testCreateSupportedSource(){
		SupportedSource cd = AssertedValueSetServices.createSupportedSource("CDISC", CODING_SCHEME_URI);
		assertTrue(cd.getLocalId().equals("CDISC"));
		assertTrue(cd.getContent().equals("CDISC"));
		assertTrue(cd.getUri().equals(CODING_SCHEME_URI));
    }
	
	 
		@Test
	    public void testGetDefaultSourceIfNull(){
			String source = AssertedValueSetServices.getDefaultSourceIfNull(null);
			assertTrue(source.equals("Contributing_Source"));
			String test = AssertedValueSetServices.getDefaultSourceIfNull("testSource");
			assertTrue(test.equals("testSource"));
	    }
	    
		@Test
	    public void testCreateUri(){
			String uri = AssertedValueSetServices.createUri("http://evs.nci.nih.gov/valueset/", "CDISC", "C12345");
			assertEquals(uri,"http://evs.nci.nih.gov/valueset/CDISC/C12345");
			String test = AssertedValueSetServices.createUri("http://evs.nci.nih.gov/valueset/", null, "C12345");
			assertTrue(test.equals("http://evs.nci.nih.gov/valueset/C12345"));
	    }
		
		@Test
		public void testGetPropertiesForPropertyName(){
			Property prop1 = new Property();
			Property prop2 = new Property();
			Property prop3 = new Property();
			Property prop4 = new Property();
			prop1.setPropertyName(AssertedValueSetServices.DEFAULT_DO_PUBLISH_NAME);
			prop2.setPropertyName("aName");
			prop3.setPropertyName(AssertedValueSetServices.DEFAULT_DO_PUBLISH_NAME);
			prop4.setPropertyName("anotherName");
			prop1.setValue(Constructors.createText(AssertedValueSetServices.DEFAULT_DO_PUBLISH_VALUE));
			prop2.setValue(Constructors.createText("aValue"));
			prop3.setValue(Constructors.createText("Maybe"));
			prop4.setValue(Constructors.createText("anotherValue"));
			
			List<Property> props = new ArrayList<Property>();
			String name = AssertedValueSetServices.DEFAULT_DO_PUBLISH_NAME;
			props.add(prop1);
			props.add(prop2);
			props.add(prop3);
			props.add(prop4);
			List<Property> results = AssertedValueSetServices.getPropertiesForPropertyName(props , name);
			assertTrue(results.stream().filter(x -> x.getPropertyName().equals(name)).collect(Collectors.toList()).contains(prop1));
			assertTrue(results.stream().filter(x -> x.getPropertyName().equals(name)).collect(Collectors.toList()).contains(prop3));
			assertFalse(results.stream().filter(x -> x.getPropertyName().equals(name)).collect(Collectors.toList()).contains(prop2));
			assertFalse(results.stream().filter(x -> x.getPropertyName().equals(name)).collect(Collectors.toList()).contains(prop4));
			
		}
		
		@Test
		public void testGetPropertyQualifierValueForSource(){
			PropertyQualifier prop1 = new PropertyQualifier();
			PropertyQualifier prop2 = new PropertyQualifier();
			PropertyQualifier prop3 = new PropertyQualifier();
			PropertyQualifier prop4 = new PropertyQualifier();
			prop1.setPropertyQualifierName(AssertedValueSetServices.SOURCE);
			prop2.setPropertyQualifierName("aName");
			prop3.setPropertyQualifierName(AssertedValueSetServices.SOURCE);
			prop4.setPropertyQualifierName("anotherName");
			prop1.setValue(Constructors.createText("NCI"));
			prop2.setValue(Constructors.createText("aValue"));
			prop3.setValue(Constructors.createText("CDISC"));
			prop4.setValue(Constructors.createText("anotherValue"));
			
			List<PropertyQualifier> props = new ArrayList<PropertyQualifier>();
			String name = AssertedValueSetServices.DEFAULT_DO_PUBLISH_NAME;
			props.add(prop1);
			props.add(prop2);
			props.add(prop3);
			props.add(prop4);
			String result = AssertedValueSetServices.getPropertyQualifierValueForSource(props);
			assertEquals("NCI", result);
		}
		
		@Test
	    public void testTruncateDefNameforCodingSchemeName(){
			String longName ="Thisisacodingschemenamethatislongerthanitshouldbebecauseitis";
			String rightName = "Thisisacodingschemenamethatislongerthanitshouldbe1";
			String shortName = "Thisisacodingschemenamethatislongerthanitshouldbe";
			assertEquals(AssertedValueSetServices.truncateDefNameforCodingSchemeName(longName, true).length(), 49);
			assertEquals(AssertedValueSetServices.truncateDefNameforCodingSchemeName(rightName, true).length(), 50);
			assertTrue(AssertedValueSetServices.truncateDefNameforCodingSchemeName(shortName, true).length() < 50);
			
			assertTrue(AssertedValueSetServices.truncateDefNameforCodingSchemeName(longName, false).length() > 49);
			assertEquals(AssertedValueSetServices.truncateDefNameforCodingSchemeName(rightName,false).length(), 50);
			assertTrue(AssertedValueSetServices.truncateDefNameforCodingSchemeName(shortName, false).length() < 50);
	    }
		
		@Test
		public void testGetConceptCodeForURI() throws URISyntaxException {
			assertEquals(AssertedValueSetServices.getConceptCodeForURI(new URI(AssertedValueSetServices.BASE + "/NCIT/" + "C61410")), "C61410");
		}
		
		@Test
		public void testTransform() throws ParseException, LBException {
			Entity entity = new Entity();
			entity.setEntityCode("C010101");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date d = sdf.parse("21/12/2012");
			entity.setEffectiveDate(d);
			
			java.util.Date expd = sdf.parse("21/12/2021");
			entity.setExpirationDate(expd);
			
			entity.setEntityDescription(Constructors.createEntityDescription("This is where we describe the entity"));
			entity.setIsActive(true);
			Property prop = new Property();
			prop.setPropertyName(AssertedValueSetServices.CONCEPT_DOMAIN);
			prop.setValue(Constructors.createText("Intellectual Product"));
			entity.getPropertyAsReference().add(prop);
			Property p1 = new Property();
			p1.setPropertyName("PropWSource");
			p1.setValue(Constructors.createText("ValueWSource"));
			PropertyQualifier pq = new PropertyQualifier();
			pq.setPropertyQualifierName(AssertedValueSetServices.SOURCE);
			pq.setValue(Constructors.createText("CDISC"));
			p1.getPropertyQualifierAsReference().add(pq);
			entity.getPropertyAsReference().add(p1);
			entity.setEntityCodeNamespace("NCI_Thesaurus");
			entity.setOwner("NCI");
			entity.setStatus("Complete");
			
			Entity subent1 = new Entity();
			Entity subent2 = new Entity();
			Entity subent3 = new Entity();
			
			subent1.setEntityCode("C1");
			subent1.setEntityDescription(Constructors.createEntityDescription("description1"));
			
			subent2.setEntityCode("C01");
			subent2.setEntityDescription(Constructors.createEntityDescription("description2"));
			
			subent2.setEntityCode("C11");
			subent2.setEntityDescription(Constructors.createEntityDescription("description3"));
			
			Entities ents = new Entities();
			ents.getEntityAsReference().add(subent1);
			ents.getEntityAsReference().add(subent2);
			ents.getEntityAsReference().add(subent3);
			
			CodingScheme scheme = AssertedValueSetServices.transform(entity, "CDISC", null, ents, "17.08d", "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
			assertEquals(scheme.getCodingSchemeName(), "This is where we describe the entity");
			assertEquals(scheme.getCodingSchemeURI(), "http://evs.nci.nih.gov/valueset/CDISC/C010101" );
			assertEquals(scheme.getEffectiveDate(), d);
			assertEquals(scheme.getExpirationDate(), expd);
			assertEquals(scheme.getRepresentsVersion(), "17.08d");
			assertEquals(scheme.getStatus(), "Complete");
			assertEquals(scheme.getMappings().getSupportedCodingSchemeAsReference().get(0).getContent(), "NCI_Thesaurus");
			assertEquals(scheme.getMappings().getSupportedCodingSchemeAsReference().get(0).getUri(), "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
			assertEquals(scheme.getMappings().getSupportedConceptDomainAsReference().get(0).getContent(), "Intellectual Product");
			assertEquals(scheme.getMappings().getSupportedConceptDomainAsReference().get(0).getUri(), "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
			assertEquals(scheme.getMappings().getSupportedNamespaceAsReference().get(0).getContent(), "NCI_Thesaurus");
			assertEquals(scheme.getMappings().getSupportedNamespaceAsReference().get(0).getUri(), "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
			assertEquals(scheme.getSourceAsReference().get(0).getContent(), "CDISC");
			assertEquals(scheme.getProperties().getPropertyAsReference().stream().filter(p -> p.getPropertyName().
					equals("resolvedAgainstCodingSchemeVersion")).findFirst().get().getValue().getContent(),"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#"); 
			assertEquals(scheme.getProperties().getPropertyAsReference().stream().filter(p -> p.getPropertyName().
					equals("resolvedAgainstCodingSchemeVersion")).findFirst().get().getPropertyQualifierAsReference().stream().filter(
					pqual -> pqual.getPropertyQualifierName().equals("version")).findFirst().get().getValue().getContent(), "17.08d"); 
			assertEquals(scheme.getProperties().getPropertyAsReference().stream().filter(p -> p.getPropertyName().
					equals("resolvedAgainstCodingSchemeVersion")).findFirst().get().getPropertyQualifierAsReference().stream().filter(
					pqtoo -> pqtoo.getPropertyQualifierName().equals("codingSchemeName")).findFirst().get().getValue().getContent(), "This is where we describe the entity"); 
			assertEquals(scheme.getOwner(), "NCI");
		}

}
