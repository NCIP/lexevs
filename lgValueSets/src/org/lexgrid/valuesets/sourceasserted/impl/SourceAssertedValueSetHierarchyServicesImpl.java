package org.lexgrid.valuesets.sourceasserted.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.service.valuesets.LexEVSTreeItem;
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
	private LexBIGService lbs ;
	private HashMap<String, CodingScheme> sourceMap;

	private SourceAssertedValueSetHierarchyServicesImpl() {
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
	
	protected void initSourceMap(){
		sourceMap = new HashMap<String, CodingScheme>();
		List<CodingScheme> schemes = getSourceCodingSchemes();
	}

	private List<CodingScheme> getSourceCodingSchemes() {
		List<CodingScheme> schemes = new ArrayList<CodingScheme>();
		try {
			schemes.add(getSourceAssertedScheme());
		} catch (LBException e) {
			throw new RuntimeException("Error resolving source scheme for value set: ", e);
		}
		schemes.addAll(getOtherServiceSchemes());
		return schemes;
	}

	private List<CodingScheme> getOtherServiceSchemes() {
		List<CodingScheme> serviceSchemes = lbs.getRegularResolvedVSCodingSchemes();
		serviceSchemes.stream().map(x -> {CodingScheme scheme = null;
		try {
			scheme = lbs.resolveCodingScheme(
					x.getCodingSchemeURI(), Constructors.
					createCodingSchemeVersionOrTagFromVersion(x.getRepresentsVersion()));
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scheme;}).collect(Collectors.toSet());		
		List<CodingScheme> sourceSchemes = new ArrayList<CodingScheme>();
		return sourceSchemes;
	}

	private CodingScheme getSourceAssertedScheme() throws LBException {
		return lbs.resolveCodingScheme(ValueSetHierarchyService.SCHEME, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(
						getVSHierarchyService().getVersion()));
	}
	
	public HashMap<String, LexEVSTreeItem> getSourceDefinedTree() throws LBException{
		HashMap<String, LexEVSTreeItem> sourceTree = getSourceValueSetTree();
		List<HashMap<String, LexEVSTreeItem>> serviceTrees = getServiceTrees();
		HashMap<String, LexEVSTreeItem> fullTree = new HashMap<String, LexEVSTreeItem>();
		LexEVSTreeItem sourceTreeRoot = getSourceTreeRoot(sourceTree);

		List<LexEVSTreeItem> serviceTreeRoots = getServiceTreeRoots(serviceTrees);
		serviceTreeRoots.add(sourceTreeRoot);
		LexEVSTreeItem super_root = new LexEVSTreeItem(ValueSetHierarchyServiceImpl.ROOT,
				"Root node");
		super_root._assocToChildMap.put(ValueSetHierarchyService.INVERSE_IS_A, serviceTreeRoots);
		fullTree.put(ValueSetHierarchyServiceImpl.INVERSE_IS_A, super_root);
		
		return fullTree;
	}

	private List<LexEVSTreeItem> getServiceTreeRoots(List<HashMap<String,LexEVSTreeItem>> items) {
		// TODO Auto-generated method stub
		return null;
	}

	private LexEVSTreeItem getSourceTreeRoot(HashMap<String,LexEVSTreeItem> item) throws LBException {
		LexEVSTreeItem root = item.get(ValueSetHierarchyService.ROOT);
		List<LexEVSTreeItem> sources = root.get_assocToChildMap().get(ValueSetHierarchyService.INVERSE_IS_A);
		LexEVSTreeItem sourceItem = sources.get(0);
		CodingScheme scheme = lbs.resolveCodingScheme(sourceItem.get_code(), null);
		LexEVSTreeItem source = getTreeItemForSourceTerminology(scheme);
	//	return new LexEVSTreeItem(scheme.)
		return null;
	}

	private LexEVSTreeItem getTreeItemForSourceTerminology(CodingScheme scheme) {
		//LexEVSTreeItem item = new LexEVSTreeItem();
		String name = getNameForResolvedCodingSchemeProperties(scheme.getProperties());
		return null;
	}

	private String getNameForResolvedCodingSchemeProperties(Properties properties) {
		return properties.getPropertyAsReference().stream().filter( x -> x.getPropertyName()
				.equals("codingSchemeName")).findAny().get().getValue().getContent();
	}

	private List<HashMap<String, LexEVSTreeItem>> getServiceTrees() {
		// TODO Auto-generated method stub
		return null;
	}
	


}
