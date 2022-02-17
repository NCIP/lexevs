
package org.lexgrid.loader.lexbigadmin;

import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.service.SystemResourceService;
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
	
	private boolean retry = false;
	private String prefix;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if(!retry) {
			Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
			RegistryEntry entry = registry.getCodingSchemeEntry(
					DaoUtility.createAbsoluteCodingSchemeVersionReference(
							getCurrentCodingSchemeUri(), 
							getCurrentCodingSchemeVersion()));
			
			entry.setStagingPrefix(prefix);
			
			registry.updateEntry(entry);
		}
		return RepeatStatus.FINISHED;
	}

	@Override
	protected String getCurrentCodingSchemeUri() throws Exception {
		return this.getCodingSchemeIdSetter().getCodingSchemeUri();
	}

	@Override
	protected String getCurrentCodingSchemeVersion() throws Exception {
		return this.getCodingSchemeIdSetter().getCodingSchemeVersion();
	}

	@Override
	//We don't want this to be recoverable.
	protected RepeatStatus doExecute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		return null;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}