package org.LexGrid.LexBIG.Extensions.Generic;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;

public class CodeSystemReference {
	
	private String codingScheme;
	
	private CodingSchemeVersionOrTag versionOrTag;

	public String getCodingScheme() {
		return codingScheme;
	}

	public void setCodingScheme(String codingScheme) {
		this.codingScheme = codingScheme;
	}

	public CodingSchemeVersionOrTag getVersionOrTag() {
		return versionOrTag;
	}

	public void setVersionOrTag(CodingSchemeVersionOrTag versionOrTag) {
		this.versionOrTag = versionOrTag;
	}

}