package org.lexgrid.valuesets.sourceasserted.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.service.valuesets.LexEVSTreeItem;
import org.lexevs.dao.database.service.valuesets.LexEVSTreeItem.TextComparator;
import org.lexevs.dao.database.service.valuesets.ValueSetHierarchyService;
import org.lexevs.dao.database.service.valuesets.ValueSetHierarchyServiceImpl;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetHierarchyServices;

public class SourceAssertedValueSetHierarchyServicesImpl implements SourceAssertedValueSetHierarchyServices {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6898341008352626661L;
	private static SourceAssertedValueSetHierarchyServices assertedService = null;
	private transient LexBIGService lbs ;
	
	public static SourceAssertedValueSetHierarchyServices defaultInstance(){
		if(assertedService == null){
		assertedService = new SourceAssertedValueSetHierarchyServicesImpl();}
		return assertedService;
	}

	@Override
	public void preprocessSourceHierarchyData() {
		getVSHierarchyService().init();
	}
	
	@Override
	public void preprocessSourceHierarchyData(String scheme, String version, 
			String association, String sourceDesignation, String publishName, String root_code) {
		getVSHierarchyService(scheme, version, association, sourceDesignation, publishName, root_code);
	}

	private ValueSetHierarchyServiceImpl getVSHierarchyService(String scheme, String version, String association, String sourceDesignation,
			String publishName, String root_code) {
		return (ValueSetHierarchyServiceImpl) LexEvsServiceLocator.
				getInstance().getDatabaseServiceManager().getValueSetHierarchyService().
				init(scheme, version, association, sourceDesignation, publishName, root_code);
		
	}

	@Override
	public HashMap<String, LexEVSTreeItem> getSourceValueSetTree() throws LBException {
		return getVSHierarchyService().getSourceValueSetTree();
	}
	
	@Override
	public HashMap<String, LexEVSTreeItem> getFullServiceValueSetTree() throws LBException {
		HashMap<String, LexEVSTreeItem> sourceTree = getVSHierarchyService().getSourceValueSetTree();
		LexEVSTreeItem treeItem = sourceTree.get(ValueSetHierarchyServiceImpl.ROOT); 
		List<LexEVSTreeItem> treeItems = getTreeItemListFromSchemes(
				getLexBIGService().getRegularResolvedVSCodingSchemes(), false);
		treeItem.addAll(ValueSetHierarchyServiceImpl.INVERSE_IS_A, treeItems);
		sortOnText(treeItem.get_assocToChildMap().get(ValueSetHierarchyServiceImpl.INVERSE_IS_A), new TextComparator());
		return sourceTree;
	}
	

	private List<LexEVSTreeItem> getTreeItemListFromSchemes(List<CodingScheme> schemes, boolean expandable) {
		return schemes.stream().map(x -> { 
			LexEVSTreeItem item = 
				new LexEVSTreeItem(x.getCodingSchemeURI(), x.getCodingSchemeName());
				item.set_expandable(expandable);
				return item;}).
					collect(Collectors.toList());
	}

	@Override
	public HashMap<String, LexEVSTreeItem> getHierarchyValueSetRoots(String code) throws LBException {
		return getVSHierarchyService().getHierarchyValueSetRoots(code);
	}


	public List<VSHierarchyNode> getSourceValueSetTreeBranch(VSHierarchyNode node, LexEVSTreeItem ti) {
		return getVSHierarchyService().getSourceValueSetTreeBranch(node, ti);
	}
	
	private ValueSetHierarchyServiceImpl getVSHierarchyService(){
		return (ValueSetHierarchyServiceImpl) LexEvsServiceLocator.
		getInstance().getDatabaseServiceManager().getValueSetHierarchyService();
	}
	
	@LgClientSideSafe
	public LexBIGService getLexBIGService(){
		if(lbs == null){
			lbs = LexBIGServiceImpl.defaultInstance();
		}
		return lbs;
	}
	
	@LgClientSideSafe
	public void setLexBIGService(LexBIGService lbs){
		this.lbs = lbs;
	}

	private List<CodingScheme> getOtherServiceSchemes() {
		List<CodingScheme> serviceSchemes = getLexBIGService().getRegularResolvedVSCodingSchemes();
		List<CodingScheme> sourceSchemes = serviceSchemes.stream().map(x -> {CodingScheme scheme = null;
		try {
			scheme = getLexBIGService().resolveCodingScheme(
					x.getCodingSchemeURI()
					, null);
		} catch (LBException e) {
			throw new RuntimeException("Problem getting non-asserted value set schemes from data base: ", e);
		}
		return scheme;}).collect(Collectors.toList());		
		return sourceSchemes;
	}
	
