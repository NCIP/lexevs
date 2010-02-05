package org.lexevs.dao.database.setup.schemacheck;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractLesGridSchemaCheck implements LexGridSchemaCheck {
	
	private DataSource dataSource;
	
	private String prefix;
	
	public AbstractLesGridSchemaCheck(){
		super();
	}
	
	public AbstractLesGridSchemaCheck(DataSource dataSource){
		this.dataSource = dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public boolean isLgSchemaInstalled() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		return checkResult(template,
				getDbCheckSql());
	}
	
	protected abstract String getDbCheckSql();
	protected abstract boolean checkResult(JdbcTemplate template, String sql);

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}
