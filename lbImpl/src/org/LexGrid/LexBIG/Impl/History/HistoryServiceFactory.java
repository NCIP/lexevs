package org.LexGrid.LexBIG.Impl.History;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.service.SystemResourceService;

public class HistoryServiceFactory {

    private static String VERSION_17 = "1.7";
    private static String VERSION_18 = "1.8";
    private static String VERSION_20 = "2.0";

    public HistoryService getHistoryService(String codingScheme) throws LBException {

        SystemResourceService resourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
        
        String version = resourceService.getInternalVersionStringForTag(codingScheme, null);

        String uri = resourceService.getUriForUserCodingSchemeName(codingScheme, version);

        Registry registry = LexEvsServiceLocator.getInstance().getRegistry();

        RegistryEntry entry = registry.getCodingSchemeEntry(Constructors.createAbsoluteCodingSchemeVersionReference(
                uri, version));

        if (entry.getDbSchemaVersion().equals(VERSION_18) || entry.getDbSchemaVersion().equals(VERSION_17)) {
            try {
                
                if (uri.equals(NCIThesaurusHistorySQLQueries.NCIThesaurusURN)) {
                    return new NCIThesaurusHistoryServiceImpl(uri);
                } else if (uri.equals(HistoryService.metaURN)) {
                    return new UMLSHistoryServiceImpl(uri);
                } else {
                    throw new LBParameterException("No history service could be located for", "codingScheme",
                            codingScheme);
                }
            } catch (LBException e) {
                throw new LBException("No history service could be located for codingScheme" + codingScheme, e);
            }

        } else if (entry.getDbSchemaVersion().equals(VERSION_20)) {

            if (uri.equals(NCIThesaurusHistorySQLQueries.NCIThesaurusURN)) {
                return new UriBasedHistoryServiceImpl(uri);
            } else if (uri.equals(HistoryService.metaURN)) {
                return new UriBasedHistoryServiceImpl(uri);
            } else {
                throw new LBParameterException("No history service could be located for", "codingScheme", codingScheme);
            }
        }

        return null;
    }
}
