
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