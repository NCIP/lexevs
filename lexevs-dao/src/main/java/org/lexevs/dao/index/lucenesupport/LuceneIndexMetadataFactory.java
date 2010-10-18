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
package org.lexevs.dao.index.lucenesupport;

import java.io.File;

import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

import edu.mayo.informatics.indexer.utility.MetaData;

public class LuceneIndexMetadataFactory implements FactoryBean {
	
	private SystemVariables systemVariables;

	@Override
	public Object getObject() throws Exception {
		return new MetaData(new File(systemVariables.getAutoLoadIndexLocation()));
	}

	@Override
	public Class getObjectType() {
		return MetaData.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}
}