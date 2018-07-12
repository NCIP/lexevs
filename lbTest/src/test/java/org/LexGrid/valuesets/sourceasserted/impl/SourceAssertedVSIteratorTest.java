package org.LexGrid.valuesets.sourceasserted.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
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
	
	@Test
	public void testGetLast() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.get(2,5);

		assertNotNull(list);
		assertTrue(list.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list.getResolvedConceptReference()[0]);
		assertEquals(3, list.getResolvedConceptReferenceCount());
	}
	
	@Test
	public void testGetFirst() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.get(0,2);

		assertNotNull(list);
		assertTrue(list.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list.getResolvedConceptReference()[0]);
		assertEquals(2, list.getResolvedConceptReferenceCount());
	}
	
	@Test
	public void testGetAll() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.get(0,5);

		assertNotNull(list);
		assertTrue(list.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list.getResolvedConceptReference()[0]);
		assertEquals(5, list.getResolvedConceptReferenceCount());
	}
	
	@Test
	public void testGetTooManyBoundary() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.get(0,6);

		assertNotNull(list);
		assertTrue(list.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list.getResolvedConceptReference()[0]);
		assertEquals(5, list.getResolvedConceptReferenceCount());
	}
	
	@Test
	public void testGetOneTooFewBoundary() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.get(0,4);

		assertNotNull(list);
		assertTrue(list.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list.getResolvedConceptReference()[0]);
		assertEquals(4, list.getResolvedConceptReferenceCount());
	}
	
	@Test
	public void testGetOneBoundary() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.get(0,1);

		assertNotNull(list);
		assertTrue(list.getResolvedConceptReferenceCount() > 0);
		assertNotNull(list.getResolvedConceptReference()[0]);
		assertEquals(1, list.getResolvedConceptReferenceCount());
	}
	
	@Test
	public void testGetNoneBoundary() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.get(0,0);

		assertNotNull(list);
		assertEquals(0, list.getResolvedConceptReferenceCount());
	}
	
	@Test
	public void testGetFinishBeforeStart() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReferenceList list = itr.get(1,0);

		assertNotNull(list);
		assertEquals(0, list.getResolvedConceptReferenceCount());
	}
	
	@Test
	public void testTransformationFromDBToRCR() {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		ResolvedConceptReference ref = itr.next();

		assertNotNull(ref);
		assertEquals(ref.getCodingSchemeName(), "owl2lexevs");
		assertEquals(ref.getCodingSchemeURI(), "http://evs.nci.nih.gov/valueset/FDA/C54453");
		
	}
	
	@Test
	public void testHasNext() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		List<ResolvedConceptReference> list = new ArrayList<ResolvedConceptReference>();
		while(itr.hasNext()) {
			list.add(itr.next());
		}
		assertTrue(list.size() > 0);
		assertEquals(list.size(), 5);
	}
	
	@Test
	public void testHasNextPage() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		List<ResolvedConceptReference> list = new ArrayList<ResolvedConceptReference>();
		while(itr.hasNext()) {
			list.addAll(Arrays.asList(itr.next(2).getResolvedConceptReference()));
		}
		assertTrue(list.size() > 0);
		assertEquals(list.size(), 5);
	}
	
	@Test
	public void testHasNextPageLowerBoundary() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		List<ResolvedConceptReference> list = new ArrayList<ResolvedConceptReference>();
		while(itr.hasNext()) {
			list.addAll(Arrays.asList(itr.next(1).getResolvedConceptReference()));
		}
		assertTrue(list.size() > 0);
		assertEquals(list.size(), 5);
	}
	
	@Test
	public void testHasNextPageLowerBoundaryBadRequest() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		List<ResolvedConceptReference> list = new ArrayList<ResolvedConceptReference>();
		while(itr.hasNext()) {
			list.addAll(Arrays.asList(itr.next(0).getResolvedConceptReference()));
		}
		assertEquals(list.size(), 0);
	}
	
	@Test
	public void testHasNextPageUpperBoundary() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		List<ResolvedConceptReference> list = new ArrayList<ResolvedConceptReference>();
		while(itr.hasNext()) {
			list.addAll(Arrays.asList(itr.next(5).getResolvedConceptReference()));
		}
		assertTrue(list.size() > 0);
		assertEquals(list.size(), 5);
	}
	
	@Test
	public void testHasNextPageUpperBoundaryOverFlow() throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		AssertedValueSetResolvedConceptReferenceIterator itr = new 
				AssertedValueSetResolvedConceptReferenceIterator("C54453", params);
		List<ResolvedConceptReference> list = new ArrayList<ResolvedConceptReference>();
		while(itr.hasNext()) {
			list.addAll(Arrays.asList(itr.next(6).getResolvedConceptReference()));
		}
		assertTrue(list.size() > 0);
		assertEquals(list.size(), 5);
	}
	

}
