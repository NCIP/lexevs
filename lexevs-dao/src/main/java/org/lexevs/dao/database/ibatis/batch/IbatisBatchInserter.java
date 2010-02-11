package org.lexevs.dao.database.ibatis.batch;

public interface IbatisBatchInserter extends IbatisInserter {

	public void startBatch();
	
	public void executeBatch();
}
