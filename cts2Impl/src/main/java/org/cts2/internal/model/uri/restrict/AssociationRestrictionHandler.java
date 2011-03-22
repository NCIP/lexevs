/**
 * 
 */
package org.cts2.internal.model.uri.restrict;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.cts2.core.VersionTagReference;
import org.cts2.service.core.NameOrURI;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public interface AssociationRestrictionHandler extends
		NonIterableBasedResolvingRestrictionHandler<CodedNodeGraph> {
	
	/**
	 * Restrict to code systems.
	 *
	 * @param codeSystems the code systems
	 * @param tag the tag
	 * @return the restriction
	 */
	public Restriction<CodedNodeGraph> restrictToCodeSystems(NameOrURI codeSystems, VersionTagReference tag);
	
	/**
	 * Restrict to code system versions.
	 *
	 * @param codeSystemVersions the code system versions
	 * @return the restriction
	 */
	public Restriction<CodedNodeGraph> restrictToCodeSystemVersions(NameOrURI codeSystemVersions);

}
