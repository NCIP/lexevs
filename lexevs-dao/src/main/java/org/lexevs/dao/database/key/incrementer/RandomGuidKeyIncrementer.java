
package org.lexevs.dao.database.key.incrementer;

import java.util.Arrays;
import java.util.List;

import org.lexevs.dao.database.key.Java5UUIDKeyGenerator;
import org.lexevs.dao.database.type.DatabaseType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

/**
 * The Class RandomGuidKeyIncrementer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RandomGuidKeyIncrementer extends AbstractKeyIncrementer {

	/** The SIZE. */
	private static int SIZE = 36;
	
	/** The NAME. */
	private static String NAME = "GUID";

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#getKeyType()
	 */
	@Override
	public KeyType getKeyType() {
		return KeyType.VARCHAR;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#valueOf(java.lang.String)
	 */
	@Override
	public Object valueOf(String key) {
		return key;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#stringValue(java.lang.Object)
	 */
	@Override
	public String stringValue(Object key) {
		if(key == null) {return null;}
		return key.toString();
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

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.AbstractKeyIncrementer#createDataFieldMaxValueIncrementer()
	 */
	@Override
	protected DataFieldMaxValueIncrementer createDataFieldMaxValueIncrementer() {
		return new RandomGuidIncrementer();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#destroy()
	 */
	@Override
	public void destroy() {
		// no-op
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#initialize()
	 */
	@Override
	public void initialize() {
		// no-op
	}

	/**
	 * The Class RandomGuidIncrementer.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class RandomGuidIncrementer implements DataFieldMaxValueIncrementer {

		/* (non-Javadoc)
		 * @see org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer#nextIntValue()
		 */
		@Override
		public int nextIntValue() throws DataAccessException {
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer#nextLongValue()
		 */
		@Override
		public long nextLongValue() throws DataAccessException {
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer#nextStringValue()
		 */
		@Override
		public String nextStringValue() throws DataAccessException {
			return Java5UUIDKeyGenerator.getRandomUUID().toString();
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.AbstractKeyIncrementer#getSupportedDatabaseTypes()
	 */
	@Override
	protected List<DatabaseType> getSupportedDatabaseTypes() {
		return Arrays.asList(DatabaseType.values());
	}
}