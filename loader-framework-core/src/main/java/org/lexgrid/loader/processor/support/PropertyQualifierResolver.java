
package org.lexgrid.loader.processor.support;

public interface PropertyQualifierResolver<I> extends QualifierResolver<I>{
	
	public String getPropertyQualifierType(I item);
	
}