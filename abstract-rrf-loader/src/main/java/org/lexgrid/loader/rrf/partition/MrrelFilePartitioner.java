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
package org.lexgrid.loader.rrf.partition;

import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class MrrelFilePartitioner.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrelFilePartitioner implements DataPartitioner<Mrrel> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.partition.DataPartitioner#getPartitionNumberForInputRow(java.lang.Object)
	 */
	public int getPartitionNumberForInputRow(Mrrel item) {
		String cui = item.getCui1();
		return getLastNumberOfCui(cui);
	}
	
	/**
	 * Gets the last number of cui.
	 * 
	 * @param cui the cui
	 * 
	 * @return the last number of cui
	 */
	public static int getLastNumberOfCui(String cui){
		String lastLetterOfCui = cui.substring(cui.length()-1);
		return Integer.valueOf(lastLetterOfCui);
	}
}