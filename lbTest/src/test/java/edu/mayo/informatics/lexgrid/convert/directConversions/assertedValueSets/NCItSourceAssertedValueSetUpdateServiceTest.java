package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.NCItSourceAssertedValueSetUpdateServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

public class NCItSourceAssertedValueSetUpdateServiceTest {
	NCItSourceAssertedValueSetUpdateServiceImpl vsUpdate;
	LexEVSResolvedValueSetServiceImpl resVSSvc;
	LexBIGService lbs;
	LexEVSValueSetDefinitionServices svc;
	@Before
	public void setUp() throws Exception {
		AssertedValueSetParameters params = new AssertedValueSetParameters.
				Builder("0.1.5.1")
				.codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl").
				assertedValueSetRelation("Concept_In_Subset").build();
//		vsUpdate = new NCItSourceAssertedValueSetUpdateServiceImpl(
//				"owl2lexevs", "0.1.5.1", "Concept_In_Subset", "true", 
//				"http://evs.nci.nih.gov/valueset/","NCI","Contributing_Source",
//				"Semantic_Type", "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl");
		resVSSvc = new LexEVSResolvedValueSetServiceImpl(params);
		lbs = LexBIGServiceImpl.defaultInstance();
		svc = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
	}

//	@Test
//	public void getCodingSchemeNamespaceForURIandVersion() throws LBException {
//		String namespace = vsUpdate.getCodingSchemeNamespaceForURIandVersion("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5.1");
//		assertTrue(namespace != null);
//		assertEquals(namespace, "owl2lexevs");
//	}
	

//	@Test
//	public void getReferencesForVersionTest() throws LBException {
//		List<String> refs = vsUpdate.getReferencesForVersion("0.1.5.1");
//		assertTrue(refs.size() > 0);
//		assertEquals(refs.stream().filter(x -> x.equals("C48326")).findAny().get(), "C48326");
//	}
//	
//	@Test 
//	public void getDateForVersionTest() throws LBException, ParseException{
//		Date date = vsUpdate.getDateForVersion("0.1.5.1");
//		assertNotNull(date);
//		DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		assertEquals(outputDateFormat.format(date).toString(), "2017-01-08 00:00:00");		
//	}

//	@Test
//	public void getVersionsForDateRangeTest() throws LBInvocationException, LBException{
//		Date currentDate = vsUpdate.getDateForVersion("0.1.5.1");
//		Date previousDate = vsUpdate.getDateForVersion("0.1.5");
//		List<String> versions = vsUpdate.getVersionsForDateRange(previousDate, 
//				currentDate);
//		assertTrue(versions.size() > 1);
//	}
	
//	@Test
//	public void getTopNodeForValueSet() throws LBException{
//		Node node = new Node();
//		node.setEntityCode("C54453");
//		List<AbsoluteCodingSchemeVersionReference> vref = vsUpdate.getVsService().getValueSetDefinitionSchemeRefForTopNodeSourceCode(node);
//		assertTrue(vref.size() > 0);
//	}
//	
//	@Test
//	public void isValueSetTopNodeTest() throws LBException{
//		Node node = new Node();
//		node.setEntityCode("C54453");
//		assertTrue(vsUpdate.isValueSetTopNode(node));
//	}
	
	@Test
	public void newValueSetIsLoaded() throws LBException, URISyntaxException{
		CodingScheme scheme = this.resVSSvc.
				getResolvedValueSetForValueSetURI(new URI("http://evs.nci.nih.gov/valueset/C111112"));
		assertNotNull(scheme);
	}
	
	@Test
	public void newValuesLoaded() throws LBException{
		ResolvedConceptReferenceList list = resVSSvc.getValueSetEntitiesForURI("http://evs.nci.nih.gov/valueset/C54453");

		assertTrue(Arrays.asList(list.getResolvedConceptReference()).
				stream().anyMatch(x -> x.getCode().equals("C111112")));
		assertTrue(Arrays.asList(list.getResolvedConceptReference()).
				stream().anyMatch(x -> x.getCode().equals("C48326")));
		assertTrue(Arrays.asList(list.getResolvedConceptReference()).
				stream().anyMatch(x -> x.getCode().equals("C48327")));
	}
	
	@Test(expected = LBException.class)
	public void badNewValueSetIsNotLoaded() throws LBException{
		CodingScheme scheme =lbs.resolveCodingScheme("Clinical Data Interchange Standards Consortium Terminology", 
				Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5.1"));
	}
	
}
