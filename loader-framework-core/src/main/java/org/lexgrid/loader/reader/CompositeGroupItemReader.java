

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

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lexgrid.loader.reader.support.CompositeGroupComparator;
import org.lexgrid.loader.reader.support.CompositeReaderChunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * The Class CompositeGroupItemReader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CompositeGroupItemReader<I1,I2> implements ItemReader<CompositeReaderChunk<I1,I2>>{
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(CompositeGroupItemReader.class);
	
	/** The master group item reader. */
	private ItemReader<List<I1>> masterGroupItemReader;
	
	/** The slave group item reader. */
	private ItemReader<List<I2>> slaveGroupItemReader;
	
	/** The composite group comparator. */
	private CompositeGroupComparator<I1,I2> compositeGroupComparator;
	
	/** The slave cache. */
	private Queue<List<I2>> slaveCache = new LinkedBlockingQueue<List<I2>>(1);

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	public CompositeReaderChunk<I1,I2> read() throws Exception, UnexpectedInputException,
			ParseException {
		CompositeReaderChunk<I1,I2> compositeReaderChunk = new CompositeReaderChunk<I1,I2>();
		
		List<I1> masterGroup = masterGroupItemReader.read();
		if(masterGroup == null){
			return null;
		}
		
		List<I2> slaveGroup = null;
		if(!slaveCache.isEmpty()){
			slaveGroup = slaveCache.poll();
			log.debug("Getting Group from queue.");
		} else {
			slaveGroup = slaveGroupItemReader.read();
		}
		
		if(!compositeGroupComparator.doGroupsMatch(masterGroup, slaveGroup)){
			if(slaveGroup != null) {
				slaveCache.add(slaveGroup);
			}
			log.debug("Group mismatch -- queueing...");
		} else {
			compositeReaderChunk.setItem2List(slaveGroup);
		}
		compositeReaderChunk.setItem1List(masterGroup);
		return compositeReaderChunk;
	}

	public ItemReader<List<I1>> getMasterGroupItemReader() {
		return masterGroupItemReader;
	}

	public void setMasterGroupItemReader(ItemReader<List<I1>> masterGroupItemReader) {
		this.masterGroupItemReader = masterGroupItemReader;
	}

	public ItemReader<List<I2>> getSlaveGroupItemReader() {
		return slaveGroupItemReader;
	}

	public void setSlaveGroupItemReader(ItemReader<List<I2>> slaveGroupItemReader) {
		this.slaveGroupItemReader = slaveGroupItemReader;
	}

	public CompositeGroupComparator<I1, I2> getCompositeGroupComparator() {
		return compositeGroupComparator;
	}

	public void setCompositeGroupComparator(CompositeGroupComparator<I1, I2> compositeGroupComparator) {
		this.compositeGroupComparator = compositeGroupComparator;
	}

}