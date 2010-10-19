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
package org.lexgrid.loader.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The Class ListReader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ListReader<T> implements ItemReader<T>, InitializingBean {

	/** The list. */
	private List<T> list;
	
	/** The list iterator. */
	private Iterator<T> listIterator;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	public T read() throws Exception, UnexpectedInputException, ParseException {
		if(listIterator.hasNext()){
			return listIterator.next();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(list);
		listIterator = list.iterator();
	}

	/**
	 * Gets the list.
	 * 
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * Sets the list.
	 * 
	 * @param list the new list
	 */
	public void setList(List<T> list) {
		this.list = list;
	}
}