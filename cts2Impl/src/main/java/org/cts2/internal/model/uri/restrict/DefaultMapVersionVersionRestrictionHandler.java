package org.cts2.internal.model.uri.restrict;

import java.util.List;

import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.internal.match.ResolvableModelAttributeReference;
import org.cts2.uri.MapVersionDirectoryURI;

public class DefaultMapVersionVersionRestrictionHandler extends AbstractIterableLexEvsBackedRestrictionHandler<Mapping, MapVersionDirectoryURI> {
	
	@Override
	public List<ResolvableModelAttributeReference<Mapping>> registerSupportedModelAttributeReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IterableRestriction<Mapping>> processOtherRestictions(
			MapVersionDirectoryURI directoryUri) {
		//directoryUri.getRestrictionState().
		return null;
	}

	@Override
	public List<MatchAlgorithmReference> registerSupportedMatchAlgorithmReferences() {
		// TODO Auto-generated method stub
		return null;
	}

}
