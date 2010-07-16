/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and Research
 * (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo
 * logo are trademarks and service marks of MFMER.
 * 
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.lexevs.cts2.query;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;

/**
 * LexEVS Implementation of CTS2 Value Set Query Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface ValueSetQueryOperation {
	
	/**
	 * Lists the value sets that are available to the CTS 2 service. 
	 * @param codeSystemId (Optional) code system id the value set should reference
	 * @param conceptDomainId (Optional) conceptDomain the value set should be bound to
	 * @param usageContextId (Optional) usage context, the value set can be used
	 * @param sortOption Ascending or Descending the return list of value set identifiers
	 * @return List of value set identifiers
	 * @throws LBException
	 */
	public List<String> listValueSets(String codeSystemId, String conceptDomainId, String usageContextId, SortOption sortOption) throws LBException;
	
	/**
	 * Lists all the value sets that are available to the CTS 2 service.
	 * @param sortOption Ascending or Descending the return list of value set identifiers
	 * @return List of value set identifiers
	 * @throws LBException
	 */
	public List<String> listAllValueSets(SortOption sortOption) throws LBException;
	
	/**
	 * Returns detailed information (meta data) for a given value set. 
	 * @param valueSetId id of the value set
	 * @param valueSetVersion (Optional) version of the value set
	 * @return object ValueSetDefinition
	 * @throws LBException
	 */
	public ValueSetDefinition getValueSetDetails(String valueSetId, String valueSetVersion) throws LBException;
	
	/**
	 * Lists out the contents (entries) of a given value set, filtering based 
	 * on input criteria. This function is to be used to create the value set expansion.
	 * 
	 * @param valueSetId id of value set
	 * @param valueSetVersion (Optional) version of value set
	 * @param csVersionList code system version reference list to be used to resolve value set
	 * @param versionTag the tag (e.g. "devel", "production", ...) to be used to determine which code system to be used
	 * @param sortOption (Optional) sort option to apply on resolved concepts
	 * @return A resolved Value Set definition containing the code system version reference list
	 * that was used to resolve the value set and an iterator for resolved concepts.
	 * @throws LBException
	 */
	public ResolvedValueSetDefinition listValueSetContents(String valueSetId, String valueSetVersion, AbsoluteCodingSchemeVersionReferenceList csVersionList,
			String versionTag, SortOption sortOption) throws LBException;
	
		
	/**
	 * Determine whether one of the two supplied value sets subsumes the other.
	 * 
	 * @param childValueSetId child value set id
	 * @param childValueSetVersion (Optional) child value set version
	 * @param parentValueSetId parent value set id
	 * @param parentValueSetVersion (Optional) parent value set version
	 * @param csVersionList list of code system versions to use in resolution.
     * @param versionTag the tag (e.g. "devel", "production", ...) to be used to determine which code system to be used
	 * @return True; if childValueSet subsumes parentValueSet. False; otherwise.
	 * @throws LBException
	 */
	public boolean checkValueSetSubsumption(String childValueSetId, String childValueSetVersion, 
			String parentValueSetId, String parentValueSetVersion, AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException;
	
	/**
	 * Determine whether the supplied coded concept exists in the supplied value set .
	 * @param conceptCode coded concept id
	 * @param entityCodeNamespace the URI of the entity code namespace.  If omitted, the default coding scheme namespace for the value domain
	 *                               will be used, if it is present.  Otherwise the first matching entity code, if any, will pass
	 * @param codeSystemAndVersion code system and version reference to be used
	 * @param valueSetId value set id
	 * @param valueSetVersion (Optional) value set version
	 * @param versionTag the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     *                   Note that non-tagged versions will be used if the tagged version is missing.
	 * @return True; if coded concept exists in value set. False; otherwise.
	 * @throws LBException
	 */
	public boolean checkConceptValueSetMembership(String conceptCode, URI entityCodeNamespace, AbsoluteCodingSchemeVersionReference codeSystemAndVersion, String valueSetId, String valueSetVersion, String versionTag) throws LBException;
}