	@Override
	public HashMap<String, LexEVSTreeItem> getSourceDefinedTree() throws LBException{
		HashMap<String, LexEVSTreeItem> fullTree = new HashMap<String, LexEVSTreeItem>();
		HashMap<String, LexEVSTreeItem> sourceTree = getSourceValueSetTree();
		LexEVSTreeItem sourceTreeRoot = getSourceTreeRoot(sourceTree);
		//assuming the other source trees are all surfaced on one level.
		List<CodingScheme> serviceSchemes = getOtherServiceSchemes();

		List<LexEVSTreeItem> serviceTreeRoots = getServiceTreeRoots(serviceSchemes);		
//		serviceTreeRoots.add(sourceTreeRoot);
//		sortOnText(serviceTreeRoots, new TextComparator());
		List<LexEVSTreeItem> temp = new ArrayList<LexEVSTreeItem>();
		temp.add(sourceTreeRoot);
		temp.addAll(serviceTreeRoots);
		LexEVSTreeItem super_root = new LexEVSTreeItem(ValueSetHierarchyServiceImpl.ROOT,
				"Root node");
		super_root._assocToChildMap.put(ValueSetHierarchyService.INVERSE_IS_A, temp);
		fullTree.put(ValueSetHierarchyServiceImpl.ROOT, super_root);
		
		return fullTree;
	}

	private List<LexEVSTreeItem> getServiceTreeRoots(List<CodingScheme> schemes) {
		List<LexEVSTreeItem> roots = new ArrayList<LexEVSTreeItem>();
				for(CodingScheme scheme: schemes){
			LexEVSTreeItem item = getTreeItemForSourceTerminology(scheme);
			if(!roots.contains(item)){
				item.addChild(ValueSetHierarchyService.INVERSE_IS_A, new LexEVSTreeItem(
						scheme.getCodingSchemeURI(), scheme.getCodingSchemeName(), 
						AssertedValueSetServices.getNameSpaceForCodingScheme(scheme), null));
				roots.add(item);
			}
			else{
				roots.stream().filter(x -> x.equals(item)).findFirst().get().
				addChild(ValueSetHierarchyService.INVERSE_IS_A, 
						new LexEVSTreeItem(scheme.getCodingSchemeURI(), scheme.getCodingSchemeName(),
								AssertedValueSetServices.getNameSpaceForCodingScheme(scheme), null));
			}
		}
	return roots;
	}

	private LexEVSTreeItem getSourceTreeRoot(HashMap<String,LexEVSTreeItem> item) throws LBException {
		LexEVSTreeItem root = item.get(ValueSetHierarchyService.ROOT);
		List<LexEVSTreeItem> sources = root.get_assocToChildMap().get(ValueSetHierarchyService.INVERSE_IS_A);
		LexEVSTreeItem sourceItem = sources.get(0);
		CodingScheme scheme = null;
		try{
			scheme = getLexBIGService().resolveCodingScheme(sourceItem.get_text(), null);
		}catch(LBException e){
			throw new RuntimeException("Unable to resolve value set coding scheme. "
					+ " This scheme may not be tagged as a PRODUCTION scheme", e);
		}
		LexEVSTreeItem new_root = getTreeItemForSourceTerminology(scheme);
		new_root.addAll(ValueSetHierarchyService.INVERSE_IS_A, sources);
		return new_root;
	}

	private LexEVSTreeItem getTreeItemForSourceTerminology(CodingScheme scheme) {
		CodingScheme source = null;
		try {
			source = getLexBIGService().resolveCodingScheme(
					getURNForResolvedCodingSchemeProperties(scheme.getProperties()), null);
		} catch (LBException e) {
			throw new RuntimeException("Error resolving source terminology for value set: ", e);
		}
		LexEVSTreeItem sourceTI = new LexEVSTreeItem(source.getCodingSchemeURI(), source.getFormalName(), 
				AssertedValueSetServices.getNameSpaceForCodingScheme(scheme), null);
		sourceTI.set_expandable(true);
		return sourceTI;
	}
	
	private String getURNForResolvedCodingSchemeProperties(Properties properties) {
		return properties.getPropertyAsReference().stream().filter( x -> x.getPropertyName()
				.equals("resolvedAgainstCodingSchemeVersion")).findAny().get().getValue().getContent();
	}
	
	public void sortOnText(List<LexEVSTreeItem> items, Comparator<LexEVSTreeItem> compare){
		Collections.sort(items, compare);
	}


}
