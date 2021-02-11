
package org.lexevs.dao.database.ibatis.association.parameter;

import java.util.List;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertAssociationSourceBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateAssociationTargetBean extends IdableParameterBean{
	
	/** The association predicate id. */
	private String associationPredicateUId;
	
	/** The association source. */
	private AssociationSource associationSource;
	
	/** The association target. */
	private AssociationTarget associationTarget;
	
	/** The association qualifications and usage contexts.*/
	private List<InsertAssociationQualificationOrUsageContextBean> assnQualsAndUsageContextList = null;
	
	/**
	 * Gets the association predicate id.
	 * 
	 * @return the association predicate id
	 */
	public String getAssociationPredicateUId() {
		return associationPredicateUId;
	}
	
	/**
	 * Sets the association predicate id.
	 * 
	 * @param associationPredicateUId the new association predicate id
	 */
	public void setAssociationPredicateUId(String associationPredicateUId) {
		this.associationPredicateUId = associationPredicateUId;
	}
	
	/**
	 * Gets the association source.
	 * 
	 * @return the association source
	 */
	public AssociationSource getAssociationSource() {
		return associationSource;
	}
	
	/**
	 * Sets the association source.
	 * 
	 * @param associationSource the new association source
	 */
	public void setAssociationSource(AssociationSource associationSource) {
		this.associationSource = associationSource;
	}
	
	/**
	 * Gets the association target.
	 * 
	 * @return the association target
	 */
	public AssociationTarget getAssociationTarget() {
		return associationTarget;
	}
	
	/**
	 * Sets the association target.
	 * 
	 * @param associationTarget the new association target
	 */
	public void setAssociationTarget(AssociationTarget associationTarget) {
		this.associationTarget = associationTarget;
	}

	/**
	 * @return the assnQualsAndUsageContext
	 */
	public List<InsertAssociationQualificationOrUsageContextBean> getAssnQualsAndUsageContext() {
		return assnQualsAndUsageContextList;
	}

	/**
	 * @param assnQualsAndUsageContext the assnQualsAndUsageContext to set
	 */
	public void setAssnQualsAndUsageContext(
			List<InsertAssociationQualificationOrUsageContextBean> assnQualsAndUsageContext) {
		this.assnQualsAndUsageContextList = assnQualsAndUsageContext;
	}
}