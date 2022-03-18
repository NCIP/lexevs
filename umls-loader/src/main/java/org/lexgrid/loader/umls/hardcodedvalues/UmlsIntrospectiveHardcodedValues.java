
package org.lexgrid.loader.umls.hardcodedvalues;

import org.LexGrid.relations.Relations;
import org.lexgrid.loader.hardcodedvalues.AbstractIntrospectiveHardcodedValues;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * The Class UmlsIntrospectiveHardcodedValues.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsIntrospectiveHardcodedValues extends AbstractIntrospectiveHardcodedValues {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.hardcodedvalues.AbstractIntrospectiveHardcodedValues#loadObjects()
	 */
	@Override
	public void loadObjects() {
		Relations relation = new Relations();
		relation.setContainerName(RrfLoaderConstants.UMLS_RELATIONS_NAME);
		
		this.getDatabaseServiceManager().
			getRelationService().
			insertRelation(
					this.getCodingSchemeIdSetter().getCodingSchemeUri(), 
					this.getCodingSchemeIdSetter().getCodingSchemeVersion(), 
					relation);

		this.getSupportedAttributeTemplate().addSupportedNamespace(
				this.getCodingSchemeIdSetter().getCodingSchemeUri(), 
				this.getCodingSchemeIdSetter().getCodingSchemeVersion(), 
				this.getCodingSchemeIdSetter().getCodingSchemeName(), 
				null, 
				this.getCodingSchemeIdSetter().getCodingSchemeName(), 
				this.getCodingSchemeIdSetter().getCodingSchemeName());
	}
}