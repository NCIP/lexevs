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

import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.system.ResourceManager;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * The Class RegisteringTasklet.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RegisteringTasklet extends AbstractLexEvsUtilityTasklet implements Tasklet {
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		boolean alreadyLoaded = true;
		try {
			ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(getCurrentCodingSchemeUri(), getCurrentCodingSchemeVersion());
		} catch (LBParameterException e) {
			// this is a good thing, means that is hasn't been loaded.
			alreadyLoaded = false;
		}

		if(alreadyLoaded){
			getLogger().error("Coding Scheme is already Loaded.");
			throw new Exception("Coding Scheme is already Loaded.");
		}
		
		getLogger().info("Registering CodingScheme -- Load is now restartable.");

		String prefix = (String)chunkContext.getStepContext().getJobParameters().get("prefix");
		String url = (String)chunkContext.getStepContext().getJobParameters().get("jdbcUrl");
		String database = (String)chunkContext.getStepContext().getJobParameters().get("database");		
		
		/*TODO:
		getConnectionManager().register(getCurrentCodingSchemeUri(),
				getCurrentCodingSchemeVersion(),
				CodingSchemeVersionStatus.PENDING.toString(), 
				url, 
				null, 
				database, 
				prefix);
		 */
		return RepeatStatus.FINISHED;
	}

	@Override
	//We don't want this to be recoverable.
	protected RepeatStatus doExecute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		return null;
	}
}
