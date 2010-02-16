package org.lexevs.dao.database.utility;

import org.springframework.beans.factory.FactoryBean;

public class JdbcDriverBootstrap implements FactoryBean{

	private String driverClass;
	
	private ClassLoader classLoader;

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public Object getObject() throws Exception {
		return Class.forName(driverClass, true, classLoader);
	}

	public Class getObjectType() {
		return Class.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}
}
