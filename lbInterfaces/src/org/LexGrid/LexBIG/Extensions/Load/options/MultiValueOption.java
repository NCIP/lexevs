
package org.LexGrid.LexBIG.Extensions.Load.options;

import java.util.List;

/**
 * The Interface MultiValueOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MultiValueOption<T> extends BaseOption<List<T>>{

    public List<T> getPickList();
}