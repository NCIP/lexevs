package org.LexGrid.persistence.connection;

import org.LexGrid.LexBIG.Impl.helpers.MyClassLoader;
import org.springframework.beans.factory.FactoryBean;

public class JdbcDriverBootstrap implements FactoryBean{

	private String driverClass;

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public Object getObject() throws Exception {
		return Class.forName(driverClass, true, MyClassLoader.instance());
	}

	public Class getObjectType() {
		return Class.class;
	}

	public boolean isSingleton() {
		return true;
	}
}
