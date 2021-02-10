
package org.lexgrid.loader.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.lexgrid.loader.processor.support.ListFilter;
import org.springframework.batch.item.ItemProcessor;

import static org.junit.Assert.*;

/**
 * The Class PreFilteringListProcessorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PreFilteringListProcessorTest {

	/**
	 * Test process.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testProcess() throws Exception{
		PreFilteringListProcessor<String, Integer> pflp = new PreFilteringListProcessor<String, Integer>();
		
		
		pflp.setDelegate(new ItemProcessor<String, Integer>(){
			
			public Integer process(String item){
				return Integer.valueOf(item);
				
			}
		
		});
		
		pflp.setListFilter(new ListFilter<String>(){

			public List<String> filter(List<String> items) {
				List<String> returnList = new ArrayList<String>();
				for(String num : items){
					if(!num.equals("2")){
						returnList.add(num);
					}
				}
				return returnList;
			}
		});
		
		
		List<String> testList = Arrays.asList("1", "2", "3");
		
		List<Integer> processedList = pflp.process(testList);
		
		assertTrue(processedList.size() == 2);
		assertTrue(processedList.get(0) == 1);
		assertTrue(processedList.get(1) == 3);	
	}
}