package org.cts2.internal.mapper.converter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.cts2.entity.NamedEntityDescription;
import org.cts2.internal.mapper.BaseDozerBeanMapper;
import org.dozer.DozerConverter;

public class NamedEntityDescriptionPropertyListConverter extends
		DozerConverter<ResolvedConceptReference, NamedEntityDescription> {

	private BaseDozerBeanMapper baseDozerBeanMapper;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NamedEntityDescription convertTo(ResolvedConceptReference source,
			NamedEntityDescription target) {
		for (org.LexGrid.commonTypes.Property p : source.getEntity()
				.getProperty()) {
			org.cts2.core.Property ctsProp = this.baseDozerBeanMapper.map(p,
					org.cts2.core.Property.class);
			target.addProperty(ctsProp);
		}
		return target;
	}

}
