
package org.lexgrid.loader.processor.decorator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lexgrid.loader.logging.LoggerFactory;
import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

/**
 * The Class PostProcessorValidatingDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PostProcessorValidatingDecorator<I,O> extends LoggingBean implements ItemProcessor<I,O>{
	
	/** The processor. */
	private ItemProcessor<I,O> processor;
	
	/** The validator. */
	private Validator<O> validator;
	
	/**
	 * Instantiates a new post processor validating decorator.
	 * 
	 * @param processor the processor
	 */
	public PostProcessorValidatingDecorator(ItemProcessor<I,O> processor){
		this.processor = processor;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public O process(I item) throws Exception {
		O processedItem = processor.process(item);
		
		try {
			validator.validate(processedItem);
		} catch (ValidationException e) {
			getLogger().info("Skipping processed result: " + e.getMessage());
			return null;
		}
		
		return processedItem;
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