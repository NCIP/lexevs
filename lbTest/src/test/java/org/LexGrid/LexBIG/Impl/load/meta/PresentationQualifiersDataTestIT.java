
package org.LexGrid.LexBIG.Impl.load.meta;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;

/**
 * The Class PresentationQualifiersDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PresentationQualifiersDataTestIT extends DataLoadTestBase {
	
	/** The test entity. */
	private Presentation presentation;

	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		cns.restrictToCodes(Constructors.createConceptReferenceList("C0000005"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		Entity testEntity = rcr1.getEntity();
		
		for(Presentation pres : testEntity.getPresentation()) {
			if(pres.getValue().getContent().equals("(131)I-MAA")) {
				presentation = pres;
			}
		}
	}
	
	@Test
	public void testPresentationNotNull() throws Exception {	
		assertNotNull(presentation);
	}
	
	@Test
	public void testSourceSize() throws Exception {	
		assertTrue(presentation.getSource().length == 1);
	}
	
	@Test
	public void testSource() throws Exception {	
		assertTrue(presentation.getSource()[0].getContent().equals("MSH"));
	}
	
	@Test
	public void testAuiQualifierSize() throws Exception {	
		List<PropertyQualifier> auiQuals = DataTestUtils.getPropertyQualifiersFromProperty(presentation, RrfLoaderConstants.AUI_QUALIFIER);
		assertTrue(auiQuals.size() == 1);
	}
	
	@Test
	public void testAuiQualifierName() throws Exception {	
		PropertyQualifier auiQual = DataTestUtils.getPropertyQualifiersFromProperty(presentation, RrfLoaderConstants.AUI_QUALIFIER).get(0);
		assertTrue(auiQual.getPropertyQualifierName().equals(RrfLoaderConstants.AUI_QUALIFIER));
	}
	
	@Test
	public void testAuiQualifierValue() throws Exception {	
		PropertyQualifier auiQual = DataTestUtils.getPropertyQualifiersFromProperty(presentation, RrfLoaderConstants.AUI_QUALIFIER).get(0);
		assertTrue(auiQual.getValue().getContent().equals("A4332670"));
	}
	
	@Test
	public void testLuiQualifierValue() throws Exception {
		PropertyQualifier luiQual = DataTestUtils.getPropertyQualifiersFromProperty(presentation, RrfLoaderConstants.LUI_QUALIFIER).get(0);
		assertTrue(luiQual.getValue().getContent().equals("L0187013"));
	}
	
	@Test
	public void testSuiQualifierValue() throws Exception {
		PropertyQualifier suiQual = DataTestUtils.getPropertyQualifiersFromProperty(presentation, RrfLoaderConstants.SUI_QUALIFIER).get(0);
		assertTrue(suiQual.getValue().getContent().equals("S2192303"));
	}
	
	@Test
	public void testSauiQualifierValue() throws Exception {
		CodedNodeSet cns = getCodedNodeSet().restrictToCodes(Constructors.createConceptReferenceList("C0000097"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		Entity testEntity = rcr1.getEntity();
		boolean found = false;
		for(Presentation pres : testEntity.getPresentation()) {
			List<PropertyQualifier> sauiQuals = DataTestUtils.getPropertyQualifiersFromProperty(pres, RrfLoaderConstants.SAUI_QUALIFIER);
			for(PropertyQualifier sauiQual : sauiQuals) {
				if(sauiQual.getValue().getContent().equals("679406011") == true) {
					found = true;
				}
			}
		}
		assertTrue(found);		
	}
	
	@Test	
	public void testScuiQualifierValue() throws Exception {
		PropertyQualifier scuiQual = DataTestUtils.getPropertyQualifiersFromProperty(presentation, RrfLoaderConstants.SCUI_QUALIFIER).get(0);
		assertTrue(scuiQual.getValue().getContent().equals("M0019694"));
	}
	
	@Test	
	public void testSduiQualifierValue() throws Exception {
		PropertyQualifier sduiQual = DataTestUtils.getPropertyQualifiersFromProperty(presentation, RrfLoaderConstants.SDUI_QUALIFIER).get(0);
		assertTrue(sduiQual.getValue().getContent().equals("D012711"));
	}
	
	@Test	
	public void testSuppressQualifierValue() throws Exception {
		//'N' values aren't loaded.
		assertEquals(0,DataTestUtils.getPropertyQualifiersFromProperty(presentation, RrfLoaderConstants.SUPPRESS_QUALIFIER).size());
	}
	
	@Test
	public void testCvfQualifierValue() throws Exception {
		List<PropertyQualifier> cvfQuals = DataTestUtils.getPropertyQualifiersFromProperty(presentation, RrfLoaderConstants.CVF_QUALIFIER);
		assertTrue(cvfQuals.size() == 0);
		
		CodedNodeSet cns = getCodedNodeSet().restrictToCodes(Constructors.createConceptReferenceList("C0000039"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		Entity testEntity = rcr1.getEntity();
		boolean found = false;
		for(Presentation pres : testEntity.getPresentation()) {
			List<PropertyQualifier> cvfQuals2 = DataTestUtils.getPropertyQualifiersFromProperty(pres, RrfLoaderConstants.CVF_QUALIFIER);
			for(PropertyQualifier cvfQual : cvfQuals2) {
				if(cvfQual.getValue().getContent().equals("cvf") == true) {
					found = true;
				}
			}
		}
		assertTrue(found);		
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(PresentationQualifiersDataTestIT.class);  
	}  
}