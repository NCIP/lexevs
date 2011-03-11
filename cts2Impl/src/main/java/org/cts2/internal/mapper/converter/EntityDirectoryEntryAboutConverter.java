package org.cts2.internal.mapper.converter;

import java.lang.reflect.InvocationTargetException;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedNamespace;
import org.apache.commons.lang.StringUtils;
import org.cts2.entity.EntityDirectoryEntry;
import org.cts2.internal.lexevs.identity.DefaultLexEvsIdentityConverter;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.dozer.DozerConverter;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;

public class EntityDirectoryEntryAboutConverter extends
		DozerConverter<ResolvedConceptReference, EntityDirectoryEntry> {

	public EntityDirectoryEntryAboutConverter() {
		super(ResolvedConceptReference.class, EntityDirectoryEntry.class);
	}

	@Override
	public ResolvedConceptReference convertFrom(EntityDirectoryEntry arg0,
			ResolvedConceptReference arg1) {
		return null;
	}

	@Override
	public EntityDirectoryEntry convertTo(ResolvedConceptReference conRef,
			EntityDirectoryEntry dirEntry) {

		if (StringUtils.isBlank(conRef.getCode())
				|| StringUtils.isBlank(conRef.getCodeNamespace())) {
			return dirEntry;
		}
		CodingSchemeService service = LexEvsServiceLocator.getInstance()
				.getDatabaseServiceManager().getCodingSchemeService();

		CodingScheme cs = service.getCodingSchemeByUriAndVersion(
				conRef.getCodingSchemeURI(), conRef.getCodingSchemeVersion());

		if (cs != null) {
			try {
				SupportedNamespace sns = DaoUtility.getURIMap(cs,
						SupportedNamespace.class, conRef.getCodeNamespace());
				if (sns != null && !StringUtils.isBlank(sns.getUri())) {
					dirEntry.setAbout(sns.getUri()
							+ DefaultLexEvsIdentityConverter.DEFAULT_URI_CONCAT_STRING
							+ conRef.getCode());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return dirEntry;
	}

}
