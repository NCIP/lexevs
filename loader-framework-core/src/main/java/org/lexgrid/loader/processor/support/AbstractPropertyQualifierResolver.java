
package org.lexgrid.loader.processor.support;


public abstract class AbstractPropertyQualifierResolver<I> extends AbstractNullValueSkippingResolver<I> implements OptionalPropertyQualifierResolver<I>{

	@Override
	protected String getValueToCheckForNull(I item) {
		return this.getQualifierValue(item).getContent();
	}

	@Override
	public String getPropertyQualifierType(I item) {
		return null;
	}

	@Override
	public boolean toProcess(I item) {
		return super.toProcess(item) &&  isProcessableValue(this.getQualifierValue(item).getContent());
	}
	
	protected boolean isProcessableValue(String value) {
		return true;
	}
}