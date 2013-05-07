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
package org.lexevs.dao.database.key.incrementer;

/**
 * The Class AbstractBigIntKeyIncrementer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBigIntKeyIncrementer extends AbstractKeyIncrementer {

	/** The SIZE. */
	private static int SIZE = 20;
	
	/** The NAME. */
	private static String NAME = "SEQUENTIAL_INTEGER";

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#getKeyType()
	 */
	@Override
	public KeyType getKeyType() {
		return KeyType.BIGINT;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#valueOf(java.lang.String)
	 */
	@Override
	public Object valueOf(String key) {
		return Long.parseLong(key);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#stringValue(java.lang.Object)
	 */
	@Override
	public String stringValue(Object key) {
		if(key == null) {return null;}
		return Long.toString((Long)key);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#getKeyLength()
	 */
	@Override
	public int getKeyLength() {
		return SIZE;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#getName()
	 */
	@Override
	public String getName() {
		return NAME;
	}
}