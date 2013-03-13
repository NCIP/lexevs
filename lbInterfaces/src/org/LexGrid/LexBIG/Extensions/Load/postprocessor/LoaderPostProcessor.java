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
package org.LexGrid.LexBIG.Extensions.Load.postprocessor;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;

/**
 * The Interface LoaderPostProcessor allows user defined process logic 
 * to be injected into a Loader load process in the form of an Extension.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LoaderPostProcessor extends GenericExtension {

	/**
	 * Executes a Loader post process. Loader post processes can be used to modify
	 * database content, do extra cleanup, or send notifications, for example.
	 * 
	 * NOTE: Post Process error/exception conditions will not effect Loader status.
	 * 
	 * Implementors can assume that database content has been loaded at the point of
	 * this call, but the load is not yet in a completed state and Lucene indexing
	 * has not been done.
	 * 
	 * @param reference the uri/version of the requesting Coding Scheme
	 * @param ontFormat the OntologyFormat of the requesting Loader
	 */
	public void runPostProcess(AbsoluteCodingSchemeVersionReference reference, OntologyFormat ontFormat);
}