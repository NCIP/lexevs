
package org.lexgrid.loader.staging.fieldmapper;

import org.lexgrid.loader.staging.support.KeyValuePair;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * The Class KeyValueFieldMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class KeyValueFieldMapper implements FieldSetMapper<KeyValuePair>{

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	public KeyValuePair mapFieldSet(FieldSet values) {
		KeyValuePair kevyValuePair = new KeyValuePair();
		kevyValuePair.setKey(values.readString(0));
		kevyValuePair.setValue(values.readString(1));	
		return kevyValuePair;		
	}	
}