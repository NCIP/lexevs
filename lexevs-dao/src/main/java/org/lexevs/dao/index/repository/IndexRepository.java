package org.lexevs.dao.index.repository;

import java.util.List;

public interface IndexRepository<I,O> {
	public O getResourceById(String id);
	
	public void deleteResourceById(String id);
	
	public void insertResource(O resource);
	
	public void deleteResource(O resource);
	
	public List<O> query(I query);
	
	public List<O> query(String query);
	
	public List<O> query(String query, int page);
	
	public List<O> query(I query, int page);

}
