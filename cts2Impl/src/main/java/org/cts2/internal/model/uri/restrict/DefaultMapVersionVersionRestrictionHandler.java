package org.cts2.internal.model.uri.restrict;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.match.AttributeResolver;
import org.cts2.internal.match.ResolvableModelAttributeReference;
import org.cts2.uri.MapVersionDirectoryURI;

public class DefaultMapVersionVersionRestrictionHandler
		extends
		AbstractIterableLexEvsBackedRestrictionHandler<CodingSchemeRendering, MapVersionDirectoryURI> {

	private LexEvsIdentityConverter lexEvsIdentityConverter;

	@Override
	public List<ResolvableModelAttributeReference<CodingSchemeRendering>> registerSupportedModelAttributeReferences() {
		List<ResolvableModelAttributeReference<CodingSchemeRendering>> returnList = new ArrayList<ResolvableModelAttributeReference<CodingSchemeRendering>>();

		ResolvableModelAttributeReference<CodingSchemeRendering> mapVersionName = new ResolvableModelAttributeReference<CodingSchemeRendering>(
				new MapVersionNameAttributeResolver());

		returnList.add(mapVersionName);

		return returnList;
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

	/**
	 * The Class MapVersionNameAttributeResolver.
	 * 
	 * @author <a href="mailto:lian.zonghui@mayo.edu">Zonghui Lian</a>
	 */
	private class MapVersionNameAttributeResolver implements
			AttributeResolver<CodingSchemeRendering> {
		private static final long serialVersionUID = -6257292429464806843L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.cts2.internal.match.AttributeResolver#resolveAttribute(java.lang
		 * .Object)
		 */
		@Override
		public String resolveAttribute(CodingSchemeRendering modelObject) {

			return lexEvsIdentityConverter
					.codingSchemeSummaryToMapVersionName(modelObject
							.getCodingSchemeSummary());
		}
	}

}