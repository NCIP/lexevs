package org.cts2.internal.mapper.converter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedNamespace;

import org.apache.commons.lang.StringUtils;
import org.cts2.entity.AnonymousEntityDescription;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.dozer.DozerConverter;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;

public class AnonymousEntityDescriptionAboutConverter extends
		DozerConverter<ResolvedConceptReference, AnonymousEntityDescription> {
	private CodingSchemeService codingSchemeService;
	private LexEvsIdentityConverter lexEvsIdentityConverter;

	public AnonymousEntityDescriptionAboutConverter() {
		super(ResolvedConceptReference.class, AnonymousEntityDescription.class);
	}

	public CodingSchemeService getCodingSchemeService() {
		return codingSchemeService;
	}

	public void setCodingSchemeService(CodingSchemeService codingSchemeService) {
		this.codingSchemeService = codingSchemeService;
	}

	public LexEvsIdentityConverter getLexEvsIdentityConverter() {
		return lexEvsIdentityConverter;
	}

	public void setLexEvsIdentityConverter(
			LexEvsIdentityConverter lexEvsIdentityConverter) {
		this.lexEvsIdentityConverter = lexEvsIdentityConverter;
	}

	@Override
	public ResolvedConceptReference convertFrom(AnonymousEntityDescription source,
			ResolvedConceptReference target) {
		return null;
	}

	@Override
	public AnonymousEntityDescription convertTo(ResolvedConceptReference source,
			AnonymousEntityDescription target) {
		
		if (StringUtils.isBlank(source.getCode())
				|| StringUtils.isBlank(source.getCodeNamespace())) {
			return target;
		}
		CodingScheme cs = codingSchemeService.getCodingSchemeByUriAndVersion(
				source.getCodingSchemeURI(), source.getCodingSchemeVersion());
		
		if (cs != null) {
			try {
				SupportedNamespace sns = DaoUtility.getURIMap(cs,
						SupportedNamespace.class, source.getCodeNamespace());
				String uri;
				if (sns != null && !StringUtils.isBlank(sns.getUri())) {
					uri = lexEvsIdentityConverter.nsUriAndCodeToUri(sns.getUri(), source.getCode());
				}
				else {
					uri = lexEvsIdentityConverter.nsUriAndCodeToUri(cs.getCodingSchemeURI(), source.getCode());
				}
				target.setAbout(uri);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return target;
	}

}
