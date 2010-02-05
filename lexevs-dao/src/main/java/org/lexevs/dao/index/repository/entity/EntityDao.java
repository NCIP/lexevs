package org.lexevs.dao.index.repository.entity;

import java.util.List;

import org.lexevs.dao.index.repository.IndexRepository;

public interface EntityDao<I,O> extends IndexRepository<I,O>{

	@Deprecated
	public List<O> query(I combinedQueries, List<I> individualQueries);
}
