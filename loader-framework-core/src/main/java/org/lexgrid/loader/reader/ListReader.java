
package org.lexgrid.loader.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The Class ListReader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ListReader<T> implements ItemReader<T>, InitializingBean {

	/** The list. */
	private List<T> list;
	
	/** The list iterator. */
	private Iterator<T> listIterator;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	public T read() throws Exception, UnexpectedInputException, ParseException {
		if(listIterator.hasNext()){
			return listIterator.next();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(list);
		listIterator = list.iterator();
	}

	/**
	 * Gets the list.
	 * 
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * Sets the list.
	 * 
	 * @param list the new list
	 */
	public void setList(List<T> list) {
		this.list = list;
	}
}