package org.lexevs.dao.database.service.listener;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.apache.commons.lang.StringUtils;
import org.lexevs.locator.LexEvsServiceLocator;

public class NullAssociationNamespaceListener extends AbstractPreAssociationInsertValidatingListener{

	@Override
	protected boolean doValidateNullNamespace(String uri, String version,
			Relations relation, AssociationSource source) {

		if( relation == null || source == null ) {
			return true;
		}
		
		String sourceCodingSchemeName = relation.getSourceCodingScheme();

		CodingScheme cs = LexEvsServiceLocator.getInstance()
				.getDatabaseServiceManager().getCodingSchemeService()
				.getCodingSchemeByUriAndVersion(uri, version);
		String codingSchemeName = cs.getCodingSchemeName();
		
		if (StringUtils.isBlank(source.getSourceEntityCodeNamespace())) {

			if (StringUtils.isNotBlank(sourceCodingSchemeName)) {
				source.setSourceEntityCodeNamespace(sourceCodingSchemeName);
			} else {
				source.setSourceEntityCodeNamespace(codingSchemeName);
			}
		}

		for (AssociationTarget target : source.getTarget()) {

			if (target != null
					&& StringUtils.isBlank(target
							.getTargetEntityCodeNamespace())) {
				String targetCodingSchemeName = relation
						.getTargetCodingScheme();

				if (StringUtils.isNotBlank(targetCodingSchemeName)) {
					target.setTargetEntityCodeNamespace(targetCodingSchemeName);
				} else {
					target.setTargetEntityCodeNamespace(codingSchemeName);
				}
			}
		}

		return true;
	}
}
