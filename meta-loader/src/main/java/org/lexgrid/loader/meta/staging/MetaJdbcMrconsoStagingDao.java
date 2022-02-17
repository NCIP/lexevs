
package org.lexgrid.loader.meta.staging;

import java.util.List;

import org.lexgrid.loader.meta.staging.processor.MetaMrconsoStagingDao;
import org.lexgrid.loader.rrf.staging.JdbcMrconsoStagingDao;
import org.springframework.dao.DataAccessException;

public class MetaJdbcMrconsoStagingDao extends JdbcMrconsoStagingDao implements MetaMrconsoStagingDao {

	protected String getRootCuis = "select CUI from " + PREFIX_PLACEHOLDER + TABLENAME_PLACEHOLDER + " where SAB = 'SRC' and TTY = 'RHT'";

	public List<String> getMetaRootCuis(){
		try {
			return (List<String>)getJdbcTemplate().queryForList(setPlaceholders(getRootCuis), String.class);
		} catch (DataAccessException e) {
			logger.error("Error getting Meta Root Cuis", e);	
			throw new RuntimeException(e);
		}
	}
}