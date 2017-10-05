package org.LexGrid.LexBIG.Impl.loaders;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.lexevs.dao.database.access.association.model.Node;
import org.LexGrid.LexBIG.Impl.loaders.NCItSourceAssertedValueSetUpdateServiceImpl;

public class SourceAssertedValueSetBatchUpdater {
    NCItSourceAssertedValueSetUpdateServiceImpl updater;
    String previousVersion;

    public SourceAssertedValueSetBatchUpdater() {
        // TODO Auto-generated constructor stub
    }

    public SourceAssertedValueSetBatchUpdater(String codingScheme, String previousVersion, String currentVersion, String association,
           String target, String uri, String owner, String source, String conceptDomainIndicator, String schemeUri) {
       updater = new NCItSourceAssertedValueSetUpdateServiceImpl(codingScheme, currentVersion, association,
                target, uri, owner, source, conceptDomainIndicator, schemeUri);
       this.previousVersion = previousVersion;
    }
    
    public void run() {

        List<String> valueSetCodes = updater.resolveUpdatedVSToReferences(previousVersion, updater.getVersion());
        List<Node> finalNodes = null;
        try {
            List<Node> mappedNodes = updater.mapSimpleReferencesToNodes(valueSetCodes);
            finalNodes = updater.getNodeListForUpdate(mappedNodes);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        updater.prepServiceForUpdate(finalNodes);

        updater.loadUpdatedValueSets(finalNodes);
    }
    

}
