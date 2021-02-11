
package org.lexgrid.loader.rrf.partition;

import java.util.HashMap;
import java.util.Map;

import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

/**
 * The Class RrfFilePartitioner.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RrfFilePartitioner implements Partitioner {

	/** The step name. */
	private String stepName = "rrfPartitionedStep";

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.partition.support.Partitioner#partition(int)
	 */
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> map = new HashMap<String, ExecutionContext>(gridSize);
		for (int i = 0; i < gridSize; i++) {
			ExecutionContext context = new ExecutionContext();
			context.put(RrfLoaderConstants.RRF_PARTITION_NUMBER, i);
			map.put(stepName + i, context);
		}
		return map;

	}
}