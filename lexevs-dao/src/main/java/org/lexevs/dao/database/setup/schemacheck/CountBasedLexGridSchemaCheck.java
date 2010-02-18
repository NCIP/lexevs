package org.lexevs.dao.database.setup.schemacheck;

import javax.sql.DataSource;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.springframework.jdbc.core.JdbcTemplate;

public class CountBasedLexGridSchemaCheck extends AbstractLesGridSchemaCheck {
	
	private String checkTableName = "registry";

	public CountBasedLexGridSchemaCheck() {
		super();
	}

	public CountBasedLexGridSchemaCheck(DataSource dataSource,
			PrefixResolver prefixResolver) {
		super(dataSource, prefixResolver);
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
		return "SELECT count(*) from " + this.getPrefixResolver().resolveDefaultPrefix() + checkTableName;
	}

	public void setCheckTableName(String checkTableName) {
		this.checkTableName = checkTableName;
	}

	public String getCheckTableName() {
		return checkTableName;
	}
}
