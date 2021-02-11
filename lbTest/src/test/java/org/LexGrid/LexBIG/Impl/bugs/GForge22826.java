
package org.LexGrid.LexBIG.Impl.bugs;

import java.io.File;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public class GForge22826 extends TestCase {

    
    public void testAssociationEntryState() throws LBException {
    	CodingSchemeVersionOrTag tagOrVersion = new CodingSchemeVersionOrTag();
    	tagOrVersion.setVersion("1.0");

    	ResolvedConceptReferencesIterator itr = 
    		ServiceHolder.instance().getLexBIGService().
    			getNodeSet("Automobiles", null, Constructors.createLocalNameList("association")).resolve(null, null, null);
    	
    	assert(itr.hasNext());
    }

/**
     * @param args
     */
public static void main(String[] args) {
        try {
            ServiceHolder.configureForSingleConfig();
            
            loadAutomobilesOntology();
            new GForge22826().testAssociationEntryState();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void loadAutomobilesOntology() throws Exception {
        LexBIGServiceManager lbsm = null;

        lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(
                null);

        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
                .getLoader("LexGridLoader");

        loader.load(new File("resources/testData/Automobiles.xml").toURI(),
                true, false);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        
    }

}