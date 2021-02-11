
package org.lexevs.paging;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class AbstractRefereshingPageableIteratorTest {

	
	@Test
	public void testIterate() {
		Iterator<String> itr = getTestIterator(2);
		for(int i=1; i<7;i++) {
			String foundString = itr.next();
			assertEquals(String.valueOf(i), foundString);
		}
		
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testIterateWithBigPage() {
		Iterator<String> itr = getTestIterator(50);
		for(int i=1; i<7;i++) {
			String foundString = itr.next();
			assertEquals(String.valueOf(i), foundString);
		}
		
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testIterateWithSmallPage() {
		Iterator<String> itr = getTestIterator(1);
		for(int i=1; i<7;i++) {
			String foundString = itr.next();
			assertEquals(String.valueOf(i), foundString);
		}
		
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testPageMoreResultsReturned() {
		Iterator<String> itr = getTestIteratorMoreResultsReturned(2);
		for(int i=1; i<7;i++) {
			String foundString = itr.next();
			assertEquals(String.valueOf(i), foundString);
		}
		
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testPageMoreResultsReturnedOddPageSize() {
		Iterator<String> itr = getTestIteratorMoreResultsReturned(3);
		for(int i=1; i<7;i++) {
			String foundString = itr.next();
			assertEquals(String.valueOf(i), foundString);
		}
		
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testPageLessResultsReturnedPageSize() {
		Iterator<String> itr = getTestIteratorLessResultsReturned(3);
		for(int i=1; i<7;i++) {
			String foundString = itr.next();
			assertEquals(String.valueOf(i), foundString);
		}
		
		assertFalse(itr.hasNext());
		
	}

	private Iterator<String> getTestIterator(int pageSize){
		return new AbstractRefereshingPageableIterator<String,String>(pageSize) {
			
			private String[] strings = new String[] {"1", "2", "3", 
					"4", "5", "6"};

			@Override
			protected List<String> doPage(int position, int pageSize) {
				List<String> returnList = new ArrayList<String>();
				
				for(int i=position;i<pageSize+position && i<strings.length;i++) {
					returnList.add(strings[i]);
				}
				
				return returnList;
			}

			@Override
			protected String doGetRefresh() {
				return "";
			}

			@Override
			protected void doRefresh(String refresh) {
				//
			}
			
		};
	}
	
	private Iterator<String> getTestIteratorMoreResultsReturned(int pageSize){
		return new AbstractRefereshingPageableIterator<String,String>(pageSize) {
			
			private String[] strings = new String[] {"1", "2", "3", 
					"4", "5", "6"};
			
			private Iterator<String> itr = Arrays.asList(strings).iterator();

			@Override
			protected List<String> doPage(int position, int pageSize) {
				List<String> returnList = new ArrayList<String>();
				
				for(int i=0;i<pageSize + 2 && itr.hasNext();i++){
					returnList.add(itr.next());
				}
				return returnList;
			}

			@Override
			protected String doGetRefresh() {
				return "";
			}

			@Override
			protected void doRefresh(String refresh) {
				//
			}

		};
	}

	private Iterator<String> getTestIteratorLessResultsReturned(int pageSize){
		return new AbstractRefereshingPageableIterator<String,String>(pageSize) {

			private String[] strings = new String[] {"1", "2", "3", 
					"4", "5", "6"};

			private Iterator<String> itr = Arrays.asList(strings).iterator();

			@Override
			protected List<String> doPage(int position, int pageSize) {
				List<String> returnList = new ArrayList<String>();

				if(itr.hasNext()){
					returnList.add(itr.next());
				} 

				return returnList;
			}
			
			@Override
			protected String doGetRefresh() {
				return "";
			}

			@Override
			protected void doRefresh(String refresh) {
				//
			}
		};
	}

}