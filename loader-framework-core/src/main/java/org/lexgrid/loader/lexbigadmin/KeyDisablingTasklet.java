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

import org.lexgrid.loader.database.MysqlKeyUtility;
import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.support.DatabaseType;

/**
 * The Class KeyDisablingTasklet.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class KeyDisablingTasklet extends LoggingBean implements Tasklet {

	private MysqlKeyUtility mysqlKeyUtility;
	
	private DatabaseType databaseType;
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if(databaseType.equals(DatabaseType.MYSQL)){
			getLogger().info("Disabling MySQL keys.");
			mysqlKeyUtility.disableKeys();
		} else {
			getLogger().info("Not disabling keys for non-MySQL database.");
		}
		return RepeatStatus.FINISHED;
	}
	public MysqlKeyUtility getMysqlKeyUtility() {
		return mysqlKeyUtility;
	}
	public void setMysqlKeyUtility(MysqlKeyUtility mysqlKeyUtility) {
		this.mysqlKeyUtility = mysqlKeyUtility;
	}
	public DatabaseType getDatabaseType() {
		return databaseType;
	}
	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}
}
