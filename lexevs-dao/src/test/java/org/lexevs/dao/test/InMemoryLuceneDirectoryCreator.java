
package org.lexevs.dao.test;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryCreator;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.system.constants.SystemVariables;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The Class InMemoryLuceneDirectoryCreator.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InMemoryLuceneDirectoryCreator implements LuceneDirectoryCreator {

	private static final Object MUTEX = new Object();

	private static Map<String,Directory> map = new HashMap<String,Directory>();

	public static void clearAll() {
		String indexName = SystemVariables.getMetaDataIndexName();

		for(Map.Entry<String, Directory> entry : new HashSet<Map.Entry<String, Directory>>(map.entrySet())) {
			if(! entry.getKey().equals(indexName)) {
				doRemove(entry.getKey());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucenesupport.LuceneDirectoryCreator#getDirectory(java.lang.String, java.io.File)
	 */
	@Override
	public NamedDirectory getDirectory(final String indexName, File baseDirectory) {

		final Directory indexDirectory;
		synchronized (MUTEX) {
			if (!this.map.containsKey(indexName)) {
				Directory directory = new RAMDirectory();

				this.map.put(indexName, directory);
			}

			indexDirectory = this.map.get(indexName);
		}

		return new NamedDirectory(indexDirectory, indexName) {

			public void remove() {
				doRemove(indexName);
				super.remove();
			}
		};
	}

	private static void doRemove(String indexName) {
		synchronized (MUTEX) {
			try {
				map.get(indexName).close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			map.remove(indexName);
		}
	}
}