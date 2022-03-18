
package org.lexevs.dao.database.ibatis.codingscheme;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.utility.RegistryUtility;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisCodingSchemeDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Transactional
public class IbatisCodingSchemeDaoHistoryTest extends LexEvsDbUnitTestBase {

	/** The ibatis coding scheme dao. */
	@Resource
	private IbatisCodingSchemeDao ibatisCodingSchemeDao;
	
	@Before
	public void loadCodingScheme() throws Exception{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('1', 'csname', 'csuri', 'csversion')");
	}
	
	@Test
	public void getHistoryCodingSchemeByRevisionWithHistoryFromHistoryWithMultiple() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('1', 'csname', 'csuri', 'csversion1', '1')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('1', 'csname', 'csuri', 'csversion2', '2')");
	
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('2', 'rid2', NOW() )");
	
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('1', '1', 'cs', 'NEW', '1', '0')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid,  relativeorder) " +
			"values ('2', '1', 'cs', 'MODIFY', '2', '0')");
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("1", "rid2");
		
		assertEquals("csversion2", cs.getRepresentsVersion());	
	}
	
	@Test
	public void getHistoryCodingSchemeByRevisionWithLocalName() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into csmultiattrib (csMultiAttribGuid, codingSchemeGuid, attributetype, attributevalue, entrystateguid) " +
			"values ('1', '1', 'localName', 'local name', '2')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('1', 'csname', 'csuri', 'csversion1', '1')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('1', 'csname', 'csuri', 'csversion2', '2')");
		
		template.execute("Insert into h_csmultiattrib (csMultiAttribGuid, codingSchemeGuid, attributetype, attributevalue, entrystateguid) " +
			"values ('1', '1', 'localName', 'local name', '2')");
	
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('2', 'rid2', NOW() )");
	
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('1', '1', 'cs', 'NEW', '1', '0')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('2', '1', 'cs', 'MODIFY', '2', '0')");
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("1", "rid2");
		
		assertEquals("local name", cs.getLocalName(0));
	}
	
	@Test
	public void getHistoryCodingSchemeByRevisionWithSource() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into csmultiattrib (csMultiAttribGuid, codingSchemeGuid, attributetype, attributevalue, entrystateguid) " +
			"values ('1', '1', 'source', 'a source', '2')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('1', 'csname', 'csuri', 'csversion1', '1')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('1', 'csname', 'csuri', 'csversion2', '2')");
		
		template.execute("Insert into h_csmultiattrib (csMultiAttribGuid, codingSchemeGuid, attributetype, attributevalue, entrystateguid) " +
			"values ('1', '1', 'source', 'a source', '2')");
	
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('2', 'rid2', NOW() )");
	
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('1', '1', 'cs', 'NEW', '1', '0')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('2', '1', 'cs', 'MODIFY', '2', '0')");
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("1", "rid2");
		
		assertEquals("a source", cs.getSource(0).getContent());
	}
	
	@Test
	public void getHistoryCodingSchemeByRevisionWithHistoryFromHistory() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('1', 'csname', 'csuri', 'csversion2', '2')");
	
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('2', 'rid2', NOW() )");
	
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('1', '1', 'cs', 'NEW', '1', '0')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('2', '1', 'cs', 'MODIFY', '2', '0')");
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("1", "rid2");
		
		assertNotNull(cs);
		
	}
}