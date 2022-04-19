
package org.lexevs.dao.database.ibatis.association;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.transaction.
//import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisAssociationDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
//@TransactionConfiguration
public class IbatisAssociationTargetDaoTest extends LexEvsDbUnitTestBase {
	
	/** The ibatis association dao. */
	@Resource
	private IbatisAssociationTargetDao ibatisAssociationTargetDao;
	

	@Test
//	@Transactional
	public void getTriple() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('1', '1', 'c-name')");
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('1', '1', 'apname')");
		
		template.execute("insert into " +
				"entityassnstoentity (" +
				"entityAssnsGuid, " +
				"associationPredicateGuid, " +
				"sourceEntityCode, " +
				"sourceEntityCodeNamespace, " +
				"targetEntityCode, " +
				"targetEntityCodeNamespace " +
				") values " +
				"('1', " +
				"'1'," +
				"'sc'," +
				"'sns'," +
				"'tc'," +
				"'tns')");
		
		template.execute("insert into " +
				"entityassnstodata (" +
				"entityAssnsDataGuid, " +
				"associationPredicateGuid, " +
				"sourceEntityCode, " +
				"sourceEntityCodeNamespace) values " +
				"('2', " +
				"'1'," +
				"'sc'," +
				"'sns')");
		
		template.execute("insert into " +
				"entityassnquals values ( " +
				"'1', " +
				"'2'," +
				"'qualName'," +
				"'qualValue'," +
				"'1' )");
		
		template.execute("insert into " +
				"entityassnquals values ( " +
				"'2', " +
				"'1'," +
				"'qualName'," +
				"'qualValue'," +
				"'2' )");
		
		
		
		AssociationSource source = ibatisAssociationTargetDao.getTripleByUid("1", "1");
		assertNotNull(source);
		
		assertEquals("sc", source.getSourceEntityCode());
		assertEquals("sns", source.getSourceEntityCodeNamespace());
		
		assertEquals(1,source.getTargetCount());
		
		AssociationTarget target = source.getTarget(0);
		
		assertEquals("tc", target.getTargetEntityCode());
		assertEquals("tns", target.getTargetEntityCodeNamespace());
	}
}