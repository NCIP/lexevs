
package org.lexgrid.valuesets.helper.compiler;

import java.util.HashMap;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.valueSets.ValueSetDefinition;

/**
 * The Interface ValueSetDefinitionCompiler.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ValueSetDefinitionCompiler {
	
	/**
	 * Compile value set definition.
	 * 
	 * @param vdd the vdd
	 * @param refVersions the ref versions
	 * @param versionTag the version tag
	 * @param referencedVSDs - List of ValueSetDefinitions referenced by vsDef. If provided, these ValueSetDefinitions will be used to resolve vsDef.
	 * @return the coded node set
	 * 
	 * @throws LBException the LB exception
	 */
	public CodedNodeSet compileValueSetDefinition(
			ValueSetDefinition vdd, HashMap<String, String> refVersions, 
			String versionTag, HashMap<String, ValueSetDefinition> referencedVSDs) throws LBException;
}