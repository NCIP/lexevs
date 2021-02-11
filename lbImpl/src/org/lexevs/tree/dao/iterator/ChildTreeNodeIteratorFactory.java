
package org.lexevs.tree.dao.iterator;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.lexevs.tree.dao.LexEvsTreeDao;
import org.lexevs.tree.dao.LexEvsTreeDao.Direction;
import org.lexevs.tree.listener.NodeAddedListenerSupport;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.dao.iterator.PagingChildNodeIterator;

/**
 * A factory for creating ChildTreeNodeIterator objects.
 */
@Deprecated
public class ChildTreeNodeIteratorFactory extends NodeAddedListenerSupport implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6875624940847217861L;
	
	/** The lex evs tree dao. */
	private LexEvsTreeDao lexEvsTreeDao;
	
	/** The coding scheme. */
	private String codingScheme; 
	
	/** The version or tag. */
	private CodingSchemeVersionOrTag versionOrTag; 
	
	/** The direction. */
	private Direction direction; 
	
	/** The association names. */
	private List<String> associationNames;
	
	/** The page size. */
	private int pageSize;
	
	/**
	 * Instantiates a new child tree node iterator factory.
	 * 
	 * @param lexEvsTreeDao the lex evs tree dao
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param direction the direction
	 * @param associationNames the association names
	 * @param pageSize the page size
	 */
	public ChildTreeNodeIteratorFactory(
			LexEvsTreeDao lexEvsTreeDao,
			String codingScheme, 
			CodingSchemeVersionOrTag versionOrTag, 
			Direction direction, 
			List<String> associationNames, 
			int pageSize){
		this.lexEvsTreeDao = lexEvsTreeDao;
		this.codingScheme = codingScheme;
		this.versionOrTag = versionOrTag;
		this.direction = direction;
		this.associationNames = associationNames;
		this.pageSize = pageSize;
	}
	
	/**
	 * Builds the child node iterator.
	 * 
	 * @param node the node
	 * @param countOnly the count only
	 */
	public void buildChildNodeIterator(LexEvsTreeNode node, boolean countOnly){
		this.fireNodeAddedEvent(node);
		PagingChildNodeIterator itr = new PagingChildNodeIterator(
				lexEvsTreeDao,
				codingScheme, 
				versionOrTag, 
				node.getCode(), 
				node.getNamespace(), 
				direction, 
				associationNames, 
				pageSize);
		itr.setIteratorFactory(this);
		itr.setNodeAddedListener(this.getNodeAddedListener());
		node.setChildIterator(itr, countOnly);
	}
}