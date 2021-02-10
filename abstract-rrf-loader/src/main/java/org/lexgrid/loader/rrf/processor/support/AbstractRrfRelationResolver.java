
package org.lexgrid.loader.rrf.processor.support;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.processor.support.RelationResolver;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class AbstractRrfRelationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractRrfRelationResolver implements RelationResolver<Mrrel> {

	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getContainerName()
	 */
	public abstract String getContainerName();

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getRelation(java.lang.Object)
	 */
	public String getRelation(Mrrel item){
		return item.getRel();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSource(java.lang.Object)
	 */
	public abstract String getSource(Mrrel item);

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getTarget(java.lang.Object)
	 */
	public abstract String getTarget(Mrrel item);

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSourceScheme(java.lang.Object)
	 */
	public String getSourceScheme(Mrrel item) {
		return item.getSab();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getRelationNamespace(java.lang.Object)
	 */
	public String getRelationNamespace(Mrrel item) {
		return codingSchemeIdSetter.getCodingSchemeName();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSourceNamespace(java.lang.Object)
	 */
	public String getSourceNamespace(Mrrel item) {
		return codingSchemeIdSetter.getCodingSchemeName();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getTargetNamespace(java.lang.Object)
	 */
	public String getTargetNamespace(Mrrel item) {
		return codingSchemeIdSetter.getCodingSchemeName();
	}

	/**
	 * Gets the coding scheme name setter.
	 * 
	 * @return the coding scheme name setter
	 */
	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}

	/**
	 * Sets the coding scheme name setter.
	 * 
	 * @param codingSchemeIdSetter the new coding scheme name setter
	 */
	public void setCodingSchemeIdSetter(
			CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}
}