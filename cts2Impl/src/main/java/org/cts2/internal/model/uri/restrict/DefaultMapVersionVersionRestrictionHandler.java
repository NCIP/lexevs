package org.cts2.internal.model.uri.restrict;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.internal.match.OperationExecutingModelAttributeReference;
import org.cts2.internal.match.ResolvableModelAttributeReference;
import org.cts2.uri.CodeSystemVersionDirectoryURI;
import org.cts2.uri.MapVersionDirectoryURI;
import org.cts2.uri.restriction.CodeSystemVersionRestrictionState.RestrictToEntitiesRestriction;

public class DefaultMapVersionVersionRestrictionHandler
		extends
		AbstractIterableLexEvsBackedRestrictionHandler<CodingSchemeRendering, MapVersionDirectoryURI> {

	private LexBIGService lexBigService;

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