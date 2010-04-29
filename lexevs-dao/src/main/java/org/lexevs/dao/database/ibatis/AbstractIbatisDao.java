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
package org.lexevs.dao.database.ibatis;

import org.lexevs.dao.database.access.AbstractBaseDao;
import org.lexevs.dao.database.ibatis.batch.IbatisBatchGroupInserter;
import org.lexevs.dao.database.ibatis.batch.IbatisInserter;
import org.lexevs.dao.database.ibatis.batch.InOrderOrderingBatchInserterDecorator;
import org.lexevs.dao.database.ibatis.batch.SqlMapClientTemplateInserter;
import org.lexevs.dao.database.ibatis.batch.SqlMapExecutorBatchInserter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class AbstractIbatisDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractIbatisDao extends AbstractBaseDao implements InitializingBean {

	/** The sql map client template. */
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	/** The non batch template inserter. */
	private IbatisInserter nonBatchTemplateInserter;
	
	/** The VERSION s_ namespace. */
	public static String VERSIONS_NAMESPACE = "Versions.";
	
	/** query to see if entrystate exists.  */
	private static String CHECK_ENTRYSTATE_EXISTS = VERSIONS_NAMESPACE + "checkEntryStateExists";
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		setNonBatchTemplateInserter(new SqlMapClientTemplateInserter(this.getSqlMapClientTemplate()));
	}
	
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao#executeInTransaction(org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao.IndividualDaoCallback)
	 */
	@Transactional
	public <T> T executeInTransaction(IndividualDaoCallback<T> callback) {
		return callback.execute();
	}



	/**
	 * Sets the sql map client template.
	 * 
	 * @param sqlMapClientTemplate the new sql map client template
	 */
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	/**
	 * Gets the sql map client template.
	 * 
	 * @return the sql map client template
	 */
	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}
	
	public IbatisBatchGroupInserter getBatchTemplateInserter(SqlMapExecutor executor) {
		return new InOrderOrderingBatchInserterDecorator(
					new SqlMapExecutorBatchInserter(executor));
	}

	/**
	 * Sets the non batch template inserter.
	 * 
	 * @param nonBatchTemplateInserter the new non batch template inserter
	 */
	public void setNonBatchTemplateInserter(IbatisInserter nonBatchTemplateInserter) {
		this.nonBatchTemplateInserter = nonBatchTemplateInserter;
	}

	/**
	 * Gets the non batch template inserter.
	 * 
	 * @return the non batch template inserter
	 */
	public IbatisInserter getNonBatchTemplateInserter() {
		return nonBatchTemplateInserter;
	}
	
	/**
	 * Method finds if the given entryState already exists. 
	 * Returns true if entryState exists or else returns false.
	 * 
	 * @param entryStateUId
	 * @return boolean
	 */
	public boolean entryStateExists(String prefix, String entryStateUId) {
		
		String count = (String) this.getSqlMapClientTemplate().queryForObject(
				CHECK_ENTRYSTATE_EXISTS, 
				new PrefixedParameter(prefix, entryStateUId));
		
		if( count != null &&  new Integer(count).intValue() > 0 )
			return true;
		
		return false;
	}
	
	
	

}
