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

import org.lexgrid.loader.rrf.model.Mrsat;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class MrsatFieldSetMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsatFieldSetMapper implements FieldSetMapper<Mrsat>{
	
	//MRSAT.RRF|Simple Concept, Term and String Attributes|
	//CUI,LUI,SUI,METAUI,STYPE,CODE,ATUI,SATUI,ATN,SAB,ATV,SUPPRESS,CVF|13|0|0|
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	public Mrsat mapFieldSet(FieldSet values) {
		int index = 0;
		Mrsat mrsat = null;

		mrsat = new Mrsat();
		mrsat.setCui(values.readString(index++));
		mrsat.setLui(values.readString(index++));	
		mrsat.setSui(values.readString(index++));
		mrsat.setMetaui(values.readString(index++));
		mrsat.setStype(values.readString(index++));
		mrsat.setCode(values.readString(index++));
		mrsat.setAtui(values.readString(index++));
		mrsat.setSatui(values.readString(index++));
		mrsat.setAtn(values.readString(index++));
		mrsat.setSab(values.readString(index++));
		mrsat.setAtv(values.readString(index++));
		mrsat.setSuppress(values.readString(index++));
		mrsat.setCvf(values.readString(index++));

		return mrsat;	
	}
}