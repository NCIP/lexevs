
package org.lexevs.dao.database.service.event.revision;

/**
 * The Class ReviseEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ReviseEvent<T> {
	
	/** The original. */
	private T original;
	
	/** The changed. */
	private T changed;

	/**
	 * Gets the original.
	 * 
	 * @return the original
	 */
	public T getOriginal() {
		return original;
	}
	
	/**
	 * Sets the original.
	 * 
	 * @param original the new original
	 */
	public void setOriginal(T original) {
		this.original = original;
	}
	
	/**
	 * Gets the changed.
	 * 
	 * @return the changed
	 */
	public T getChanged() {
		return changed;
	}
	
	/**
	 * Sets the changed.
	 * 
	 * @param changed the new changed
	 */
	public void setChanged(T changed) {
		this.changed = changed;
	}
}