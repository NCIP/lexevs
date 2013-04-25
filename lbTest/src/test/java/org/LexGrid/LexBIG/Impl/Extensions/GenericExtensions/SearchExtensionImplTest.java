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
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;

import java.util.HashSet;
import java.util.Set;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.CodeSystemReference;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public class SearchExtensionImplTest extends LexBIGServiceTestCase {
    final static String testID = "SearchExtensionImplTest";

	@Override
	protected String getTestID() {
		return testID;
	}
	
	public void testIsExtensionAvailable() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		assertNotNull(searchExtension);
	}
	
	public void testSimpleSearch() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		ResolvedConceptReferencesIterator itr = searchExtension.search("Jaguar");
		assertTrue(itr.hasNext());
		assertEquals("Jaguar", itr.next().getCode());
		assertFalse(itr.hasNext());
	}
	
	@SuppressWarnings("serial")
	public void testSimpleSearchLimitedToCodingScheme() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		final CodeSystemReference ref = new CodeSystemReference();
		ref.setCodingScheme(AUTO_SCHEME);
		
		Set<CodeSystemReference> includes = 
			new HashSet<CodeSystemReference>() {{ add(ref); }};
			
		ResolvedConceptReferencesIterator itr = searchExtension.search("Jaguar", includes);
		assertTrue(itr.hasNext());
		assertEquals("Jaguar", itr.next().getCode());
		assertFalse(itr.hasNext());
	}
	
	@SuppressWarnings("serial")
	public void testSimpleSearchLimitedToDifferentCodingScheme() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		final CodeSystemReference ref = new CodeSystemReference();
		ref.setCodingScheme(PARTS_SCHEME);
		
		Set<CodeSystemReference> includes = 
			new HashSet<CodeSystemReference>() {{ add(ref); }};
			
		ResolvedConceptReferencesIterator itr = searchExtension.search("Jaguar", includes);
		assertFalse(itr.hasNext());
	}
	
	@SuppressWarnings("serial")
	public void testSimpleSearchExcludeCodingScheme() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		final CodeSystemReference ref = new CodeSystemReference();
		ref.setCodingScheme(PARTS_SCHEME);
		
		Set<CodeSystemReference> excludes = 
			new HashSet<CodeSystemReference>() {{ add(ref); }};
			
		ResolvedConceptReferencesIterator itr = searchExtension.search("Jaguar", null, excludes);
		assertTrue(itr.hasNext());
	}
	
	@SuppressWarnings("serial")
	public void testSimpleSearchExcludeCodingSchemeNoneReturned() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		final CodeSystemReference ref = new CodeSystemReference();
		ref.setCodingScheme(AUTO_SCHEME);
		
		Set<CodeSystemReference> excludes = 
			new HashSet<CodeSystemReference>() {{ add(ref); }};
			
		ResolvedConceptReferencesIterator itr = searchExtension.search("Jaguar", null, excludes);
		assertFalse(itr.hasNext());
	}
	
	@SuppressWarnings("serial")
	public void testSimpleSearchLimitedWithIncludeAndExclude() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		final CodeSystemReference auto = new CodeSystemReference();
		auto.setCodingScheme(AUTO_SCHEME);
		
		final CodeSystemReference parts = new CodeSystemReference();
		parts.setCodingScheme(PARTS_SCHEME);
		
		Set<CodeSystemReference> includes = 
			new HashSet<CodeSystemReference>() {{ add(auto); add(parts); }};
			
		Set<CodeSystemReference> excludes = 
					new HashSet<CodeSystemReference>() {{ add(auto); }};
			
		ResolvedConceptReferencesIterator itr = searchExtension.search("Tires", includes, excludes);
		assertTrue(itr.hasNext());
	}
	
	@SuppressWarnings("serial")
	public void testSimpleSearchLimitedWithIncludeAndExcludeAllExcluded() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		final CodeSystemReference auto = new CodeSystemReference();
		auto.setCodingScheme(AUTO_SCHEME);
		
		final CodeSystemReference parts = new CodeSystemReference();
		parts.setCodingScheme(PARTS_SCHEME);
		
		Set<CodeSystemReference> includes = 
			new HashSet<CodeSystemReference>() {{ add(auto); add(parts); }};
			
		Set<CodeSystemReference> excludes = 
					new HashSet<CodeSystemReference>() {{ add(parts); }};
			
		ResolvedConceptReferencesIterator itr = searchExtension.search("tire", includes, excludes);
		assertFalse(itr.hasNext());
	}
	
	public void testSimpleSearchCaseInsensitive() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		ResolvedConceptReferencesIterator itr = searchExtension.search("jaguar");
		assertTrue(itr.hasNext());
		assertEquals("Jaguar", itr.next().getCode());
		assertFalse(itr.hasNext());
	}
	
	public void testSimpleSearchCaseInsensitiveAndWildcard() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		ResolvedConceptReferencesIterator itr = searchExtension.search("jagu*");
		assertTrue(itr.hasNext());
		assertEquals("Jaguar", itr.next().getCode());
		assertFalse(itr.hasNext());
	}
	
	public void testSimpleSearchNone() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		ResolvedConceptReferencesIterator itr = searchExtension.search("____NONE____");
		assertFalse(itr.hasNext());
	}
	
	public void testSimpleSearchExactCode() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		ResolvedConceptReferencesIterator itr = searchExtension.search("code:C0001");
		assertTrue(itr.hasNext());
		assertEquals("C0001", itr.next().getCode());
		assertFalse(itr.hasNext());
	}
	
	public void testSimpleSearchFuzzyAndNegation() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtension searchExtension = (SearchExtension) lbs.getGenericExtension("SearchExtension");
	
		ResolvedConceptReferencesIterator itr = searchExtension.search("cor~ -Trailer");
		assertTrue(itr.hasNext());
		assertEquals("C0001", itr.next().getCode());
		assertFalse(itr.hasNext());
	}

}