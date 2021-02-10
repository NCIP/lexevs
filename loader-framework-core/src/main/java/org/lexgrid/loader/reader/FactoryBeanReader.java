
package org.lexgrid.loader.reader;

import java.util.ArrayList;
import java.util.List;

import org.lexgrid.loader.reader.support.NeverSkipPolicy;
import org.lexgrid.loader.reader.support.SkipPolicy;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class FactoryBeanReader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class FactoryBeanReader<T> implements FactoryBean, InitializingBean{
	
	/** The delegate. */
	private ItemReader<T> delegate;
	
	/** The return list. */
	private List<T> returnList = new ArrayList<T>();
	
	/** The skip policy. */
	private SkipPolicy<T> skipPolicy = new NeverSkipPolicy();

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		for(T item = delegate.read(); item != null; item = delegate.read()){
			if(!skipPolicy.toSkip(item)){
				returnList.add(item);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return List.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Gets the delegate.
	 * 
	 * @return the delegate
	 */
	public ItemReader<T> getDelegate() {
		return delegate;
	}

	/**
	 * Sets the delegate.
	 * 
	 * @param delegate the new delegate
	 */
	public void setDelegate(ItemReader<T> delegate) {
		this.delegate = delegate;
	}

	/**
	 * Gets the skip policy.
	 * 
	 * @return the skip policy
	 */
	public SkipPolicy<T> getSkipPolicy() {
		return skipPolicy;
	}

	/**
	 * Sets the skip policy.
	 * 
	 * @param skipPolicy the new skip policy
	 */
	public void setSkipPolicy(SkipPolicy<T> skipPolicy) {
		this.skipPolicy = skipPolicy;
	}
}