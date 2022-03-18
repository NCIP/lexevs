
package org.lexgrid.loader.rrf.fieldmapper;

import org.lexgrid.loader.rrf.model.Mrhier;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class MrhierFieldSetMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrhierFieldSetMapper implements FieldSetMapper<Mrhier>{
	
	//MRHIER.RRF|Computable hierarchies|CUI,AUI,CXN,PAUI,SAB,RELA,PTR,HCD,CVF|9|1512|97390|
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	public Mrhier mapFieldSet(FieldSet values) {
		if(values.getFieldCount() < 1){
			return null;
		}
		int index = 0;
		Mrhier mrhier = new Mrhier();
		mrhier.setCui(values.readString(index++));
		mrhier.setAui(values.readString(index++));
		mrhier.setCxn(values.readString(index++));
		mrhier.setPaui(values.readString(index++));
		mrhier.setSab(values.readString(index++));
		mrhier.setRela(values.readString(index++));
		mrhier.setPtr(values.readString(index++));
		mrhier.setHcd(values.readString(index++));
		mrhier.setCvf(values.readString(index++));
		
		return mrhier;	
	}
}