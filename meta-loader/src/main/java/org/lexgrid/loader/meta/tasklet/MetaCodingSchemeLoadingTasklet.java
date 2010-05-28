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
			loadRevision(process(), null);
		
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
