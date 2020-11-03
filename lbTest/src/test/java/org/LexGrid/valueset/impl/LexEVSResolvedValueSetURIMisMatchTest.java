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
package org.LexGrid.valueset.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.dao.database.service.valuesets.LexEVSTreeItem;
import org.lexevs.dao.database.service.valuesets.ValueSetHierarchyServiceImpl;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetHierarchyServices;
import org.lexgrid.valuesets.sourceasserted.impl.SourceAssertedValueSetHierarchyServicesImpl;

/**
 * JUnit for Resolved Value Set Service.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @author <A HREF="mailto:bauer.scott@mayo.edu">Scott Bauer</A>
 */
public class LexEVSResolvedValueSetURIMisMatchTest {
	static AssertedValueSetParameters params;
	static LexEVSResolvedValueSetService service;
	static private LexBIGService lbs;
	static SourceAssertedValueSetSearchIndexService vsSvc;
	private static SourceAssertedValueSetHierarchyServices hService;

	@BeforeClass
	public static void setUp() {
		lbs = getLexBIGService();
		params =
		new AssertedValueSetParameters.Builder("18.06d")
		.build();
		service = new LexEVSResolvedValueSetServiceImpl(params);
		vsSvc = LexEvsServiceLocator.getInstance().getIndexServiceManager().getAssertedValueSetIndexService();
		hService = SourceAssertedValueSetHierarchyServicesImpl.defaultInstance();
		hService.preprocessSourceHierarchyData();
//		vsSvc.createIndex(Constructors.createAbsoluteCodingSchemeVersionReference(
//				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.06d"));
	}

	@Test
	public void testListAllResolvedValueSets() throws Exception {
		List<CodingScheme> list = service.listAllResolvedValueSets();
		list.stream().forEach(x -> System.out.println(x.getCodingSchemeURI()));
	}
	

	@Test
	public void testBuildTree() throws LBException{
		Map<String, LexEVSTreeItem> items  = hService.getFullServiceValueSetTree();
		LexEVSTreeItem item = items.get(ValueSetHierarchyServiceImpl.ROOT);
		List<String> uris = new ArrayList<String>();
		printTree(item._assocToChildMap.get(ValueSetHierarchyServiceImpl.INVERSE_IS_A), uris);
		uris.stream().forEach(x -> System.out.println(x));
	}
	
	@Test
	public void testCompareTreeToRVSOutput() throws LBException {
		List<String> resolvedVS = getResolvedValueSetList();
		System.out.println("Resolved Value Set Count: " + resolvedVS.size());
		List<String> treeItems = getAllTreeItemUris();
		System.out.println("Tree Item Count: " + treeItems.size());
		Set<String> cleanedRVS = resolvedVS.stream().collect(Collectors.toSet());
		System.out.println("Cleanded Resolved Value Set Count: " + cleanedRVS.size());
		Set<String> cleanedTreeItems = treeItems.stream().collect(Collectors.toSet());
		System.out.println("Cleaned Tree Item Count: " + cleanedTreeItems.size());
		List<String> rVSCompareToTreeItems = resolvedVS.stream().filter(x -> !treeItems.contains(x)).collect(Collectors.toList());
		List<String> treeItemCompareToRVS = treeItems.stream().filter(t -> !resolvedVS.contains(t)).collect(Collectors.toList());
		System.out.println("\nResolved Value Sets Not in Tree Items: ");
		rVSCompareToTreeItems.stream().forEach(n -> System.out.println(n));
		System.out.println("\nTree Items Not in Resolved Value Sets: ");
		treeItemCompareToRVS.stream().forEach(z -> System.out.println(z));
		rVSCompareToTreeItems.addAll(treeItemCompareToRVS );
		System.out.println("\nSize of compared set: " + rVSCompareToTreeItems.size());
	}
	
	private void printTree(List<LexEVSTreeItem> items, List<String> uris){
		if(items == null || items.isEmpty()){return;}
		for(LexEVSTreeItem x : items){
			uris.add(x.get_code());
			List<LexEVSTreeItem> list = x._assocToChildMap.get(ValueSetHierarchyServiceImpl.INVERSE_IS_A);
			printTree(list, uris);
		}
	}
	
	private List<String> getResolvedValueSetList() throws LBException{
		return service.listAllResolvedValueSets().stream().map(x-> x.getCodingSchemeURI()).collect(Collectors.toList());
		}

	private List<String> getAllTreeItemUris() throws LBException{
		Map<String, LexEVSTreeItem> items  = hService.getFullServiceValueSetTree();
		LexEVSTreeItem item = items.get(ValueSetHierarchyServiceImpl.ROOT);
		List<String> uris = new ArrayList<String>();
		printTree(item._assocToChildMap.get(ValueSetHierarchyServiceImpl.INVERSE_IS_A), uris);
		return uris;
	}
	
	
	public static LexBIGService getLexBIGService(){
		if(lbs == null){
			lbs = LexBIGServiceImpl.defaultInstance();
		}
		return lbs;
	}
	
}