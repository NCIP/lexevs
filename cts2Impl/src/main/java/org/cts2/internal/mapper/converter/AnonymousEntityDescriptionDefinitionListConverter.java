package org.cts2.internal.mapper.converter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedContext;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedLanguage;
import org.apache.commons.lang.StringUtils;
import org.cts2.core.ContextReference;
import org.cts2.entity.AnonymousEntityDescription;
import org.cts2.internal.mapper.BaseDozerBeanMapper;
import org.dozer.DozerConverter;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;

public class AnonymousEntityDescriptionDefinitionListConverter extends
		DozerConverter<ResolvedConceptReference, AnonymousEntityDescription> {

	private BaseDozerBeanMapper baseDozerBeanMapper;
	private CodingSchemeService codingSchemeService;

	public AnonymousEntityDescriptionDefinitionListConverter() {
		super(ResolvedConceptReference.class, AnonymousEntityDescription.class);
	}

	public BaseDozerBeanMapper getBaseDozerBeanMapper() {
		return baseDozerBeanMapper;
	}

	public void setBaseDozerBeanMapper(BaseDozerBeanMapper baseDozerBeanMapper) {
		this.baseDozerBeanMapper = baseDozerBeanMapper;
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

		for (org.LexGrid.concepts.Definition d : source.getEntity()
				.getDefinition()) {
			org.cts2.core.Definition definition = this.baseDozerBeanMapper.map(
					d, org.cts2.core.Definition.class);

			// process supported mappings
			if (cs != null) {
				if (definition.getFormat() != null
						&& !StringUtils.isEmpty(definition.getFormat()
								.getContent())) {
					SupportedDataType supportedDataType = DaoUtility.getURIMap(
							cs, SupportedDataType.class, definition.getFormat()
									.getContent());
					if (supportedDataType != null)
						definition.getFormat().setMeaning(
								supportedDataType.getUri());
				}

				if (definition.getLanguage() != null
						&& !StringUtils.isEmpty(definition.getLanguage()
								.getContent())) {
					SupportedLanguage supportedLanguage = DaoUtility.getURIMap(
							cs, SupportedLanguage.class, definition
									.getLanguage().getContent());
					if (supportedLanguage != null)
						definition.getLanguage().setMeaning(
								supportedLanguage.getUri());
				}

				ContextReference context = definition.getUsageContext();
				if (context != null
						&& !StringUtils.isEmpty(context.getContent())) {
					SupportedContext supportedContext = DaoUtility.getURIMap(
							cs, SupportedContext.class, context.getContent());
					if (supportedContext != null) {
						context.setMeaning(supportedContext.getUri());
					}
				}
			}
			target.addDefinition(definition);
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
