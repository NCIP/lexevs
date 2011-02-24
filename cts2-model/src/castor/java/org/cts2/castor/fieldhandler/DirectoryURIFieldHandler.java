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
package org.cts2.castor.fieldhandler;
import org.exolab.castor.mapping.AbstractFieldHandler;


/**
 * The Class DirectoryURIFieldHandler.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DirectoryURIFieldHandler extends AbstractFieldHandler {

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.AbstractFieldHandler#getValue(java.lang.Object)
	 */
	@Override
	public Object getValue(Object arg0) throws IllegalStateException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.AbstractFieldHandler#newInstance(java.lang.Object)
	 */
	@Override
	public Object newInstance(Object arg0) throws IllegalStateException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.AbstractFieldHandler#newInstance(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public Object newInstance(Object arg0, Object[] arg1)
			throws IllegalStateException {

		return null;
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.AbstractFieldHandler#resetValue(java.lang.Object)
	 */
	@Override
	public void resetValue(Object arg0) throws IllegalStateException,
			IllegalArgumentException {
	
		System.out.println(arg0);
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.AbstractFieldHandler#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void setValue(Object arg0, Object arg1)
			throws IllegalStateException, IllegalArgumentException {
		System.out.println(arg1);
	}

}
