package org.lexevs.dao.index.model;


public interface IndexableResourceBuilder<I,O> {
	
	public O buildIndexableResource(I input);
	
	public String resolveId(I input);

}
