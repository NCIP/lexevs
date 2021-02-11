
package org.lexevs.dao.database.key.incrementer;

/**
 * The Class AbstractBigIntKeyIncrementer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBigIntKeyIncrementer extends AbstractKeyIncrementer {

	/** The SIZE. */
	private static int SIZE = 20;
	
	/** The NAME. */
	private static String NAME = "SEQUENTIAL_INTEGER";

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#getKeyType()
	 */
	@Override
	public KeyType getKeyType() {
		return KeyType.BIGINT;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#valueOf(java.lang.String)
	 */
	@Override
	public Object valueOf(String key) {
		return Long.parseLong(key);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#stringValue(java.lang.Object)
	 */
	@Override
	public String stringValue(Object key) {
		if(key == null) {return null;}
		return Long.toString((Long)key);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#getKeyLength()
	 */
	@Override
	public int getKeyLength() {
		return SIZE;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#getName()
	 */
	@Override
	public String getName() {
		return NAME;
	}
}