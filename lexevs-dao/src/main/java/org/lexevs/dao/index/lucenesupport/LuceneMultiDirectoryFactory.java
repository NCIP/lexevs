package org.lexevs.dao.index.lucenesupport;

import java.io.File;
import java.io.IOException;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.springframework.core.io.Resource;

public class LuceneMultiDirectoryFactory {
	
	
	public Resource getIndexDirectory() {
		return indexDirectory;
	}

	public void setIndexDirectory(Resource indexDirectory) {
		this.indexDirectory = indexDirectory;
	}

	public LuceneDirectoryCreator getLuceneDirectoryCreator() {
		return luceneDirectoryCreator;
	}

	public void setLuceneDirectoryCreator(
			LuceneDirectoryCreator luceneDirectoryCreator) {
		this.luceneDirectoryCreator = luceneDirectoryCreator;
	}

	private Resource indexDirectory;
	
	private LuceneDirectoryCreator luceneDirectoryCreator;
	
	public LuceneMultiDirectoryFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public NamedDirectory getNamedDirectory(String indexName) throws IOException{
		return luceneDirectoryCreator.getDirectory(indexName, indexDirectory.getFile());
	}
	
	public CodingSchemeMetaData getCodingSchemeMetaData(String indexName,
			AbsoluteCodingSchemeVersionReference ref, 
			boolean norm, 
			boolean dMphone, 
			boolean start, 
			boolean finish
			) throws IOException{
		return new CodingSchemeMetaData(
				ref.getCodingSchemeURN(), 
				ref.getCodingSchemeVersion(), 
				norm, dMphone, start, finish,
				getNamedDirectory(indexName));
		
	}

}
