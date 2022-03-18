
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