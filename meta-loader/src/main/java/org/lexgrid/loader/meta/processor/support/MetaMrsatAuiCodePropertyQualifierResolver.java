
package org.lexgrid.loader.meta.processor.support;

import org.lexgrid.loader.processor.support.SourceResolver;
import org.lexgrid.loader.rrf.model.Mrsat;

public class MetaMrsatAuiCodePropertyQualifierResolver implements
		SourceResolver<Mrsat> {

	public String getRole(Mrsat item) {
		return null;
	}

	public String getSource(Mrsat item) {
		return item.getSab();
	}

	public String getSubRef(Mrsat item) {
		return null;
	}
}