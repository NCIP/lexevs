/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
