
package org.lexgrid.loader.rrf.staging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexgrid.loader.logging.StatusTrackingLogger;
import org.lexgrid.loader.rrf.data.codingscheme.MrsabUtility;
import org.lexgrid.loader.rrf.staging.model.CodeSabPair;
import org.lexgrid.loader.wrappers.CodeCodingSchemePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * The Class JdbcMrconsoStagingDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "JdbcMrconsoStagingDaoCache")
public class JdbcMrconsoStagingDao extends JdbcDaoSupport implements MrconsoStagingDao {
	
	/** The log. */
	@Autowired
	protected StatusTrackingLogger logger;

	/** The mrsab utility. */
	private MrsabUtility mrsabUtility;
		
	/** The PREFI x_ placeholder. */
	protected final String PREFIX_PLACEHOLDER = "%PREFIX_PLACEHOLDER%";
	
	/** The TABLENAM e_ placeholder. */
	protected final String TABLENAME_PLACEHOLDER = "%TABLENAME_PLACEHOLDER%";
	
	/** The prefix. */
	private String prefix = "";
	
	/** The table name. */
	private String tableName = "mrconsoStaging";
	
	/** The get code from cui and aui. */
	protected String getCodeFromCuiAndAui = "select distinct CODE, SAB from " + PREFIX_PLACEHOLDER + TABLENAME_PLACEHOLDER + " where CUI = ? and AUI = ?";
	
	/** The get codes from cui and sab. */
	protected String getCodesFromCuiAndSab = "select distinct CODE from " + PREFIX_PLACEHOLDER + TABLENAME_PLACEHOLDER + " where CUI = ? and SAB = ?";
	
	/** The get code from aui. */
	protected String getCodeFromAui = "select CODE from " + PREFIX_PLACEHOLDER + TABLENAME_PLACEHOLDER + " where AUI = ?";
	
	/** The get cui from aui and sab. */
	protected String getCuiFromAuiAndSab = "select CUI from " + PREFIX_PLACEHOLDER + TABLENAME_PLACEHOLDER + " where AUI = ? and SAB = ?";	
	
	protected String getCuiFromAui = "select CUI from " + PREFIX_PLACEHOLDER + TABLENAME_PLACEHOLDER + " where AUI = ?";
	
	protected String getCuisFromCode = "select distinct CUI from " + PREFIX_PLACEHOLDER + TABLENAME_PLACEHOLDER + " where CODE = ?";
	
	protected String getCodesFromCui = "select CODE from " + PREFIX_PLACEHOLDER + TABLENAME_PLACEHOLDER + " where CUI = ?";
	
	protected String getNameSpaceFromAui = "select SAB from " + PREFIX_PLACEHOLDER + TABLENAME_PLACEHOLDER + " where AUI = ?";
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.staging.MrconsoStagingDao#getCodeAndSab(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public CodeSabPair getCodeAndSab(String cui, String aui){
		try {
			return (CodeSabPair)getJdbcTemplate().queryForObject(setPlaceholders(getCodeFromCuiAndAui), new Object[] { cui, aui }, new RowMapper(){

				public Object mapRow(ResultSet arg0, int arg1)
						throws SQLException {
					int index = 1;

					CodeSabPair pair = new CodeSabPair();

					pair.setCode(arg0.getString(index++));
					pair.setSab(arg0.getString(index++));
					
					return pair;
				}	
			});
		} catch (DataAccessException e) {
			logger.error("Error getting Code and SAB for CUI:" + cui  + " AUI: " + aui, e);	
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.staging.MrconsoStagingDao#getCodes(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public List<String> getCodes(String cui, String sab){
		try {
			return (List<String>)getJdbcTemplate().queryForList(setPlaceholders(getCodesFromCuiAndSab), new Object[] { cui, sab }, String.class);
		} catch (DataAccessException e) {
			logger.error("Error getting Codes for CUI:" + cui  + " SAB: " + sab, e);	
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.staging.MrconsoStagingDao#getCodeFromAui(java.lang.String)
	 */
	@CacheMethod
	public String getCodeFromAui(String aui){
		try {
			return (String)getJdbcTemplate().queryForObject(setPlaceholders(getCodeFromAui), new Object[] { aui }, String.class);
		} catch (DataAccessException e) {
			logger.error("Error getting Codes for AUI:" + aui, e);
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.staging.MrconsoStagingDao#getCodeFromAui(java.lang.String)
	 */
	@CacheMethod
	public List<String> getCodesFromCui(String cui){
		try {
			return (List<String>)getJdbcTemplate().queryForList(setPlaceholders(getCodesFromCui), new Object[] { cui }, String.class);
		} catch (DataAccessException e) {
			logger.error("Error getting Codes for CUI:" + cui, e);
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.staging.MrconsoStagingDao#getCuiFromAuiAndSab(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public String getCuiFromAuiAndSab(String aui, String sab) {
		try {
			return (String)getJdbcTemplate().queryForObject(setPlaceholders(getCuiFromAuiAndSab), new Object[] { aui, sab }, String.class);
		} catch (DataAccessException e) {
			logger.error("Error getting CUI for AUI:" + aui  + " SAB: " + sab, e);	
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.staging.MrconsoStagingDao#getCuiFromAuiAndSab(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public String getCuiFromAui(String aui) {
		try {
			return (String)getJdbcTemplate().queryForObject(setPlaceholders(getCuiFromAui), new Object[] { aui }, String.class);
		} catch (DataAccessException e) {
			logger.error("Error getting CUI for AUI:" + aui, e);	
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.staging.MrconsoStagingDao#getCodeAndCodingScheme(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public CodeCodingSchemePair getCodeAndCodingScheme(String cui, String aui) {
		CodeSabPair codeSab = getCodeAndSab(cui, aui);
		CodeCodingSchemePair codeCodingScheme = new CodeCodingSchemePair();
		codeCodingScheme.setCode(codeSab.getCode());
		codeCodingScheme.setCodingScheme(mrsabUtility.getCodingSchemeNameFromSab(codeSab.getSab()));
		return codeCodingScheme;
	}

	@CacheMethod
	public List<String> getCuisFromCode(String code) {
		return (List<String>)getJdbcTemplate().queryForList(setPlaceholders(getCuisFromCode), new Object[] { code }, String.class);
	}
	
	/**
	 * Sets the placeholders.
	 * 
	 * @param sql the sql
	 * 
	 * @return the string
	 */
	protected String setPlaceholders(String sql){
		sql = sql.replaceAll(PREFIX_PLACEHOLDER, prefix);
		sql = sql.replaceAll(TABLENAME_PLACEHOLDER, tableName);
		return sql;
	}
	
	/**
	 * Gets the mrsab utility.
	 * 
	 * @return the mrsab utility
	 */
	public MrsabUtility getMrsabUtility() {
		return mrsabUtility;
	}

	/**
	 * Sets the mrsab utility.
	 * 
	 * @param mrsabUtility the new mrsab utility
	 */
	public void setMrsabUtility(MrsabUtility mrsabUtility) {
		this.mrsabUtility = mrsabUtility;
	}

	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the prefix.
	 * 
	 * @param prefix the new prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Gets the table name.
	 * 
	 * @return the table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Sets the table name.
	 * 
	 * @param tableName the new table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}	
}