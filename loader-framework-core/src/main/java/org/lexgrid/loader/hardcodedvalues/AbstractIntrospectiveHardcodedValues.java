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
package org.lexgrid.loader.hardcodedvalues;

import java.util.List;

import org.LexGrid.persistence.dao.LexEvsDao;
import org.lexgrid.loader.dao.SupportedAttributeSupport;
import org.lexgrid.loader.data.codingScheme.CodingSchemeNameSetter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * The Class AbstractIntrospectiveHardcodedValues.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractIntrospectiveHardcodedValues extends SupportedAttributeSupport implements Tasklet {

	/** The coding scheme name setter. */
	private CodingSchemeNameSetter codingSchemeNameSetter;
	
	/** The lex evs dao. */
	private LexEvsDao lexEvsDao;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
			throws Exception {
		
		for(Object obj : loadObjects()){
			lexEvsDao.insert(obj);
		}
		return RepeatStatus.FINISHED;
	}
	
	/**
	 * Load objects.
	 * 
	 * @return the list< object>
	 */
	public abstract List<Object> loadObjects();
	
	/**
	 * Gets the coding scheme name setter.
	 * 
	 * @return the coding scheme name setter
	 */
	public CodingSchemeNameSetter getCodingSchemeNameSetter() {
		return codingSchemeNameSetter;
	}

	/**
	 * Sets the coding scheme name setter.
	 * 
	 * @param codingSchemeNameSetter the new coding scheme name setter
	 */
	public void setCodingSchemeNameSetter(
			CodingSchemeNameSetter codingSchemeNameSetter) {
		this.codingSchemeNameSetter = codingSchemeNameSetter;
	}

	/**
	 * Gets the lex evs dao.
	 * 
	 * @return the lex evs dao
	 */
	public LexEvsDao getLexEvsDao() {
		return lexEvsDao;
	}

	/**
	 * Sets the lex evs dao.
	 * 
	 * @param lexEvsDao the new lex evs dao
	 */
	public void setLexEvsDao(LexEvsDao lexEvsDao) {
		this.lexEvsDao = lexEvsDao;
	}
}
