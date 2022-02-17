
package org.lexevs.dao.database.jdbc;

import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

public class MysqlJdbcUrlAppendingFactory implements FactoryBean{

	private String urlAppendString = "?useCursorFetch=true";
	
	private SystemVariables systemVariables;
	
	private static String MYSQL_URL_MATCH = "mysql";
	
	@Override
	public Object getObject() throws Exception {
		String url = this.systemVariables.getAutoLoadDBURL();
		
		if(isMysqlUrl(url)) {
			
			if(! url.contains(urlAppendString)) {
			
				return url + urlAppendString;
			}
		}
		
		return url;
	}

	protected boolean isMysqlUrl(String url) {
		return url.equalsIgnoreCase(MYSQL_URL_MATCH);
	}

	@Override
	public Class getObjectType() {
		return String.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setUrlAppendString(String urlAppendString) {
		this.urlAppendString = urlAppendString;
	}

	public String getUrlAppendString() {
		return urlAppendString;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}	
}