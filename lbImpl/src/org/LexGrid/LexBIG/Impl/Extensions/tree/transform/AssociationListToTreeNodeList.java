
package org.LexGrid.LexBIG.Impl.Extensions.tree.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;

/**
 * The Class AssociationListToTreeNodeList.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationListToTreeNodeList implements Transformer<AssociationList, List<LexEvsTreeNode>>{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4791839960034941586L;

	/* (non-Javadoc)
	 * @see org.lexevs.tree.transform.Transformer#transform(java.lang.Object)
	 */
	public List<LexEvsTreeNode> transform(AssociationList input) {
		Map<String,LexEvsTreeNode> codeMap = new HashMap<String,LexEvsTreeNode>();
		return getTreeNodesForLevel(input, codeMap);
	}
	
	/**
	 * Gets the tree nodes for level.
	 * 
	 * @param level the level
	 * @param codeMap the code map
	 * 
	 * @return the tree nodes for level
	 */
	protected List<LexEvsTreeNode> getTreeNodesForLevel(AssociationList level, Map<String,LexEvsTreeNode> codeMap){
		List<LexEvsTreeNode> returnList = new ArrayList<LexEvsTreeNode>();
		if(level == null || level.getAssociation() == null){return returnList;}
		
		for(Association assoc : level.getAssociation()){
			returnList.addAll(
					getLexEvsTreeNodesFromAssociatedConcepts(assoc.getAssociatedConcepts(), codeMap));
		}
		return returnList;
	}
	
	/**
	 * Gets the lex evs tree nodes from associated concepts.
	 * 
	 * @param list the list
	 * @param codeMap the code map
	 * 
	 * @return the lex evs tree nodes from associated concepts
	 */
	protected List<LexEvsTreeNode> getLexEvsTreeNodesFromAssociatedConcepts(AssociatedConceptList list, Map<String,LexEvsTreeNode> codeMap){
		List<LexEvsTreeNode> returnList = new ArrayList<LexEvsTreeNode>();
		for(AssociatedConcept concept : list.getAssociatedConcept()){
			returnList.add(
					buildLexEvsTreeNode(concept, codeMap));
		}
		return returnList;
	}
	
	/**
	 * Builds the lex evs tree node.
	 * 
	 * @param concept the concept
	 * @param codeMap the code map
	 * 
	 * @return the lex evs tree node
	 */
	protected LexEvsTreeNode buildLexEvsTreeNode(AssociatedConcept concept, Map<String,LexEvsTreeNode> codeMap){
		if(codeMap.containsKey(concept.getCode())){
			return codeMap.get(concept.getCode());
		} else {
			LexEvsTreeNode node = new LexEvsTreeNode();
			node.setCode(concept.getCode());
			node.setEntityDescription(concept.getEntityDescription().getContent());
			node.setNamespace(concept.getCodeNamespace());
			
			if (concept.getEntity().getIsAnonymous() == null || concept.getEntity().getIsAnonymous() == false) {
			    node.setAnonymous(false);
			}
			else{
			    node.setAnonymous(true);
			}

			List<LexEvsTreeNode> pathToRootParents = getTreeNodesForLevel(concept.getSourceOf(), codeMap);
			for(LexEvsTreeNode parent : pathToRootParents){
				parent.addPathToRootChildren(node);
				node.addPathToRootParents(parent);
			}

			codeMap.put(node.getCode(), node);
			return node;
		}
	}
}