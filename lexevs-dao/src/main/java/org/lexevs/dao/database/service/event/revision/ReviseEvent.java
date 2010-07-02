package org.lexevs.dao.database.service.event.revision;

public class ReviseEvent<T> {
	
	private T original;
	private T changed;

	public T getOriginal() {
		return original;
	}
	public void setOriginal(T original) {
		this.original = original;
	}
	public T getChanged() {
		return changed;
	}
	public void setChanged(T changed) {
		this.changed = changed;
	}
}