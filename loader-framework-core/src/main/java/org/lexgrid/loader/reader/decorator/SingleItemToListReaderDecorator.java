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
package org.lexgrid.loader.reader.decorator;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class SingleItemToListReaderDecorator<I> implements ItemReader<List<I>>{

	private ItemReader<I> decoratedItemReader;
	

	public SingleItemToListReaderDecorator(ItemReader<I> decoratedItemReader){
		this.decoratedItemReader = decoratedItemReader;		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<I> read() throws Exception, UnexpectedInputException, ParseException {
		I item = decoratedItemReader.read();
		if(item == null) {
			return null;
		}
		return Arrays.asList(item);
	}
}