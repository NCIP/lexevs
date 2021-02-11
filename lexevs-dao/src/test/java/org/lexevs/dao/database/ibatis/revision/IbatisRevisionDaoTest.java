
package org.lexevs.dao.database.ibatis.revision;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Test;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisPropertyDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IbatisRevisionDaoTest extends LexEvsDbUnitTestBase {

	/** The ibatis property dao. */
	@Autowired
	private IbatisRevisionDao ibatisRevisionDao;
	
	@Test
	public void getRevisionIdForFirstDate(){
		final Timestamp date1 = new Timestamp(1l);
		final Timestamp date2 = new Timestamp(2l);
		final Timestamp date3 = new Timestamp(3l);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('1', 'r1', '" + date1.toString() + "')");
		
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('2', 'r2', '" + date2.toString() + "')");

		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('3', 'r3', '" + date3.toString() + "')");
		
		
		assertEquals("r1",this.ibatisRevisionDao.getRevisionIdForDate(date1));
		
	}
	
	@Test
	public void getRevisionIdForSecondDate(){
		final Timestamp date1 = new Timestamp(1l);
		final Timestamp date2 = new Timestamp(2l);
		final Timestamp date3 = new Timestamp(3l);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('1', 'r1', '" + date1.toString() + "')");
		
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('2', 'r2', '" + date2.toString() + "')");

		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('3', 'r3', '" + date3.toString() + "')");
		
		
		assertEquals("r2",this.ibatisRevisionDao.getRevisionIdForDate(date2));
		
	}
	
	@Test
	public void getRevisionIdForThirdDate(){
		final Timestamp date1 = new Timestamp(1l);
		final Timestamp date2 = new Timestamp(2l);
		final Timestamp date3 = new Timestamp(3l);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('1', 'r1', '" + date1.toString() + "')");
		
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('2', 'r2', '" + date2.toString() + "')");

		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('3', 'r3', '" + date3.toString() + "')");
		
		
		assertEquals("r3",this.ibatisRevisionDao.getRevisionIdForDate(date3));
		
	}
	
	@Test
	public void getRevisionIdForAfterDate(){
		final Timestamp date1 = new Timestamp(1l);
		final Timestamp date2 = new Timestamp(2l);
		final Timestamp date3 = new Timestamp(3l);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('1', 'r1', '" + date1.toString() + "')");
		
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('2', 'r2', '" + date2.toString() + "')");

		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('3', 'r3', '" + date3.toString() + "')");
		
		
		assertEquals("r3",this.ibatisRevisionDao.getRevisionIdForDate(new Timestamp(4l)));
		
	}
}