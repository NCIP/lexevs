
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