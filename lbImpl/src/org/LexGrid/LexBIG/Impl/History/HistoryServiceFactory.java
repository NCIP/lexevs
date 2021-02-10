
package org.LexGrid.LexBIG.Impl.History;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.apache.commons.lang.StringUtils;
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
        
        String uri = null;
        try {
            String version = resourceService.getInternalVersionStringForTag(codingScheme, null);

            uri = resourceService.getUriForUserCodingSchemeName(codingScheme, version);
        } catch (LBException e1) {
            
            //if the coding scheme isn't loaded, we can still continue if the user
            //passed in the exact uri
            if(codingScheme.equals(NCIThesaurusHistorySQLQueries.NCIThesaurusURN)) {
                uri = NCIThesaurusHistorySQLQueries.NCIThesaurusURN;
            }
            if(codingScheme.equals(HistoryService.metaURN)) {
                uri = HistoryService.metaURN;
            }
            if(StringUtils.isBlank(uri)) {
                throw e1;
            }
        }

        Registry registry = LexEvsServiceLocator.getInstance().getRegistry();

        RegistryEntry entry = registry.getNonCodingSchemeEntry(uri);

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