package org.lexevs.dao.test;

import org.springframework.beans.factory.InitializingBean;

public class SystemVariableSettingBean implements InitializingBean {

	public void afterPropertiesSet() throws Exception {
		System.setProperty("LG_CONFIG_FILE", "src/test/resources/lbconfig.props");
	}
}
