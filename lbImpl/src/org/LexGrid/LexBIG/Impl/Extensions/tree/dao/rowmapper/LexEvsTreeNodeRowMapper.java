
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.rowmapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.springframework.jdbc.core.RowMapper;

/**
 * The Class LexEvsTreeNodeRowMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsTreeNodeRowMapper implements RowMapper, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -843416909209638045L;

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		LexEvsTreeNode node = new LexEvsTreeNode();
		node.setCode(rs.getString("childEntityCode"));
		node.setEntityDescription(rs.getString("description"));
		node.setNamespace(rs.getString("entityCodeNamespace"));
		
		return node;
	}
}