
package org.lexgrid.loader.processor.support;

public abstract class AbstractAssociationQualifierResolver<I> extends AbstractNullValueSkippingResolver<I> implements OptionalQualifierResolver<I>{

	@Override
	protected String getValueToCheckForNull(I item) {
		return this.getQualifierValue(item).getContent();
	}
	
	@Override
	public boolean toProcess(I item) {
		return super.toProcess(item) &&  isProcessableValue(this.getQualifierValue(item).getContent());
	}
	
	protected boolean isProcessableValue(String value) {
		return true;
	}
}