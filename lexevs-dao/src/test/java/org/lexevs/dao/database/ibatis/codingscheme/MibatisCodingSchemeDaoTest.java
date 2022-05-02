package org.lexevs.dao.database.ibatis.codingscheme;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.enums.PropertyType;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:lexevsDao.xml"})
@Transactional(readOnly=false)
public class MibatisCodingSchemeDaoTest extends AbstractTransactionalJUnit4SpringContextTests{
	

    @Autowired
    private IbatisCodingSchemeDao csdao;

    
    	@Test
    	public void testGetCodingSchemeByUId(){
    		System.out.println("test cs");
    		CodingScheme cs = csdao.getCodingSchemeByUId("3");
    		assertNotNull(cs);
    		assertEquals(cs.getFormalName(),"autos");
    	}

	    @Test
	    public void testGetCodingSchemeUIdByUriAndVersion(){
	        System.out.println("test cs");
	        String cs = csdao.getCodingSchemeUIdByUriAndVersion("urn:oid:11.11.0.1", "1.0");
	        assertNotNull(cs);
	        assertEquals(cs,"3");
	    }
	    
	    @Test
	    public void testGetCodingSchemeByUriAndVersion(){
	        System.out.println("test cs");
	        CodingScheme cs = csdao.getCodingSchemeByUriAndVersion("urn:oid:11.11.0.1", "1.0");
	        assertNotNull(cs);
	        assertEquals(cs.getCodingSchemeURI(),"urn:oid:11.11.0.1");
	        assertEquals(cs.getRepresentsVersion(),"1.0");
	    }
	    
//	    @Test
//	    public void testetCodingSchemeByNameAndVersion(){
//	        System.out.println("test cs");
//	        CodingScheme cs = csdao.getCodingSchemeByNameAndVersion("Automobiles", "1.0");
//	        assertNotNull(cs);
//	        assertEquals(cs.getCodingSchemeURI(),"urn:oid:11.11.0.1");
//	        assertEquals(cs.getRepresentsVersion(),"1.0");
//	    }
	    
	    @Test
	    public void testGetCodingSchemeSummaryByUriAndVersion() {
	        System.out.println("test cs");
	        CodingSchemeSummary cs = csdao.getCodingSchemeSummaryByUriAndVersion("urn:oid:11.11.0.1", "1.0");
	        assertNotNull(cs);
	        assertEquals(cs.getCodingSchemeURI(),"urn:oid:11.11.0.1");
	        assertEquals(cs.getRepresentsVersion(),"1.0");
	    }
	    
//	    @Test
//	    public void testGetHistoryCodingSchemeByRevision() {
//	        System.out.println("test cs");
//	        CodingScheme cs = csdao.getHistoryCodingSchemeByRevision("urn:oid:11.11.0.1", "1.0");
//	        assertNotNull(cs);
//	        assertEquals(cs.getCodingSchemeURI(),"urn:oid:11.11.0.1");
//	        assertEquals(cs.getRepresentsVersion(),"1.0");
//	    }
	    
	    @Test
	    public void testGetEntryStateUId() {
	        System.out.println("test cs");
	        String cs = csdao.getEntryStateUId("3");
	        assertNotNull(cs);
	        assertEquals(cs,"214");
	    }
	    
	    @Test
	    public void testGetURIMap() {
	        System.out.println("test cs");
	        SupportedCodingScheme scs = csdao.getUriMap("3","Automobiles",SupportedCodingScheme.class);
	        assertNotNull(scs);
	        assertEquals(scs.getUri(),"urn:oid:11.11.0.1");
	        assertEquals(scs.getLocalId(),"Automobiles");
	        assertEquals(scs.getContent(),"Automobiles");
	    }
	    
	    
	    @Test
	    public void testGetPropertyUriMapForPropertyType() {
	        System.out.println("test cs");
	        
	        List<SupportedProperty> scs = csdao.getPropertyUriMapForPropertyType("3",PropertyTypes.PROPERTY);
	        assertNotNull(scs);
	        assertFalse(scs.stream().anyMatch(x -> x.getLocalId().equals("definition")));
	        assertFalse(scs.stream().anyMatch(x -> x.getLocalId().equals("textualpresentation")));
	        assertTrue(scs.stream().anyMatch(x -> x.getLocalId().equals("genericProperty")));

	    }
	    
	    @Test
	    public void testValidateSupportedAttribute() {
	        System.out.println("test cs");
	        
	        assertTrue(csdao.validateSupportedAttribute("3", "is_a", SupportedAssociation.class));
	    }
	    
	    
	    @Test
	    public void testgetMappings() {
	    		
		        System.out.println("test cs");
		        
		        Mappings scs = csdao.getMappings("3");
		        assertNotNull(scs);
		        assertEquals(scs.getSupportedPropertyCount(), 3);
		        
	    }
	    
	    
	    @Test
	    public void testgetDistinctPropertyNamesOfCodingScheme() {
    		
	        System.out.println("test cs");
	        
	        List<String> scs = csdao.getDistinctPropertyNamesOfCodingScheme("3");
	        assertNotNull(scs);
	        assertEquals(scs.size(), 3);
	        assertTrue(scs.stream().anyMatch(x -> x.equals("definition")));
	        assertTrue(scs.stream().anyMatch(x -> x.equals("textualPresentation")));
	        assertTrue(scs.stream().anyMatch(x -> x.equals("genericProperty")));
	    }
	    
	    @Test
	    public void testgetDistinctPropertyNameAndType() {
    		
	        System.out.println("test cs");
	        
	        List<NameAndValue> scs = csdao.getDistinctPropertyNameAndType("3");
	    	        assertNotNull(scs);
	    	        assertEquals(scs.size(), 3);
	    	        assertTrue(scs.stream().anyMatch(x -> x.getName().equals("definition")));
	    	        assertTrue(scs.stream().anyMatch(x -> x.getName().equals("textualPresentation")));
	    	        assertTrue(scs.stream().anyMatch(x -> x.getName().equals("genericProperty")));
	    	        assertTrue(scs.stream().anyMatch(x -> x.getContent().equals("definition")));
	    	        assertTrue(scs.stream().anyMatch(x -> x.getContent().equals("presentation")));
	    	        assertTrue(scs.stream().anyMatch(x -> x.getContent().equals("property")));
	    	    }

}
