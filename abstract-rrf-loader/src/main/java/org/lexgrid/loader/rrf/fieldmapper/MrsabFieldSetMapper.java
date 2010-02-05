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

import org.lexgrid.loader.rrf.model.Mrhier;
import org.lexgrid.loader.rrf.model.Mrsab;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class MrsabFieldSetMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsabFieldSetMapper implements FieldSetMapper<Mrsab>{
	
	//MRSAB.RRF|Source Metadata|VCUI,RCUI,VSAB,RSAB,SON,SF,SVER,VSTART,VEND,IMETA,RMETA,SLC,SCC
	//SRL,TFR,CFR,CXTY,TTYL,ATNL,LAT,CENC,CURVER,SABIN,SSN,SCIT|25|78|45730|
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	public Mrsab mapFieldSet(FieldSet values) {
		if(values.getFieldCount() < 1){
			return null;
		}
		
		int index = 0;
		Mrsab mrsab = new Mrsab();
		mrsab.setVcui(values.readString(index++));
		mrsab.setRcui(values.readString(index++));
		mrsab.setVsab(values.readString(index++));
		mrsab.setRsab(values.readString(index++));
		mrsab.setSon(values.readString(index++));
		mrsab.setSf(values.readString(index++));
		mrsab.setSver(values.readString(index++));
		mrsab.setVstart(values.readString(index++));
		mrsab.setVend(values.readString(index++));
		mrsab.setImeta(values.readString(index++));
		mrsab.setRmeta(values.readString(index++));
		mrsab.setSlc(values.readString(index++));
		mrsab.setScc(values.readString(index++));
		mrsab.setSrl(values.readString(index++));
		mrsab.setTfr(values.readString(index++));
		mrsab.setCfr(values.readString(index++));
		mrsab.setCxty(values.readString(index++));
		mrsab.setTtyl(values.readString(index++));
		mrsab.setAtnl(values.readString(index++));
		mrsab.setLat(values.readString(index++));
		mrsab.setCenc(values.readString(index++));
		mrsab.setCurver(values.readString(index++));
		mrsab.setSabin(values.readString(index++));
		mrsab.setSsn(values.readString(index++));
		mrsab.setScit(values.readString(index++));
		
		return mrsab;	
	}
}
