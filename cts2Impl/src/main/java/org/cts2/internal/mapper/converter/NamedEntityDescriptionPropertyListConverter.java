package org.cts2.internal.mapper.converter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedLanguage;
import org.apache.commons.lang.StringUtils;
import org.cts2.core.EntryDescription;
import org.cts2.entity.NamedEntityDescription;
import org.cts2.internal.mapper.BaseDozerBeanMapper;
import org.dozer.DozerConverter;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;

public class NamedEntityDescriptionPropertyListConverter extends
		DozerConverter<ResolvedConceptReference, NamedEntityDescription> {

	private BaseDozerBeanMapper baseDozerBeanMapper;
	private CodingSchemeService codingSchemeService;

	public NamedEntityDescriptionPropertyListConverter() {
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

		for (org.LexGrid.commonTypes.Property p : source.getEntity()
				.getProperty()) {
			org.cts2.core.Property ctsProp = this.baseDozerBeanMapper.map(p,
					org.cts2.core.Property.class);

			EntryDescription enDesc = ctsProp.getValue();
			// process supported mappings
			if (cs != null && enDesc != null) {
				if (enDesc.getFormat() != null
						&& StringUtils.isEmpty(enDesc.getFormat().getContent()) == false) {
					SupportedDataType supportedDataType = DaoUtility.getURIMap(
							cs, SupportedDataType.class, enDesc.getFormat()
									.getContent());
					if (supportedDataType != null)
						enDesc.getFormat().setMeaning(
								supportedDataType.getUri());
				}
				if (enDesc.getLanguage() != null
						&& StringUtils.isEmpty(enDesc.getLanguage()
								.getContent()) == false) {
					SupportedLanguage supportedLanguage = DaoUtility.getURIMap(
							cs, SupportedLanguage.class, enDesc.getLanguage()
									.getContent());
					if (supportedLanguage != null)
						enDesc.getLanguage().setMeaning(
								supportedLanguage.getUri());
				}
			}
			target.addProperty(ctsProp);
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
