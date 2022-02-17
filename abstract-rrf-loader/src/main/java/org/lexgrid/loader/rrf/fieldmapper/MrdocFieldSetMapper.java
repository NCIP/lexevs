
package org.lexgrid.loader.rrf.fieldmapper;

import org.lexgrid.loader.rrf.model.Mrdoc;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class MrdocFieldSetMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdocFieldSetMapper implements FieldSetMapper<Mrdoc>{
	
	/**
	 * MRDOC.RRF|Typed key value metadata map|DOCKEY,VALUE,TYPE,EXPL|4|1702|107080|
	 * 
	 * @param values the values
	 * 
	 * @return the mrdoc
	 */
	public Mrdoc mapFieldSet(FieldSet values) {
		int index = 0;
		Mrdoc mrdoc = null;
		try {
			mrdoc = new Mrdoc();
			mrdoc.setDockey(values.readString(index++));
			mrdoc.setValue(values.readString(index++));	
			mrdoc.setType(values.readString(index++));
			mrdoc.setExpl(values.readString(index++));	
		} catch (RuntimeException e) {
			System.out.println(values.getValues().toString());
		}		
		return mrdoc;		
	}
}