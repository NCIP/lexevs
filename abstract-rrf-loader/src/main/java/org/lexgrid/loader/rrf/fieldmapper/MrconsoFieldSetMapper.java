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

import org.lexgrid.loader.rrf.model.Mrconso;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class MrconsoFieldSetMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrconsoFieldSetMapper implements FieldSetMapper<Mrconso>{

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	public Mrconso mapFieldSet(FieldSet values) {
		int index = 0;
		Mrconso mrconso = null;

		mrconso = new Mrconso();
		mrconso.setCui(values.readString(index++));
		mrconso.setLat(values.readString(index++));	
		mrconso.setTs(values.readString(index++));
		mrconso.setLui(values.readString(index++));
		mrconso.setStt(values.readString(index++));
		mrconso.setSui(values.readString(index++));
		mrconso.setIspref(values.readString(index++));
		mrconso.setAui(values.readString(index++));
		mrconso.setSaui(values.readString(index++));
		mrconso.setScui(values.readString(index++));
		mrconso.setSdui(values.readString(index++));
		mrconso.setSab(values.readString(index++));
		mrconso.setTty(values.readString(index++));
		mrconso.setCode(values.readString(index++));
		mrconso.setStr(values.readString(index++));	
		mrconso.setSrl(values.readString(index++));	
		mrconso.setSuppress(values.readString(index++));
		mrconso.setCvf(values.readString(index++));

		return mrconso;	
	}
}