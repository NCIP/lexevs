package org.lexgrid.valuesets.sourceasserted.impl;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.service.valuesets.LexEVSTreeItem;
import org.lexevs.dao.database.service.valuesets.ValueSetHierarchyServiceImpl;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetHierarchyServices;

public class SourceAssertedValueSetHierarchyServicesImpl implements SourceAssertedValueSetHierarchyServices {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6898341008352626661L;
	private static SourceAssertedValueSetHierarchyServices assertedService = null;
	private LexBIGService lbs ;

	public SourceAssertedValueSetHierarchyServicesImpl() {
		lbs = getLexBIGService();
	}
	
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
		List<CodingScheme> schemes = lbs.getRegularResolvedVSCodingSchemes();
		List<LexEVSTreeItem> treeItems = schemes.stream().map(x -> { LexEVSTreeItem item = 
				new LexEVSTreeItem(x.getCodingSchemeURI(), x.getCodingSchemeName());
		item.set_expandable(false);
		return item;}).collect(Collectors.toList());
		treeItem.addAll(ValueSetHierarchyServiceImpl.INVERSE_IS_A, treeItems);
		return sourceTree;
	}

	@Override
	public HashMap<String, LexEVSTreeItem> getHierarchyValueSetRoots(String code) throws LBException {
		return getVSHierarchyService().getHierarchyValueSetRoots(code);
	}
	
	@Override
	public VSHierarchyNode addNodeToRoot(String URI, VSHierarchyNode root){
		return root;
	};


	public List<VSHierarchyNode> getSourceValueSetTreeBranch(VSHierarchyNode node, LexEVSTreeItem ti) {
		return getVSHierarchyService().getSourceValueSetTreeBranch(node, ti);
	}
	
	private ValueSetHierarchyServiceImpl getVSHierarchyService(){
		return (ValueSetHierarchyServiceImpl) LexEvsServiceLocator.
		getInstance().getDatabaseServiceManager().getValueSetHierarchyService();
	}
	
	public LexBIGService getLexBIGService(){
		if(lbs == null){
			lbs = LexBIGServiceImpl.defaultInstance();
		}
		return lbs;
	}
	
	public void setLexBIGService(LexBIGService lbs){
		this.lbs = lbs;
	}
	


}
