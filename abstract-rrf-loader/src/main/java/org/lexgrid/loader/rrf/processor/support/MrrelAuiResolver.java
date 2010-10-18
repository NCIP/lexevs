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

import org.lexgrid.loader.rrf.model.Mrrel;

public class MrrelAuiResolver implements AuiResolver<Mrrel> {

	public enum AuiType {SOURCE_AUI, TARGET_AUI};
	
	private AuiType auiToReturn;
	
	public String getAui(Mrrel item) {
		if(auiToReturn == null){
			throw new RuntimeException("Please Specify an AUI type.");
		}
		
		if(auiToReturn.equals(AuiType.SOURCE_AUI)){
			return item.getAui1();
		} else {
			return item.getAui2();
		}
	}

	public AuiType getAuiToReturn() {
		return auiToReturn;
	}

	public void setAuiToReturn(AuiType auiToReturn) {
		this.auiToReturn = auiToReturn;
	}
}