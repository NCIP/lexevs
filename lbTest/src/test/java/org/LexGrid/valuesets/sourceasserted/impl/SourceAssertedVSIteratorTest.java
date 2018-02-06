package org.LexGrid.valuesets.sourceasserted.impl;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.sourceasserted.impl.AssertedValueSetResolvedConceptReferenceIterator;

public class SourceAssertedVSIteratorTest {
	AssertedValueSetParameters params;
	AssertedValueSetService vsSvc;
	int count;

	@Before
	public void setUp() throws Exception {
		
		params = new AssertedValueSetParameters.Builder("0.1.5.1").
				assertedDefaultHierarchyVSRelation("Concept_In_Subset").
				codingSchemeName("owl2lexevs").
				codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl")
				.build();
		vsSvc = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAssertedValueSetService();
		vsSvc.init(params);
		count =vsSvc.getVSEntityCountForTopNodeCode("C54453");
	}

	@Test
	public void testReturnAll() throws LBResourceUnavailableException, LBInvocationException {

		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.next(-1);
		assertNotNull(list);
		assertTrue(list.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list.getResolvedConceptReference()[0]);
		assertEquals(count, list.getResolvedConceptReferenceCount());
	}
	
	@Test
	public void testPage() throws LBResourceUnavailableException, LBInvocationException {

		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.next(2);
		assertNotNull(list);
		assertTrue(list.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list.getResolvedConceptReference()[0]);
		assertEquals(2, list.getResolvedConceptReferenceCount());
		assertEquals(3, itr.numberRemaining());
		
		ResolvedConceptReferenceList list2 = itr.next(2);
		assertNotNull(list2);
		assertTrue(list2.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list2.getResolvedConceptReference()[0]);
		assertEquals(2, list2.getResolvedConceptReferenceCount());
		assertEquals(1, itr.numberRemaining());
		
		ResolvedConceptReferenceList list3 = itr.next(2);
		assertNotNull(list3);
		assertTrue(list3.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list3.getResolvedConceptReference()[0]);
		assertEquals(1, list3.getResolvedConceptReferenceCount());
		assertEquals(0, itr.numberRemaining());
		
		ResolvedConceptReferenceList list4 = itr.next(2);
		assertNotNull(list4);
		assertTrue(list4.getResolvedConceptReferenceCount() == 0);
		assertEquals(0, itr.numberRemaining());
	}
	
	@Test
	public void testPageUpperBoundary() throws LBResourceUnavailableException, LBInvocationException {

		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.next(count);
		assertNotNull(list);
		assertTrue(list.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list.getResolvedConceptReference()[0]);
		assertEquals(count, list.getResolvedConceptReferenceCount());
		assertEquals(0, itr.numberRemaining());
		
		AssertedValueSetResolvedConceptReferenceIterator itr2 = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list2 = itr2.next(count - 1);
		assertNotNull(list2);
		assertTrue(list2.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list2.getResolvedConceptReference()[0]);
		assertEquals(count - 1, list2.getResolvedConceptReferenceCount());
		assertEquals(1, itr2.numberRemaining());
	}
	
	@Test
	public void testPageUpperBoundaryOverFlow() throws LBResourceUnavailableException, LBInvocationException {

		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.next(count + 1);
		assertNotNull(list);
		assertTrue(list.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list.getResolvedConceptReference()[0]);
		assertEquals(count, list.getResolvedConceptReferenceCount());
	}
	
	@Test
	public void testPageTwoSizes() throws LBResourceUnavailableException, LBInvocationException {

		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.next(3);
		assertNotNull(list);
		assertTrue(list.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list.getResolvedConceptReference()[0]);
		assertEquals(3, list.getResolvedConceptReferenceCount());
		assertEquals(count - 3, itr.numberRemaining());
		
		ResolvedConceptReferenceList list2 = itr.next(2);
		assertNotNull(list2);
		assertTrue(list2.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list2.getResolvedConceptReference()[0]);
		assertEquals(2, list2.getResolvedConceptReferenceCount());
		assertEquals(count - 5, itr.numberRemaining());
		
		//checking possible overlap
		assertNotSame(list.getResolvedConceptReference(2).getCode(), list2.getResolvedConceptReference(0).getCode());
		
		ResolvedConceptReferenceList list4 = itr.next(2);
		assertNotNull(list4);
		assertTrue(list4.getResolvedConceptReferenceCount() == 0);
		assertEquals(0, itr.numberRemaining());
	}
	
	
	
	@Test
	public void testNext() throws LBResourceUnavailableException, LBInvocationException {

		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		int remaining = count;
		for(int i = 0; count > i; i++) {
		ResolvedConceptReference ref = itr.next();
		assertNotNull(ref);
		remaining = remaining -1;
		assertEquals(itr.numberRemaining(), remaining);
		System.out.println(i);
		}
	}
}
