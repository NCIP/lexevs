
package org.lexevs.tree.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.tree.dao.rowmapper.LexEvsTreeNodeRowMapper;
import org.lexevs.tree.dao.sqlbuilder.GetChildrenSqlBuilder;
import org.lexevs.tree.dao.sqlbuilder.GetNodeSqlBuilder;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.service.ApplicationContextFactory;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class JdbcLexEvsTreeDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class JdbcLexEvsTreeDao extends JdbcDaoSupport implements LexEvsTreeDao {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7492572066371522447L;
	
	/** The get children sql builder. */
	private GetChildrenSqlBuilder getChildrenSqlBuilder;
	
	/** The get node sql builder. */
	private GetNodeSqlBuilder getNodeSqlBuilder;
	
	/** The lex evs tree node row mapper. */
	private RowMapper lexEvsTreeNodeRowMapper = new LexEvsTreeNodeRowMapper();
	
	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.LexEvsTreeDao#getTree(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String, java.lang.String)
	 */
	@Transactional
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
		
			setDataSource();
			String[] arguments = null;
			if(namespace != null){
			 arguments= new String[]{code, namespace};
			}
			else{
				arguments = new String[]{code};
			}
				try {
				   List<LexEvsTreeNode> nodes = this.getJdbcTemplate().query(
                            getNodeSqlBuilder.buildSql(
                                    codingScheme, 
                                    versionOrTag, 
                                    code, 
                                    namespace), arguments, new RowMapper(){

                                        public Object mapRow(ResultSet rs,
                                                int rowNum) throws SQLException {
                                            LexEvsTreeNode node = new LexEvsTreeNode();
                                            node.setCode(rs.getString("entityCode"));
                                            node.setEntityDescription(rs.getString("description"));
                                            node.setNamespace(rs.getString("entityCodeNamespace"));
                                            return node;
                                        }
                    });
                   if(namespace != null){
				   for(LexEvsTreeNode node: nodes){

				       if(node != null && node.getNamespace().equals(namespace)){
				           return node;
				       }
				       }
				    return null;
				   }
				return nodes.get(0);
				} catch (EmptyResultDataAccessException e) {
					try {
						if(! ServiceUtility.isSupplement(codingScheme, versionOrTag)) {
							throw e;
						} else {
							AbsoluteCodingSchemeVersionReference parent = 
								ServiceUtility.getParentOfSupplement(codingScheme, versionOrTag);
							
							return this.getNode(
									parent.getCodingSchemeURN(), 
									Constructors.createCodingSchemeVersionOrTagFromVersion(parent.getCodingSchemeVersion()), 
									code, 
									namespace);
						}
					} catch (LBParameterException e2) {
						throw e;
					}
				}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.LexEvsTreeDao#getChildrenCount(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String, java.lang.String, org.lexevs.tree.dao.LexEvsTreeDao.Direction, java.util.List)
	 */
	@Transactional
	public int getChildrenCount(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String code,
			String namespace, Direction direction, List<String> associationNames) {
		
		setDataSource();
		
		return this.getJdbcTemplate().queryForObject(
				getChildrenSqlBuilder.buildSql(
						codingScheme, 
						versionOrTag, 
						code, 
						namespace, 
						direction, 
						associationNames,
						null,
						0,
						0,
						true), Integer.class).intValue();
	}
	

	/**
	 * Gets the gets the children sql builder.
	 * 
	 * @return the gets the children sql builder
	 */
	public GetChildrenSqlBuilder getGetChildrenSqlBuilder() {
		return getChildrenSqlBuilder;
	}

	/**
	 * Sets the gets the children sql builder.
	 * 
	 * @param getChildrenSqlBuilder the new gets the children sql builder
	 */
	public void setGetChildrenSqlBuilder(GetChildrenSqlBuilder getChildrenSqlBuilder) {
		this.getChildrenSqlBuilder = getChildrenSqlBuilder;
	}

	/**
	 * Gets the gets the node sql builder.
	 * 
	 * @return the gets the node sql builder
	 */
	public GetNodeSqlBuilder getGetNodeSqlBuilder() {
		return getNodeSqlBuilder;
	}

	/**
	 * Sets the gets the node sql builder.
	 * 
	 * @param getNodeSqlBuilder the new gets the node sql builder
	 */
	public void setGetNodeSqlBuilder(GetNodeSqlBuilder getNodeSqlBuilder) {
		this.getNodeSqlBuilder = getNodeSqlBuilder;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.LexEvsTreeDao#getChildren(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String, java.lang.String, org.lexevs.tree.dao.LexEvsTreeDao.Direction, java.util.List, java.util.List, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<LexEvsTreeNode> getChildren(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String code,
			String namespace, Direction direction,
			List<String> associationNames, List<String> knownCodes, int start,
			int limit) {
		
		setDataSource();
		
		return (List<LexEvsTreeNode>) this.getJdbcTemplate().query(
				getChildrenSqlBuilder.buildSql(
						codingScheme, 
						versionOrTag, 
						code, 
						namespace, 
						direction, 
						associationNames, 
						knownCodes,
						start, 
						limit,
						false), lexEvsTreeNodeRowMapper);
	}

	/**
	 * Sets the data source.
	 */
	public void setDataSource() {
		if(this.getDataSource() == null){
			ApplicationContext ctx = ApplicationContextFactory.getInstance().getApplicationContext();

			DataSource ds = (DataSource)ctx.getBean("dataSource");
			JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
			this.setJdbcTemplate(jdbcTemplate);
		}
	}
}