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
package org.lexgrid.loader.rrf.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.lexgrid.loader.rrf.model.Mrdoc;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class MrdocRelationNameProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdocRelationNameProcessor implements ItemProcessor<Mrdoc, String>{
	
	/** The forward name comparator. */
	private Comparator<String> forwardNameComparator = new ForwardNameComparator();
	
	/** The processed names. */
	private List<String> processedNames = new ArrayList<String>();

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public String process(Mrdoc item) throws Exception {
		String firstName = item.getValue();
		String secondName = item.getExpl();

		if(!processedNames.contains(firstName) && !processedNames.contains(secondName)){
			if(forwardNameComparator.compare(firstName, secondName) > 0){
				return firstName;
			} else {
				return secondName;
			}
		}
		return null;
	}

	// *_of* is reverse
	// *_by* is reverse
	/**
	 * The Class ForwardNameComparator.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class ForwardNameComparator implements Comparator<String> {
		
		/** The inverse keys. */
		private final List<String> inverseKeys = Arrays.asList("PAR");
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(String name1, String name2) {
			processedNames.add(name1);
			processedNames.add(name2);
			
			if(containsStringInList(name1) && !containsStringInList(name2)){
				return -1;
			}
			
			if(containsStringInList(name2) && !containsStringInList(name1)){
				return 1;
			}

			return 1;
		}
		
		/**
		 * Contains string in list.
		 * 
		 * @param name the name
		 * 
		 * @return true, if successful
		 */
		protected boolean containsStringInList(String name){
			for(String key : inverseKeys){
				if(name.contains(key)){
					return true;
				}
			}
			return false;
		}
		
	}
	
}