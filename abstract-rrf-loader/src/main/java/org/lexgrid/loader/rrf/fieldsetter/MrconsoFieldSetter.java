
package org.lexgrid.loader.rrf.fieldsetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.lexgrid.loader.rrf.model.Mrconso;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

/**
 * The Class MrconsoFieldSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrconsoFieldSetter implements ItemPreparedStatementSetter<Mrconso>{
	//MRCONSO.RRF|Concept names and sources|
	//CUI,LAT,TS,LUI,STT,SUI,ISPREF,AUI,SAUI,
	//SCUI,SDUI,SAB,TTY,CODE,STR,SRL,SUPPRESS,CVF
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.database.ItemPreparedStatementSetter#setValues(java.lang.Object, java.sql.PreparedStatement)
	 */
	public void setValues(Mrconso item, PreparedStatement ps)
			throws SQLException {
		ps.setString(1, item.getCui());
		ps.setString(2, item.getLat());
		ps.setString(3, item.getTs());
		ps.setString(4, item.getLui());
		ps.setString(5, item.getStt());
		ps.setString(6, item.getSui());
		ps.setString(7, item.getIspref());
		ps.setString(8, item.getAui());
		ps.setString(9, item.getSaui());
		ps.setString(10, item.getScui());
		ps.setString(11, item.getSdui());
		ps.setString(12, item.getSab());
		ps.setString(13, item.getTty());
		ps.setString(14, item.getCode());
		ps.setString(15, item.getStr());
		ps.setString(16, item.getSrl());
		ps.setString(17, item.getSuppress());
		ps.setString(18, item.getCvf());
	}
	
			
			
			
}