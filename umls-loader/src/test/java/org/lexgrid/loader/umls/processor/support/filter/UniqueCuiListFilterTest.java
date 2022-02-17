
package org.lexgrid.loader.umls.processor.support.filter;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class UniqueCuiListFilterTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UniqueCuiListFilterTest {

	/**
	 * Test filter by cui.
	 */
	@Test
	public void testFilterByCui(){
		UniqueCuiListFilter filter = new UniqueCuiListFilter();	
		
		Mrconso mrconso1 = new Mrconso();
		mrconso1.setCui("1");
		
		Mrconso mrconso2 = new Mrconso();
		mrconso2.setCui("2");
		
		Mrconso mrconso3 = new Mrconso();
		mrconso3.setCui("1");
		
		List<Mrconso> filteredList = filter.filter(Arrays.asList(new Mrconso[]{mrconso1, mrconso2, mrconso3}));
		
		Assert.assertTrue(filteredList.size() == 2);
			
		boolean foundOne = false;
		boolean foundTwo = false;
		for(Mrconso item : filteredList){
			if(item.getCui().equals("1")){
				foundOne = true;
			}
			if(item.getCui().equals("2")){
				foundTwo = true;
			}
		}
		
		Assert.assertTrue(foundOne);
		
		Assert.assertTrue(foundTwo);		
	}
}