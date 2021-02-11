
package edu.mayo.informatics.lexgrid.convert.inserter;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.processor.ResolverProcessor;
import edu.mayo.informatics.lexgrid.convert.validator.processor.ValidationProcessor;

/**
 * The Class PreValidatingInserterDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PreValidatingInserterDecorator implements CodingSchemeInserter {

    /** The validation processor. */
    private ValidationProcessor<CodingScheme> validationProcessor;
    
    /** The resolver processor. */
    private ResolverProcessor resolverProcessor;
    
    /** The delegate. */
    private CodingSchemeInserter delegate;
    
    /**
     * Instantiates a new pre validating inserter decorator.
     * 
     * @param delegate the delegate
     */
    public PreValidatingInserterDecorator(CodingSchemeInserter delegate){
        this.delegate = delegate;
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.CodingSchemeInserter#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
     */
    public List<ResolvedLoadValidationError> insertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
        List<LoadValidationError> errors = this.validationProcessor.validate(codingScheme);
        List<ResolvedLoadValidationError> resolvedErrors =  this.resolverProcessor.resolve(errors);
        
        resolvedErrors.addAll(delegate.insertCodingScheme(codingScheme));
       
        return resolvedErrors;
    }

    /**
     * Gets the validation processor.
     * 
     * @return the validation processor
     */
    public ValidationProcessor<CodingScheme> getValidationProcessor() {
        return validationProcessor;
    }

    /**
     * Sets the validation processor.
     * 
     * @param validationProcessor the new validation processor
     */
    public void setValidationProcessor(ValidationProcessor<CodingScheme> validationProcessor) {
        this.validationProcessor = validationProcessor;
    }

    /**
     * Gets the resolver processor.
     * 
     * @return the resolver processor
     */
    public ResolverProcessor getResolverProcessor() {
        return resolverProcessor;
    }

    /**
     * Sets the resolver processor.
     * 
     * @param resolverProcessor the new resolver processor
     */
    public void setResolverProcessor(ResolverProcessor resolverProcessor) {
        this.resolverProcessor = resolverProcessor;
    }  
}