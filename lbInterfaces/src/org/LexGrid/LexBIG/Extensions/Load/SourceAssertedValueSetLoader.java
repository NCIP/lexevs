/**
 * 
 */
package org.LexGrid.LexBIG.Extensions.Load;

import org.LexGrid.concepts.Entity;

/**
 * The Interface SourceAssertedValueSetLoader.
 * 
 * @author <a href="mailto:bauer.scott@mayo.edu">Scott Bauer</a>
 */
public interface SourceAssertedValueSetLoader extends Loader {
	public static String NAME = "SourceAssertedValueSetLoader";
	public static String VERSION = "1.0";
	public static String DESCRIPTION = "This loader loads Value Set Definitions defined by a source association into the LexGrid database.";
	
	
	/* @param valueSetDefinitionURI - the URI of the value set definition
	 * @param valueSetDefintionRevisionId - the version of the value set definition
	 * @param csVersionList - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in the service.  If absent, the most recent version will be used instead.
	 * @param csVersionTag - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present. Note that non-tagged versions will be used if the tagged version is missing.
	 */

	public void load(String vsAssociationName) throws Exception;

	public void validate(Entity entity) throws Exception;
}
