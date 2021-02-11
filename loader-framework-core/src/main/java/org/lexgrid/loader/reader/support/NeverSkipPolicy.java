
package org.lexgrid.loader.reader.support;

/**
 * The Class NeverSkipPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NeverSkipPolicy implements SkipPolicy {

	/**
	 * Utility Class -- use when you never want to skip any records;.
	 * 
	 * @param item the item
	 * 
	 * @return true, if to skip
	 */
	public boolean toSkip(Object item) {
		return false;
	}
}