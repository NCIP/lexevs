
package edu.mayo.informatics.lexgrid.convert.inserter;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;

/**
 * The Class AbstractCodingSchemeInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractCodingSchemeInserter implements CodingSchemeInserter {

    /** The database service manager. */
    private DatabaseServiceManager databaseServiceManager = 
        LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
    
    private SystemResourceService systemResourceService = 
        LexEvsServiceLocator.getInstance().getSystemResourceService();
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.CodingSchemeInserter#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
     */
    public List<ResolvedLoadValidationError> insertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
        
        List<ResolvedLoadValidationError> errors = doInsertCodingScheme(codingScheme);
        systemResourceService.refresh();
        
        return errors;
    }

    protected abstract List<ResolvedLoadValidationError> doInsertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException;
    /**
     * Gets the database service manager.
     * 
     * @return the database service manager
     */
    public DatabaseServiceManager getDatabaseServiceManager() {
        return databaseServiceManager;
    }

    /**
     * Sets the database service manager.
     * 
     * @param databaseServiceManager the new database service manager
     */
    public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
        this.databaseServiceManager = databaseServiceManager;
    }

    public void setSystemResourceService(SystemResourceService systemResourceService) {
        this.systemResourceService = systemResourceService;
    }

    public SystemResourceService getSystemResourceService() {
        return systemResourceService;
    } 
}