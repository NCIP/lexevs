package org.cts.internal.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.cts2.internal.util.PagingList;
import org.junit.Test;

public class PagingListTest {
	
	@Test
	public void testEmptyIterator(){
		List<String> list = new ArrayList<String>();
		
		PagingList<String> plist = new PagingList<String>(list.iterator(), list.size());
		
		assertEquals(0, plist.size());
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testEmptyIteratorException(){
		List<String> list = new ArrayList<String>();
		
		PagingList<String> plist = new PagingList<String>(list.iterator(), list.size());
		
		plist.get(0);
	}

	@Test
	public void testGetLast(){
		List<String> list = Arrays.asList("one","two","three");
		
		PagingList<String> plist = new PagingList<String>(list.iterator(), list.size());
		
		assertEquals("three",plist.get(2));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetLastPlusOne(){
		List<String> list = Arrays.asList("one","two","three");
		
		PagingList<String> plist = new PagingList<String>(list.iterator(), list.size());
		
		plist.get(3);
	}
	
	@Test
	public void testGetCached(){
		List<String> list = Arrays.asList("one","two","three", "four");
		
		PagingList<String> plist = new PagingList<String>(list.iterator(), list.size());
		
		assertEquals("four", plist.get(3));
		
		assertEquals("two", plist.get(1));
	}
	
	@Test
	public void testIterate(){
		List<String> list = Arrays.asList("one","two","three", "four");
		
		PagingList<String> plist = new PagingList<String>(list.iterator(), list.size());
		
		Iterator<String> itr = plist.iterator();
		
		assertEquals("one", itr.next());
		assertEquals("two", itr.next());
		assertEquals("three", itr.next());
		assertEquals("four", itr.next());
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testIterateTwice(){
		List<String> list = Arrays.asList("one","two","three", "four");
		
		PagingList<String> plist = new PagingList<String>(list.iterator(), list.size());
		
		for(int i=0;i<2;i++){
			Iterator<String> itr = plist.iterator();
			
			assertEquals("one", itr.next());
			assertEquals("two", itr.next());
			assertEquals("three", itr.next());
			assertEquals("four", itr.next());
			assertFalse(itr.hasNext());
		}
	}

}
