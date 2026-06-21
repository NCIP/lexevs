package org.lexevs.dao.database.ibatis.association;

import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.relations.AssociationSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.association.model.Triple;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:lexevsDao.xml"})
@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
public class MybatisAssocGraphDBDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	/** The ibatis association dao. */
	@Resource
	private IbatisAssociationDao ibatisAssociationDao;
	
	@Resource
	private IbatisAssociationDataDao ibatisAssociationDataDao;

	
	@Test
	public void getAllTriplesOfCodingScheme() {
		List<Triple> triples = ibatisAssociationDao.getAllTriplesOfCodingScheme(null, null, 0, 0);
	}

	@Test
	public void getAllGraphDbTriplesOfCodingScheme() {
	}
	
	@Test
	public void testGetAllGraphDbTriplesOfCodingScheme() {
	}

	@Test
	public void getAllAncestorTriplesTrOfCodingScheme() {
	}

	@Test
	public void getAllDescendantTriplesTrOfCodingScheme() {
	}
	
    @Test
    public void getTripleByUid() {
    	AssociationSource source = ibatisAssociationDataDao.getTripleByUid(null, null);
    }
	
}
