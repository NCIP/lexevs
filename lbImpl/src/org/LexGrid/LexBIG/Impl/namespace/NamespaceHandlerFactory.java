
package org.LexGrid.LexBIG.Impl.namespace;

import java.io.Serializable;

import org.lexevs.locator.LexEvsServiceLocator;

public class NamespaceHandlerFactory implements Serializable {
    
    private static final long serialVersionUID = 7323618560653009696L;
    
    private transient static NamespaceHandler namespaceHandler;
    
    public static NamespaceHandler getNamespaceHandler() {
        if(namespaceHandler == null) {
            namespaceHandler = 
                LexEvsServiceLocator.getInstance().
                    getCacheWrappingFactory().wrapForCaching(new DefaultNamespaceHandler());
        }
        
        return namespaceHandler;
    }
}