
package org.lexevs.dao.database.access.codednodegraph;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.MappingExtensionImpl;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.custom.relations.TerminologyMapBean;
import org.LexGrid.relations.Relations;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;

public class MappingCodedNodeDaoFunctionTest extends LexBIGServiceTestCase{
    
    MappingExtension mappingExtension;
    
	@Override
    protected String getTestID() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Before
    public void setUp() throws Exception {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
    }

    @Test
    public void test() throws LBException {
       String relationsContainerName =  ((MappingExtensionImpl)mappingExtension).getDefaultMappingRelationsContainer(
                Constructors.createAbsoluteCodingSchemeVersionReference(MAPPING_SCHEME_URI, MAPPING_SCHEME_VERSION));

        List<TerminologyMapBean> beanList = (List<TerminologyMapBean>) LexEvsServiceLocator.getInstance().getDatabaseServiceManager()
                .getDaoCallbackService().executeInDaoLayer(new DaoCallback<List<TerminologyMapBean>>() {

                    @Override
                    public List<TerminologyMapBean> execute(DaoManager daoManager) {
                        Relations rels = ServiceUtility.getRelationsForMappingScheme(
                                MAPPING_SCHEME_URI, MAPPING_SCHEME_VERSION, relationsContainerName);
                        try {
							String sourceUri = ServiceUtility.getUriForCodingSchemeName(rels.getSourceCodingScheme());
						String sourceVersion = ServiceUtility.getVersion(sourceUri, null);
                        String targetUri = ServiceUtility.getUriForCodingSchemeName(rels.getTargetCodingScheme());
                        String targetVersion = ServiceUtility.getVersion(targetUri, null);
                    	final String mappingUid =  daoManager.getCodingSchemeDao(
                    	        MAPPING_SCHEME_URI, MAPPING_SCHEME_VERSION).
                    	getCodingSchemeUIdByUriAndVersion(MAPPING_SCHEME_URI, MAPPING_SCHEME_VERSION);
                    	final String sourceUid = daoManager.getCodingSchemeDao(
                                sourceUri, sourceVersion).
                        getCodingSchemeUIdByUriAndVersion(sourceUri, sourceVersion);
                    	final String targetUid = daoManager.getCodingSchemeDao(
                                targetUri, targetVersion).
                        getCodingSchemeUIdByUriAndVersion(targetUri, targetVersion);

                        return daoManager.getCodedNodeGraphDao(MAPPING_SCHEME_URI, MAPPING_SCHEME_VERSION)
                                .getMapAndTermsForMappingAndReferences(mappingUid, sourceUid, targetUid, null,
                                        "score");
                        } catch (LBParameterException e) {
                            fail(e.toString());
                        }
                        return null;
                    }
                });
        
        assertNotNull(beanList);
        assertTrue(beanList.size() > 0);
        assertTrue(beanList.stream().anyMatch(x -> x.getSourceCode().equals("Jaguar")));
        assertTrue(beanList.stream().anyMatch(x -> x.getTargetCode().equals("E0001")));
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("Ford") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
    }
    
   
    
    @Test
    public void testExtensionTest(){
        List<TerminologyMapBean> beanList = mappingExtension.resolveBulkMapping(MAPPING_SCHEME_URI, MAPPING_SCHEME_VERSION);
        assertNotNull(beanList);
        assertTrue(beanList.size() > 0);
        assertEquals(beanList.size(), 6);
        assertTrue(beanList.stream().anyMatch(x -> x.getSourceCode().equals("Jaguar")));
        assertTrue(beanList.stream().anyMatch(x -> x.getTargetCode().equals("E0001")));
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("Jaguar") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("A0001") && 
                x.getTargetCode().equals("R0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("C0001") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("005") && 
                x.getTargetCode().equals("P0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("Ford") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("C0002") && 
                x.getTargetCode().equals("P0001")).findAny().isPresent());
        assertTrue(beanList.stream().anyMatch(x -> x.getRel().equals("mapsTo")));
        assertTrue(beanList.stream().anyMatch(x -> x.getRel().equals("hasPart")));
        assertTrue(beanList.stream().anyMatch(x -> x.getSourceName().equals("Automobile")));
        assertTrue(beanList.stream().anyMatch(x -> x.getTargetName().equals("Engine")));
        assertFalse(beanList.stream().anyMatch(x -> x.getSourceName().equals("Engine")));
        assertFalse(beanList.stream().anyMatch(x -> x.getTargetName().equals("Automobile")));
    }
    
