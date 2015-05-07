/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexevs.tree.service;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.lexevs.tree.dao.LexEvsTreeDao;
import org.lexevs.tree.dao.LexEvsTreeDao.Direction;
import org.lexevs.tree.dao.hierarchy.HierarchyResolver;
import org.lexevs.tree.dao.iterator.ChildTreeNodeIteratorFactory;
import org.lexevs.tree.dao.pathtoroot.PathToRootResolver;
import org.lexevs.tree.evstree.ChildPagingEvsTreeConverter;
import org.lexevs.tree.evstree.EvsTreeConverter;
import org.lexevs.tree.json.ChildPagingJsonConverter;
import org.lexevs.tree.json.JsonConverter;
import org.lexevs.tree.model.LexEvsTree;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.LexGrid.naming.SupportedHierarchy;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;


/**
 * The Class PathToRootTreeServiceImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class PathToRootTreeServiceImpl extends AbstractExtendable implements TreeService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1903672606721006683L;
	
	/** The path to root resolver. */
	private PathToRootResolver pathToRootResolver;
	
	/** The lex evs tree dao. */
	private LexEvsTreeDao lexEvsTreeDao;
	
	/** The hierarchy resolver. */
	private HierarchyResolver hierarchyResolver;
	
	/** The page size. */
	private int pageSize = 20;
	
	/**
	 * Gets the spring managed bean.
	 * 
	 * @return the spring managed bean
	 */
	public TreeService getSpringManagedBean(){
		return (TreeService)TreeServiceFactory.getInstance().getTreeServiceSpringBean();
	}

	/**
	 * Gets the supported hierarchies.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param hierarchyId the hierarchy id
	 * 
	 * @return the supported hierarchies
	 */
	protected List<SupportedHierarchy> getSupportedHierarchies(
			String codingScheme, 
			CodingSchemeVersionOrTag versionOrTag,
			String hierarchyId){
		List<SupportedHierarchy> hierarchies = new ArrayList<SupportedHierarchy>();
		
		if(StringUtils.isBlank(hierarchyId)){
			SupportedHierarchy[] hiers = hierarchyResolver.getHierarchies(codingScheme, versionOrTag);
			for(SupportedHierarchy hier : hiers){
				hierarchies.add(hier);
			}
		} else {
			hierarchies.add(hierarchyResolver.getHierarchy(codingScheme, versionOrTag, hierarchyId));
		}
		return hierarchies;
	}

	/**
	 * Gets the direction.
	 * 
	 * @param hierarchies the hierarchies
	 * 
	 * @return the direction
	 */
	protected Direction getDirection(List<SupportedHierarchy> hierarchies){
		Direction direction = null;
		
		for(SupportedHierarchy hiers : hierarchies){
			Direction foundDirection;
			if(hiers.getIsForwardNavigable()){
				foundDirection = Direction.FORWARD;
			} else {
				foundDirection = Direction.BACKWARD;
			}
			
			if(direction == null){
				direction = foundDirection;
			} else if(!direction.equals(foundDirection)){
				throw new RuntimeException("Does not support more than one directional Hierarchy per CodingScheme.");
			}
			direction = Direction.valueOf(foundDirection.toString());
		}
		return direction;
	}
	
	/**
	 * Gets the root.
	 * 
	 * @param hierarchies the hierarchies
	 * 
	 * @return the root
	 */
	protected String getRoot(List<SupportedHierarchy> hierarchies){
		String root = null;
		
		for(SupportedHierarchy hiers : hierarchies){
			String foundRoot = hiers.getRootCode();
			
			if(root == null){
				root = foundRoot;
			} else if(!root.equals(foundRoot)){
				throw new RuntimeException("Does not support more than one Hierarchy per CodingScheme.");
			}
			root = foundRoot;
		}
		return root;
	}
	
	/**
	 * Gets the association names.
	 * 
	 * @param hierarchies the hierarchies
	 * 
	 * @return the association names
	 */
	protected List<String> getAssociationNames(List<SupportedHierarchy> hierarchies){
		List<String> associationNames = new ArrayList<String>();
		
		for(SupportedHierarchy hiers : hierarchies){
			String[] names = hiers.getAssociationNames();
			for(String name : names){
				associationNames.add(name);
			}
		}
		return associationNames;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.tree.service.TreeService#getTree(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String, java.lang.String, java.lang.String)
	 */
	public LexEvsTree getTree(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, final String code,
			String namespace, String hierarchyId) {
		LexEvsTree tree = new LexEvsTree();

		List<SupportedHierarchy> hierarchies = this.getSupportedHierarchies(codingScheme, versionOrTag, hierarchyId);
		Direction direction = this.getDirection(hierarchies);
		String root = this.getRoot(hierarchies);
		List<String> associationNames = this.getAssociationNames(hierarchies);

		ChildTreeNodeIteratorFactory factory = new 
		ChildTreeNodeIteratorFactory(
				lexEvsTreeDao, 
				codingScheme, 
				versionOrTag,
				direction,
				associationNames,
				pageSize);
		
		factory.addNodeAddedListener(tree);
		
		List<LexEvsTreeNode> pathToRoot = pathToRootResolver.getPathToRoot(codingScheme, versionOrTag, code, namespace);
		
		LexEvsTreeNode focusNode = lexEvsTreeDao.getNode(codingScheme, versionOrTag, code, namespace);
		if(focusNode == null){
		    throw new RuntimeException("No node found for codingscheme: " + codingScheme + ", version: " + versionOrTag + ", code: " + code + ", namespace: " + namespace);
		}
		for(LexEvsTreeNode node : pathToRoot){
			focusNode.addPathToRootParents(node);
			node.addPathToRootChildren(focusNode);
		}

		addRootNodes(this.buildRootNode(root), focusNode);
		addChildIterator(factory, focusNode, false);
		
		tree.setCurrentFocus(focusNode);
		tree.setCodingScheme(codingScheme);
		tree.setVersionOrTag(versionOrTag);
		tree.setDirection(direction);
		
		return tree;
	}
	
	protected String resolveNamespace(AbsoluteCodingSchemeVersionReference ref, final String code) {
		final String uri = ref.getCodingSchemeURN();
		final String version = ref.getCodingSchemeVersion();
		
		return LexEvsServiceLocator.getInstance().
			getDatabaseServiceManager().
				getDaoCallbackService().
					executeInDaoLayer(new DaoCallback<String>() {

						@Override
						public String execute(DaoManager daoManager) {
							CodingSchemeDao csDao = 
								daoManager.getCodingSchemeDao(uri, version);
							
							String csUid = csDao.getCodingSchemeUIdByUriAndVersion(uri, version);
							
							EntityDao entityDao = 
								daoManager.getEntityDao(uri, version);
							
							ResolvedConceptReference ref = 
								entityDao.getResolvedCodedNodeReferenceByCodeAndNamespace(csUid, code, null);
							
							if(ref != null ) {
								return ref.getCodeNamespace();
							} else {
								return null;
							}
						}
		});
	}
	
	/**
	 * Adds the root nodes.
	 * 
	 * @param root the root
	 * @param node the node
	 */
	protected void addRootNodes(LexEvsTreeNode root, LexEvsTreeNode node){
		if(node.getCode().equals(root.getCode())){return;}
		
		if(node.getPathToRootParents() == null || node.getPathToRootParents().size() == 0){
			node.addPathToRootParents(root);
			root.addPathToRootChildren(node);
		} else {
			for(LexEvsTreeNode parent : node.getPathToRootParents()){
				addRootNodes(root, parent);
			}
		}
	}
	
	/**
	 * Builds the root node.
	 * 
	 * @param root the root
	 * 
	 * @return the lex evs tree node
	 */
	protected LexEvsTreeNode buildRootNode(String root){
		LexEvsTreeNode rootNode = new LexEvsTreeNode();
		rootNode.setCode(root);
		return rootNode;
	}
	
	/**
	 * Adds the child iterator.
	 * 
	 * @param factory the factory
	 * @param node the node
	 * @param countOnly the count only
	 */
	protected void addChildIterator(ChildTreeNodeIteratorFactory factory, LexEvsTreeNode node, boolean countOnly){
		factory.buildChildNodeIterator(node, countOnly);
		if(node.getPathToRootParents() != null){
			for(LexEvsTreeNode parent : node.getPathToRootParents()){
				addChildIterator(factory, parent, countOnly);
			}
		}
	}
	
	/**
	 * Gets the expandable status.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * @param hierarchyId the hierarchy id
	 * @param direction the direction
	 * @param associationNames the association names
	 * 
	 * @return the expandable status
	 */
	protected ExpandableStatus getExpandableStatus(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String code,
			String namespace, String hierarchyId, Direction direction, List<String> associationNames){
		int count = lexEvsTreeDao.
			getChildrenCount(codingScheme, versionOrTag, code, namespace, direction, associationNames);
		if(count > 0){
			return ExpandableStatus.IS_EXPANDABLE;
		} else {
			return ExpandableStatus.IS_NOT_EXPANDABLE;
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.service.TreeService#getTree(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String, java.lang.String)
	 */
	public LexEvsTree getTree(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String code,
			String namespace) {
		return this.getTree(codingScheme, versionOrTag, code, namespace, null);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.service.TreeService#getTree(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String)
	 */
	public LexEvsTree getTree(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String code) {
		return this.getTree(codingScheme, versionOrTag, code, null, null);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.service.TreeService#getSubConcepts(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String, java.lang.String, java.lang.String)
	 */
	public LexEvsTreeNode getSubConcepts(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String code,
			String namespace, String hierarchyId) {
		List<SupportedHierarchy> hierarchies = this.getSupportedHierarchies(codingScheme, versionOrTag, hierarchyId);
		Direction direction = this.getDirection(hierarchies);
		List<String> associationNames = this.getAssociationNames(hierarchies);
		
		ChildTreeNodeIteratorFactory factory = new 
		ChildTreeNodeIteratorFactory(
				lexEvsTreeDao, 
				codingScheme, 
				versionOrTag,
				direction,
				associationNames,
				pageSize);
		
		LexEvsTreeNode focusNode = lexEvsTreeDao.getNode(codingScheme, versionOrTag, code, namespace);
		
		addChildIterator(factory, focusNode, false);
		
		return focusNode;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.service.TreeService#getSubConcepts(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String, java.lang.String)
	 */
	public LexEvsTreeNode getSubConcepts(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String code, String namespace) {
		return this.getSubConcepts(codingScheme, versionOrTag, code, namespace, null);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.service.TreeService#getSubConcepts(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String)
	 */
	public LexEvsTreeNode getSubConcepts(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String code) {
		return this.getSubConcepts(codingScheme, versionOrTag, code, null);
	}

	/**
	 * Gets the path to root resolver.
	 * 
	 * @return the path to root resolver
	 */
	public PathToRootResolver getPathToRootResolver() {
		return pathToRootResolver;
	}

	/**
	 * Sets the path to root resolver.
	 * 
	 * @param pathToRootResolver the new path to root resolver
	 */
	public void setPathToRootResolver(PathToRootResolver pathToRootResolver) {
		this.pathToRootResolver = pathToRootResolver;
	}

	/**
	 * Sets the lex evs tree dao.
	 * 
	 * @param lexEvsTreeDao the new lex evs tree dao
	 */
	public void setLexEvsTreeDao(LexEvsTreeDao lexEvsTreeDao) {
		this.lexEvsTreeDao = lexEvsTreeDao;
	}

	/**
	 * Gets the lex evs tree dao.
	 * 
	 * @return the lex evs tree dao
	 */
	public LexEvsTreeDao getLexEvsTreeDao() {
		return lexEvsTreeDao;
	}

	/**
	 * Sets the hierarchy resolver.
	 * 
	 * @param hierarchyResolver the new hierarchy resolver
	 */
	public void setHierarchyResolver(HierarchyResolver hierarchyResolver) {
		this.hierarchyResolver = hierarchyResolver;
	}

	/**
	 * Gets the hierarchy resolver.
	 * 
	 * @return the hierarchy resolver
	 */
	public HierarchyResolver getHierarchyResolver() {
		return hierarchyResolver;
	}

	/**
	 * Sets the page size.
	 * 
	 * @param pageSize the new page size
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Gets the page size.
	 * 
	 * @return the page size
	 */
	public int getPageSize() {
		return pageSize;
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
	 */
	@Override
	protected ExtensionDescription buildExtensionDescription() {
		ExtensionDescription ed = new ExtensionDescription();
		ed.setDescription("LexEVS Tree Utility");
		ed.setExtensionBaseClass("org.lexevs.tree.service.TreeService");
		ed.setExtensionClass(this.getClass().getName());
		ed.setVersion("1.0");
		ed.setName("tree-utility");
		return ed;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.service.TreeService#getJsonConverter()
	 */
	public JsonConverter getJsonConverter() {
		return new ChildPagingJsonConverter();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.tree.service.TreeService#getEvsTreeConverter()
	 */
	public EvsTreeConverter getEvsTreeConverter() {
		return new ChildPagingEvsTreeConverter();
	}

	@Override
	protected void doRegister(ExtensionRegistry registry,
			ExtensionDescription description) throws LBParameterException {
		  registry.registerGenericExtension(description);
		
	}
	
}
