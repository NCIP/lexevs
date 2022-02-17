
package edu.mayo.informatics.lexgrid.convert.inserter;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;

/**
 * The Class AbstractPagingCodingSchemeInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractPagingCodingSchemeInserter extends AbstractCodingSchemeInserter implements PagingCodingSchemeInserter{

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.AbstractCodingSchemeInserter#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
     */
    protected List<ResolvedLoadValidationError> doInsertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
 
        List<LoadValidationError> errors = new ArrayList<LoadValidationError>();
        
        errors.addAll(loadNonPagedItems(codingScheme));
        
        String uri = codingScheme.getCodingSchemeURI();
        String version = codingScheme.getRepresentsVersion();
        
        if (codingScheme.getEntities() != null)
        {
            errors.addAll(pageEntities(uri, version, codingScheme.getEntities()));
        }
        
        if (codingScheme.getRelations() != null)
        {
            for(Relations relations : codingScheme.getRelations()) {
                errors.addAll(pageRelations(uri, version, relations));
            }
        }
        
        return doResolveErrors(errors);
    }
    
    protected abstract List<ResolvedLoadValidationError> doResolveErrors(List<LoadValidationError> errors);
    
    /**
     * Load non paged items.
     * 
     * @param codingScheme the coding scheme
     */
    protected abstract List<LoadValidationError> loadNonPagedItems(CodingScheme codingScheme);
    
    /**
     * Page entities.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param codingSchemeVersion the coding scheme version
     * @param entities the entities
     */
    protected abstract List<LoadValidationError> pageEntities(String codingSchemeUri, String codingSchemeVersion, Entities entities);
    
    /**
     * Page relations.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param codingSchemeVersion the coding scheme version
     * @param relations the relations
     */
    protected abstract List<LoadValidationError> pageRelations(String codingSchemeUri, String codingSchemeVersion, Relations relations);
}