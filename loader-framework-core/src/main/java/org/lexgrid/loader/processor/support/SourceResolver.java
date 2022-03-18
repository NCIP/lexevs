
package org.lexgrid.loader.processor.support;

public interface SourceResolver<I> {

	public String getSource(I item);
	
	public String getSubRef(I item);
	
	public String getRole(I item);
}