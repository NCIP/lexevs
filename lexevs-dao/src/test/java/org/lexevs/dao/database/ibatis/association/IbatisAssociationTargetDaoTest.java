/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.ibatis.association;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisAssociationDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@TransactionConfiguration
public class IbatisAssociationTargetDaoTest extends LexEvsDbUnitTestBase {
	
	/** The ibatis association dao. */
	@Resource
	private IbatisAssociationTargetDao ibatisAssociationTargetDao;
	

	@Test
	@Transactional
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
