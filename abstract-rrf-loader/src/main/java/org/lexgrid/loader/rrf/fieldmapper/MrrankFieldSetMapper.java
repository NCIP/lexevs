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

import org.lexgrid.loader.rrf.model.Mrrank;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class MrrankFieldSetMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrankFieldSetMapper implements FieldSetMapper<Mrrank>{
	
	//MRRANK.RRF|Concept Name Ranking|RANK,SAB,TTY,SUPPRESS|4|347|6216|
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	public Mrrank mapFieldSet(FieldSet values) {
		if(values.getFieldCount() < 1){
			return null;
		}
		int index = 0;
		Mrrank mrrank = new Mrrank();
		mrrank.setRank(values.readString(index++));
		mrrank.setSab(values.readString(index++));
		mrrank.setTty(values.readString(index++));
		mrrank.setSuppress(values.readString(index++));
		
		return mrrank;	
	}
}
