
package org.lexgrid.loader.processor.decorator;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lexgrid.loader.logging.LoggerFactory;
import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

/**
 * The Class ListPostProcessorValidatingDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ListPostProcessorValidatingDecorator<I,O> extends LoggingBean implements ItemProcessor<List<I>, List<O>> {

	/** The processor. */
	private ItemProcessor<List<I>, List<O>> processor;
	
	/** The validator. */
	private Validator<O> validator;
	
	/**
	 * Instantiates a new list post processor validating decorator.
	 * 
	 * @param processor the processor
	 */
	public ListPostProcessorValidatingDecorator(ItemProcessor<List<I>, List<O>> processor){
		this.processor = processor;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<O> process(List<I> items) throws Exception {
		List<O> returnList = new ArrayList<O>();

		for(O item : processor.process(items)){
			try {
				validator.validate(item);
				returnList.add(item);
			} catch (ValidationException e) {
				getLogger().info("Skipping processed result: " + e.getMessage());
			}
		}

		return returnList;
	}

	/**
	 * Gets the processor.
	 * 
	 * @return the processor
	 */
	public ItemProcessor<List<I>, List<O>> getProcessor() {
		return processor;
	}

	/**
	 * Sets the processor.
	 * 
	 * @param processor the processor
	 */
	public void setProcessor(ItemProcessor<List<I>, List<O>> processor) {
		this.processor = processor;
	}

	/**
	 * Gets the validator.
	 * 
	 * @return the validator
	 */
	public Validator<O> getValidator() {
		return validator;
	}

	/**
	 * Sets the validator.
	 * 
	 * @param validator the new validator
	 */
	public void setValidator(Validator<O> validator) {
		this.validator = validator;
	}



	
}