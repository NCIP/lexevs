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

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.Assert;

public class SingleItemToListProcessorDecorator<I,O> implements ItemProcessor<I,List<O>>{

	private ItemProcessor<List<I>,List<O>> decoratedItemProcessor;
	

	public SingleItemToListProcessorDecorator(ItemProcessor<List<I>,List<O>> decoratedItemProcessor){
		this.decoratedItemProcessor = decoratedItemProcessor;		
	}

	public List<O> process(I item) throws Exception {
		List<I> inputList = new ArrayList<I>();
		inputList.add(item);
		return decoratedItemProcessor.process(inputList);
		
	}
}