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
package org.cts2.uri.restriction;

import org.cts2.core.types.SetOperator;
import org.cts2.uri.DirectoryURI;

/**
 * The Class SetComposite.
 *
 * @param <T> the
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SetComposite<T extends DirectoryURI> {

	/** The set operator. */
	private SetOperator setOperator;

	/** The directory uri1. */
	private T directoryUri1;

	/** The directory uri2. */
	private T directoryUri2;

	/**
	 * Gets the sets the operator.
	 *
	 * @return the sets the operator
	 */
	public SetOperator getSetOperator() {
		return setOperator;
	}

	/**
	 * Sets the sets the operator.
	 *
	 * @param setOperator the new sets the operator
	 */
	public void setSetOperator(SetOperator setOperator) {
		this.setOperator = setOperator;
	}

	/**
	 * Gets the directory uri1.
	 *
	 * @return the directory uri1
	 */
	public T getDirectoryUri1() {
		return directoryUri1;
	}

	/**
	 * Sets the directory uri1.
	 *
	 * @param directoryUri1 the new directory uri1
	 */
	public void setDirectoryUri1(T directoryUri1) {
		this.directoryUri1 = directoryUri1;
	}

	/**
	 * Gets the directory uri2.
	 *
	 * @return the directory uri2
	 */
	public T getDirectoryUri2() {
		return directoryUri2;
	}

	/**
	 * Sets the directory uri2.
	 *
	 * @param directoryUri2 the new directory uri2
	 */
	public void setDirectoryUri2(T directoryUri2) {
		this.directoryUri2 = directoryUri2;
	}
}