package org.lexevs.dao.database.ibatis.codingscheme;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.enums.PropertyType;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
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
    		
    		CodingScheme cs = csdao.getCodingSchemeByUId("3");
    		assertNotNull(cs);
    		assertEquals(cs.getFormalName(),"autos");
    	}

	    @Test
	    public void testGetCodingSchemeUIdByUriAndVersion(){
	       
	        String cs = csdao.getCodingSchemeUIdByUriAndVersion("urn:oid:11.11.0.1", "1.0");
	        assertNotNull(cs);
	        assertEquals(cs,"3");
	    }
	    
	    @Test
	    public void testGetCodingSchemeByUriAndVersion(){
	       
	        CodingScheme cs = csdao.getCodingSchemeByUriAndVersion("urn:oid:11.11.0.1", "1.0");
	        assertNotNull(cs);
	        assertEquals(cs.getCodingSchemeURI(),"urn:oid:11.11.0.1");
	        assertEquals(cs.getRepresentsVersion(),"1.0");
	    }
	    
//	    @Test
//	    public void testetCodingSchemeByNameAndVersion(){
//	       
//	        CodingScheme cs = csdao.getCodingSchemeByNameAndVersion("Automobiles", "1.0");
//	        assertNotNull(cs);
//	        assertEquals(cs.getCodingSchemeURI(),"urn:oid:11.11.0.1");
//	        assertEquals(cs.getRepresentsVersion(),"1.0");
//	    }
	    
	    @Test
	    public void testGetCodingSchemeSummaryByUriAndVersion() {
	       
	        CodingSchemeSummary cs = csdao.getCodingSchemeSummaryByUriAndVersion("urn:oid:11.11.0.1", "1.0");
	        assertNotNull(cs);
	        assertEquals(cs.getCodingSchemeURI(),"urn:oid:11.11.0.1");
	        assertEquals(cs.getRepresentsVersion(),"1.0");
	    }
	    
	    @Test
	    public void testGetHistoryCodingSchemeByRevision() {
	       
        
        	String scs = csdao.getLatestRevision("3");
	        CodingScheme cs = csdao.getHistoryCodingSchemeByRevision("3", scs);
	        assertNotNull(cs);
	        assertEquals(cs.getCodingSchemeURI(),"urn:oid:11.11.0.1");
	        assertEquals(cs.getRepresentsVersion(),"1.0");
	    }
	    
	    @Test
	    public void testGetEntryStateUId() {
	       
	        String cs = csdao.getEntryStateUId("3");
	        assertNotNull(cs);
	        assertEquals(cs,"214");
	    }
	    
	    @Test
	    public void testGetURIMap() {
	       
	        SupportedCodingScheme scs = csdao.getUriMap("3","Automobiles",SupportedCodingScheme.class);
	        assertNotNull(scs);
	        assertEquals(scs.getUri(),"urn:oid:11.11.0.1");
	        assertEquals(scs.getLocalId(),"Automobiles");
	        assertEquals(scs.getContent(),"Automobiles");
	    }
	    
	    
	    @Test
	    public void testGetPropertyUriMapForPropertyType() {
	       
	        
	        List<SupportedProperty> scs = csdao.getPropertyUriMapForPropertyType("3",PropertyTypes.PROPERTY);
	        assertNotNull(scs);
	        assertFalse(scs.stream().anyMatch(x -> x.getLocalId().equals("definition")));
	        assertFalse(scs.stream().anyMatch(x -> x.getLocalId().equals("textualpresentation")));
	        assertTrue(scs.stream().anyMatch(x -> x.getLocalId().equals("genericProperty")));

	    }
	    
	    @Test
	    public void testValidateSupportedAttribute() {
	       
	        
	        assertTrue(csdao.validateSupportedAttribute("3", "is_a", SupportedAssociation.class));
	    }
	    
	    
	    @Test
	    public void testgetMappings() {
	    		
		       
		        
		        Mappings scs = csdao.getMappings("3");
		        assertNotNull(scs);
		        assertEquals(scs.getSupportedPropertyCount(), 3);
		        
	    }
	    
	    
	    @Test
	    public void testgetDistinctPropertyNamesOfCodingScheme() {
    		
	       
	        
	        List<String> scs = csdao.getDistinctPropertyNamesOfCodingScheme("3");
	        assertNotNull(scs);
	        assertEquals(scs.size(), 3);
	        assertTrue(scs.stream().anyMatch(x -> x.equals("definition")));
	        assertTrue(scs.stream().anyMatch(x -> x.equals("textualPresentation")));
	        assertTrue(scs.stream().anyMatch(x -> x.equals("genericProperty")));
	    }
	    
	    @Test
	    public void testgetDistinctPropertyNameAndType() {
    		
	       
	        
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
	    
	    @Test
	    public void testgetDistinctFormatsOfCodingScheme() {
    		
	       
	        
	        List<String> scs = csdao.getDistinctFormatsOfCodingScheme("3");
	        assertNotNull(scs);
	        assertEquals(scs.size(), 2);
	        assertTrue(scs.stream().anyMatch(x -> x.equals("text/plain")));
	        assertTrue(scs.stream().anyMatch(x -> x.equals("textplain")));

	    }
	    
	    @Test
	    public void testgetDistinctPropertyQualifierNamesOfCodingScheme() {
    		
	       
	        
	        List<String> scs = csdao.getDistinctPropertyQualifierNamesOfCodingScheme("3");
	        assertNotNull(scs);
	        assertTrue((scs.size() == 0 || scs.get(0) == null));
	        
	        String uid = csdao.getCodingSchemeUIdByUriAndVersion("urn:oid:2.16.840.1.113883.3.26.1.2", "200902_For_Test");
	        List<String> metaQuals = csdao.getDistinctPropertyQualifierNamesOfCodingScheme(uid);
	        assertNotNull(metaQuals);
	        assertTrue(!(metaQuals.size() == 0));
	        assertEquals(metaQuals.size(),14);
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("SAUI")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("source-code")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("CVF")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("AUI")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("SCUI")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("mrrank")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("SDUI")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("LUI")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("SUI")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("METAUI")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("STYPE")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("ATUI")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("SATUI")));
	        assertTrue(metaQuals.stream().filter(x -> x != null).anyMatch(x -> x.equals("SAUI")));
	    }
	    
	    @Test
	    public void testgetDistinctPropertyQualifierTypesOfCodingScheme() {
    		
	       
	        
	        List<String> scs = csdao.getDistinctPropertyQualifierTypesOfCodingScheme("3");
	        assertNotNull(scs);
	        assertTrue(scs.stream().anyMatch(x -> x.equals("source")));
	    }
	    
	    @Test
	    public void testgetDistinctNamespacesOfCodingScheme() {
    		
	       
	        
	        List<String> scs = csdao.getDistinctNamespacesOfCodingScheme("3");
	        assertNotNull(scs);
	        assertTrue(scs.stream().anyMatch(x -> x.equals("Automobiles")));
	        assertTrue(scs.stream().anyMatch(x -> x.equals("TestForSameCodeNamespace")));
	        assertTrue(scs.stream().anyMatch(x -> x.equals("TestForDifferentNamespaceSameCode")));

	    }
	    
	    @Test
	    public void testgetDistinctEntityTypesOfCodingScheme() {
    		
	       
	        
	        List<String> scs = csdao.getDistinctEntityTypesOfCodingScheme("3");
	        assertNotNull(scs);
	        assertTrue(scs.stream().anyMatch(x -> x.equals("concept")));
	        assertTrue(scs.stream().anyMatch(x -> x.equals("valueDomain")));
	        assertTrue(scs.stream().anyMatch(x -> x.equals("association")));
	    }
	    
	    @Test
	    public void testgetDistinctLanguagesOfCodingScheme() {
    		
	       
	        
	        List<String> scs = csdao.getDistinctLanguagesOfCodingScheme("3");
	        assertNotNull(scs);
	        assertTrue(scs.stream().anyMatch(x -> x.equals("en")));
	    }
	    
	    
	    @Test
	    public void testgetLatestRevision() {
    		
	       
	        
	        String scs = csdao.getLatestRevision("3");
	        assertNotNull(scs);
	        System.out.println(scs);
	    }
	    
	    @Test
	    public void testgetRevisionWhenNew() {
    		
	       
	        
	        String scs = csdao.getRevisionWhenNew("3");
	        assertNotNull(scs);
	        System.out.println(scs);
	    }
	    
	    @Test
	    public void testgetAllCodingSchemeRevisions() {
    		
	       
	        
	        List<String> scs = csdao.getAllCodingSchemeRevisions("3");
	        assertNotNull(scs);
	        scs.stream().forEach(x -> System.out.println(x));
	    }
	    
