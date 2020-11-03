package org.lexgrid.loader.rrf.data.association;

import org.lexgrid.loader.data.association.AssociationInstanceIdResolver;
import org.lexgrid.loader.rrf.model.Mrsat;

public class MrsatRuiAssociationInstanceIdResolver implements AssociationInstanceIdResolver<Mrsat> {

	@Override
	public String resolveAssociationInstanceId(Mrsat key) {
		return key.getMetaui();
	}

}
