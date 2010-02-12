package org.lexgrid.loader.processor.support;

public abstract class AbstractAssociationQualifierResolver<I> extends AbstractNullValueSkippingResolver<I> implements QualifierResolver<I>{

	@Override
	protected String getValueToCheckForNull(I item) {
		return this.getQualifierValue(item);
	}
}
