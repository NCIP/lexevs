/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexgrid.loader.reader.support;

import java.util.List;

/**
 * Will iterate through a list of Skip Policies. If ANY of the Skip policies indicates that record should be skipped,
 * it will be.
 * 
 * @param <I>  * 
 * @author m005256
 */
public class CompoundSkipPolicy<I> implements SkipPolicy<I> {

	/** The skip policy list. */
	private List<SkipPolicy<I>> skipPolicyList;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.SkipPolicy#toSkip(java.lang.Object)
	 */
	public boolean toSkip(I item) {
		for(SkipPolicy<I> policy : skipPolicyList){
			if(policy.toSkip(item) == true){
				return true;
			}
		}
		return false;
	}

}
