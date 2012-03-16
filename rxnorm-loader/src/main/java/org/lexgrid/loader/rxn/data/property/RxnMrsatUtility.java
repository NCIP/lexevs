package org.lexgrid.loader.rxn.data.property;

import java.util.Map;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexgrid.loader.rrf.data.property.MrsatUtility;
import org.lexgrid.loader.rxn.factory.RxnMrsatUsageFactory.RxnMrsatPropertyTypes;
import org.lexgrid.loader.rrf.model.Mrsat;

public class RxnMrsatUtility implements MrsatUtility {

	/** The mrsat map. */
	private Map<String,RxnMrsatPropertyTypes> mrsatMap;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.property.MrsatUtility#getPropertyName(org.lexgrid.loader.rrf.model.Mrsat)
	 */
	public String getPropertyName(Mrsat mrsat){
		String type =  mrsat.getAtn();
		if(type.equals(RxnMrsatPropertyTypes.PRESENTATION)){
			return SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION;
		} 
		if(type.equals(RxnMrsatPropertyTypes.COMMENT)){
			return SQLTableConstants.TBLCOLVAL_COMMENT;
		}
		return type;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.property.MrsatUtility#getPropertyType(org.lexgrid.loader.rrf.model.Mrsat)
	 */
	public String getPropertyType(Mrsat mrsat){
		String type =  mrsat.getAtn();
		if(type.equals(RxnMrsatPropertyTypes.PRESENTATION)){
			return SQLTableConstants.TBLCOLVAL_PRESENTATION;
		} 
		if(type.equals(RxnMrsatPropertyTypes.COMMENT)){
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
			if(mrsatMap.get(prop).equals(RxnMrsatPropertyTypes.SKIP)){
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
	public Map<String, RxnMrsatPropertyTypes> getMrsatMap() {
		return mrsatMap;
	}

	/**
	 * Sets the mrsat map.
	 * 
	 * @param mrsatMap the mrsat map
	 */
	public void setMrsatMap(Map<String, RxnMrsatPropertyTypes> mrsatMap) {
		this.mrsatMap = mrsatMap;
	}

}
