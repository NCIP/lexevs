
package org.lexgrid.loader.processor;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.batch.item.ItemProcessor;

import static org.junit.Assert.*;

/**
 * The Class BasicListProcessorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BasicListProcessorTest {

	/**
	 * Test process.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testProcess() throws Exception{
		BasicListProcessor<String, Integer> blp = new BasicListProcessor<String, Integer>();
		
		
		blp.setDelegate(new ItemProcessor<String, Integer>(){
			
			public Integer process(String item){
				return Integer.valueOf(item);
				
			}
		
		});
		
		List<String> testList = Arrays.asList("1", "2", "3");
		
		List<Integer> processedList = blp.process(testList);
		
		assertTrue(processedList.size() == 3);
		assertTrue(processedList.get(0) == 1);
		assertTrue(processedList.get(1) == 2);
		assertTrue(processedList.get(2) == 3);
		
		
	}
}