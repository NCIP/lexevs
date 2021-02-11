
package org.lexgrid.loader.rrf.data.property;

import java.util.Map;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexgrid.loader.rrf.factory.MrsatUsageFactory.MrsatPropertyTypes;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * The Class DefaultMrsatUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultMrsatUtility implements MrsatUtility {
	
	/** The mrsat map. */
	private Map<String,MrsatPropertyTypes> mrsatMap;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.property.MrsatUtility#getPropertyName(org.lexgrid.loader.rrf.model.Mrsat)
	 */
	public String getPropertyName(Mrsat mrsat){
		String type =  mrsat.getAtn();
		if(type.equals(MrsatPropertyTypes.PRESENTATION)){
			return SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION;
		} 
		if(type.equals(MrsatPropertyTypes.COMMENT)){
			return SQLTableConstants.TBLCOLVAL_COMMENT;
		}
		return type;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.property.MrsatUtility#getPropertyType(org.lexgrid.loader.rrf.model.Mrsat)
	 */
	public String getPropertyType(Mrsat mrsat){
		String type =  mrsat.getAtn();
		if(type.equals(MrsatPropertyTypes.PRESENTATION)){
			return SQLTableConstants.TBLCOLVAL_PRESENTATION;
		} 
		if(type.equals(MrsatPropertyTypes.COMMENT)){
			return SQLTableConstants.TBLCOLVAL_COMMENT;
		}
		return SQLTableConstants.TBLCOLVAL_PROPERTY;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.property.MrsatUtility#toSkip(org.lexgrid.loader.rrf.model.Mrsat)
	 */
	public boolean toSkip(Mrsat mrsat) {
		String prop = mrsat.getAtn();
		if(mrsatMap.containsKey(prop)){
			if(mrsatMap.get(prop).equals(MrsatPropertyTypes.SKIP)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the mrsat map.
	 * 
	 * @return the mrsat map
	 */
	public Map<String, MrsatPropertyTypes> getMrsatMap() {
		return mrsatMap;
	}

	/**
	 * Sets the mrsat map.
	 * 
	 * @param mrsatMap the mrsat map
	 */
	public void setMrsatMap(Map<String, MrsatPropertyTypes> mrsatMap) {
		this.mrsatMap = mrsatMap;
	}
}