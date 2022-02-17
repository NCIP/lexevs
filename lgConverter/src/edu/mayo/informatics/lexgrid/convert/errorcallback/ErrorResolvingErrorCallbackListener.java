
package edu.mayo.informatics.lexgrid.convert.errorcallback;

import java.util.Arrays;
import java.util.List;

import org.lexevs.dao.database.service.error.DatabaseError;
import org.lexevs.dao.database.service.error.ErrorCallbackListener;
import org.lexevs.logging.LoggerFactory;

import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.WrappingLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.processor.DefaultResolverProcessor;

public class ErrorResolvingErrorCallbackListener extends DefaultResolverProcessor implements ErrorCallbackListener {

    @Override
    public void onDatabaseError(DatabaseError databaseError) {
        List<ResolvedLoadValidationError> errors = this.resolve(Arrays.asList(new WrappingLoadValidationError(databaseError)));
        
        for(ResolvedLoadValidationError resolvedError : errors) {
               LoggerFactory.getLogger().loadLogError(resolvedError.toString(), null);
        }   
    }
}