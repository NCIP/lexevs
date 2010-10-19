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