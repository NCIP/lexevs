
package org.lexgrid.loader.lexbigadmin;

import java.net.URI;

import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

/**
 * The Class MetadataLoadingTasklet.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetadataLoadingTasklet extends AbstractLexEvsUtilityTasklet implements Tasklet {

	/** The Constant loaderName. */
	private static final String loaderName = "MetaDataLoader";
	
	/** The Constant loaderClass. */
	private static final Class<MetaData_Loader> loaderClass = MetaData_Loader.class;
	
	/** The input resource. */
	private Resource inputResource;
	
	/** The stop on errors. */
	private boolean stopOnErrors = true;
	
	/** The async. */
	private boolean async = false;
	
	/** The overwrite. */
	private boolean overwrite = false;
	
	private boolean deleteXmlAfterLoad = true;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	public RepeatStatus doExecute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		getLogger().info("Loading Metadata.");
		
		MetaData_Loader metadataLoader =
			(MetaData_Loader) LexBIGServiceImpl.defaultInstance().getServiceManager(null).getLoader(loaderName);
		
		//we know this URI is always going to be a file, so to make
		//it easier for our loader down the line we'll convert this
		//URI to a pure File URI, and eliminate any Authority errors.
		URI inputUri = inputResource.getFile().toURI();
		
		getLogger().info("Reading Metadata from: " + inputUri);
		if(deleteXmlAfterLoad){
			getLogger().info("Metadata XML file will be deleted after load.");
		} else {
			getLogger().info("Metadata XML file will NOT be deleted after load.");
		}
		
		metadataLoader.loadAuxiliaryData(
				inputUri, 
				Constructors.createAbsoluteCodingSchemeVersionReference(
						getCurrentCodingSchemeUri(), 
						getCurrentCodingSchemeVersion()), 
						overwrite, 
						stopOnErrors, async);
		
		if(deleteXmlAfterLoad){
			FileUtils.forceDelete(this.inputResource.getFile());
		}
		
		return RepeatStatus.FINISHED;
	}
	


	/**
	 * Gets the input resource.
	 * 
	 * @return the input resource
	 */
	public Resource getInputResource() {
		return inputResource;
	}

	/**
	 * Sets the input resource.
	 * 
	 * @param inputResource the new input resource
	 */
	public void setInputResource(Resource inputResource) {
		this.inputResource = inputResource;
	}

	/**
	 * Checks if is stop on errors.
	 * 
	 * @return true, if is stop on errors
	 */
	public boolean isStopOnErrors() {
		return stopOnErrors;
	}

	/**
	 * Sets the stop on errors.
	 * 
	 * @param stopOnErrors the new stop on errors
	 */
	public void setStopOnErrors(boolean stopOnErrors) {
		this.stopOnErrors = stopOnErrors;
	}

	/**
	 * Checks if is async.
	 * 
	 * @return true, if is async
	 */
	public boolean isAsync() {
		return async;
	}

	/**
	 * Sets the async.
	 * 
	 * @param async the new async
	 */
	public void setAsync(boolean async) {
		this.async = async;
	}

	/**
	 * Checks if is overwrite.
	 * 
	 * @return true, if is overwrite
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * Sets the overwrite.
	 * 
	 * @param overwrite the new overwrite
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	/**
	 * Gets the loader name.
	 * 
	 * @return the loader name
	 */
	public static String getLoaderName() {
		return loaderName;
	}

	/**
	 * Gets the loader class.
	 * 
	 * @return the loader class
	 */
	public static Class<MetaData_Loader> getLoaderClass() {
		return loaderClass;
	}

	public boolean isDeleteXmlAfterLoad() {
		return deleteXmlAfterLoad;
	}

	public void setDeleteXmlAfterLoad(boolean deleteXmlAfterLoad) {
		this.deleteXmlAfterLoad = deleteXmlAfterLoad;
	}
}