package org.lexevs.dao.database.service.listener;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.locator.LexEvsServiceLocator;


public class NullEntityNamespaceListener extends AbstractPreEntityInsertValidatingListener{

	protected boolean doValidate(String uri, String version, Entity entity) {

		if(StringUtils.isBlank(entity.getEntityCodeNamespace())){
			CodingScheme cs = LexEvsServiceLocator.getInstance().
			getDatabaseServiceManager().
			getCodingSchemeService().getCodingSchemeByUriAndVersion(uri, version);

			entity.setEntityCodeNamespace(cs.getCodingSchemeName());
		}

		return true;
	}
}
