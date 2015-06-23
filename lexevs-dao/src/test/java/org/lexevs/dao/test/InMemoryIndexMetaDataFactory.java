/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.test;

import org.lexevs.dao.indexer.utility.MetaData;
import org.springframework.beans.factory.FactoryBean;


/**
 * A factory for creating InMemoryIndexMetaData objects.
 */
public class InMemoryIndexMetaDataFactory implements FactoryBean {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Object getObject() throws Exception {
		return new InMemoryMetaData();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class getObjectType() {
		return MetaData.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * The Class InMemoryMetaData.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class InMemoryMetaData extends MetaData {

		/* (non-Javadoc)
		 * @see edu.mayo.informatics.indexer.utility.MetaData#rereadFile(boolean)
		 */
		@Override
		public void rereadFile(boolean releaseLockWhenDone)
				throws RuntimeException {
			//no-op - in memory
		}

		/* (non-Javadoc)
		 * @see edu.mayo.informatics.indexer.utility.MetaData#writeFile(boolean)
		 */
		@Override
		protected synchronized void writeFile(boolean unlockWhenDone)
				throws RuntimeException {
			//no-op - in memory
		}	
	}
}


