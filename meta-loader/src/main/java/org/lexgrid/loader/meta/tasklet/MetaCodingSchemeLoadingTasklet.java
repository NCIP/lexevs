
package org.lexgrid.loader.meta.tasklet;

import java.util.Properties;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.loader.constants.LoaderConstants;
import org.lexgrid.loader.dao.SupportedAttributeSupport;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * The Class MetaCodingSchemeProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaCodingSchemeLoadingTasklet extends SupportedAttributeSupport implements Tasklet {
	
	/** The coding scheme properties. */
	private Properties codingSchemeProperties;
	
	private CodingSchemeIdSetter codingSchemeIdSetter;
		
	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
			throws Exception {
		LexEvsServiceLocator.getInstance().
			getDatabaseServiceManager().
			getAuthoringService().
			loadRevision(process(), null, null);
		
		return RepeatStatus.FINISHED;
	}

	public CodingScheme process() throws Exception {
			CodingScheme cs = new CodingScheme();
			cs.setCodingSchemeName(codingSchemeIdSetter.getCodingSchemeName());
			cs.setRepresentsVersion(codingSchemeIdSetter.getCodingSchemeVersion());
			cs.setCodingSchemeURI(codingSchemeIdSetter.getCodingSchemeUri());
			cs.setFormalName(codingSchemeProperties.getProperty(LoaderConstants.FORMAL_NAME_PROPERTY));
			cs.setDefaultLanguage(codingSchemeProperties.getProperty(LoaderConstants.DEFAULT_LANGUAGE_PROPERTY));
			cs.setCopyright(DaoUtility.createText(codingSchemeProperties.getProperty(LoaderConstants.COPYRIGHT_PROPERTY)));
			cs.addLocalName(codingSchemeProperties.getProperty(LoaderConstants.DEPRECATED_NAME_PROPERTY));
			
			EntityDescription ed = new EntityDescription();
			ed.setContent(codingSchemeProperties.getProperty(LoaderConstants.ENTITY_DESCRIPTION_PROPERTY));
			cs.setEntityDescription(ed);
			cs.setIsActive(true);
			
			return cs;
	}

	/**
	 * Gets the coding scheme properties.
	 * 
	 * @return the coding scheme properties
	 */
	public Properties getCodingSchemeProperties() {
		return codingSchemeProperties;
	}

	/**
	 * Sets the coding scheme properties.
	 * 
	 * @param codingSchemeProperties the new coding scheme properties
	 */
	public void setCodingSchemeProperties(Properties codingSchemeProperties) {
		this.codingSchemeProperties = codingSchemeProperties;
	}

	public void setCodingSchemeIdSetter(CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}

	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}
}