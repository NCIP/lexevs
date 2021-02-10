
package org.lexevs.dao.database.hibernate.prefix;

import org.hibernate.EmptyInterceptor;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.dao.database.prefix.PrefixResolver;

/**
 * Hibernate Interceptor used to modify the SQL query sent to the database.
 * This interceptor changes the prefix, and also places some extra constraints
 * on the query to ensure that critical queries always use DB table indexes.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixInterceptor extends EmptyInterceptor {  

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1940273682945001115L;

	/** The PREFI x_ placeholder. */
	public static String PREFIX_PLACEHOLDER = DatabaseConstants.PREFIX_PLACEHOLDER;
	
	/** The prefix. */
	private PrefixResolver prefixResolver;
	
	/**
	 * Instantiates a new prefix interceptor.
	 */
	public PrefixInterceptor(){
		super();
	}
	
	/**
	 * Instantiates a new prefix interceptor.
	 * 
	 * @param prefixResolver the prefix resolver
	 */
	public PrefixInterceptor(PrefixResolver prefixResolver){
		this.prefixResolver = prefixResolver;
	}
	
	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onPrepareStatement(java.lang.String)
	 */
	public String onPrepareStatement(String sql) { 	
		sql = sql.replaceAll(PREFIX_PLACEHOLDER, prefixResolver.resolveDefaultPrefix());
	
		return sql;			
	}


	/**
	 * Sets the prefix resolver.
	 * 
	 * @param prefixResolver the new prefix resolver
	 */
	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	/**
	 * Gets the prefix resolver.
	 * 
	 * @return the prefix resolver
	 */
	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}
}