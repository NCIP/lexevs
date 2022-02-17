
package org.LexGrid.LexBIG.Impl.load.meta;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;

public class MrhierPropertyQualifierTestIT extends DataLoadTestBase {
	
	private ResolvedConceptReference refC0000005;
	private ResolvedConceptReference refC0036775;
	private ResolvedConceptReference refC0001555;
	private ResolvedConceptReference refCL385598;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		refC0000005 = cng.resolveAsList(Constructors.createConceptReference("C0000005", LexBIGServiceTestCase.META_URN), true, false, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];	
		refC0036775 = cng.resolveAsList(Constructors.createConceptReference("C0036775", LexBIGServiceTestCase.META_URN), true, false, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];
		refC0001555 = cng.resolveAsList(Constructors.createConceptReference("C0001555", LexBIGServiceTestCase.META_URN), true, false, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];
		refCL385598 = cng.resolveAsList(Constructors.createConceptReference("CL385598", LexBIGServiceTestCase.META_URN), true, false, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];
	}
	
	@Test
	public void testNotNull() throws Exception {	
		assertNotNull(refC0000005);
		assertNotNull(refC0036775);
		assertNotNull(refC0001555);
		assertNotNull(refCL385598);
	}
	
	/*
	@Test
	public void testIsC0000005HcdPropQualifierPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(refC0000005.getEntity(), "(131)I-Macroaggregated Albumin");
		
		assertTrue(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.HCD_QUALIFIER, "testHierarchy:A3586555.A5703273.A5784826", prop)				
		);
	}
	
	@Test
	public void testIsNotHierarchyParticipatingC0000005HcdPropQualifierNotPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(refC0000005.getEntity(), "(131)I-MAA");
		
		assertFalse(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.HCD_QUALIFIER, "testHierarchy:A3586555.A5703273.A5784826", prop)				
		);
	}
	
	@Test
	public void testIsC0036775HcdPropQualifierPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(refC0036775.getEntity(), "Serum Albumin, Radio-Iodinated");
		
		assertTrue(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.HCD_QUALIFIER, "testHierarchy:A3586555.A5703273.A5784826", prop)				
		);
	}
	
	@Test
	public void testIsNotHierarchyParticipatingC0036775HcdPropQualifierNotPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(refC0036775.getEntity(), "Serum Albumin, Radio Iodinated");
		
		assertFalse(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.HCD_QUALIFIER, "testHierarchy:A3586555.A5703273.A5784826", prop)				
		);
	}
	
	@Test
	public void testIsC0001555HcdPropQualifierPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithId(refC0001555.getEntity().getPresentation(), "A5703273");
		
		assertTrue(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.HCD_QUALIFIER, "testHierarchy:A3586555.A5703273.A5784826", prop)				
		);
	}
	
	@Test
	public void testIsNotHierarchyParticipatingC0001555HcdPropQualifierNotPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithId(refC0001555.getEntity().getPresentation(), "A7737265");
		
		assertFalse(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.HCD_QUALIFIER, "testHierarchy:A3586555.A5703273.A5784826", prop)				
		);
	}
	
	@Test
	public void testIsCL385598HcdPropQualifierPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithId(refCL385598.getEntity().getPresentation(), "A5784826");
		
		assertTrue(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.HCD_QUALIFIER, "testHierarchy:A3586555.A5703273.A5784826", prop)				
		);
	}
	
	@Test
	public void testIsNotHierarchyParticipatingCL385598HcdPropQualifierNotPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithId(refCL385598.getEntity().getPresentation(), "A7709649");
		
		assertFalse(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.HCD_QUALIFIER, "testHierarchy:A3586555.A5703273.A5784826", prop)				
		);
	}
	
	/*
	@Test
	@Ignore
	public void testIsC0000005PtrPropQualifierPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(refC0000005.getEntity(), "(131)I-Macroaggregated Albumin");
		
		assertTrue(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.PTR_QUALIFIER, "A3586555.A5703273.A5784826", prop)				
		);
	}
	
	@Test
	@Ignore
	public void testIsNotHierarchyParticipatingC0000005PtrPropQualifierNotPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(refC0000005.getEntity(), "(131)I-MAA");
		
		assertFalse(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.PTR_QUALIFIER, "A3586555.A5703273.A5784826", prop)				
		);
	}
	
	@Test
	@Ignore
	public void testIsC0036775PtrPropQualifierPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(refC0036775.getEntity(), "Serum Albumin, Radio-Iodinated");
		
		assertTrue(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.PTR_QUALIFIER, "A3586555.A5703273.A5784826", prop)			
		);
	}
	
	@Test
	@Ignore
	public void testIsNotHierarchyParticipatingC0036775PtrPropQualifierNotPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithValue(refC0036775.getEntity(), "Serum Albumin, Radio Iodinated");
		
		assertFalse(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.PTR_QUALIFIER, "A3586555.A5703273.A5784826", prop)			
		);
	}
	
	@Test
	@Ignore
	public void testIsC0001555PtrPropQualifierPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithId(refC0001555.getEntity().getPresentation(), "A5703273");
		
		assertTrue(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.PTR_QUALIFIER, "A3586555.A5703273.A5784826", prop)			
		);
	}
	
	@Test
	@Ignore
	public void testIsNotHierarchyParticipatingC0001555PtrPropQualifierNotPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithId(refC0001555.getEntity().getPresentation(), "A7737265");
		
		assertFalse(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.PTR_QUALIFIER, "A3586555.A5703273.A5784826", prop)				
		);
	}
	
	@Test
	@Ignore
	public void testIsCL385598PtrPropQualifierPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithId(refCL385598.getEntity().getPresentation(), "A5784826");
		
		assertTrue(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.PTR_QUALIFIER, "A3586555.A5703273.A5784826", prop)				
		);
	}
	
	@Test
	@Ignore
	public void testIsNotHierarchyParticipatingCL385598PtrPropQualifierNotPresent() throws Exception {	
		Property prop = DataTestUtils.getPropertyWithId(refCL385598.getEntity().getPresentation(), "A7709649");
		
		assertFalse(
				DataTestUtils.isQualifierNameAndValuePresentInProperty(
						RrfLoaderConstants.PTR_QUALIFIER, "A3586555.A5703273.A5784826", prop)				
		);
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(MrhierPropertyQualifierTestIT.class);  
	} 
	*/ 
}