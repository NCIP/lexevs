
package org.lexgrid.valuesets.dto;

import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Class to hold list of resolved pick list entries.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
@SuppressWarnings("serial")
@LgClientSideSafe
public class ResolvedPickListEntryList implements java.io.Serializable {

	// --------------------------/
	// - Class/Member Variables -/
	// --------------------------/
	
	/**
	 * Field _ResolvedPickListEntryList.
	 */
	private java.util.List<org.lexgrid.valuesets.dto.ResolvedPickListEntry> _resolvedPickListEntryList;

	// ----------------/
	// - Constructors -/
	// ----------------/

	public ResolvedPickListEntryList() {
		super();
		this._resolvedPickListEntryList = new java.util.ArrayList<org.lexgrid.valuesets.dto.ResolvedPickListEntry>();
	}

	// -----------/
	// - Methods -/
	// -----------/

	/**
	 * 
	 * 
	 * @param vResolvedPickListEntry
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if the index given is outside the bounds of the collection
	 */
	public void addResolvedPickListEntry(
			final org.lexgrid.valuesets.dto.ResolvedPickListEntry vResolvedPickListEntry)
			throws java.lang.IndexOutOfBoundsException {
		this._resolvedPickListEntryList.add(vResolvedPickListEntry);
	}

	/**
	 * 
	 * 
	 * @param index
	 * @param vResolvedPickListEntry
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if the index given is outside the bounds of the collection
	 */
	public void addResolvedPickListEntry(
			final int index,
			final org.lexgrid.valuesets.dto.ResolvedPickListEntry vResolvedPickListEntry)
			throws java.lang.IndexOutOfBoundsException {
		this._resolvedPickListEntryList.add(index, vResolvedPickListEntry);
	}

	/**
	 * Method enumerateResolvedPickListEntry.
	 * 
	 * @return an Enumeration over all possible elements of this collection
	 */
	public java.util.Enumeration<org.lexgrid.valuesets.dto.ResolvedPickListEntry> enumerateResolvedPickListEntry() {
		return java.util.Collections.enumeration(this._resolvedPickListEntryList);
	}

	/**
	 * Method getResolvedPickListEntry.
	 * 
	 * @param index
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if the index given is outside the bounds of the collection
	 * @return the value of the
	 *         org.LexGrid.LexBIG.DataModel.Core.ResolvedPickListEntry at the given
	 *         index
	 */
	public org.lexgrid.valuesets.dto.ResolvedPickListEntry getResolvedPickListEntry(
			final int index) throws java.lang.IndexOutOfBoundsException {
		// check bounds for index
		if (index < 0 || index >= this._resolvedPickListEntryList.size()) {
			throw new IndexOutOfBoundsException(
					"getResolvedPickListEntry: Index value '" + index
							+ "' not in range [0.."
							+ (this._resolvedPickListEntryList.size() - 1) + "]");
		}

		return (org.lexgrid.valuesets.dto.ResolvedPickListEntry) _resolvedPickListEntryList
				.get(index);
	}

	/**
	 * Method getResolvedPickListEntry.Returns the contents of the collection in an
	 * Array.
	 * <p>
	 * Note: Just in case the collection contents are changing in another
	 * thread, we pass a 0-length Array of the correct type into the API call.
	 * This way we <i>know</i> that the Array returned is of exactly the correct
	 * length.
	 * 
	 * @return this collection as an Array
	 */
	public org.lexgrid.valuesets.dto.ResolvedPickListEntry[] getResolvedPickListEntry() {
		org.lexgrid.valuesets.dto.ResolvedPickListEntry[] array = new org.lexgrid.valuesets.dto.ResolvedPickListEntry[0];
		return (org.lexgrid.valuesets.dto.ResolvedPickListEntry[]) this._resolvedPickListEntryList
				.toArray(array);
	}

	/**
	 * Method getResolvedPickListEntryCount.
	 * 
	 * @return the size of this collection
	 */
	public int getResolvedPickListEntryCount() {
		return this._resolvedPickListEntryList.size();
	}

	/**
	 * Method iterateResolvedPickListEntry.
	 * 
	 * @return an Iterator over all possible elements in this collection
	 */
	public java.util.Iterator<org.lexgrid.valuesets.dto.ResolvedPickListEntry> iterateResolvedPickListEntry() {
		return this._resolvedPickListEntryList.iterator();
	}

	/**
     */
	public void removeAllResolvedPickListEntry() {
		this._resolvedPickListEntryList.clear();
	}

	/**
	 * Method removeResolvedPickListEntry.
	 * 
	 * @param vResolvedPickListEntry
	 * @return true if the object was removed from the collection.
	 */
	public boolean removeResolvedPickListEntry(
			final org.lexgrid.valuesets.dto.ResolvedPickListEntry vResolvedPickListEntry) {
		boolean removed = _resolvedPickListEntryList.remove(vResolvedPickListEntry);
		return removed;
	}

	/**
	 * Method removeResolvedPickListEntryAt.
	 * 
	 * @param index
	 * @return the element removed from the collection
	 */
	public org.lexgrid.valuesets.dto.ResolvedPickListEntry removeResolvedPickListEntryAt(
			final int index) {
		java.lang.Object obj = this._resolvedPickListEntryList.remove(index);
		return (org.lexgrid.valuesets.dto.ResolvedPickListEntry) obj;
	}

	/**
	 * 
	 * 
	 * @param index
	 * @param vResolvedPickListEntry
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if the index given is outside the bounds of the collection
	 */
	public void setResolvedPickListEntry(
			final int index,
			final org.lexgrid.valuesets.dto.ResolvedPickListEntry vResolvedPickListEntry)
			throws java.lang.IndexOutOfBoundsException {
		// check bounds for index
		if (index < 0 || index >= this._resolvedPickListEntryList.size()) {
			throw new IndexOutOfBoundsException(
					"setResolvedPickListEntry: Index value '" + index
							+ "' not in range [0.."
							+ (this._resolvedPickListEntryList.size() - 1) + "]");
		}

		this._resolvedPickListEntryList.set(index, vResolvedPickListEntry);
	}

	/**
	 * 
	 * 
	 * @param vResolvedPickListEntryArray
	 */
	public void setResolvedPickListEntry(
			final org.lexgrid.valuesets.dto.ResolvedPickListEntry[] vResolvedPickListEntryArray) {
		// -- copy array
		_resolvedPickListEntryList.clear();

		for (int i = 0; i < vResolvedPickListEntryArray.length; i++) {
			this._resolvedPickListEntryList.add(vResolvedPickListEntryArray[i]);
		}
	}
}