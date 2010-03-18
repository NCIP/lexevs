package edu.mayo.informatics.lexgrid.convert.inserter;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

public class DirectCodingSchemeInserter extends AbstractCodingSchemeInserter {

    public void insertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
       super.getDatabaseServiceManager().
           getCodingSchemeService().
           insertCodingScheme(codingScheme);
    } 
}
