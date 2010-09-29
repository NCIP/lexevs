package org.lexevs.dao.index.lucenesupport;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

public class LuceneDirectoryFactory implements FactoryBean {

	private String indexName;
	
	private Resource indexDirectory;

	@Override
	public Object getObject() throws Exception {
		Directory directory = FSDirectory.getDirectory(this.indexDirectory.getFile());

		return new NamedDirectory(
				directory, indexName);
	}

	@Override
	public Class getObjectType() {
		return NamedDirectory.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexDirectory(Resource indexDirectory) {
		this.indexDirectory = indexDirectory;
	}

	public Resource getIndexDirectory() {
		return indexDirectory;
	}
	
	public static class NamedDirectory {
		private Directory directory;
		private String indexName;
		private IndexSearcher indexSearcher;
		private IndexReader indexReader;

		public NamedDirectory(Directory directory, String indexName) {
			super();
			this.directory = directory;
			this.indexName = indexName;
			try {
				this.initIndexDirectory(directory);
				this.indexReader = this.createIndexReader(directory);
				this.indexSearcher = this.createIndexSearcher(this.indexReader);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		public Directory getDirectory() {
			return directory;
		}
		public void setDirectory(Directory directory) {
			this.directory = directory;
		}
		public String getIndexName() {
			return indexName;
		}
		public void setIndexName(String indexName) {
			this.indexName = indexName;
		}
		
		public IndexSearcher getIndexSearcher() {
			return indexSearcher;
		}
		public void setIndexSearcher(IndexSearcher indexSearcher) {
			this.indexSearcher = indexSearcher;
		}
		public IndexReader getIndexReader() {
			return indexReader;
		}
		public void setIndexReader(IndexReader indexReader) {
			this.indexReader = indexReader;
		}
		
		protected IndexSearcher createIndexSearcher(IndexReader indexReader) throws Exception {
			return new IndexSearcher(indexReader);
		}

		protected IndexReader createIndexReader(Directory directory) throws Exception {
			IndexReader reader = IndexReader.open(directory, true);
			return reader;
		}

		private void initIndexDirectory(Directory directory) throws IOException,
			CorruptIndexException, LockObtainFailedException {

			if(!IndexReader.indexExists(directory)){
				IndexWriter writer = new IndexWriter(
						directory, 
						LuceneLoaderCode.getAnaylzer(), 
						IndexWriter.MaxFieldLength.UNLIMITED);

				writer.close();
			}
		}
		
		public void refresh() {
			try {
				IndexReader reopenedReader = this.indexReader.reopen();
				
				if(reopenedReader != this.indexReader){
					this.indexReader.close();
					this.indexReader = this.createIndexReader(this.directory);
				}
				
				this.indexSearcher.close();
				this.indexSearcher = this.createIndexSearcher(this.indexReader);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public void remove() {
			try {
				this.indexSearcher.close();
				this.indexReader.close();

				if(this.directory instanceof FSDirectory) {
					FSDirectory dir = (FSDirectory)this.directory;
					FileUtils.deleteDirectory(dir.getFile());
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
