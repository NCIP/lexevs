
package org.lexgrid.loader.lexbigadmin;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * The Class TransitivityBuildingTasklet.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class TransitivityBuildingTasklet extends AbstractLexEvsUtilityTasklet implements Tasklet {

	private boolean retry = false;
	
	private DatabaseUtility databaseUtility;
	
	private boolean skip = false;
	
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if(skip){
			this.getLogger().warn("Building the Transitivity Table has been skipped.");
			return RepeatStatus.FINISHED;
		} else {
			return super.execute(contribution, chunkContext);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	public RepeatStatus doExecute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		if(retry){
			try {
				databaseUtility.truncateTable(
						SQLTableConstants.TBL_ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE);
			} catch (Throwable e) {
				getLogger().error("Error Truncating the : " 
						+ SQLTableConstants.TBL_ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE
						+ " table. This most likely is due to a restarted load that hasn't "
						+ "build the table yet. Continuing...");
			}
		}
			getConnectionManager().computeTransitiveTable(
					getCurrentCodingSchemeUri(),
					getCurrentCodingSchemeVersion());
	
		return RepeatStatus.FINISHED;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	public DatabaseUtility getDatabaseUtility() {
		return databaseUtility;
	}

	public void setDatabaseUtility(DatabaseUtility databaseUtility) {
		this.databaseUtility = databaseUtility;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}
}