
package org.lexgrid.loader.processor.support.validator;

import java.util.List;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

/**
 * The Class ListValidatorAdapter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ListValidatorAdapter<I> implements Validator<List<I>> {

	/** The delegate. */
	private Validator<I> delegate;
	
	/**
	 * Instantiates a new list validator adapter.
	 * 
	 * @param delegate the delegate
	 */
	public ListValidatorAdapter(Validator<I> delegate){
		this.delegate = delegate;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.validator.Validator#validate(java.lang.Object)
	 */
	public void validate(List<I> items) throws ValidationException {
		for(I item : items){
			delegate.validate(item);
		}
	}
}