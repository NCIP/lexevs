
package org.lexgrid.loader.logging;

import java.util.Date;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class LgMessageDirectorIFFactory implements InitializingBean, FactoryBean {

	private CachingMessageDirectorIF logger;

	public void afterPropertiesSet() throws Exception {
		LoadStatus status = new LoadStatus();
		status.setState(ProcessState.PROCESSING);
		status.setStartTime(new Date(System.currentTimeMillis()));
		logger = new SpringBatchMessageDirector("SpringBatchLoader", status);
	}

	public Object getObject() throws Exception {	
		return logger;
	}

	public Class getObjectType() {
		return StatusTrackingLogger.class;
	}

	public boolean isSingleton() {
		return true;
	}
}