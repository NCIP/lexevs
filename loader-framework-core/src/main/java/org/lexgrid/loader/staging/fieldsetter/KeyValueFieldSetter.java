
package org.lexgrid.loader.staging.fieldsetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.lexgrid.loader.staging.support.KeyValuePair;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

/**
 * The Class KeyValueFieldSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class KeyValueFieldSetter implements ItemPreparedStatementSetter<KeyValuePair>{
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.database.ItemPreparedStatementSetter#setValues(java.lang.Object, java.sql.PreparedStatement)
	 */
	public void setValues(KeyValuePair keyValuePair, PreparedStatement ps)
			throws SQLException {
		ps.setString(1, keyValuePair.getKey());
		ps.setString(2, keyValuePair.getValue());
	}
}