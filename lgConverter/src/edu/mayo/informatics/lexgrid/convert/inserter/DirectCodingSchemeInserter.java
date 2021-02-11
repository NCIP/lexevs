
package edu.mayo.informatics.lexgrid.convert.inserter;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.utility.DaoUtility;

import edu.mayo.informatics.lexgrid.convert.validator.error.FatalError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.WrappingLoadValidationError;

/**
 * The Class DirectCodingSchemeInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DirectCodingSchemeInserter extends AbstractCodingSchemeInserter {

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.AbstractCodingSchemeInserter#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
     */
    protected List<ResolvedLoadValidationError> doInsertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
        try {
            super.getDatabaseServiceManager().
            getAuthoringService().
            loadRevision(codingScheme, null, null);
            
            return new ArrayList<ResolvedLoadValidationError>();
            
        } catch (Exception e) {
            return DaoUtility.createList(ResolvedLoadValidationError.class,
                      new WrappingLoadValidationError(
                              new FatalError(codingScheme, e)));
        }
    } 
}