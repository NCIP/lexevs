package edu.mayo.informatics.lexgrid.convert.inserter;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

public interface CodingSchemeInserter {

    public void insertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException;
}
