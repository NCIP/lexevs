/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;

/**
 * The Interface MetaBatchLoader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MetaBatchLoader extends SpringBatchLoader {
	
	public String NAME = "MetaBatchLoader";
	public String VERSION = "1.1";
	public String DESCRIPTION = "NCI MetaThesaurus Spring Batch Loader";
	
	/**
	 * Load meta.
	 * 
	 * @param rrfDir the rrf dir
	 * 
	 * @return the job execution
	 * 
	 * @throws Exception the exception
	 */
	public void loadMeta(URI rrfDir) throws Exception;

	/**
	 * Resume meta.
	 * 
	 * @param rrfDir the rrf dir
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the job execution
	 * 
	 * @throws Exception the exception
	 */
	public void resumeMeta(URI rrfDir, String uri, String version) throws Exception;

	public void removeLoad(String uri, String version) throws LBParameterException;	

}
