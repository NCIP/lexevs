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
		
		if(retry){
			getConnectionManager().reIndex(
					getCurrentCodingSchemeUri(), 
					getCurrentCodingSchemeVersion());
		} else {
			getLogger().info("Starting Lucene Indexing.");
			
			getConnectionManager().index(getCodingSchemeIdSetter().getCodingSchemeName(),
					getCurrentCodingSchemeUri(), 
					getCurrentCodingSchemeVersion());		
		}
		return RepeatStatus.FINISHED;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}
}
