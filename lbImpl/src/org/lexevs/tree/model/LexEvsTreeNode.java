
package org.lexevs.tree.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.lexevs.tree.dao.iterator.ChildTreeNodeIterator;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * The Class LexEvsTreeNode.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
@LgClientSideSafe
public class LexEvsTreeNode implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7950060687242664885L;

	/**
	 * The Enum ExpandableStatus.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum ExpandableStatus {/** The I s_ expandable. */
IS_EXPANDABLE, /** The I s_ no t_ expandable. */
 IS_NOT_EXPANDABLE, /** The UNKNOWN. */
 UNKNOWN};
	
	/** The code. */
	private String code;
	
	/** The entity description. */
	private String entityDescription;
	
	/** The namespace. */
	private String namespace;

	/** The path to root parents. */
	private List<LexEvsTreeNode> pathToRootParents;
	
	/** The path to root children. */
	private List<LexEvsTreeNode> pathToRootChildren;
	
	/** The expandable status. */
	private ExpandableStatus expandableStatus = ExpandableStatus.UNKNOWN;
	
	/** The child iterator. */
	private ChildTreeNodeIterator childIterator;
	
	/**
	 * Instantiates a new lex evs tree node.
	 */
	public LexEvsTreeNode(){};
	
	public LexEvsTreeNode(LexEvsTreeNode letn ) {
		this.code= letn.code;
		this.entityDescription= letn.entityDescription;
		if(letn.namespace != null){
		this.namespace = letn.namespace;
		}
		this.expandableStatus= letn.expandableStatus;
		
	}
	
	/**
	 * Instantiates a new lex evs tree node.
	 * 
	 * @param childIterator the child iterator
	 */
	public LexEvsTreeNode(ChildTreeNodeIterator childIterator){
		this.childIterator = childIterator;
	}
	
	/**
	 * Gets the expandable status.
	 * 
	 * @return the expandable status
	 */
	@LgClientSideSafe
	public ExpandableStatus getExpandableStatus() {
		return expandableStatus;
	}
	
	/**
	 * Sets the expandable status.
	 * 
	 * @param expandableStatus the new expandable status
	 */
	public void setExpandableStatus(ExpandableStatus expandableStatus) {
		this.expandableStatus = expandableStatus;
	}
	
	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	@LgClientSideSafe
	public String getCode() {
		return code;
	}
	
	/**
	 * Sets the code.
	 * 
	 * @param code the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Gets the entity description.
	 * 
	 * @return the entity description
	 */
	@LgClientSideSafe
	public String getEntityDescription() {
		return entityDescription;
	}
	
	/**
	 * Sets the entity description.
	 * 
	 * @param entityDescription the new entity description
	 */
	public void setEntityDescription(String entityDescription) {
		this.entityDescription = entityDescription;
	}
	
	/**
	 * Gets the path to root parents.
	 * 
	 * @return the path to root parents
	 */
	public List<LexEvsTreeNode> getPathToRootParents() {
		return pathToRootParents;
	}
	
	/**
	 * Adds the path to root parents.
	 * 
	 * @param pathToRootParent the path to root parent
	 */
	public void addPathToRootParents(LexEvsTreeNode pathToRootParent) {
		if(pathToRootParents == null){
			pathToRootParents = new ArrayList<LexEvsTreeNode>();
		}
		
		if(!containsNode(pathToRootParent, this.pathToRootParents)){
			this.pathToRootParents.add(pathToRootParent);
		}
	}
	
	/**
	 * Gets the namespace.
	 * 
	 * @return the namespace
	 */
	@LgClientSideSafe
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Sets the namespace.
	 * 
	 * @param namespace the new namespace
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Gets the child iterator.
	 * 
	 * @return the child iterator
	 */
	public ChildTreeNodeIterator getChildIterator() {
		childIterator.reset();
		return childIterator;
	}
	
	/**
	 * Sets the child iterator.
	 * 
	 * @param childIterator the child iterator
	 * @param countOnly the count only
	 */
	public void setChildIterator(ChildTreeNodeIterator childIterator, boolean countOnly) {
		this.childIterator = childIterator;
		this.childIterator.initIterator(this, countOnly);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	@LgClientSideSafe
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LexEvsTreeNode other = (LexEvsTreeNode) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		if (namespace == null) {
			if (other.namespace != null) {
				return false;
			}
		} else if (!namespace.equals(other.namespace)) {
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@LgClientSideSafe
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\nCode: " + this.code
			+ "\nEntityDescription: " + this.entityDescription
			+ "\nNamespace: " + this.namespace
			+ "\nExpandable Status: " + this.expandableStatus.toString());
		
		return sb.toString();
	}

	/**
	 * Gets the path to root children.
	 * 
	 * @return the path to root children
	 */
	public List<LexEvsTreeNode> getPathToRootChildren() {
		return pathToRootChildren;
	}	
	
	/**
	 * Adds the path to root children.
	 * 
	 * @param child the child
	 */
	public void addPathToRootChildren(LexEvsTreeNode child) {
		if(pathToRootChildren == null){
			pathToRootChildren = new ArrayList<LexEvsTreeNode>();
		}
		
		if(!containsNode(child, this.pathToRootChildren)){
			this.pathToRootChildren.add(child);
		}
	}
	
	private boolean containsNode(LexEvsTreeNode node, List<LexEvsTreeNode> list){
		for(LexEvsTreeNode listNode : list){
			if(listNode.getCode().equals(node.getCode())){
				return true;
			}
		}
		
		return false;
	}
}