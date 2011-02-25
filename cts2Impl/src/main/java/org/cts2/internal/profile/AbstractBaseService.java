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
package org.cts2.internal.profile;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.internal.uri.factory.DirectoryURIFactory;
import org.cts2.uri.DirectoryURI;

/**
 * The Class AbstractBaseService.
 *
 * @param <T> the
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBaseService<T extends DirectoryURI> {
	
	/** The lex big service. */
	private LexBIGService lexBigService;
	
	/** The directory uri factory. */
	private DirectoryURIFactory<T> directoryURIFactory;

	/**
	 * Sets the lex big service.
	 *
	 * @param lexBigService the new lex big service
	 */
	public void setLexBigService(LexBIGService lexBigService) {
		this.lexBigService = lexBigService;
	}

	/**
	 * Gets the lex big service.
	 *
	 * @return the lex big service
	 */
	public LexBIGService getLexBigService() {
		return this.lexBigService;
	}

	/**
	 * Sets the directory uri factory.
	 *
	 * @param directoryURIFactory the new directory uri factory
	 */
	public void setDirectoryURIFactory(DirectoryURIFactory<T> directoryURIFactory) {
		this.directoryURIFactory = directoryURIFactory;
	}

	/**
	 * Gets the directory uri factory.
	 *
	 * @return the directory uri factory
	 */
	public DirectoryURIFactory<T> getDirectoryURIFactory() {
		return this.directoryURIFactory;
	}
}
