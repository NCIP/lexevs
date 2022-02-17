
package org.lexgrid.loader.rrf.data.codingscheme;

import java.util.List;

import org.lexgrid.loader.rrf.model.Mrsab;

/**
 * The Class DefaultMrsabUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultMrsabUtility implements MrsabUtility {

	/** The mrsab list. */
	private List<Mrsab> mrsabList;
	
	public String getCodingSchemeVersionFromSab(String sab){
		return getMrsabRowFromRsab(sab).getSver();
	}

	//SELECT SSN FROM MRSAB WHERE RSAB = ?");
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.codingscheme.MrsabUtility#getCodingSchemeNameFromSab(java.lang.String)
	 */
	public String getCodingSchemeNameFromSab(String sab){
		return getMrsabRowFromRsab(sab).getSsn();
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.codingscheme.MrsabUtility#getMrsabRowFromRsab(java.lang.String)
	 */
	public Mrsab getMrsabRowFromRsab(String rsab){
		for(Mrsab mrsab : mrsabList){
			if(mrsab.getRsab().equals(rsab)){
				return mrsab;
			}
		}
		throw new RuntimeException("Couldn't map RSAB: '" + rsab + "' to a CodingScheme.");
	}

	/**
	 * Gets the mrsab list.
	 * 
	 * @return the mrsab list
	 */
	public List<Mrsab> getMrsabList() {
		return mrsabList;
	}

	/**
	 * Sets the mrsab list.
	 * 
	 * @param mrsabList the new mrsab list
	 */
	public void setMrsabList(List<Mrsab> mrsabList) {
		this.mrsabList = mrsabList;
	}
}