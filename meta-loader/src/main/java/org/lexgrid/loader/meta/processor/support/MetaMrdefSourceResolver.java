
package org.lexgrid.loader.meta.processor.support;

import org.lexgrid.loader.processor.support.SourceResolver;
import org.lexgrid.loader.rrf.model.Mrdef;

/**
 * The Class MetaMrdefSourceMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaMrdefSourceResolver implements SourceResolver<Mrdef>{

	public String getRole(Mrdef item) {
		return null;
	}

	public String getSource(Mrdef item) {
		return item.getSab();
	}

	public String getSubRef(Mrdef item) {
		return null;
	}
}