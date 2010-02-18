package org.lexevs.dao.database.setup.schemacheck;

import javax.sql.DataSource;

import org.lexevs.dao.database.prefix.PrefixResolver;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractLesGridSchemaCheck implements LexGridSchemaCheck {
	
	private DataSource dataSource;
	
	private PrefixResolver prefixResolver;
	
	public AbstractLesGridSchemaCheck(){
		super();
	}
	
	public AbstractLesGridSchemaCheck(DataSource dataSource, PrefixResolver prefixResolver){
		this.dataSource = dataSource;
		this.prefixResolver = prefixResolver;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public boolean isCommonLexGridSchemaInstalled() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		return checkResult(template,
				getDbCheckSql());
	}
	
	protected abstract String getDbCheckSql();
	protected abstract boolean checkResult(JdbcTemplate template, String sql);

	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}

	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}
}
