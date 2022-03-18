
package org.lexgrid.loader.umls.data.codingscheme;

import java.util.Map;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.rrf.data.codingscheme.MrsabUtility;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class UmlsCodingSchemeIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MedRTUmlsCodingSchemeIdSetter implements CodingSchemeIdSetter, InitializingBean {

	/** The mrsab utility. */
	private MrsabUtility mrsabUtility;
	
	/** The iso map. */
	private Map<String,String> isoMap;
	
	/** The sab. */
	private String sab;
	
	/** The coding scheme name. */
	private String codingSchemeName;
	
	private String codingSchemeUri;
	
	private String codingSchemeVersion;
	

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		//get the sab
		sab = getSab();
		codingSchemeName = mrsabUtility.getCodingSchemeNameFromSab(sab);
		codingSchemeUri = isoMap.get(sab);
		codingSchemeVersion = mrsabUtility.getCodingSchemeVersionFromSab(sab);
	}

	public String getCodingSchemeName() {
		return codingSchemeName;
	}

	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}

	public String getCodingSchemeVersion() {
		return codingSchemeVersion;
	}

	/**
	 * Gets the mrsab utility.
	 * 
	 * @return the mrsab utility
	 */
	public MrsabUtility getMrsabUtility() {
		return mrsabUtility;
	}

	/**
	 * Sets the mrsab utility.
	 * 
	 * @param mrsabUtility the new mrsab utility
	 */
	public void setMrsabUtility(MrsabUtility mrsabUtility) {
		this.mrsabUtility = mrsabUtility;
	}

	/**
	 * Gets the sab.
	 * 
	 * @return the sab
	 */
	public String getSab() {
		return sab;
	}

	/**
	 * Sets the sab.
	 * 
	 * @param sab the new sab
	 */
	public void setSab(String sab) {
		this.sab = sab;
	}

	public Map<String, String> getIsoMap() {
		return isoMap;
	}

	public void setIsoMap(Map<String, String> isoMap) {
		this.isoMap = isoMap;
	}
	
}