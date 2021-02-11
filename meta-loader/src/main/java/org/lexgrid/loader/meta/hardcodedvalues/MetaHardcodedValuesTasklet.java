
package org.lexgrid.loader.meta.hardcodedvalues;

import org.LexGrid.relations.Relations;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.hardcodedvalues.AbstractIntrospectiveHardcodedValues;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * A factory for creating MetaHardcodedValues objects.
 */
public class MetaHardcodedValuesTasklet extends AbstractIntrospectiveHardcodedValues {
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.hardcodedvalues.AbstractHardcodedValuesFactory#buildList(java.util.List)
	 */
	@Override
	public void loadObjects() {
		Relations relation = new Relations();

		relation.setContainerName(RrfLoaderConstants.UMLS_RELATIONS_NAME);

		this.getSupportedAttributeTemplate().addSupportedContainerName(
				this.getCodingSchemeIdSetter().getCodingSchemeUri(), 
				this.getCodingSchemeIdSetter().getCodingSchemeVersion(), 
				RrfLoaderConstants.UMLS_RELATIONS_NAME, 
				null, 
				RrfLoaderConstants.UMLS_RELATIONS_NAME);	
		
		this.getSupportedAttributeTemplate().addSupportedNamespace(
				getCodingSchemeIdSetter().getCodingSchemeUri(), 
				getCodingSchemeIdSetter().getCodingSchemeVersion(), 
				getCodingSchemeIdSetter().getCodingSchemeName(), 
				null, 
				getCodingSchemeIdSetter().getCodingSchemeName(), 
				getCodingSchemeIdSetter().getCodingSchemeName());
		
		this.getDatabaseServiceManager().
		getRelationService().
		insertRelation(
				this.getCodingSchemeIdSetter().getCodingSchemeUri(), 
				this.getCodingSchemeIdSetter().getCodingSchemeVersion(), 
				relation);
	}
}