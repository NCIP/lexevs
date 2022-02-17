
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.codingSchemes.CodingScheme;

/**
 * The Interface ValueSetDefinitionLoader.
 * 
 * @author <a href="mailto:bauer.scott@mayo.edu">Scott Bauer</a>
 */
public interface SourceAssertedVStoCodingSchemeLoader  extends Loader {
	
	public static String NAME = "SourceAssertedVStoCodingSchemeLoader";
	public static String VERSION = "1.0";
	public static String DESCRIPTION = "This loader loads a value set asserted by the source system instead of a rules based definition.";
	
	
	/*
	 * @param scheme - the Scheme to be loaded
	 */
  
	public void load(CodingScheme scheme);
	public void validate(URI valueSetDefinitionURI, int validationLevel) throws Exception;

}