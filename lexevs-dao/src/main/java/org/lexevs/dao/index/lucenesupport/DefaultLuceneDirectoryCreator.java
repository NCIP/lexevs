
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