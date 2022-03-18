
package org.lexgrid.loader.writer.classifier;

import java.util.List;

import org.springframework.batch.classify.Classifier;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * The Class WriterClassifier.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class WriterClassifier implements Classifier<Object, ItemWriter>, ApplicationContextAware {

	/** The ctx. */
	private ApplicationContext ctx;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.classify.Classifier#classify(java.lang.Object)
	 */
	public ItemWriter classify(Object objectToWrite) {
		String writerName = getWriterName(objectToWrite);
		if(!ctx.containsBean(writerName)){
			throw new RuntimeException("Cannont find a writer for: " + objectToWrite + ". Looking for a writer named: " + writerName + ".");
		}
		return (ItemWriter)ctx.getBean(writerName);		
	}
	
	/**
	 * Gets the writer name.
	 * 
	 * @param objectToWrite the object to write
	 * 
	 * @return the writer name
	 */
	protected String getWriterName(Object objectToWrite){
		if(objectToWrite instanceof List){
			Object listObj = ((List)objectToWrite).get(0);
			String writerName = makeFirstLetterLowercase(getSimpleName(listObj));
			return writerName + "ListWriter";
		} else {
			String writerName = makeFirstLetterLowercase(getSimpleName(objectToWrite));
			return writerName + "Writer";
		}
	}
	
	/**
	 * Gets the simple name.
	 * 
	 * @param obj the obj
	 * 
	 * @return the simple name
	 */
	private String getSimpleName(Object obj){
		return obj.getClass().getSimpleName();
	}
	
	/**
	 * Make first letter lowercase.
	 * 
	 * @param stringToChange the string to change
	 * 
	 * @return the string
	 */
	private String makeFirstLetterLowercase(String stringToChange){
		return stringToChange.substring(0,1).toLowerCase() + stringToChange.substring(1);	
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext ctx)
	throws BeansException {
		this.ctx = ctx;
	}
}