    @Test
    public void testExtensionMappingNameTest(){
        List<TerminologyMapBean> beanList = mappingExtension.resolveBulkMapping("Mapping Sample", MAPPING_SCHEME_VERSION);
        assertNotNull(beanList);
        assertTrue(beanList.size() > 0);
        assertEquals(beanList.size(), 6);
        assertTrue(beanList.stream().anyMatch(x -> x.getSourceCode().equals("Jaguar")));
        assertTrue(beanList.stream().anyMatch(x -> x.getTargetCode().equals("E0001")));
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("Jaguar") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("A0001") && 
                x.getTargetCode().equals("R0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("C0001") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("005") && 
                x.getTargetCode().equals("P0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("Ford") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("C0002") && 
                x.getTargetCode().equals("P0001")).findAny().isPresent());
        assertTrue(beanList.stream().anyMatch(x -> x.getRel().equals("mapsTo")));
        assertTrue(beanList.stream().anyMatch(x -> x.getRel().equals("hasPart")));
        assertTrue(beanList.stream().anyMatch(x -> x.getSourceName().equals("Automobile")));
        assertTrue(beanList.stream().anyMatch(x -> x.getTargetName().equals("Engine")));
        assertFalse(beanList.stream().anyMatch(x -> x.getSourceName().equals("Engine")));
        assertFalse(beanList.stream().anyMatch(x -> x.getTargetName().equals("Automobile")));
    }

    @Test
    public void testExtensionMappingFormalNameTest(){
        List<TerminologyMapBean> beanList = mappingExtension.resolveBulkMapping("MappingSample", MAPPING_SCHEME_VERSION);
        assertNotNull(beanList);
        assertTrue(beanList.size() > 0);
        assertEquals(beanList.size(), 6);
        assertTrue(beanList.stream().anyMatch(x -> x.getSourceCode().equals("Jaguar")));
        assertTrue(beanList.stream().anyMatch(x -> x.getTargetCode().equals("E0001")));
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("Jaguar") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("A0001") && 
                x.getTargetCode().equals("R0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("C0001") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("005") && 
                x.getTargetCode().equals("P0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("Ford") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("C0002") && 
                x.getTargetCode().equals("P0001")).findAny().isPresent());
        assertEquals(beanList.stream().filter(x -> x.getSourceCode().equals("Jaguar") && 
                x.getTargetCode().equals("E0001")).findAny().get().getMapRank(), null);
        assertEquals(beanList.stream().filter(x -> x.getSourceCode().equals("A0001") && 
                x.getTargetCode().equals("R0001")).findAny().get().getMapRank(), "1");
        assertEquals(beanList.stream().filter(x -> x.getSourceCode().equals("005") && 
                x.getTargetCode().equals("P0001")).findAny().get().getMapRank(), "2");
        assertTrue(beanList.stream().anyMatch(x -> x.getRel().equals("mapsTo")));
        assertTrue(beanList.stream().anyMatch(x -> x.getRel().equals("hasPart")));
        assertTrue(beanList.stream().anyMatch(x -> x.getSourceName().equals("Automobile")));
        assertTrue(beanList.stream().anyMatch(x -> x.getTargetName().equals("Engine")));
        assertFalse(beanList.stream().anyMatch(x -> x.getSourceName().equals("Engine")));
        assertFalse(beanList.stream().anyMatch(x -> x.getTargetName().equals("Automobile")));
    }
    
    @Test
    public void testExtensionMappingNullVersionTest(){
        List<TerminologyMapBean> beanList = mappingExtension.resolveBulkMapping("MappingSample", null);
        assertNotNull(beanList);
        assertTrue(beanList.size() > 0);
        assertEquals(beanList.size(), 6);
        assertTrue(beanList.stream().anyMatch(x -> x.getSourceCode().equals("Jaguar")));
        assertTrue(beanList.stream().anyMatch(x -> x.getTargetCode().equals("E0001")));
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("Jaguar") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("A0001") && 
                x.getTargetCode().equals("R0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("C0001") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("005") && 
                x.getTargetCode().equals("P0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("Ford") && 
                x.getTargetCode().equals("E0001")).findAny().isPresent());
        assertTrue(beanList.stream().filter(x -> x.getSourceCode().equals("C0002") && 
                x.getTargetCode().equals("P0001")).findAny().isPresent());
        assertTrue(beanList.stream().anyMatch(x -> x.getRel().equals("mapsTo")));
        assertTrue(beanList.stream().anyMatch(x -> x.getRel().equals("hasPart")));
        assertTrue(beanList.stream().anyMatch(x -> x.getSourceName().equals("Automobile")));
        assertTrue(beanList.stream().anyMatch(x -> x.getTargetName().equals("Engine")));
        assertFalse(beanList.stream().anyMatch(x -> x.getSourceName().equals("Engine")));
        assertFalse(beanList.stream().anyMatch(x -> x.getTargetName().equals("Automobile")));
    }

}