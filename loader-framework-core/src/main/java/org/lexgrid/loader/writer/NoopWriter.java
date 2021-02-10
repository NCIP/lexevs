
package org.lexgrid.loader.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

/**
 * The Class NoopWriter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NoopWriter<I> implements ItemWriter<I>{

	/**
	 * No-op Writer for writing to System.out -- useful for testing.
	 * 
	 * @param arg0 the arg0
	 * 
	 * @throws Exception the exception
	 * 
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends I> arg0) throws Exception {
		for(I item : arg0){
			System.out.println(item.toString());
		}
	}

}