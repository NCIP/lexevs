
package org.lexevs.tree.json;

import java.util.List;

import org.lexevs.tree.dao.iterator.ChildTreeNodeIterator;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.lexevs.tree.utility.TreeUtility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Class ChildPagingJsonConverter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class ChildPagingJsonConverter implements JsonConverter {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5665088464809753637L;
	
	/** The Constant ONTOLOGY_NODE_CHILD_COUNT. */
	public static final String ONTOLOGY_NODE_CHILD_COUNT = "ontology_node_child_count";
	
	/** The Constant ONTOLOGY_NODE_ID. */
	public static final String ONTOLOGY_NODE_ID = "ontology_node_id";
	
	/** The Constant ONTOLOGY_NODE_NAME. */
	public static final String ONTOLOGY_NODE_NAME = "ontology_node_name";
	
	/** The Constant ONTOLOGY_NODE_NS. */
	public static final String ONTOLOGY_NODE_NS = "ontology_node_ns"; // namespace
	
	/** The Constant CHILDREN_NODES. */
	public static final String CHILDREN_NODES = "children_nodes";
	
	/** The Constant NODES. */
	public static final String NODES = "nodes";
	
	/** The Constant PAGE. */
	public static final String PAGE = "page";

	/** The MA x_ children. */
	private int MAX_CHILDREN = 5;
	
	/** The SUBCHILDRE n_ levels. */
	private int SUBCHILDREN_LEVELS = 1;
	
	/** The MOR e_ childre n_ indicator. */
	private static String MORE_CHILDREN_INDICATOR = "...";
	
	/* (non-Javadoc)
	 * @see org.lexevs.tree.json.JsonConverters#buildJsonPathFromRootTree(org.lexevs.tree.model.LexEvsTreeNode)
	 */
	public String buildJsonPathFromRootTree(LexEvsTreeNode focusNode){
		LexEvsTreeNode root = TreeUtility.getRoot(focusNode);
		return buildChildrenPathToRootNodes(root).toString();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.tree.json.JsonConverter#buildChildrenNodes(org.lexevs.tree.model.LexEvsTreeNode)
	 */
	public String buildChildrenNodes(LexEvsTreeNode focusNode) {
		JSONObject json = new JSONObject();
		try {
			json.put(NODES, buildChildren(focusNode, 1, SUBCHILDREN_LEVELS));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return json.toString();
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.tree.json.JsonConverter#buildChildrenNodes(org.lexevs.tree.model.LexEvsTreeNode, int)
	 */
	public String buildChildrenNodes(LexEvsTreeNode focusNode, int page) {
		JSONObject json = new JSONObject();
		try {
			json.put(NODES, buildChildren(focusNode, page, SUBCHILDREN_LEVELS));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return json.toString();
	}

	/**
	 * Builds the children.
	 * 
	 * @param focusNode the focus node
	 * @param page the page
	 * @param levels the levels
	 * 
	 * @return the jSON array
	 */
	private JSONArray buildChildren(LexEvsTreeNode focusNode, int page, int levels) {
		int children = 0;
		JSONArray childrenArray = new JSONArray();

		ChildTreeNodeIterator itr = focusNode.getChildIterator();
		while(itr.hasNext() && children < (MAX_CHILDREN * page) && levels > 0){
			LexEvsTreeNode child = itr.next();
			JSONObject obj = buildNode(child);
			try {
				obj.put("CHILDREN_NODES", buildChildrenNodes(child, levels - 1));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			childrenArray.put(obj);
			children++;
		}

		if(children >= MAX_CHILDREN){
			childrenArray.put(buildMoreChildrenNode(focusNode.getCode()));
		}
	
		return childrenArray;
	}

	/**
	 * Builds the children path to root nodes.
	 * 
	 * @param focusNode the focus node
	 * 
	 * @return the jSON array
	 */
	public JSONArray buildChildrenPathToRootNodes(LexEvsTreeNode focusNode){
		try {
			return (JSONArray)walkTreeFromRoot(focusNode).get(CHILDREN_NODES);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Walk tree from root.
	 * 
	 * @param node the node
	 * 
	 * @return the jSON object
	 */
	private JSONObject walkTreeFromRoot(LexEvsTreeNode node){
		JSONObject nodeObject = new JSONObject();

		try {
			nodeObject = buildNode(node);
		
			JSONArray childrenArray = new JSONArray();
			
			if(node.getPathToRootChildren() != null){	
				int children = 0;
				for(LexEvsTreeNode child : node.getPathToRootChildren()){
					children++;
					childrenArray.put(walkTreeFromRoot(child));
				} 
				
				ChildTreeNodeIterator itr = node.getChildIterator();
				while(itr.hasNext() && children < MAX_CHILDREN){
					LexEvsTreeNode child = itr.next();
					if(!knownChildrenContainsCode(node.getPathToRootChildren(), child.getCode())){
						childrenArray.put(walkTreeFromRoot(child));
						children++;
					}
				}
				
				if(children >= MAX_CHILDREN){
					childrenArray.put(buildMoreChildrenNode(node.getCode()));
				}
			}
			nodeObject.put(CHILDREN_NODES, childrenArray);
			
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		
		return nodeObject;
	}
	
	/**
	 * Builds the node.
	 * 
	 * @param node the node
	 * 
	 * @return the jSON object
	 */
	private JSONObject buildNode(LexEvsTreeNode node){
		JSONObject nodeObject = new JSONObject();

		try {
			nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, expandableStatusToInt(node.getExpandableStatus()));
			nodeObject.put(ONTOLOGY_NODE_ID, node.getCode());
			nodeObject.put(ONTOLOGY_NODE_NS, node.getNamespace() == null ? "" : node.getNamespace());
			nodeObject.put(ONTOLOGY_NODE_NAME, node.getEntityDescription());

			JSONArray childrenArray = new JSONArray();
			nodeObject.put(CHILDREN_NODES, childrenArray);
			
			return nodeObject;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Builds the more children node.
	 * 
	 * @param parent the parent
	 * 
	 * @return the jSON object
	 */
	private JSONObject buildMoreChildrenNode(String parent){
		JSONObject nodeObject = new JSONObject();

		try {
			nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, 0);
			nodeObject.put(ONTOLOGY_NODE_ID, parent);
			nodeObject.put(ONTOLOGY_NODE_NAME, MORE_CHILDREN_INDICATOR);
			nodeObject.put(PAGE, 1);

			JSONArray childrenArray = new JSONArray();
			nodeObject.put(CHILDREN_NODES, childrenArray);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return nodeObject;
	}
	
	/**
	 * Known children contains code.
	 * 
	 * @param list the list
	 * @param code the code
	 * 
	 * @return true, if successful
	 */
	private boolean knownChildrenContainsCode(List<LexEvsTreeNode> list, String code){
		for(LexEvsTreeNode node : list){
			if(node.getCode().equals(code)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Expandable status to int.
	 * 
	 * @param status the status
	 * 
	 * @return the int
	 */
	public int expandableStatusToInt(ExpandableStatus status){
		if(status.equals(ExpandableStatus.IS_EXPANDABLE)){
			return 1;
		} else {
			return 0;
		}
	}
}