/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