//		@Test
//		public void testInsertCodingScheme() throws SQLException{
//			CodingScheme cs = new CodingScheme();
//			
//			final Timestamp effectiveDate = new Timestamp(1l);
//			final Timestamp expirationDate = new Timestamp(2l);
//			
//			cs.setCodingSchemeName("csName");
//			cs.setCodingSchemeURI("uri");
//			cs.setRepresentsVersion("1.2");
//			cs.setFormalName("csFormalName");
//			cs.setDefaultLanguage("lang");
//			cs.setApproxNumConcepts(22l);
//			
//			EntityDescription ed = new EntityDescription();
//			ed.setContent("cs Description");
//			cs.setEntityDescription(ed);
//			
//			Text copyright = new Text();
//			copyright.setContent("cs Copyright");
//			cs.setCopyright(copyright);
//			
//			cs.setIsActive(false);
//			
//			cs.setOwner("cs owner");
//			
//			cs.setStatus("testing");
//			
//			cs.setEffectiveDate(effectiveDate);
//			cs.setExpirationDate(expirationDate);
//
//			EntryState es = new EntryState();
//			es.setChangeType(ChangeType.REMOVE);
//			es.setRelativeOrder(22l);
//			cs.setEntryState(es);
//			
//			String id = csdao.insertCodingScheme(cs, null, true);
//			
//			CodingScheme rscs = csdao.getCodingSchemeByUriAndVersion("csName", "id");
//			assertNotNull(rscs);
//			System.out.println(cs.getCodingSchemeName());
//		}
	    

}
