
package org.lexgrid.loader.reader.decorator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The Class ProcessingListDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ProcessingListDecorator<I,O> implements FactoryBean, InitializingBean{
	
	/** The list. */
	private List<I> list;
	
	/** The output list. */
	private List<O> outputList = new ArrayList<O>();
	
	/** The processor. */
	private ItemProcessor<I,O> processor;

	/**
	 * Instantiates a new processing list decorator.
	 * 
	 * @param list the list
	 */
	public ProcessingListDecorator(List<I> list){
		this.list = list;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return outputList;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return List.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(list, "This decorator assumes a List to decorate.");
		
		for(I item : list){
			outputList.add(processor.process(item));
		}
	}

	/**
	 * Gets the processor.
	 * 
	 * @return the processor
	 */
	public ItemProcessor<I, O> getProcessor() {
		return processor;
	}

	/**
	 * Sets the processor.
	 * 
	 * @param processor the processor
	 */
	public void setProcessor(ItemProcessor<I, O> processor) {
		this.processor = processor;
	}	
}