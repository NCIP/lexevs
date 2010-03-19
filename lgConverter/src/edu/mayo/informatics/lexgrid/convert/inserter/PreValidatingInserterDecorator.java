package edu.mayo.informatics.lexgrid.convert.inserter;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.processor.ResolverProcessor;
import edu.mayo.informatics.lexgrid.convert.validator.processor.ValidationProcessor;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport;

public class PreValidatingInserterDecorator implements CodingSchemeInserter {

    private ValidationProcessor<CodingScheme> validationProcessor;
    private ResolverProcessor resolverProcessor;
    
    private CodingSchemeInserter delegate;
    
    public PreValidatingInserterDecorator(CodingSchemeInserter delegate){
        this.delegate = delegate;
    }
    
    public List<ErrorResolutionReport> insertAndValidateCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
        List<LoadValidationError> errors = this.validationProcessor.validate(codingScheme);
        return this.resolverProcessor.resolve(errors);
    }
    
    public void insertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
        delegate.insertCodingScheme(codingScheme);
    }

    public ValidationProcessor<CodingScheme> getValidationProcessor() {
        return validationProcessor;
    }

    public void setValidationProcessor(ValidationProcessor<CodingScheme> validationProcessor) {
        this.validationProcessor = validationProcessor;
    }

    public ResolverProcessor getResolverProcessor() {
        return resolverProcessor;
    }

    public void setResolverProcessor(ResolverProcessor resolverProcessor) {
        this.resolverProcessor = resolverProcessor;
    }  
}
