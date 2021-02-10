
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;

/**
 * The Interface ValueSetDefinitionLoader.
 * 
 * @author <a href="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</a>
 */
public interface ResolvedValueSetDefinitionLoader  extends Loader {
	
	public static String NAME = "ResolvedValueSetDefinitionLoader";
	public static String VERSION = "1.0";
	public static String DESCRIPTION = "This loader loads the Resolved Value Set Definition  into the LexGrid database.";
	
	
	/* @param valueSetDefinitionURI - the URI of the value set definition
	 * @param valueSetDefintionRevisionId - the version of the value set definition
	 * @param csVersionList - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in the service.  If absent, the most recent version will be used instead.
	 * @param csVersionTag - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present. Note that non-tagged versions will be used if the tagged version is missing.
	 */

	public void load(URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, AbsoluteCodingSchemeVersionReferenceList csVersionList, String csVersionTag, String version) throws Exception;

	public void validate(URI valueSetDefinitionURI, int validationLevel) throws Exception;

	

}