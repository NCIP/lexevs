package org.lexgrid.loader.processor.support;

public abstract class AbstractPropertyQualifierResolver<I> extends AbstractNullValueSkippingResolver<I> implements QualifierResolver<I>{

	@Override
	protected String getValueToCheckForNull(I item) {
		return this.getQualifierValue(item);
	}
}
