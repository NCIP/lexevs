
package org.lexgrid.loader.meta.reader.support;

import org.lexgrid.loader.reader.support.SkipPolicy;
import org.lexgrid.loader.rrf.model.Mrsat;

public class MetaMrsatAssQualSkipPolicy implements SkipPolicy<Mrsat> {

	@Override
	public boolean toSkip(Mrsat item) {
		if(!item.getStype().equals("RUI")) {
			return true;
		}
		
		return false;
	}

}