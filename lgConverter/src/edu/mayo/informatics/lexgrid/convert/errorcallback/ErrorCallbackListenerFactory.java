
package edu.mayo.informatics.lexgrid.convert.errorcallback;

import edu.mayo.informatics.lexgrid.convert.inserter.resolution.EntityBatchInsertResolver;

public class ErrorCallbackListenerFactory {

    public static ErrorResolvingErrorCallbackListener getErrorCallbackListener() {
        ErrorResolvingErrorCallbackListener listener = new ErrorResolvingErrorCallbackListener();
        listener.addResolver(new EntityBatchInsertResolver());
        
        return listener;
    }
}