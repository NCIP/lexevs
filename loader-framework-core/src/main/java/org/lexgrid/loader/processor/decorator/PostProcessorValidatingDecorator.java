/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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