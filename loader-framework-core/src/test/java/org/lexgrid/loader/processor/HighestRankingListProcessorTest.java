
package org.lexgrid.loader.processor;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.sql.DatabaseMetaData;
import java.util.Arrays;

import org.junit.Test;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class HighestRankingListProcessorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HighestRankingListProcessorTest {

	/**
	 * Test get highest rank.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetHighestRank() throws Exception {
		HighestRankingListProcessor processor = new HighestRankingListProcessor();
		
		TestSortingListProcessor p = new TestSortingListProcessor();
		p.setDelegate(new ItemProcessor(){

			public Object process(Object item) throws Exception {	
				return item;
			}			
		});
		
		processor.setSortingListProcessor(p);		
		
		String val = (String)processor.process(Arrays.asList(new String[]{"one", "two"}));
	
		assertTrue(val.equals("one"));
	}
	
	/**
	 * The Class TestSortingListProcessor.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class TestSortingListProcessor extends SortingListProcessor{

		/**
		 * Process.
		 * 
		 * @param item the item
		 * 
		 * @return the object
		 * 
		 * @throws Exception the exception
		 */
		public Object process(Object item) throws Exception {
			return "one";
		}	
	}
}