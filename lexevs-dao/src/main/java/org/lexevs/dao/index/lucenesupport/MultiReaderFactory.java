
package org.lexevs.dao.index.lucenesupport;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author <A HREF="mailto:bauer.scott@mayo.edu">Scott Bauer </A>
 *
 */
public class MultiReaderFactory implements FactoryBean<MultiReader> {

	ConcurrentMetaData concurrentMetaData;
	
	public MultiReaderFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MultiReader getObject() throws Exception {
		List<IndexReader> readers = new ArrayList<IndexReader>();
		
		for(CodingSchemeMetaData md : concurrentMetaData.getCodingSchemeList()) {
			readers.add(md.getDirectory().getIndexReader());
		}
		return new MultiReader(readers.toArray(new IndexReader[readers.size()]), false);
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

	public ConcurrentMetaData getConcurrentMetaData() {
		return concurrentMetaData;
	}

	public void setConcurrentMetaData(ConcurrentMetaData concurrentMetaData) {
		this.concurrentMetaData = concurrentMetaData;
	}
	
	

}