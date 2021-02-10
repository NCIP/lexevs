
package org.lexgrid.loader.wrappers;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;

public class ParentIdHolder<T> extends CodingSchemeIdHolder<T> {

	private String parentId;

	public ParentIdHolder(){
		super();
	}

	public ParentIdHolder(CodingSchemeIdSetter codingSchemeIdSetter, String parentId, T item) {
		super(codingSchemeIdSetter, item);
		this.setParentId(parentId);
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentId() {
		return parentId;
	}
}