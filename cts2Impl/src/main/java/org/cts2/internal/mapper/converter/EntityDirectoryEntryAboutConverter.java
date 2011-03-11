package org.cts2.internal.mapper.converter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedNamespace;
import org.apache.commons.lang.StringUtils;
import org.cts2.entity.EntityDirectoryEntry;
import org.cts2.internal.lexevs.identity.DefaultLexEvsIdentityConverter;
import org.dozer.DozerConverter;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;

public class EntityDirectoryEntryAboutConverter extends
		DozerConverter<ResolvedConceptReference, EntityDirectoryEntry> {
	
	private CodingSchemeService codingSchemeService;
	
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

		CodingScheme cs = codingSchemeService.getCodingSchemeByUriAndVersion(
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

	public void setCodingSchemeService(CodingSchemeService codingSchemeService) {
		this.codingSchemeService = codingSchemeService;
	}

	public CodingSchemeService getCodingSchemeService() {
		return codingSchemeService;
	}

}
