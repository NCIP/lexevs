
package org.lexgrid.loader.rrf.partition;

import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrrel;

import static org.junit.Assert.*;

/**
 * The Class MrrelFilePartitionerTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrelFilePartitionerTest {
	
	/** The partitioner. */
	private MrrelFilePartitioner partitioner;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		partitioner = new MrrelFilePartitioner();
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
	public void testGetLastNumberOfCui() {
		assertTrue(
				MrrelFilePartitioner.getLastNumberOfCui("C12345") == 5);
				
	}
	
	/**
	 * Test get partition number for input row.
	 */
	@Test
	public void testGetPartitionNumberForInputRow() {
		Mrrel mrrel = new Mrrel();
		mrrel.setCui1("C12345");
		assertTrue(
				partitioner.getPartitionNumberForInputRow(mrrel) == 5);
	}
}