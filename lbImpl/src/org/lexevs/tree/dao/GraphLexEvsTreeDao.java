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
package org.lexevs.tree.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class JdbcLexEvsTreeDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class GraphLexEvsTreeDao implements LexEvsTreeDao {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7492572066371522447L;

	
	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.LexEvsTreeDao#getTree(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String, java.lang.String)
	 */
	public List<LexEvsTreeNode> getChildren(
			String codingScheme, 
			CodingSchemeVersionOrTag versionOrTag, 
			String code, 
			String namespace,
			Direction direction,
			List<String> associationNames,
			int start,
			int limit){
				return this.getChildren(
						codingScheme, 
						versionOrTag, 
						code, 
						namespace, 
						direction, 
						associationNames, 
						null, 
						start, 
						limit);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.LexEvsTreeDao#getNode(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String, java.lang.String)
	 */
	@Transactional
	public LexEvsTreeNode getNode(
			String codingScheme, 
			CodingSchemeVersionOrTag versionOrTag, 
			String code, 
			String namespace){
		
		try {
			CodedNodeSet cns = LexBIGServiceImpl.defaultInstance().getNodeSet(codingScheme, versionOrTag, null).
				restrictToCodes(Constructors.createConceptReferenceList(code));
			
			ResolvedConceptReference ref = cns.resolve(null, null, null).next();
			
			LexEvsTreeNode node = buildTreeNode(ref);
			
			return node;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	private LexEvsTreeNode buildTreeNode(ResolvedConceptReference ref) {
		LexEvsTreeNode node = new LexEvsTreeNode();
		
		node.setCode(ref.getCode());
		node.setEntityDescription(ref.getEntityDescription().getContent());
		node.setNamespace(ref.getCodeNamespace());
		return node;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.LexEvsTreeDao#getChildrenCount(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String, java.lang.String, org.lexevs.tree.dao.LexEvsTreeDao.Direction, java.util.List)
	 */
	public int getChildrenCount(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace, 
			Direction direction, 
			List<String> associationNames) {
		try {
			CodedNodeGraph cng = LexBIGServiceImpl.defaultInstance().getNodeGraph(codingScheme, versionOrTag, null);

			if(CollectionUtils.isNotEmpty(associationNames)) {
				NameAndValueList nvl = 
					Constructors.createNameAndValueList(associationNames.toArray(new String[associationNames.size()]));

				cng = cng.restrictToAssociations(nvl, null);
			}
			ResolvedConceptReference 

			ref = 
				cng.resolveAsList(Constructors.createConceptReference(
						code, namespace, null), 
						direction.equals(Direction.FORWARD) ? true : false, 
								direction.equals(Direction.BACKWARD) ? true : false, 
										0, 1, null, null, null, null, -1).getResolvedConceptReference(0);

			int count = 0;
			if(ref.getSourceOf() != null) {
				for(Association assoc : ref.getSourceOf().getAssociation()) {
					count += assoc.getAssociatedConcepts().getAssociatedConceptCount();
				}
			}

			return count;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	

	public List<LexEvsTreeNode> getChildren(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace, 
			Direction direction,
			List<String> associationNames, 
			List<String> knownCodes, 
			int start,
			int limit) {

		try {
			CodedNodeGraph cng = LexBIGServiceImpl.defaultInstance().getNodeGraph(codingScheme, versionOrTag, null);

			if(CollectionUtils.isNotEmpty(associationNames)) {
				NameAndValueList nvl = 
					Constructors.createNameAndValueList(associationNames.toArray(new String[associationNames.size()]));

				cng = cng.restrictToAssociations(nvl, null);
			}
		
			ConceptReference focus = new ConceptReference();
			focus.setCode(code);
			
			ResolvedConceptReferenceList refList = 
				cng.resolveAsList(focus, 
						direction.equals(Direction.FORWARD) ? true : false, 
								direction.equals(Direction.BACKWARD) ? true : false, 
										0, 1, null, null, null, null, -1);

			List<LexEvsTreeNode> returnNodes = new ArrayList<LexEvsTreeNode>();

			for(ResolvedConceptReference ref : refList.getResolvedConceptReference()) {
			
				for(Association assoc : ref.getSourceOf().getAssociation()) {
					Iterator<? extends AssociatedConcept> itr = 
						assoc.getAssociatedConcepts().iterateAssociatedConcept();

					while(returnNodes.size() < limit && itr.hasNext()) {
						returnNodes.add(this.buildTreeNode(itr.next()));
					}
				}
			}

			return returnNodes;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	
}
