package org.cts2.internal.model.uri.restrict;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.internal.match.ResolvableModelAttributeReference;
import org.cts2.uri.MapVersionDirectoryURI;

public class DefaultMapVersionVersionRestrictionHandler
		extends
		AbstractIterableLexEvsBackedRestrictionHandler<CodingSchemeRendering, MapVersionDirectoryURI> {


	@Override
	public List<ResolvableModelAttributeReference<CodingSchemeRendering>> registerSupportedModelAttributeReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IterableRestriction<CodingSchemeRendering>> processOtherRestictions(
			MapVersionDirectoryURI directoryUri) {
		List<IterableRestriction<CodingSchemeRendering>> returnList = new ArrayList<IterableRestriction<CodingSchemeRendering>>();

		// TODO: Wait for the feedback of MapVersion restrictions from Harold

		return returnList;
	}

	@Override
	public List<MatchAlgorithmReference> registerSupportedMatchAlgorithmReferences() {
		// TODO Auto-generated method stub
		return null;
	}

}