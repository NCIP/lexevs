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
package org.lexgrid.loader.rrf.fieldmapper;

import org.lexgrid.loader.rrf.model.Mrsty;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class MrstyFieldSetMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrstyFieldSetMapper implements FieldSetMapper<Mrsty>{
	
	//MRSTY.RRF|Semantic Types|CUI,TUI,STN,STY,ATUI,CVF|6|7592|441433|
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	public Mrsty mapFieldSet(FieldSet values) {
		int index = 0;
		Mrsty Mrsty = null;

		Mrsty = new Mrsty();
		Mrsty.setCui(values.readString(index++));
		Mrsty.setTui(values.readString(index++));	
		Mrsty.setStn(values.readString(index++));
		Mrsty.setSty(values.readString(index++));
		Mrsty.setAtui(values.readString(index++));
		Mrsty.setCvf(values.readString(index++));
		
		return Mrsty;	
	}
}