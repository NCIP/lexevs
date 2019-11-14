package org.LexGrid.LexBIG.Impl.function.query;

import static org.junit.Assert.*;

import java.util.stream.Stream;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
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
