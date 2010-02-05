package org.LexGrid.LexBIG.Extensions.Load;

import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.springframework.batch.core.JobExecution;

public interface SpringBatchLoader extends Loader {
	
	public JobExecution getJobExecution();
}
