
package org.LexGrid.LexBIG.Impl.bugs;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

public class GForge29940 extends LexBIGServiceTestCase {
	final static String testID = "GForge29940";
	CodedNodeGraph cng;
	
	@Override
	protected String getTestID() {
		return testID;
	}

/**
	 * 
	 * 
	 * GForge #29940
	 * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid
	 * =29940&group_id=491&atid=1850
	 * 
	 * @throws Throwable
	 */
public void testDuplicateInfoWithAnonymousClass() throws Throwable {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		CodingSchemeVersionOrTag csvt = Constructors
				.createCodingSchemeVersionOrTagFromVersion(PIZZA_SCHEME_VERSION);
		cng = lbs.getNodeGraph(PIZZA_SCHEME_NAME, csvt, null);

		AssociatedConcept focus = new AssociatedConcept();
		/* MozzarellaTopping should has two anonymous association targets. When
		 * using these two anonymous as the association source, their association
		 * targets should not be 0
		*/
		focus.setCode("MozzarellaTopping");
		focus.setCodeNamespace("pizza.owl");

		ResolvedConceptReferenceList list = cng.resolveAsList(focus, true,
				false, 1, -1, null, null, null, null, -1);
		ResolvedConceptReference conRef = list.getResolvedConceptReference()[0];
		AssociationList assnList = conRef.getSourceOf();
		for (Association assn : assnList.getAssociation()) {
			for (AssociatedConcept con : assn.getAssociatedConcepts()
					.getAssociatedConcept()) {
				if (con.getCode().startsWith("@")) {
					int count = resolveAsList(con);
					assertEquals(count>0, true);
				}
			}
		}
	}

	private int resolveAsList(AssociatedConcept con) throws LBInvocationException, LBParameterException {
		ResolvedConceptReferenceList list = cng.resolveAsList(con, true,
				false, 1, -1, null, null, null, null, -1);
		return list.getResolvedConceptReferenceCount();
	}
}