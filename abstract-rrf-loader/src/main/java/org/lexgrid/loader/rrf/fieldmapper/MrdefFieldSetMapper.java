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

import org.lexgrid.loader.rrf.model.Mrdef;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class MrdefFieldSetMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdefFieldSetMapper implements FieldSetMapper<Mrdef>{
	
	//MRDEF.RRF|Definitions|CUI,AUI,ATUI,SATUI,SAB,DEF,SUPPRESS,CVF|8|160|89526|
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	public Mrdef mapFieldSet(FieldSet values) {
		int index = 0;
		Mrdef mrdef = null;
		mrdef = new Mrdef();
		mrdef.setCui(values.readString(index++));
		mrdef.setAui(values.readString(index++));
		mrdef.setAtui(values.readString(index++));
		mrdef.setSatui(values.readString(index++));
		mrdef.setSab(values.readString(index++));
		mrdef.setDef(values.readString(index++));
		mrdef.setSuppress(values.readString(index++));
		mrdef.setCvf(values.readString(index++));
			
		return mrdef;	
	}
}