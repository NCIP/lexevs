
package org.lexgrid.loader.rrf.fieldmapper;

import org.lexgrid.loader.rrf.model.Mrsat;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class MrsatFieldSetMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsatFieldSetMapper implements FieldSetMapper<Mrsat>{
	
	//MRSAT.RRF|Simple Concept, Term and String Attributes|
	//CUI,LUI,SUI,METAUI,STYPE,CODE,ATUI,SATUI,ATN,SAB,ATV,SUPPRESS,CVF|13|0|0|
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	public Mrsat mapFieldSet(FieldSet values) {
		int index = 0;
		Mrsat mrsat = null;

		mrsat = new Mrsat();
		mrsat.setCui(values.readString(index++));
		mrsat.setLui(values.readString(index++));	
		mrsat.setSui(values.readString(index++));
		mrsat.setMetaui(values.readString(index++));
		mrsat.setStype(values.readString(index++));
		mrsat.setCode(values.readString(index++));
		mrsat.setAtui(values.readString(index++));
		mrsat.setSatui(values.readString(index++));
		mrsat.setAtn(values.readString(index++));
		mrsat.setSab(values.readString(index++));
		mrsat.setAtv(values.readString(index++));
		mrsat.setSuppress(values.readString(index++));
		mrsat.setCvf(values.readString(index++));

		return mrsat;	
	}
}