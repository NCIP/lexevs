
package org.lexgrid.loader.processor.support.validator;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

/**
 * The Class NamespaceSizeValidator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NamespaceSizeValidator implements Validator<AssociationSource> {
	
	/** The max namespace length. */
	private int maxNamespaceLength = 50;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.validator.Validator#validate(java.lang.Object)
	 */
	public void validate(AssociationSource item) throws ValidationException {
		//If for some reason the processor gives us null -- consider that validated.
		if(item == null){
			return;
		}
		
		List<String> toValidate = new ArrayList<String>();
		
		String sourceNamespace = item.getSourceEntityCodeNamespace();
		toValidate.add(sourceNamespace);
		
		for(AssociationTarget target : item.getTarget()){
			toValidate.add(target.getTargetEntityCodeNamespace());
		}
		
		for(String namespace : toValidate){
			if(namespace.length() >= 50){
				throw new ValidationException("Namespace: " + namespace + " is too long to be inserted into the database... record is skipped.");
			}
		}
	}

	/**
	 * Gets the max namespace length.
	 * 
	 * @return the max namespace length
	 */
	public int getMaxNamespaceLength() {
		return maxNamespaceLength;
	}

	/**
	 * Sets the max namespace length.
	 * 
	 * @param maxNamespaceLength the new max namespace length
	 */
	public void setMaxNamespaceLength(int maxNamespaceLength) {
		this.maxNamespaceLength = maxNamespaceLength;
	}

}