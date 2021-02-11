
package org.lexgrid.loader.lexbigadmin;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * The Class IndexingTasklet.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IndexingTasklet extends AbstractLexEvsUtilityTasklet implements Tasklet {

	private boolean retry = false;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	public RepeatStatus doExecute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		EntityIndexService entityIndexService = 
			LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService();
		
		AbsoluteCodingSchemeVersionReference ref = DaoUtility.
			createAbsoluteCodingSchemeVersionReference(getCurrentCodingSchemeUri(), getCurrentCodingSchemeVersion());
		if(retry){
			entityIndexService.dropIndex(ref);
		}
		getLogger().info("Starting Lucene Indexing.");
		entityIndexService.createIndex(ref);
		
		return RepeatStatus.FINISHED;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}
}