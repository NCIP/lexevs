package edu.mayo.informatics.lexgrid.convert.directConversions.owl2;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import junit.framework.TestCase;

public class DataLoadTestBaseSpecialCases extends TestCase {

	/** The lbs. */
	protected LexBIGService lbs;
	protected CodedNodeSet cns;
	protected CodedNodeGraph cng;
	
	/**
	 * Sets the up lbs.
	 * @throws LBException 
	 */
	@Before
	public void setUp() throws Exception{
		lbs = ServiceHolder.instance().getLexBIGService();
		lbs.getSupportedCodingSchemes();
		cns = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION ));
		cng = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION ), null);
	}
	
	public CodedNodeSet getCodedNodeSet() throws Exception {
		setUp();
		return lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION ));
	}
	
	@Test
	public void testSetUp() throws Exception {
		Assert.noNullElements(new Object[] {lbs,cns,cng});
	}
	
	protected boolean validateTarget(String target,
			Iterator<? extends ResolvedConceptReference> itr) {
		boolean validate = false;
		while (itr.hasNext()) {
			ResolvedConceptReference ref = itr.next();
			AssociationList assoc1 = ref.getSourceOf();
			if(assoc1 == null){
				continue;
			}
			Association[] assocs = assoc1.getAssociation();
			for (Association as : assocs) {
				AssociatedConceptList acl = as.getAssociatedConcepts();
				AssociatedConcept[] acs = acl.getAssociatedConcept();
				for (AssociatedConcept ac : acs) {
					if (ac.getCode().equals(target)) {
						validate = true;
						break;
					}
				}
			}
		}
		return validate;
	}

}
