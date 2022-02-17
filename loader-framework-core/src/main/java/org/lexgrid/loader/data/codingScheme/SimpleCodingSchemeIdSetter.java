
package org.lexgrid.loader.data.codingScheme;

/**
 * The Interface SimpleCodingSchemeIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SimpleCodingSchemeIdSetter implements CodingSchemeIdSetter {

	private String uri; 
	private String version;
	
	public SimpleCodingSchemeIdSetter(String uri, String version){
		this.uri = uri;
		this.version = version;
	}

	@Override
	public String getCodingSchemeName() {
		throw new UnsupportedOperationException("SimpleCodingSchemeIdSetter cannot be use to get CodingSchemeName.");
	}

	@Override
	public String getCodingSchemeUri() {
		return this.uri;
	}

	@Override
	public String getCodingSchemeVersion() {
		return this.version;
	}
	
	
}