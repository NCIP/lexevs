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

import org.lexgrid.loader.rrf.model.Mrrel;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class MrrelFieldSetMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrelFieldSetMapper implements FieldSetMapper<Mrrel>{
	
	//MRREL.RRF|Related Concepts|
	//CUI1,AUI1,STYPE1,REL,CUI2,AUI2,STYPE2,RELA,RUI,
	//SRUI,SAB,SL,RG,DIR,SUPPRESS,CVF|16|2398|179873|
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	public Mrrel mapFieldSet(FieldSet values) {
		if(values.getFieldCount() < 1){
			return null;
		}
		int index = 0;
		Mrrel mrrel = new Mrrel();
		mrrel.setCui1(values.readString(index++));
		mrrel.setAui1(values.readString(index++));
		mrrel.setStype1(values.readString(index++));
		mrrel.setRel(values.readString(index++));
		mrrel.setCui2(values.readString(index++));
		mrrel.setAui2(values.readString(index++));
		mrrel.setStype2(values.readString(index++));
		mrrel.setRela(values.readString(index++));
		mrrel.setRui(values.readString(index++));
		mrrel.setSrui(values.readString(index++));
		mrrel.setSab(values.readString(index++));
		mrrel.setSl(values.readString(index++));
		mrrel.setRg(values.readString(index++));
		mrrel.setDir(values.readString(index++));
		mrrel.setSuppress(values.readString(index++));
		mrrel.setCvf(values.readString(index++));
		
		return mrrel;	
	}
}