
package org.lexgrid.loader.hardcodedvalues;

import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexgrid.loader.dao.SupportedAttributeSupport;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The Class AbstractIntrospectiveHardcodedValues.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractIntrospectiveHardcodedValues extends SupportedAttributeSupport implements Tasklet, InitializingBean {

	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	private DatabaseServiceManager databaseServiceManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(codingSchemeIdSetter);
		Assert.notNull(databaseServiceManager);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
			throws Exception {
		
		this.loadObjects();
		return RepeatStatus.FINISHED;
	}
	
	/**
	 * Load objects.
	 * 
	 * @return the list< object>
	 */
	public abstract void loadObjects();
	

	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}

	public void setCodingSchemeIdSetter(CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}

	public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}
}