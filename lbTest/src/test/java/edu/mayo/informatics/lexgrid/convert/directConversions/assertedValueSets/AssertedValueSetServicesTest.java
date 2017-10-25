package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
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
