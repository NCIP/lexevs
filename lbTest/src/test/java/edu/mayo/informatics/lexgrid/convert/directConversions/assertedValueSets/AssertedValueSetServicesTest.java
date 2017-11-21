package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Definition;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;


public class AssertedValueSetServicesTest extends TestCase {
	Entity entityHasPubValue;
	Entity entityHasBrowserDef;
	Entity entityHasNoBrowserDef;
	Entity entityHasConceptDomain;
	public static final String CODING_SCHEME_URI = "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl";
	private static final String SEMANTIC_TYPE = "Semantic_Type";
	public static final String CODING_SCHEME = "owl2lexevs";
	private static final String EQUIV_CODING_SCHEME = "Thesaurus";
	private static final String TEST_STRING_STAGING_ADULTS_CODE = 
			"CDISC Clinical Classification WHO Clinical Staging of HIV/AIDS for Adults and Adolescents Test Code Terminology";
	private static final String TEST_STRING_STAGING_ADULTS_NAME = 
			"CDISC Clinical Classification WHO Clinical Staging of HIV/AIDS for Adults and Adolescents Test Name Terminology";
	private static final String TEST_STRING_STAGING_CHILD_CODE = 
			"CDISC Clinical Classification WHO Clinical Staging of HIV/AIDS for Children Test Code Terminology";
	private static final String TEST_STRING_STAGING_CHILD_NAME = 
			"CDISC Clinical Classification WHO Clinical Staging of HIV/AIDS for Children Test Name Terminology";
	@Before
	public void setUp(){
		entityHasPubValue = new Entity();
		entityHasPubValue.setProperty(new ArrayList<Property>());
		Property prop = new Property();
		prop.setPropertyName(AssertedValueSetServices.DEFAULT_DO_PUBLISH_NAME);
		prop.setValue(Constructors.createText(AssertedValueSetServices.DEFAULT_DO_PUBLISH_VALUE));
		entityHasPubValue.setProperty(new Property[]{prop});
		entityHasBrowserDef = new Entity();
		entityHasBrowserDef.setEntityDescription(Constructors.createEntityDescription("Not correct"));
		Property prop1 = new Property();
		prop1.setPropertyName(AssertedValueSetServices.BROWSER_VS_DEFINITION);
		prop1.setValue(Constructors.createText("&ltp&gt browser def &ltp&gt"));
		Definition prop2 = new Definition();
		prop2.setPropertyName("DEFINITION");
		prop2.setValue(Constructors.createText("default definition"));
		Property prop3 = new Property();
		prop3.setPropertyName("ALT_DEF");
		prop3.setValue(Constructors.createText("alternate definition"));
		entityHasBrowserDef.setProperty(new Property[]{prop1, prop3});
		entityHasBrowserDef.setDefinition(new Definition[]{prop2});
		entityHasNoBrowserDef = new Entity();
		entityHasNoBrowserDef.setEntityDescription(Constructors.createEntityDescription("Not correct"));
		entityHasNoBrowserDef.setProperty(new Property[]{ prop3});
		entityHasNoBrowserDef.setDefinition(new Definition[]{prop2});
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
		public void testGetSupportedNameSpace(){
			CodingScheme scheme = new CodingScheme();
			scheme.setCodingSchemeName("CodingSchemeName");
			scheme.setMappings(new Mappings());
			SupportedNamespace namsp = new SupportedNamespace();
			namsp.setContent("namespace");
			scheme.getMappings().addSupportedNamespace(namsp);
			assertNotNull(AssertedValueSetServices.getNameSpaceForCodingScheme(scheme));
			
			CodingScheme scheme1 = new CodingScheme();
			scheme1.setCodingSchemeName("CodingSchemeName");
			scheme1.setMappings(new Mappings());
			SupportedNamespace namsp1 = new SupportedNamespace();
			namsp1.setLocalId("namespace");
			scheme1.getMappings().addSupportedNamespace(namsp1);
			assertNotNull(AssertedValueSetServices.getNameSpaceForCodingScheme(scheme1));
			
			CodingScheme scheme2 = new CodingScheme();
			scheme2.setCodingSchemeName("CodingSchemeName");
			scheme2.setMappings(new Mappings());
			scheme2.getMappings().addSupportedNamespace(namsp1);
			assertNotNull(AssertedValueSetServices.getNameSpaceForCodingScheme(scheme2));
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
			
			subent3.setEntityCode("C11");
			subent3.setEntityDescription(Constructors.createEntityDescription("description3"));
			
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
			assertTrue(scheme.getEntities().getEntityCount() == 3);
			assertEquals(scheme.getEntities().getEntityAsReference().stream().filter(e -> e.getEntityCode().equals("C1")).findFirst().get().getEntityDescription().getContent(), "description1" );
			assertEquals(scheme.getEntities().getEntityAsReference().stream().filter(e -> e.getEntityCode().equals("C01")).findFirst().get().getEntityDescription().getContent(), "description2" );
			assertEquals(scheme.getEntities().getEntityAsReference().stream().filter(e -> e.getEntityCode().equals("C11")).findFirst().get().getEntityDescription().getContent(), "description3" );
		}
		
		
		@Test
		public void testGetDiff(){
			List<String> diff = AssertedValueSetServices.getDiff(TEST_STRING_STAGING_ADULTS_CODE, TEST_STRING_STAGING_CHILD_NAME);
			assertTrue(diff.contains("Adults") && diff.contains("and") && diff.contains("Adolescents") && diff.contains("Code"));	
			
//			String diff1 = AssertedValueSetServices.getDiff(
//					"some string with something in it", "some string with a difference in it");
//			assertEquals("a difference", diff1);
		}
		
		public void testGetCanonicalValue(){
			String value2 = AssertedValueSetServices.getCononicalDiffValue("this string has Code Terminology");
			assertEquals(value2, "Code");
			String value3 = AssertedValueSetServices.getCononicalDiffValue("this string has Name Terminology");
			assertEquals(value3, "Name");
		}
		
		@Test
		public void testTruncateDefName() {
			String fiftyPlus = "asdfjkl;neoimcfsha dkflajfd;l aldkj;asdfaljfdlasfdlaflaflafladsfladlfldfjasdlfd Code salfadslf";
			String target = "asdfjkl;neoimcfsha dkfl...dsfladlfldfjasdlfd Code";
			
			assertEquals(AssertedValueSetServices.truncateDefNameforCodingSchemeName(fiftyPlus, new HashMap<String, String>()), target);
		}
		
		@Test
		public void testDiffInShortName(){
			String shortName = "This short name has Adult";
			String shortName1 = "this short name has Adult and";
			String shortName2 = "This short name has Adult and Adolescent";
			String shortName3 = "This short name has Adult, Adolescent, and Code";
			List<String> list = Arrays.asList(new String[]{"Adult", "Adolescent", "and", "Code"});
			assertFalse(AssertedValueSetServices.diffInShortName(shortName, list));
			assertFalse(AssertedValueSetServices.diffInShortName(shortName1, list));
			assertFalse(AssertedValueSetServices.diffInShortName(shortName2, list));
			assertTrue(AssertedValueSetServices.diffInShortName(shortName3, list));
			List<String> list2 = Arrays.asList(new String[]{"Adult"});
			assertTrue(AssertedValueSetServices.diffInShortName(shortName, list2));
			assertTrue(AssertedValueSetServices.diffInShortName(shortName3, list2));
			List<String> list3 = Arrays.asList(new String[]{"Adult", "Adolescent"});
			assertFalse(AssertedValueSetServices.diffInShortName(shortName, list3));
			assertFalse(AssertedValueSetServices.diffInShortName(shortName1, list3));
			assertTrue(AssertedValueSetServices.diffInShortName(shortName2, list3));
			assertTrue(AssertedValueSetServices.diffInShortName(shortName3, list3));
		}
		
		@Test
		public void testCreateDifferentBaseName(){
			String shortName = TEST_STRING_STAGING_ADULTS_CODE.substring(0, 49);
			List<String> list = Arrays.asList(new String[]{"Adults", "and", "Adolescents", "Code"});
			assertEquals(AssertedValueSetServices.createDifferentBaseName(shortName, list), "CDISC Clinical Adults and Adolescents Code");
		}
		@Test
		public void testTruncateOneName(){
			String originalName = "CDISC Questionnaire BEBQ Concurrent Version Test Code Terminology";
			String similarName = "CDISC Questionnaire BEBQ Concurrent Version Test Name Terminology";
			HashMap<String, String> truncatedNames = new HashMap<String, String>();
			String processedName = AssertedValueSetServices.truncateDefNameforCodingSchemeName(originalName, truncatedNames );
			assertEquals(processedName, "CDISC Questionnaire BEB...rrent Version Test Code");
			String diff  = AssertedValueSetServices.truncateDefNameforCodingSchemeName(similarName, truncatedNames);
			assertEquals(diff , "CDISC Questionnaire BEB...rrent Version Test Name");
			assertTrue(truncatedNames.get("CDISC Questionnaire BEB...rrent Version Test Name") != null);
			
			String shortName2 =   "CDISC Questionnaire C-S...rsion 1/14/09 Test Name";
			String originalName2 = "CDISC Questionnaire C-SSRS Baseline/Screening Version Phase 1 Study Version 1/14/09 Test Name Terminology";
			HashMap<String, String> truncatedNames2 = new HashMap<String, String>();
			String name2 = AssertedValueSetServices.truncateDefNameforCodingSchemeName(originalName2, truncatedNames2);
			assertEquals(name2, shortName2);
			String similarName2 = "CDISC Questionnaire C-SSRS Baseline/Screening Version Phase 1 Study Version 1/14/09 Test Code Terminology";
			String name3  = AssertedValueSetServices.truncateDefNameforCodingSchemeName(similarName2, truncatedNames2);
			assertEquals(name3 , "CDISC Questionnaire C-S...rsion 1/14/09 Test Code");
			assertTrue(truncatedNames2.get("CDISC Questionnaire C-S...rsion 1/14/09 Test Code") != null);
			
		}
		
		@Test
		public void testTruncateNames() throws IOException{
			List<String> list = readToList(new File("resources/testData/long_names.csv"));
			HashMap<String, String> truncatedNames = new HashMap<String, String>();
			list.stream().forEach(x -> AssertedValueSetServices.truncateDefNameforCodingSchemeName(x, truncatedNames ));
//			System.out.println(truncatedNames.get("CDISC Questionnaire C-SSRS Code"));
//			System.out.println(truncatedNames.get("CDISC Questionnaire C-SSRS Name"));
			list.stream().filter(x ->  !truncatedNames.values().contains(x)).forEach(y -> System.out.println(y));
//			truncatedNames.keySet().stream().sorted().forEach(x ->System.out.println(x));
			assertEquals(list.size(),truncatedNames.size());
		}
		
		@Test
		public void testProcessForDiff(){
			String shortName = "CDISC Questionnaire C-SSRS Baseline/Screening Ve";
			String similarName = "CDISC Questionnaire C-SSRS Baseline/Screening Version Phase 1 Study Version 1/14/09 Test Code Terminology";
			HashMap<String, String> truncatedNames = new HashMap<String, String>();
			truncatedNames.put(shortName, "CDISC Questionnaire C-SSRS Baseline/Screening Version Phase 1 Study Version 1/14/09 Test Name Terminology");
			String name = AssertedValueSetServices.processForDiff(shortName, similarName, truncatedNames);
			assertEquals(name, "CDISC Questionnaire C-SSRS Name");
			
			String shortName1 =   "CDISC Questionnaire BEBQ Concurrent Version Test";
			String similarName1 = "CDISC Questionnaire BEBQ Concurrent Version Test Name Terminology";
			HashMap<String, String> truncatedNames1 = new HashMap<String, String>();
			truncatedNames1.put(shortName1, "CDISC Questionnaire BEBQ Concurrent Version Test Code Terminology");
			String name1 = AssertedValueSetServices.processForDiff(shortName1, similarName1, truncatedNames1);
			assertEquals(name1, "CDISC Questionnaire BEBQ Concurrent Version Code");
			

		}
		
		@Test
		public void testAbbreviateFromMiddle(){
			String name = "CDISC Questionnaire C-SSRS Baseline/Screening Version Phase 1 Study Version 1/14/09 Test Code Terminology";
			String abbrev = AssertedValueSetServices.abbreviateFromMiddle(name, "...", 50);
			assertTrue(abbrev.length() <= 50);
			assertEquals(abbrev,"CDISC Questionnaire C-S...9 Test Code Terminology");
			
		}
		
		@Test
		public void testGetAlternativeNamingForShortName(){
			String name = "CDISC Questionnaire C-SSRS Baseline/Screening Version Phase 1 Study Version 1/14/09 Test Name";
			String codeName = "CDISC Questionnaire WHODAS 2.0 36-item Version Self-administered Test Code Terminology";
			String name1 = "CDISC Questionnaire WHODAS 2.0 12-item Version Proxy-administered Test Name";
			String name2 = "CDISC Questionnaire WHODAS 2.0 12-item Version Self-administered Test Name";
			String name3 = "CDISC Questionnaire WHODAS 2.0 36-item Version Proxy-administered Test Name";
			String shortName = "CDISC Questionnaire WHO...administered Test Name ";
			HashMap<String, String> truncatedNames = new HashMap<String, String>();
			truncatedNames.put(shortName, name);
			String shortName1 = AssertedValueSetServices.getAlternativeNamingForShortName(shortName, codeName, truncatedNames);
			truncatedNames.put(shortName1, codeName);
			String shortName2 = AssertedValueSetServices.getAlternativeNamingForShortName(shortName1, name1, truncatedNames);
			truncatedNames.put(shortName2, name1);
			String shortName3 = AssertedValueSetServices.getAlternativeNamingForShortName(shortName2, name2, truncatedNames);
			truncatedNames.put(shortName3, name2);
			String shortName4 = AssertedValueSetServices.getAlternativeNamingForShortName(shortName3, name3, truncatedNames);
			truncatedNames.put(shortName4, name3);
			assertTrue(!shortName.equals(shortName1) && !shortName.equals(shortName2) && !shortName.equals(shortName3) && !shortName.equals(shortName4));
			assertTrue(!shortName1.equals(shortName2) && !shortName1.equals(shortName3) && !shortName1.equals(shortName4));
			assertTrue(!shortName2.equals(shortName3) && !shortName2.equals(shortName4));
			assertTrue(!shortName3.equals(shortName4));
		}
		
		@Test
		public void testBreakOnCommonName(){
			String name = "CDISC Questionnaire C-SSRS Baseline/Screening Version Phase 1 Study Version 1/14/09 Test Code Terminology";
			String adjustedName = AssertedValueSetServices.breakOnCommonDiff(name);
			assertEquals(adjustedName, "CDISC Questionnaire C-SSRS Baseline/Screening Version Phase 1 Study Version 1/14/09 Test Code");
		}
		
		private List<String> readToList(File file) throws IOException{
			return FileUtils.readLines(file);
		
		}

}
