
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.MrmapRRFLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.Relations;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

import junit.framework.TestCase;

public class TestLoadMrMap2Mappings extends TestCase {
	
	@LgAdminFunction
	public void testLoadOneMappingFromMRMAP() throws LBException, InterruptedException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, FileNotFoundException{

        LexBIGServiceManager lbsm = getLexBIGServiceManager();

        MrmapRRFLoader loader = (MrmapRRFLoader) lbsm.getLoader("MrMap_Loader");
        MappingRelationsUtil map = new  MappingRelationsUtil();
   	 HashMap<String, Relations> relationsMap = map.processMrSatBean("resources/testData/mrmap_mapping/MRSAT.RRF", "resources/testData/mrmap_mapping/MRMAP.RRF");
       for(Map.Entry<String, Relations> rel: relationsMap.entrySet()){
       loader.load(new File(("resources/testData/mrmap_mapping/MRMAP.RRF")).toURI(), 
        		new File("resources/testData/mrmap_mapping/MRSAT.RRF").toURI(), 
        		null, null, null, rel, true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
        Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
        List<RegistryEntry> entries = registry.getAllRegistryEntries();
        assertTrue(entries.stream().filter(x -> (x.getResourceUri().equals("urn:oid:CL413320.MDR.ICD9CM") || x.getResourceUri().equals("urn:oid:CL413321.MDR.CST"))).anyMatch(y -> y.getStatus().equals("inactive"))); 
        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());

       }
}
    private LexBIGServiceManager getLexBIGServiceManager() throws LBParameterException, LBInvocationException{
    	return LexBIGServiceImpl.defaultInstance().getServiceManager(null);
    }
    
    private LexBIGService getLexBigService(){
    	return LexBIGServiceImpl.defaultInstance();
    }
}