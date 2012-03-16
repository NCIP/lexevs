package org.lexgrid.loader.rxn.processor.support;

import org.lexgrid.loader.processor.support.SourceResolver;
import org.lexgrid.loader.rrf.model.Mrconso;

public class RxnMrconsoSourceResolver implements SourceResolver<Mrconso>{
	
	public String getRole(Mrconso item) {
		return null;
	}

	public String getSource(Mrconso item) {
		return item.getSab();
	}

	public String getSubRef(Mrconso item) {
		return null;
	}
}
