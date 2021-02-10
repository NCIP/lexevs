
package org.lexevs.dao.test;

import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory;

/**
 * A factory for creating InMemoryDirectory objects.
 */
public class InMemoryDirectoryFactory extends LuceneDirectoryFactory {
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory#getObject()
	 */
	@Override
	public Object getObject() throws Exception {
		
		return this.getLuceneDirectoryCreator().getDirectory(this.getIndexName(), null);
	}
}