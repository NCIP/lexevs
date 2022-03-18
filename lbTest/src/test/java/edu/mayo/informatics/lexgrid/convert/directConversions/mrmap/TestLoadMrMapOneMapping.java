
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.MrmapRRFLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.relations.Relations;

public class TestLoadMrMapOneMapping extends TestCase {
public void testLoadOneMappingFromMRMAP() throws LBException, InterruptedException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, FileNotFoundException{

        LexBIGServiceManager lbsm = getLexBIGServiceManager();
        MappingRelationsUtil map = new  MappingRelationsUtil();
   	 HashMap<String, Relations> relationsMap = map.processMrSatBean("resources/testData/mrmap_mapping/MRSAT1.RRF", "resources/testData/mrmap_mapping/MRMAP1.RRF");
       for(Map.Entry<String, Relations> rel: relationsMap.entrySet()){
        MrmapRRFLoader loader = (MrmapRRFLoader) lbsm.getLoader("MrMap_Loader");

        loader.load(new File(("resources/testData/mrmap_mapping/MRMAP1.RRF")).toURI(), 
        		new File("resources/testData/mrmap_mapping/MRSAT1.RRF").toURI(), 
        		null, null, null, rel, true, true);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
        Thread.sleep(6000);
       }
}
    private LexBIGServiceManager getLexBIGServiceManager() throws LBParameterException, LBInvocationException{
    	return LexBIGServiceImpl.defaultInstance().getServiceManager(null);
    }
}