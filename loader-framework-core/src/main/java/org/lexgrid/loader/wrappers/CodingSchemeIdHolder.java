
package org.lexgrid.loader.wrappers;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;

public class CodingSchemeIdHolder<T> {
	
	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	private T item;
	
	public CodingSchemeIdHolder(){
		super();
	}
	
	public CodingSchemeIdHolder(CodingSchemeIdSetter codingSchemeIdSetter, T item) {
		super();
		this.setCodingSchemeIdSetter(codingSchemeIdSetter);
		this.setItem(item);
	}

	public void setItem(T item) {
		this.item = item;
	}

	public T getItem() {
		return item;
	}

	public void setCodingSchemeIdSetter(CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}

	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}


}