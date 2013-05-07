package org.cts2.internal.mapper.converter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedLanguage;
import org.apache.commons.lang.StringUtils;
import org.cts2.entity.NamedEntityDescription;
import org.cts2.internal.mapper.BaseDozerBeanMapper;
import org.dozer.DozerConverter;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;

public class NamedEntityDescriptionNoteListConverter extends
		DozerConverter<ResolvedConceptReference, NamedEntityDescription> {

	private BaseDozerBeanMapper baseDozerBeanMapper;
	private CodingSchemeService codingSchemeService;

	public NamedEntityDescriptionNoteListConverter() {
		super(ResolvedConceptReference.class, NamedEntityDescription.class);
	}

	public BaseDozerBeanMapper getBaseDozerBeanMapper() {
		return baseDozerBeanMapper;
	}

	public void setBaseDozerBeanMapper(BaseDozerBeanMapper baseDozerBeanMapper) {
		this.baseDozerBeanMapper = baseDozerBeanMapper;
	}

	@Override
	public ResolvedConceptReference convertFrom(NamedEntityDescription source,
			ResolvedConceptReference target) {
		return null;
	}

	@Override
	public NamedEntityDescription convertTo(ResolvedConceptReference source,
			NamedEntityDescription target) {
		if (StringUtils.isBlank(source.getCode())
				|| StringUtils.isBlank(source.getCodeNamespace())) {
			return target;
		}
		CodingScheme cs = codingSchemeService.getCodingSchemeByUriAndVersion(
				source.getCodingSchemeURI(), source.getCodingSchemeVersion());

		for (org.LexGrid.concepts.Comment c : source.getEntity()
				.getComment()) {
			org.cts2.core.Comment comment = this.baseDozerBeanMapper.map(
					c, org.cts2.core.Comment.class);
			
			// process supported mappings
			if (cs != null) {
				if (comment.getFormat() != null
						&& !StringUtils.isEmpty(comment.getFormat()
								.getContent())) {
					SupportedDataType supportedDataType = DaoUtility.getURIMap(
							cs, SupportedDataType.class, comment.getFormat()
									.getContent());
					if (supportedDataType != null)
						comment.getFormat().setMeaning(
								supportedDataType.getUri());
				}

				if (comment.getLanguage() != null
						&& !StringUtils.isEmpty(comment.getLanguage()
								.getContent())) {
					SupportedLanguage supportedLanguage = DaoUtility.getURIMap(
							cs, SupportedLanguage.class, comment
									.getLanguage().getContent());
					if (supportedLanguage != null)
						comment.getLanguage().setMeaning(
								supportedLanguage.getUri());
				}

			}
			target.addNote(comment);
		}
		return target;
	}

	public void setCodingSchemeService(CodingSchemeService codingSchemeService) {
		this.codingSchemeService = codingSchemeService;
	}

	public CodingSchemeService getCodingSchemeService() {
		return codingSchemeService;
	}

}
