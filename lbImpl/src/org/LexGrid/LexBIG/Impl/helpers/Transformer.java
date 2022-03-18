
package org.LexGrid.LexBIG.Impl.helpers;

import java.io.Serializable;

public interface Transformer<I,C,O> extends Serializable {
    
    public O doTransform(C context,I input);

}