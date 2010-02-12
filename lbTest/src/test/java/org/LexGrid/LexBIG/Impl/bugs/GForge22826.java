package org.LexGrid.LexBIG.Impl.bugs;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.loaders.LexGridLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;

public class GForge22826 extends TestCase {

    
    public void testAssociationEntryState() throws LBException {
        CodingSchemeVersionOrTag tagOrVersion = new CodingSchemeVersionOrTag();
        tagOrVersion.setVersion("1.0");
        
        try {
            CodingScheme resolvedCodingScheme = ServiceHolder.instance().getLexBIGService().resolveCodingScheme("Automobiles", tagOrVersion);
            Relations[] relations = resolvedCodingScheme.getRelations();
           
            /* TODO fix this test
            for (int i = 0; i < relations.length; i++) {
                Association[] associations = relations[i].getAssociation();
                
                for (int j = 0; j < associations.length; j++) {
                    String associationName = associations[j].getEntityCode();
                    if( "hasSubtype".equals(associationName) ) {
                        Date effDate = associations[j].getEffectiveDate();
                        Date expDate = associations[j].getExpirationDate();
                        Source owner = associations[j].getOwner();
                        String status = associations[j].getStatus();
                        EntryState entryState = associations[j].getEntryState();
                        
                        assertNotNull(effDate);
                        assertEquals(effDate.getTime(), Timestamp.valueOf(
                                "2001-12-17 09:30:47").getTime());
                        
                        assertNotNull(expDate);
                        assertEquals(expDate.getTime(), Timestamp.valueOf(
                        "2001-12-19 09:30:47").getTime());
                        
                        assertNotNull(owner);
                        assertEquals(owner.getContent(), "lexEVS");
                        
                        assertEquals(status, "Active");
                        
                        assertNotNull(entryState);
                        assertEquals(entryState.getContainingRevision().trim(), "");
                        assertTrue(entryState.getRelativeOrder() == 0L);
                        assertTrue(entryState.getChangeType() == ChangeType.NEW);
                        
                        return;
                    }
                }
                
            }
            */
            fail();
        } catch (LBInvocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LBParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

        LexGridLoaderImpl loader = (LexGridLoaderImpl) lbsm
                .getLoader("LexGridLoader");

        loader.load(new File("resources/testData/Automobiles.xml").toURI(),
                true, false);

        while (loader.getStatus().getEndTime() == null) {
            Thread.sleep(500);
        }

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
        
    }

}
