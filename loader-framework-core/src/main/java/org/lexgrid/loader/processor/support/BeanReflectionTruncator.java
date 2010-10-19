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
package org.lexgrid.loader.processor.support;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;

/**
 * The Class BeanReflectionTruncator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BeanReflectionTruncator implements Truncator {

	/** The fields to truncate. */
	private Map<String, Integer> fieldsToTruncate;
	
	/** The path delimiter. */
	private String pathDelimiter = ".";

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.Truncator#truncate(java.lang.Object)
	 */
	public <T> T truncate(T item) throws Exception {
		for(String fields : this.fieldsToTruncate.keySet()){
			String[] path = this.getFieldPath(fields);
			this.doTruncate(path, item, fieldsToTruncate.get(fields));
		}
		return item;
	}
	
	/**
	 * Do truncate.
	 * 
	 * @param path the path
	 * @param obj the obj
	 * @param length the length
	 * 
	 * @throws Exception the exception
	 */
	protected void doTruncate(String[] path, Object obj, int length) throws Exception {
		if(path.length > 1){
			doTruncate(
					(String[])ArrayUtils.remove(path, 0),
					getObject(obj, path[0]), length);
		} else {
			Object fieldObj = getObject(obj, path[0]);
			if(fieldObj instanceof String){
				String fieldValue = (String)fieldObj;
				if(fieldValue.length() >= length){
					this.getField(obj, path[0]).set(obj, 
							fieldValue.substring(0, length));
				}		
			}
		}
	}
	
	/**
	 * Gets the field.
	 * 
	 * @param obj the obj
	 * @param fieldName the field name
	 * 
	 * @return the field
	 * 
	 * @throws Exception the exception
	 */
	public Field getField(Object obj, String fieldName) throws Exception {
		Class clazz = obj.getClass();
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field;
	}
	
	/**
	 * Gets the object.
	 * 
	 * @param obj the obj
	 * @param fieldName the field name
	 * 
	 * @return the object
	 * 
	 * @throws Exception the exception
	 */
	public Object getObject(Object obj, String fieldName) throws Exception {
		return getField(obj, fieldName).get(obj);
	}

	/**
	 * Gets the field path.
	 * 
	 * @param path the path
	 * 
	 * @return the field path
	 */
	protected String[] getFieldPath(String path){
		return StringUtils.tokenizeToStringArray(path, pathDelimiter);
	}
	
	/**
	 * Gets the fields to truncate.
	 * 
	 * @return the fields to truncate
	 */
	public Map<String, Integer> getFieldsToTruncate() {
		return fieldsToTruncate;
	}

	/**
	 * Sets the fields to truncate.
	 * 
	 * @param fieldsToTruncate the fields to truncate
	 */
	public void setFieldsToTruncate(Map<String, Integer> fieldsToTruncate) {
		this.fieldsToTruncate = fieldsToTruncate;
	}
	
	
}