
package org.lexgrid.loader.rrf.partition;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;

/**
 * The Class RrfFilePartitioner.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RrfFilePartitionerTest {
	
	/** The partitioner. */
	private RrfFilePartitioner partitioner;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		partitioner = new RrfFilePartitioner();
	}
	
	/**
	 * Test not null.
	 */
	@Test
	public void testNotNull() {
		assertNotNull(partitioner);
	}
	
	/**
	 * Test get last number of cui.
	 */
	@Test
	public void testCorrectPartitionSize() {
		Map<String, ExecutionContext> result = partitioner.partition(10);
		assertTrue(
				result.size() == 10);
	}
	
	@Test
	public void testExecutionContextsPopulated() {
		Map<String, ExecutionContext> result = partitioner.partition(10);
		for(ExecutionContext context : result.values()) {
			assertNotNull(context);
		}
	}
}