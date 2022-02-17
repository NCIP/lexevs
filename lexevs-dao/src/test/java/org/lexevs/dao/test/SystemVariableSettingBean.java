
package org.lexevs.dao.test;

import org.springframework.beans.factory.InitializingBean;

/**
 * The Class SystemVariableSettingBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SystemVariableSettingBean implements InitializingBean {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		System.setProperty("LG_CONFIG_FILE", "src/test/resources/lbconfig.props");
	}
}