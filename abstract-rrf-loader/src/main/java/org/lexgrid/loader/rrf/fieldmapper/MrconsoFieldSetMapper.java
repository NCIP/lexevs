
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