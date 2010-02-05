package org.lexevs.dao.database.ibatis.codingscheme.parameter;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertCodingSchemeBean extends IdableParameterBean{
	
	private CodingScheme codingScheme;

	public void setCodingScheme(CodingScheme codingScheme) {
		this.codingScheme = codingScheme;
	}

	public CodingScheme getCodingScheme() {
		return codingScheme;
	}
}
