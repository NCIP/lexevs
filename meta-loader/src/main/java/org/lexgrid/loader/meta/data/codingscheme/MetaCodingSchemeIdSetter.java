
package org.lexgrid.loader.meta.data.codingscheme;

import java.util.Map;
import java.util.Properties;

import org.lexgrid.loader.constants.LoaderConstants;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.rrf.model.Mrdoc;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class MetaCodingSchemeIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaCodingSchemeIdSetter implements CodingSchemeIdSetter, InitializingBean  {
	
	/** The coding scheme properties. */
	private Properties codingSchemeProperties;
	
	private String codingSchemeVersion;
	
	private String codingSchemeUri;
	
	private Map<String,String> isoMap;
	
	private ItemReader<Mrdoc> mrdocReader;

	private String DOCKEY = "RELEASE";
	
	private String VALUE = "umls.release.name";
	
	private String TYPE = "release_info";

	@Override
	public void afterPropertiesSet() throws Exception {
		codingSchemeVersion = getVersion();
		codingSchemeUri = isoMap.get(codingSchemeProperties.getProperty(LoaderConstants.CODING_SCHEME_NAME_PROPERTY));
	}
	
	protected String getVersion() {
		Mrdoc mrdoc;
		do {
			try {
				mrdoc = mrdocReader.read();
				if(mrdoc.getDockey().equals(DOCKEY)
					&&
					mrdoc.getValue().equals(VALUE)
					&&
					mrdoc.getType().endsWith(TYPE)){
					
					return mrdoc.getExpl();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
			
		} while(mrdoc != null);
		
		throw new RuntimeException("Cound not find Meta Version.");
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter#getCodingSchemeName()
	 */
	public String getCodingSchemeName() {
		return codingSchemeProperties.getProperty(LoaderConstants.CODING_SCHEME_NAME_PROPERTY);
	}

	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}

	public String getCodingSchemeVersion() {
		return this.codingSchemeVersion;
	}
	
	/**
	 * Gets the coding scheme properties.
	 * 
	 * @return the coding scheme properties
	 */
	public Properties getCodingSchemeProperties() {
		return codingSchemeProperties;
	}

	/**
	 * Sets the coding scheme properties.
	 * 
	 * @param codingSchemeProperties the new coding scheme properties
	 */
	public void setCodingSchemeProperties(Properties codingSchemeProperties) {
		this.codingSchemeProperties = codingSchemeProperties;
	}
	
	public Map<String, String> getIsoMap() {
		return isoMap;
	}

	public void setIsoMap(Map<String, String> isoMap) {
		this.isoMap = isoMap;
	}

	public void setMrdocReader(ItemReader<Mrdoc> mrdocReader) {
		this.mrdocReader = mrdocReader;
	}

	public ItemReader<Mrdoc> getMrdocReader() {
		return mrdocReader;
	}
}