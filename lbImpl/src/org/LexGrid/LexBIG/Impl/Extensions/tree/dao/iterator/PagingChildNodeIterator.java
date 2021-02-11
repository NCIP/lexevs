
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.LexEvsTreeDao;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.LexEvsTreeDao.Direction;
import org.LexGrid.LexBIG.Impl.Extensions.tree.listener.NodeAddedListenerSupport;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode.ExpandableStatus;

/**
 * The Class PagingChildNodeIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PagingChildNodeIterator extends NodeAddedListenerSupport implements ChildTreeNodeIterator{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6347213734157954617L;

	/** The lex evs tree dao. */
	private LexEvsTreeDao lexEvsTreeDao;
	
	/** The cached list. */
	private List<LexEvsTreeNode> cachedList = new ArrayList<LexEvsTreeNode>();
	
	/** The known children. */
	private List<LexEvsTreeNode> knownChildren = new ArrayList<LexEvsTreeNode>();
	
	/** The coding scheme. */
	private String codingScheme; 
	
	/** The version or tag. */
	private CodingSchemeVersionOrTag versionOrTag; 
	
	/** The code. */
	private String code; 
	
	/** The namespace. */
	private String namespace; 
	
	/** The direction. */
	private Direction direction; 
	
	/** The association names. */
	private List<String> associationNames;
	
	/** The page size. */
	private int pageSize;
	
	/** The current position. */
	private int currentPosition = 0;
	
	/** The done paging. */
	private boolean donePaging = false;
	
	/** The parent callback. */
	private LexEvsTreeNode parentCallback;
	
	/** The iterator factory. */
	private ChildTreeNodeIteratorFactory iteratorFactory;
	
	/**
	 * Instantiates a new paging child node iterator.
	 */
	public PagingChildNodeIterator(){
		super();
	}
	
	/**
	 * Instantiates a new paging child node iterator.
	 * 
	 * @param lexEvsTreeDao the lex evs tree dao
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * @param direction the direction
	 * @param associationNames the association names
	 * @param pageSize the page size
	 */
	public PagingChildNodeIterator(
			LexEvsTreeDao lexEvsTreeDao,
			String codingScheme, 
			CodingSchemeVersionOrTag versionOrTag, 
			String code, 
			String namespace, 
			Direction direction, 
			List<String> associationNames, 
			int pageSize){
		this.lexEvsTreeDao = lexEvsTreeDao;
		this.codingScheme = codingScheme;
		this.versionOrTag = versionOrTag;
		this.code = code;
		this.namespace = namespace;
		this.direction = direction;
		this.associationNames = associationNames;
		this.pageSize = pageSize;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.iterator.ChildTreeNodeIterator#initIterator(org.lexevs.tree.model.LexEvsTreeNode, boolean)
	 */
	public void initIterator(LexEvsTreeNode parent, boolean countOnly){
		this.setParentCallback(parent);
		this.knownChildren = parent.getPathToRootChildren();
		
		if(this.knownChildren != null && this.knownChildren.size() > 0){
			this.cachedList.addAll(this.knownChildren);
		}
		
		if(countOnly){
			int count = lexEvsTreeDao.getChildrenCount(codingScheme, versionOrTag, code, namespace, direction, associationNames);
			if(count > 0){
				parent.setExpandableStatus(ExpandableStatus.IS_EXPANDABLE);
			} else {
				parent.setExpandableStatus(ExpandableStatus.IS_NOT_EXPANDABLE);
			}
		} else {
			List<LexEvsTreeNode> children = pageChildren();
			if(children == null || children.size() == 0 || children.size() < pageSize){
				donePaging = true;
			}

			cachedList.addAll(children);
			
			if(cachedList == null || cachedList.size() == 0){
				parent.setExpandableStatus(ExpandableStatus.IS_NOT_EXPANDABLE);
			} else {
				parent.setExpandableStatus(ExpandableStatus.IS_EXPANDABLE);
			}
		}
	}
	
	/**
	 * Page children.
	 * 
	 * @return the list< lex evs tree node>
	 */
	protected List<LexEvsTreeNode> pageChildren(){
		List<LexEvsTreeNode> children = lexEvsTreeDao.getChildren(codingScheme, versionOrTag, code, namespace, direction, associationNames, getKnownChildrenCodes(), currentPosition, pageSize);
		for(LexEvsTreeNode child : children){
			iteratorFactory.buildChildNodeIterator(child, true);
			child.addPathToRootParents(parentCallback);
		}
		return children;
	}
	
	/**
	 * Gets the known children codes.
	 * 
	 * @return the known children codes
	 */
	protected List<String> getKnownChildrenCodes(){
		List<String> returnList = new ArrayList<String>();
		if(this.knownChildren != null){
			for(LexEvsTreeNode node : this.knownChildren){
				returnList.add(node.getCode());
			}
		}
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.iterator.ChildTreeNodeIterator#reset()
	 */
	public void reset() {
		currentPosition = 0;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		pageIfNecessary();
		if(cachedList.size() > currentPosition){
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public LexEvsTreeNode next() {
		pageIfNecessary();
		LexEvsTreeNode returnNode = cachedList.get(currentPosition);
		currentPosition++;
		this.fireNodeAddedEvent(returnNode);
		return returnNode;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Page if necessary.
	 */
	protected void pageIfNecessary(){
		if(currentPosition >= cachedList.size() && !donePaging){
			List<LexEvsTreeNode> children = pageChildren();
			if(children == null || children.size() == 0){
				donePaging = true;
				return;
			}
			
			if(cachedList == null){
				cachedList = new ArrayList<LexEvsTreeNode>();
			}
			cachedList.addAll(children);
		}
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

	/**
	 * Sets the iterator factory.
	 * 
	 * @param iteratorFactory the new iterator factory
	 */
	protected void setIteratorFactory(ChildTreeNodeIteratorFactory iteratorFactory) {
		this.iteratorFactory = iteratorFactory;
	}

	/**
	 * Sets the parent callback.
	 * 
	 * @param parentCallback the new parent callback
	 */
	protected void setParentCallback(LexEvsTreeNode parentCallback) {
		this.parentCallback = parentCallback;
	}
}