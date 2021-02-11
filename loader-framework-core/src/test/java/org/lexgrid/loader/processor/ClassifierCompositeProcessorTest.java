
package org.lexgrid.loader.processor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class ClassifierCompositeProcessorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ClassifierCompositeProcessorTest {

	/**
	 * Test process composite.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testProcessComposite() throws Exception {
		ClassifierCompositeProcessor<String> processor = new ClassifierCompositeProcessor<String>();
		
		List processorList = new ArrayList();
		processorList.add(new ItemProcessor<String,Integer>(){

			public Integer process(String item) throws Exception {
				return Integer.valueOf(item);
			}	
		});
		
		processorList.add(new ItemProcessor<String,String>(){

			public String process(String item) throws Exception {
				return item;
			}	
		});
		
		processor.setProcessorList(processorList);
		
		List<?> returnList = processor.process("1");
		
		assertTrue(returnList.size() == 2);
		
		assertTrue(returnList.contains(new String("1")));
		
		assertTrue(returnList.contains(new Integer(1)));
				
	}
	
	/**
	 * Test process composite list.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testProcessCompositeList() throws Exception {
		ClassifierCompositeProcessor<String> processor = new ClassifierCompositeProcessor<String>();
		
		List processorList = new ArrayList();
		processorList.add(new ItemProcessor<String,Integer>(){

			public Integer process(String item) throws Exception {
				return Integer.valueOf(item);
			}	
		});
		
		processorList.add(new ItemProcessor<String,List<String>>(){

			public List<String> process(String item) throws Exception {
				return Arrays.asList(new String[]{item});
			}	
		});
		
		processor.setProcessorList(processorList);
		
		List<?> returnList = processor.process("1");
		
		assertTrue(returnList.size() == 2);
		
		assertTrue(returnList.contains(new Integer(1)));
		
		boolean foundTheList = false;
		
		for(Object obj : returnList){
			if(obj instanceof List){
				List<String> list = (List<String>)obj;
				assertTrue(list.size() == 1);
				assertTrue(list.get(0) instanceof String);
				assertTrue(list.get(0).equals("1"));
				
				foundTheList = true;
			}
		}			
		
		assertTrue(foundTheList);
	}
	
}