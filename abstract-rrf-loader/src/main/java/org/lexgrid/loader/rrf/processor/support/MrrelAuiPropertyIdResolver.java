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
package org.lexgrid.loader.rrf.processor.support;

import org.lexgrid.loader.processor.support.PropertyIdResolver;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.springframework.util.Assert;

public class MrrelAuiPropertyIdResolver implements PropertyIdResolver<Mrrel>{

	public enum Aui {AUI1, AUI2};
	
	private Aui aui;
	
	public String getPropertyId(Mrrel item) {
		Assert.notNull(aui);
		if(aui.equals(Aui.AUI1)) {
			return item.getAui1();
		} else if(aui.equals(Aui.AUI2)) {
			return item.getAui2();
		}
		
		else throw new RuntimeException("Cannot get Aui Property Id.");	
	}

	public Aui getAui() {
		return aui;
	}

	public void setAui(Aui aui) {
		this.aui = aui;
	}
}