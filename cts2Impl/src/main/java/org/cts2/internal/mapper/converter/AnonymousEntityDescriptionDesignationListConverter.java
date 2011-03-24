package org.cts2.internal.mapper.converter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedContext;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedDegreeOfFidelity;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedProperty;
import org.apache.commons.lang.StringUtils;
import org.cts2.core.ContextReference;
import org.cts2.entity.AnonymousEntityDescription;
import org.cts2.internal.mapper.BaseDozerBeanMapper;
import org.dozer.DozerConverter;
import org.hsqldb.lib.StringUtil;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;

public class AnonymousEntityDescriptionDesignationListConverter extends
		DozerConverter<ResolvedConceptReference, AnonymousEntityDescription> {

	private BaseDozerBeanMapper baseDozerBeanMapper;
	private CodingSchemeService codingSchemeService;

	public AnonymousEntityDescriptionDesignationListConverter() {
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

		for (org.LexGrid.concepts.Presentation p : source.getEntity()
				.getPresentation()) {
			org.cts2.entity.Designation designation = this.baseDozerBeanMapper
					.map(p, org.cts2.entity.Designation.class);

			// process supported mappings
			if (cs != null) {
				if (designation.getDegreeOfFidelity() != null
						&& !StringUtils.isEmpty(designation
								.getDegreeOfFidelity().getContent())) {
					SupportedDegreeOfFidelity supportedDegreeOfFidelity = DaoUtility
							.getURIMap(cs, SupportedDegreeOfFidelity.class,
									designation.getDegreeOfFidelity()
											.getContent());
					if (supportedDegreeOfFidelity != null)
						designation.getDegreeOfFidelity().setMeaning(
								supportedDegreeOfFidelity.getUri());
				}
				if (designation.getDesignationType() != null
						&& !StringUtil.isEmpty(designation.getDesignationType()
								.getContent())) {
					SupportedProperty supportedProperty = DaoUtility.getURIMap(
							cs, SupportedProperty.class, designation
									.getDesignationType().getContent());
					if (supportedProperty != null)
						designation.getDesignationType().setMeaning(
								supportedProperty.getUri());
				}

				if (designation.getFormat() != null
						&& !StringUtils.isEmpty(designation.getFormat()
								.getContent())) {
					SupportedDataType supportedDataType = DaoUtility.getURIMap(
							cs, SupportedDataType.class, designation
									.getFormat().getContent());
					if (supportedDataType != null)
						designation.getFormat().setMeaning(
								supportedDataType.getUri());
				}

				if (designation.getLanguage() != null
						&& !StringUtils.isEmpty(designation.getLanguage()
								.getContent())) {
					SupportedLanguage supportedLanguage = DaoUtility.getURIMap(
							cs, SupportedLanguage.class, designation
									.getLanguage().getContent());
					if (supportedLanguage != null)
						designation.getLanguage().setMeaning(
								supportedLanguage.getUri());
				}
				
				for (ContextReference context : designation.getUsageContext()) {
					if (!StringUtils.isEmpty(context.getContent())){
						SupportedContext supportedContext = DaoUtility.getURIMap(cs, SupportedContext.class, context.getContent());
						if (supportedContext != null) {
							context.setMeaning(supportedContext.getUri());
						}
					}
				}
			}
			target.addDesignation(designation);
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
