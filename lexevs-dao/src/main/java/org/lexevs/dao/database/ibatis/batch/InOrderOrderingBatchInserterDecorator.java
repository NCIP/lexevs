
package org.lexevs.dao.database.ibatis.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lexevs.dao.database.inserter.BatchInserter;

/**
 * The Class OrderingBatchInserterDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InOrderOrderingBatchInserterDecorator implements BatchInserter {

	/** The delegate. */
	private BatchInserter delegate;
	
	private BatchOrderClassifier batchOrderClassifier;
	
	/** The statement map. */
	private Map<Integer,Map<String,List<Object>>> statementMap = new LinkedHashMap<Integer,Map<String,List<Object>>>();
	/**
	 * Instantiates a new ordering batch inserter decorator.
	 * 
	 * @param delegate the delegate
	 */
	public InOrderOrderingBatchInserterDecorator(BatchInserter delegate){
		this(delegate, new BatchOrderClassifier());
	}
	
	public InOrderOrderingBatchInserterDecorator(
			BatchInserter delegate,
			BatchOrderClassifier batchOrderClassifier){
		this.delegate = delegate;
		this.batchOrderClassifier = batchOrderClassifier;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.BatchInserter#executeBatch()
	 */
	@Override
	public void executeBatch() {
		
		for(Integer group : statementMap.keySet()) {
			for(String sql : statementMap.get(group).keySet()){
				for(Object insertObj : statementMap.get(group).get(sql)) {
					delegate.insert(sql, insertObj);
				}
			}
		}
		delegate.executeBatch();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.BatchInserter#startBatch()
	 */
	@Override
	public void startBatch() {
		delegate.startBatch();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.Inserter#insert(java.lang.String, java.lang.Object)
	 */
	@Override
	public void insert(String sql, Object parameter) {
		Integer groupNumber = this.batchOrderClassifier.classify(sql);
		if(! this.statementMap.containsKey(groupNumber)) {
			this.statementMap.put(groupNumber, new HashMap<String,List<Object>>() );
		}
		
		Map<String,List<Object>> groupMap = this.statementMap.get(groupNumber);
		
		if(!groupMap.containsKey(sql)) {
			groupMap.put(sql, new ArrayList<Object>() );
		}
		
		groupMap.get(sql).add(parameter);
	}
}