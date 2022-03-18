
package org.LexGrid.LexBIG.Extensions.Load.options;

import java.util.List;

/**
 * The Interface Option.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface Option<T> extends BaseOption<T> {

    public List<T> getPickList();
}