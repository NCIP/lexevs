package org.lexevs.dao.database.setup.schemacheck;

import javax.sql.DataSource;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.springframework.jdbc.core.JdbcTemplate;

public class CountBasedLexGridSchemaCheck extends AbstractLesGridSchemaCheck {

	public CountBasedLexGridSchemaCheck(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected boolean checkResult(JdbcTemplate template, String sql) {
		try{
			template.execute(sql);
			return true;
		} catch(Throwable t){
			return false;
		}
	}

	@Override
	protected String getDbCheckSql() {
		return "SELECT count(*) from " + this.getPrefix() + SQLTableConstants.TBL_LEXGRID_TABLE_META_DATA;
	}
}
