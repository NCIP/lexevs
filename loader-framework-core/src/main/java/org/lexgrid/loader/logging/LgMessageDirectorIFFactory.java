/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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