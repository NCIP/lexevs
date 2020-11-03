package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

public class NodeGraphSpecificIteratorTest {

	GraphNodeContentTrackingIterator itr;
	
	List<ConceptReference> list;
	
	
	@Before
	public void setUp() throws Exception {
		list = new ArrayList<ConceptReference>();
		ConceptReference ref1 = Constructors.createConceptReference( "C2342", "ncit");
		ConceptReference ref2 = Constructors.createConceptReference( "C2342", "ncit");
		ConceptReference ref3 = Constructors.createConceptReference( "C2342", "ncit");
		ConceptReference ref4 = Constructors.createConceptReference( "C2342", "ncit");
		list.add(ref1);
		list.add(ref2);
		list.add(ref3);
		list.add(ref4);
	}

	@Test
	public void testInit() {
		itr = new GraphNodeContentTrackingIterator(new ArrayList<ConceptReference>());
		try{
			itr.hasNext();
		}catch(Exception e){
			assertSame("Iterator has not been initialized", e.getMessage());
		}
	}
	
	@Test
	public void testInit2() {
		itr = new GraphNodeContentTrackingIterator(null);
		try{
			itr.hasNext();
		}catch(Exception e){
			assertSame("Iterator has not been initialized", e.getMessage());
		}
	}
	
	@Test
	public void testInitGood() {
		itr = new GraphNodeContentTrackingIterator(list);
		try{
			itr.hasNext();
			assertEquals(4, itr.getTotalCacheSize());
			itr.next();
			assertEquals(4, itr.getTotalCacheSize());
		}catch(Exception e){
			fail();
		}
	}
	
	@Test
	public void testHasNext() {
		itr = new GraphNodeContentTrackingIterator(list);
		assertTrue(itr.hasNext());
	}
	
	@Test
	public void testHasNext1() {
		itr = new GraphNodeContentTrackingIterator(list);
		itr.next();
		assertTrue(itr.hasNext());
	}
	@Test
	public void testHasNext2() {
		itr = new GraphNodeContentTrackingIterator(list);
		itr.next();
		itr.next();
		assertTrue(itr.hasNext());
	}
	
	@Test
	public void testHasNext3() {
		itr = new GraphNodeContentTrackingIterator(list);
		itr.next();
		itr.next();
		itr.next();
		assertTrue(itr.hasNext());
	}
	
	@Test
	public void testHasNext4() {
		itr = new GraphNodeContentTrackingIterator(list);
		itr.next();
		itr.next();
		itr.next();
		itr.next();
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testHasNext5() {
		itr = new GraphNodeContentTrackingIterator(list);
		itr.next();
		itr.next();
		itr.next();
		itr.next();
		try{
			itr.next();
		}catch(Exception e){
			System.out.println(e);
			assertSame("Iterator is Empty", e.getMessage());
		}
	}
	
	@Test
	public void testNextEmpty() {
		itr = new GraphNodeContentTrackingIterator(new ArrayList<ConceptReference>());
		try{
			itr.next();
		}catch(Exception e){
			System.out.println(e);
			assertSame("Iterator is Empty", e.getMessage());
		}
	}
	
	@Test
	public void testNextEmpty2() {
		itr = new GraphNodeContentTrackingIterator(null);
		try{
			itr.next();
		}catch(Exception e){
			System.out.println(e);
			assertSame("Iterator has not been initialized", e.getMessage());
		}
	}
	
	@Test
	public void testHowMany() {
		itr = new GraphNodeContentTrackingIterator(list);
		assertEquals(itr.getNumberRemaining(), itr.getTotalCacheSize());
	}
	
	@Test
	public void testHowMany1() {
		itr = new GraphNodeContentTrackingIterator(list);
		itr.next();
		assertEquals(itr.getNumberRemaining(), itr.getTotalCacheSize() - 1);
	}
	@Test
	public void testHowMany2() {
		itr = new GraphNodeContentTrackingIterator(list);
		itr.next();
		itr.next();
		assertEquals(itr.getNumberRemaining(), itr.getTotalCacheSize() - 2);
	}
	
	@Test
	public void testHowMany3() {
		itr = new GraphNodeContentTrackingIterator(list);
		itr.next();
		itr.next();
		itr.next();
		assertEquals(itr.getNumberRemaining(), itr.getTotalCacheSize() - 3);
	}
	
	@Test
	public void testHowMany4() {
		itr = new GraphNodeContentTrackingIterator(list);
		itr.next();
		itr.next();
		itr.next();
		itr.next();
		assertEquals(0 , itr.getNumberRemaining());
		assertEquals(itr.getNumberRemaining(), itr.getTotalCacheSize() - 4);
	}
	

}
