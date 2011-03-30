package org.cts2.uri.restriction;

import java.util.HashSet;
import java.util.Set;

import org.cts2.uri.MapVersionDirectoryURI;
import org.cts2.uri.restriction.EntityDirectoryRestrictionState.RestrictToCodeSystemVersionsRestriction;

/**
 * The Class MapVersionDirectoryRestrictionState
 * 
 * @author <a href="mailto:lian.zonghui@mayo.edu">Zonghui Lian</a>
 * 
 */
public class MapVersionDirectoryRestrictionState extends
		RestrictionState<MapVersionDirectoryURI> {

	private Set<RestrictToCodeSystemVersionsRestriction> restrictToCodeSystemVersionsRestrictions = new HashSet<RestrictToCodeSystemVersionsRestriction>();
	
}
