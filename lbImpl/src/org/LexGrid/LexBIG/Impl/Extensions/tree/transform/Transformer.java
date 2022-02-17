
package org.LexGrid.LexBIG.Impl.Extensions.tree.transform;

import java.io.Serializable;

/**
 * The Interface Transformer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface Transformer<I,O> extends Serializable {

	/**
	 * Transform.
	 * 
	 * @param input the input
	 * 
	 * @return the o
	 */
	public O transform(I input);
}