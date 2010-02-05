package org.lexevs.dao.database.key;

public interface KeyProvider<I,O> {

	O getKey(I record);
}
