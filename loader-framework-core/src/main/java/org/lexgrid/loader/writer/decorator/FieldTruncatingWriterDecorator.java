
package org.lexgrid.loader.writer.decorator;

import java.util.List;

import org.lexgrid.loader.processor.support.Truncator;
import org.springframework.batch.item.ItemWriter;

/**
 * The Class FieldTruncatingWriterDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class FieldTruncatingWriterDecorator<O> implements ItemWriter<O>{

	/** The decorated. */
	private ItemWriter<O> decorated;
	
	/** The truncator. */
	private Truncator truncator;
	
	/**
	 * Instantiates a new field truncating writer decorator.
	 * 
	 * @param decorated the decorated
	 */
	public FieldTruncatingWriterDecorator(
			ItemWriter<O> decorated){
		this.decorated = decorated;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends O> list) throws Exception {
		for(O item : list){
			if(item instanceof List){
				for(Object listItem : (List)item){
					truncator.truncate(listItem);
				}
			} else {
				truncator.truncate(item);
			}
		}
		decorated.write(list);
	}

	/**
	 * Gets the truncator.
	 * 
	 * @return the truncator
	 */
	public Truncator getTruncator() {
		return truncator;
	}

	/**
	 * Sets the truncator.
	 * 
	 * @param truncator the new truncator
	 */
	public void setTruncator(Truncator truncator) {
		this.truncator = truncator;
	}
}