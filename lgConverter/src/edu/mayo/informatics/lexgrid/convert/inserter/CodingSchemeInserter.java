
package edu.mayo.informatics.lexgrid.convert.inserter;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;

/**
 * The Interface CodingSchemeInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodingSchemeInserter {

    /**
     * Insert coding scheme.
     * 
     * @param codingScheme the coding scheme
     * 
     * @throws CodingSchemeAlreadyLoadedException the coding scheme already loaded exception
     */
    public List<ResolvedLoadValidationError> insertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException;
}