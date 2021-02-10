
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