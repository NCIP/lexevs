package org.lexgrid.valuesets.sourceasserted.impl;

import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
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
	private ValueSetHierarchyServiceImpl service;
	private static SourceAssertedValueSetHierarchyServicesImpl assertedService;

	public SourceAssertedValueSetHierarchyServicesImpl() {
		service = (ValueSetHierarchyServiceImpl) LexEvsServiceLocator.
				getInstance().getDatabaseServiceManager().getValueSetHierarchyService();
	}
	
	public static SourceAssertedValueSetHierarchyServicesImpl defaultInstance(){
		if(assertedService != null){
			return assertedService;
		}
		assertedService = new SourceAssertedValueSetHierarchyServicesImpl();
		return assertedService;
	}

	@Override
	public void preprocessSourceHierarchyData() {
		service.init();

	}
	
	@Override
	public void preprocessSourceHierarchyData(String scheme, String version, 
			String association, String sourceDesignation, String publishName, String root_code) {
		service.init(scheme, version, association, sourceDesignation, publishName, root_code);
	}

	@Override
	public HashMap<String, LexEVSTreeItem> getSourceValueSetTree(String scheme, String version) throws LBException {
		return service.getSourceValueSetTree(scheme, version);
	}

	@Override
	public HashMap<String, LexEVSTreeItem> getHierarchyValueSetRoots(String code) throws LBException {
		return service.getHierarchyValueSetRoots(code);
	}

	@Override
	public List<VSHierarchyNode> getSourceValueSetTreeBranch(VSHierarchyNode node, LexEVSTreeItem ti) {
		return service.getSourceValueSetTreeBranch(node, ti);
	}

}
