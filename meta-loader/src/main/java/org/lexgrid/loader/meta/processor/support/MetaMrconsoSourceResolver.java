
package org.lexgrid.loader.meta.processor.support;

import org.lexgrid.loader.processor.support.SourceResolver;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class MetaSourceMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaMrconsoSourceResolver implements SourceResolver<Mrconso>{

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