
package org.lexevs.dao.indexer.api;

import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

import org.apache.lucene.index.IndexNotFoundException;
import org.lexevs.dao.indexer.lucene.Index;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.dao.indexer.utility.Utility;

/**
 * This class will sit on top of multiple indexes, and manage them for you.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class IndexerService {
    private File rootLocation_; // The root directory of the indexes
    private Hashtable<String, Index> indexes_; // will hold all of the current indexes

    private ConcurrentMetaData concurrentMetaData;

    /**
     * Create an indexer service on a directory. A Indexer Service can contain
     * multiple indexes.
     * 
     * @param rootLocation
     *            The directory where all of your indexes are located
     * @param configureLog4j
     *            Whether or not to configure a log4j appender.
     * @throws RuntimeException
     */
    public IndexerService(String rootLocation, boolean configureLog4j) throws RuntimeException {
        initServices(rootLocation, configureLog4j);
        concurrentMetaData = ConcurrentMetaData.getInstance();
    }

    /**
     * Create an indexer service on a directory. A Indexer Service can contain
     * multiple indexes.
     * 
     * @param rootLocation
     *            The directory where all of your indexes are located
     * @throws RuntimeException
     */
    public IndexerService(String rootLocation) throws RuntimeException {
        initServices(rootLocation, false);
        concurrentMetaData = ConcurrentMetaData.getInstance();
    }

    private void initServices(String rootLocation, boolean configureLog4j) throws RuntimeException {

        indexes_ = new Hashtable<String, Index>();
        File root = new File(rootLocation);
        this.rootLocation_ = root;
        if (root.exists()) {
            loadIndexes();
        } else {
            root.mkdir();
        }

        initMetaData(root);

    }

    public void refreshAvailableIndexes() throws RuntimeException {
        File[] files = rootLocation_.listFiles();
        HashSet<String> fileNames = new HashSet<String>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                fileNames.add(files[i].getName());

                // add new ones
                if (!indexes_.contains(files[i].getName())) {
                    Index temp = new Index(files[i]);
                    indexes_.put(files[i].getName(), temp);
                }
            }
        }

        // remove ones that no longer exist
        Enumeration<String> indexes = indexes_.keys();
        while (indexes.hasMoreElements()) {
            String indexName = (String) indexes.nextElement();
            if (!fileNames.contains(indexName)) {
                indexes_.remove(indexName);
            }
        }
        //Reopen readers after load
        refreshIndexReadersInConcurrentMetaDataList(fileNames);        
        concurrentMetaData.refreshIterator();
    }

	private void refreshIndexReadersInConcurrentMetaDataList(
			HashSet<String> fileNames) {
		for (String s : fileNames) {
			CodingSchemeMetaData metadata = concurrentMetaData.getIndexMetaDataForFileName(s);
			if(metadata != null && metadata.getDirectory().getIndexReader().maxDoc() == 0)
			{
			metadata.getDirectory()
					.refresh();
			}
		}
	}

    /**
     * Delete an index from this indexerService.
     * 
     * @param indexName
     * @throws IndexNotFoundException
     */
    public void deleteIndex(String indexName) throws RuntimeException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new RuntimeException("The index " + indexName + " does not exist.");
        }
        try {
            File temp = currentIndex.getLocation();
            Utility.deleteRecursive(temp);
            indexes_.remove(indexName);
            currentIndex = null;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not remove the index.. Is another process accessing the files? " + e);
        }
    }

    private void loadIndexes() {
        File[] files = rootLocation_.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                Index temp = new Index(files[i]);
                indexes_.put(files[i].getName(), temp);
            }
        }
    }

    private void initMetaData(File root) throws RuntimeException {
       ConcurrentMetaData.getInstance();
    }

    public String getRootLocation() {
        return rootLocation_.getAbsolutePath();
    }

    public ConcurrentMetaData getMetaData() {
        return concurrentMetaData;
    }

}