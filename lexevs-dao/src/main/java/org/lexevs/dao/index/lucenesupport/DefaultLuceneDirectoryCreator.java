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
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang.SystemUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;

/**
 * The Class DefaultLuceneDirectoryCreator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultLuceneDirectoryCreator implements LuceneDirectoryCreator{
	
	@Override
	public NamedDirectory getDirectory(String indexName, File baseDirectory) {
		Directory directory;
		try {
			Path path = Paths.get(baseDirectory.toPath().toString(), indexName);
			
			if(SystemUtils.IS_OS_WINDOWS){
				directory = FSDirectory.open(path); //getDirectory(baseDirectory);
			} else {
				directory = MMapDirectory.open(path); //getDirectory(baseDirectory);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new NamedDirectory(
				directory, indexName);
	}
}
