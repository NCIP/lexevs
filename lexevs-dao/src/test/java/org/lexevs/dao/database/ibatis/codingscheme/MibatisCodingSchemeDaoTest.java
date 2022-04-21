package org.lexevs.dao.database.ibatis.codingscheme;



import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:lexevsDao.xml"})
public class MibatisCodingSchemeDaoTest extends AbstractTransactionalJUnit4SpringContextTests{
	

    @Autowired
    private IbatisCodingSchemeDao csdao;



	    @Test
	    public void testEmployee(){
	        System.out.println("test cs");
	        String cs = csdao.getCodingSchemeUIdByUriAndVersion("urn:oid:2.16.840.1.113883.6.90", "2021");
	        assertNotNull(cs);
	    }

}
