package org.lexevs.dao.database.ibatis.codingscheme.parameter;

import org.LexGrid.naming.URIMap;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertURIMapBean extends IdableParameterBean{

	private String codingSchemeId;
	private String supportedAttributeTag;
	private URIMap uriMap;
	
	public void setSupportedAttributeTag(String supportedAttributeTag) {
		this.supportedAttributeTag = supportedAttributeTag;
	}

	public String getSupportedAttributeTag() {
		return supportedAttributeTag;
	}

	public void setUriMap(URIMap uriMap) {
		this.uriMap = uriMap;
	}

	public URIMap getUriMap() {
		return uriMap;
	}

	public void setCodingSchemeId(String codingSchemeId) {
		this.codingSchemeId = codingSchemeId;
	}

	public String getCodingSchemeId() {
		return codingSchemeId;
	}
}
