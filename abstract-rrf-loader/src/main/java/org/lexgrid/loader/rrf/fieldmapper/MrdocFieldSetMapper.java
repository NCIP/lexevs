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
package org.lexgrid.loader.rrf.fieldmapper;

import org.lexgrid.loader.rrf.model.Mrdoc;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class MrdocFieldSetMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdocFieldSetMapper implements FieldSetMapper<Mrdoc>{
	
	/**
	 * MRDOC.RRF|Typed key value metadata map|DOCKEY,VALUE,TYPE,EXPL|4|1702|107080|
	 * 
	 * @param values the values
	 * 
	 * @return the mrdoc
	 */
	public Mrdoc mapFieldSet(FieldSet values) {
		int index = 0;
		Mrdoc mrdoc = null;
		try {
			mrdoc = new Mrdoc();
			mrdoc.setDockey(values.readString(index++));
			mrdoc.setValue(values.readString(index++));	
			mrdoc.setType(values.readString(index++));
			mrdoc.setExpl(values.readString(index++));	
		} catch (RuntimeException e) {
			System.out.println(values.getValues().toString());
		}		
		return mrdoc;		
	}
}
