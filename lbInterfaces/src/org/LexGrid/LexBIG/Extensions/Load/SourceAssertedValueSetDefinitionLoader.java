/**
 * 
 */
package org.LexGrid.LexBIG.Extensions.Load;

import org.LexGrid.concepts.Entity;

/**
 * The Interface SourceAssertedValueSetLoader.
 * 
 * This interface applies to those terminologies that build lists of value sets
 * which are defined by an association.  Loading value sets
 * 
 * @author <a href="mailto:bauer.scott@mayo.edu">Scott Bauer</a>
 */
public interface SourceAssertedValueSetDefinitionLoader extends Loader {
	public static String NAME = "SourceAssertedValueSetLoader";
	public static String VERSION = "1.0";
	public static String DESCRIPTION = "This loader loads Value Set Definitions defined by a source association into the LexGrid database.";
	
	
	/* @param vsAssociationName - The name of the association that asserts a vs relationship.  Cannot be null
	 * @param codingSchemeName - URI or name of coding scheme recognized by the terminology service. Cannot be null
	 * @param codingSchemeVersion - The target version of this coding scheme.  Null indicates the PRODUCTION version
	 * @param csVersionTag - Node direction relationship.  Defaults to true. False indicates source to target determination
	 */

	public void load(String vsAssociationName, String codingSchemeName, String codingSchemeVersion, boolean targetToSource) throws Exception;
	/*
	 * @param entity - The enity to validate
	 */
	public void validate(Entity entity) throws Exception;
}
