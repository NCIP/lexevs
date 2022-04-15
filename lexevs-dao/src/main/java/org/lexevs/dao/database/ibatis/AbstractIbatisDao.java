
package org.lexevs.dao.database.ibatis;

import org.lexevs.dao.database.access.AbstractBaseDao;
import org.lexevs.dao.database.ibatis.batch.InOrderOrderingBatchInserterDecorator;
import org.lexevs.dao.database.ibatis.batch.SqlMapClientTemplateInserter;
import org.lexevs.dao.database.ibatis.batch.SqlMapExecutorBatchInserter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.inserter.BatchInserter;
import org.lexevs.dao.database.inserter.Inserter;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;
//import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * The Class AbstractIbatisDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractIbatisDao extends AbstractBaseDao implements InitializingBean {

	/** The sql map client template. */
	private SqlSessionTemplate sqlSessionTemplate;
	
	/** The sql map client template. */
	private SqlSessionTemplate sqlSessionBatchTemplate;
	
	/** The non batch template inserter. */
	private Inserter nonBatchTemplateInserter;
	
	/** The VERSION s_ namespace. */
	public static String VERSIONS_NAMESPACE = "Versions.";
	
	/** The VERSION s_ namespace. */
	public static String VSENTRYSTATE_NAMESPACE = "VSEntryState.";
	
	/** query to see if entrystate exists.  */
	private static String CHECK_ENTRYSTATE_EXISTS = VERSIONS_NAMESPACE + "checkEntryStateExists";
	
	private static String CHECK_VSENTRYSTATE_EXISTS = VSENTRYSTATE_NAMESPACE + "checkVSEntryStateExists";
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		setNonBatchTemplateInserter(new SqlMapClientTemplateInserter(this.getSqlSessionTemplate()));
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
	public void setSqlSessionTemplate(SqlSessionTemplate sqlMapClientTemplate) {
		this.sqlSessionTemplate = sqlMapClientTemplate;
	}

	/**
	 * Gets the sql map client template.
	 * 
	 * @return the sql map client template
	 */
	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}
	
	public SqlSessionTemplate getSqlSessionBatchTemplate() {
		return sqlSessionBatchTemplate;
	}


	public void setSqlSessionBatchTemplate(SqlSessionTemplate sqlSessionBatchTemplate) {
		this.sqlSessionBatchTemplate = sqlSessionBatchTemplate;
	}


	public BatchInserter getBatchTemplateInserter(SqlMapExecutor executor) {
		return new InOrderOrderingBatchInserterDecorator(
					new SqlMapExecutorBatchInserter(executor));
	}

	/**
	 * Sets the non batch template inserter.
	 * 
	 * @param nonBatchTemplateInserter the new non batch template inserter
	 */
	public void setNonBatchTemplateInserter(Inserter nonBatchTemplateInserter) {
		this.nonBatchTemplateInserter = nonBatchTemplateInserter;
	}

	/**
	 * Gets the non batch template inserter.
	 * 
	 * @return the non batch template inserter
	 */
	public Inserter getNonBatchTemplateInserter() {
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
		
		String count = (String) this.getSqlSessionTemplate().selectOne(
				CHECK_ENTRYSTATE_EXISTS, 
				new PrefixedParameter(prefix, entryStateUId));
		
		if( count != null &&  new Integer(count).intValue() > 0 )
			return true;
		
		return false;
	}
	
	/**
	 * Method finds if the given entryState already exists. 
	 * Returns true if entryState exists or else returns false.
	 * 
	 * @param entryStateUId
	 * @return boolean
	 */
	public boolean vsEntryStateExists(String prefix, String entryStateUId) {
		
		String count = (String) this.getSqlSessionTemplate().selectOne(
				CHECK_VSENTRYSTATE_EXISTS, 
				new PrefixedParameter(prefix, entryStateUId));
		
		if( count != null &&  new Integer(count).intValue() > 0 )
			return true;
		
		return false;
	}
}