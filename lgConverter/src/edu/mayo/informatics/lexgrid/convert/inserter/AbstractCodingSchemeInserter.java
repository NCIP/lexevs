package edu.mayo.informatics.lexgrid.convert.inserter;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.locator.LexEvsServiceLocator;

public abstract class AbstractCodingSchemeInserter implements CodingSchemeInserter {

    public DatabaseServiceManager databaseServiceManager = 
        LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
    
    public abstract void insertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException;

    public DatabaseServiceManager getDatabaseServiceManager() {
        return databaseServiceManager;
    }

    public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
        this.databaseServiceManager = databaseServiceManager;
    } 
}
