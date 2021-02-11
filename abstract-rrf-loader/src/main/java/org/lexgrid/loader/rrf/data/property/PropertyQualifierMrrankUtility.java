
package org.lexgrid.loader.rrf.data.property;

import java.util.List;

import org.lexgrid.loader.rrf.model.Mrrank;

public class PropertyQualifierMrrankUtility  extends DefaultMrrankUtility{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.property.MrrankUtility#getRank(java.lang.String, java.lang.String)
	 */
	public Mrrank getMrrank(String sab, String tty) {
		List<Mrrank> ranklist = getMrrankList();
		for(Mrrank mrrank : ranklist){
			if(mrrank.getSab().equals(sab) && mrrank.getTty().equals(tty)){
				return mrrank;
			}
		}
		return null;
	}
}