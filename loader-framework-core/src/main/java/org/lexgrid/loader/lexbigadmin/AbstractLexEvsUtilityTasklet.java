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

import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.persistence.model.CodingScheme;
import org.lexgrid.loader.connection.LoaderConnectionManager;
import org.lexgrid.loader.connection.impl.LexEVSConnectionManager;
import org.lexgrid.loader.data.codingScheme.CodingSchemeNameSetter;
import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * The Class IndexingTasklet.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLexEvsUtilityTasklet extends LoggingBean implements Tasklet {

	/** The connection manager. */
	private LoaderConnectionManager connectionManager = new LexEVSConnectionManager();
	
	/** The lex evs dao. */
	private LexEvsDao lexEvsDao;
	
	/** The coding scheme name setter. */
	private CodingSchemeNameSetter codingSchemeNameSetter;
	
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
			return doExecute(contribution, chunkContext);
	}
		
	protected abstract RepeatStatus doExecute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception;
	/**
	 * Gets the current coding scheme uri.
	 * 
	 * @return the current coding scheme uri
	 * 
	 * @throws Exception the exception
	 */
	protected String getCurrentCodingSchemeUri() throws Exception {	
		return getCurrentCodingScheme().getCodingSchemeUri();
	}
	
	/**
	 * Gets the current coding scheme version.
	 * 
	 * @return the current coding scheme version
	 * 
	 * @throws Exception the exception
	 */
	protected String getCurrentCodingSchemeVersion() throws Exception {
		return getCurrentCodingScheme().getRepresentsVersion();
	}
	
	
	/**
	 * Gets the current coding scheme.
	 * 
	 * @return the current coding scheme
	 * 
	 * @throws Exception the exception
	 */
	protected CodingScheme getCurrentCodingScheme() throws Exception {
		return lexEvsDao.findById(CodingScheme.class, codingSchemeNameSetter.getCodingSchemeName());
	}

	public LoaderConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(LoaderConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public LexEvsDao getLexEvsDao() {
		return lexEvsDao;
	}

	public void setLexEvsDao(LexEvsDao lexEvsDao) {
		this.lexEvsDao = lexEvsDao;
	}

	public CodingSchemeNameSetter getCodingSchemeNameSetter() {
		return codingSchemeNameSetter;
	}

	public void setCodingSchemeNameSetter(
			CodingSchemeNameSetter codingSchemeNameSetter) {
		this.codingSchemeNameSetter = codingSchemeNameSetter;
	}	
}
