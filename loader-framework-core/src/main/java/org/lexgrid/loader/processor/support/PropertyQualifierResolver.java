package org.lexgrid.loader.processor.support;

import org.LexGrid.commonTypes.Text;

public interface PropertyQualifierResolver<I> {

	public String getPropertyQualifierName(I item);
	
	public String getPropertyQualifierType(I item);
	
	public Text getPropertyQualifierValue(I item);
	
}
