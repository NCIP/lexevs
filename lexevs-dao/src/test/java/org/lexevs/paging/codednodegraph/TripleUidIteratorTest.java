
package org.lexevs.paging.codednodegraph;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.junit.Test;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.utility.RegistryUtility;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * The Class IbatisAssociationDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@TransactionConfiguration
public class TripleUidIteratorTest extends LexEvsDbUnitTestBase {
	
	@Resource
	private Registry registry;
	
	@Test
	public void testGetTripleUids() throws Exception{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('1', '1', 'aname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("insert into entityassnstoentity" +
				" values ('2'," +
				" '1'," +
				" 's-code1', " +
				" 's-ns1'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		TripleUidIterator itr = new TripleUidIterator("csuri", "csversion", "c-name", "aname", "s-code", "s-ns", new GraphQuery(), TripleNode.SUBJECT, null, 5);
		
		assertTrue(itr.hasNext());
		assertEquals(itr.next(), "1");
		assertFalse(itr.hasNext());
	}
	
	
}