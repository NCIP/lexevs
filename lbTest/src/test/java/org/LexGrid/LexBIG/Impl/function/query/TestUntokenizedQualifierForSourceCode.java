package org.LexGrid.LexBIG.Impl.function.query;

import java.util.stream.Stream;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.AlgorithmMatch;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

public class TestUntokenizedQualifierForSourceCode extends LexBIGServiceTestCase {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws LBException {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService()
        		.getCodingSchemeConcepts(OWL2_SNIPPET_INDIVIDUAL_URN, 
        				Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION));
        cns.restrictToProperties(null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		null, 
        		null, 
        		Constructors.createNameAndValueList("source-code", "CTESTCODE"));
        ResolvedConceptReferenceList nodeRefs = cns.resolveToList(null, null, null, -1);
        assertNotNull(nodeRefs);
        assertTrue(nodeRefs.getResolvedConceptReferenceCount() > 0);
        assertEquals(1, nodeRefs.getResolvedConceptReferenceCount());
        assertTrue(Stream.of(nodeRefs.getResolvedConceptReference())
        		.anyMatch(x -> entityPropertyQualifierExistsForName(x , "source-code")));
        assertTrue(Stream.of(nodeRefs.getResolvedConceptReference())
        		.anyMatch(x -> entityPropertyQualifierExistsForValue(x , "CTESTCODE")));
	}
	
	@Test
	public void testMatching() throws LBException {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService()
        		.getCodingSchemeConcepts(OWL2_SNIPPET_INDIVIDUAL_URN, 
        				Constructors.createCodingSchemeVersionOrTagFromVersion(
        						OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION));
        cns.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.PRESENTATION}, 
        		Constructors.createLocalNameList("CDISC"), 
        		null, 
        		null, 
        		"OETESTCD", 
        		AlgorithmMatch.LUCENE.getMatch(), 
        		null);
        ResolvedConceptReferenceList nodeRefs = cns.resolveToList(null, null, null, -1);
        assertNotNull(nodeRefs);
        assertTrue(nodeRefs.getResolvedConceptReferenceCount() > 0);
        assertTrue(Stream.of(nodeRefs.getResolvedConceptReference())
        		.anyMatch(x -> x.getCode().equals("C117743")));
	}
	
	@Test
	public void testToo() throws LBException {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService()
        		.getCodingSchemeConcepts(OWL2_SNIPPET_INDIVIDUAL_URN, 
        				Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION));
        cns.restrictToProperties(null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		null, 
        		null, 
        		Constructors.createNameAndValueList("source-code", "CTESTCODETOO"));
        ResolvedConceptReferenceList nodeRefs = cns.resolveToList(null, null, null, -1);
        assertNotNull(nodeRefs);
        assertTrue(nodeRefs.getResolvedConceptReferenceCount() > 0);
        assertEquals(1, nodeRefs.getResolvedConceptReferenceCount());
        assertTrue(Stream.of(nodeRefs.getResolvedConceptReference())
        		.anyMatch(x -> entityPropertyQualifierExistsForName(x , "source-code")));
        assertTrue(Stream.of(nodeRefs.getResolvedConceptReference())
        		.anyMatch(x -> entityPropertyQualifierExistsForValue(x , "CTESTCODETOO")));
	}
	
	@Test
	public void testBadTestCode() throws LBException {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService()
        		.getCodingSchemeConcepts(OWL2_SNIPPET_INDIVIDUAL_URN, 
        				Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION));
        cns.restrictToProperties(null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		null, 
        		null, 
        		Constructors.createNameAndValueList("source-code", "BLACKER"));
        ResolvedConceptReferenceList nodeRefs = cns.resolveToList(null, null, null, -1);
        assertNotNull(nodeRefs);
        assertFalse(nodeRefs.getResolvedConceptReferenceCount() > 0);
	}
	
	@Test
	public void testMatchToo() throws LBException {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService()
        		.getCodingSchemeConcepts(OWL2_SNIPPET_INDIVIDUAL_URN, 
        				Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION));
        cns.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.PRESENTATION},
        		null, null, Constructors.createNameAndValueList("source-code", "CTESTCODETOO"), "http", 
        		AlgorithmMatch.CONTAINS.getMatch(), null);
        ResolvedConceptReferenceList nodeRefs = cns.resolveToList(null, null, null, -1);
        assertNotNull(nodeRefs);
        assertTrue(nodeRefs.getResolvedConceptReferenceCount() > 0);
        assertEquals(1, nodeRefs.getResolvedConceptReferenceCount());
        assertTrue(Stream.of(nodeRefs.getResolvedConceptReference())
        		.anyMatch(x -> entityPropertyQualifierExistsForName(x , "source-code")));
        assertTrue(Stream.of(nodeRefs.getResolvedConceptReference())
        		.anyMatch(x -> entityPropertyQualifierExistsForValue(x , "CTESTCODETOO")));
	}
	
	@Test
	public void testBadTestCodeBadQualSearch() throws LBException {
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService()
        		.getCodingSchemeConcepts(OWL2_SNIPPET_INDIVIDUAL_URN, 
        				Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION));
        cns.restrictToProperties(null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		null, 
        		null, 
        		Constructors.createNameAndValueList("source-code", "ctestcode"));
        ResolvedConceptReferenceList nodeRefs = cns.resolveToList(null, null, null, -1);
        assertNotNull(nodeRefs);
        assertFalse(nodeRefs.getResolvedConceptReferenceCount() > 0);
	}

	@Override
	protected String getTestID() {
		return "Untokenized Source Code";
	}
	
	private boolean entityPropertyQualifierExistsForName(ResolvedConceptReference ref, String name){
		return Stream.of(ref.getEntity().getPresentation())
		.anyMatch(x -> Stream.of(x.getPropertyQualifier())
				.anyMatch(y -> y.getPropertyQualifierName().equals(name)));
	}
	
	private boolean entityPropertyQualifierExistsForValue(ResolvedConceptReference ref, String value){
		return Stream.of(ref.getEntity().getPresentation())
		.anyMatch(x -> Stream.of(x.getPropertyQualifier())
				.anyMatch(y -> y.getValue().getContent().equals(value)));
	}
	

}
