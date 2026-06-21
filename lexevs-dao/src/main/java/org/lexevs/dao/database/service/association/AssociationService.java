
package org.lexevs.dao.database.service.association;

import java.util.List;

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.lexevs.dao.database.ibatis.association.parameter.BatchAssociationInsertBean;

/**
 * The Interface AssociationService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface AssociationService {
	
	/** The Constant INSERT_ASSOCIATIONSOURCE_ERROR. */
	public final static String INSERT_ASSOCIATIONSOURCE_ERROR = "INSERT-ASSOCIATIONSOURCE-ERROR";
	
	/** The Constant INSERT_ASSOCIATIONPREDICATE_ERROR. */
	public final static String INSERT_ASSOCIATIONPREDICATE_ERROR = "INSERT-ASSOCIATIONPREDICATE-ERROR";

	/**
	 * Insert association source.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationContainerName the relation container name
	 * @param associationPredicateName the association predicate name
	 * @param source the source
	 */
	public void insertAssociationSource(String codingSchemeUri, String version,
			String relationContainerName, String associationPredicateName,
			AssociationSource source);
	
	public void insertAssociationSourceBatch(String codingSchemeUri, String version,
			List<BatchAssociationInsertBean> sources);
	
	/**
	 * Insert association predicate.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param relationsName the relations name
	 * @param predicate the predicate
	 */
	public void insertAssociationPredicate(
			String codingSchemeUri, String version, String relationsName, AssociationPredicate predicate);

	/**
	 * Gets the association triple by association instance id.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param associationInstanceId the association instance id
	 * 
	 * @return the association triple by association instance id
	 */
	public AssociationTriple getAssociationTripleByAssociationInstanceId(
			String codingSchemeUri, 
			String version,
			String associationInstanceId);
	
	/**
	 * The Class AssociationTriple.
	 */
	public static class AssociationTriple {
		
		/** The association source. */
		private AssociationSource associationSource;
		
		/** The relation container name. */
		private String relationContainerName;
		
		/** The association predicate name. */
		private String associationPredicateName;

		/**
		 * Instantiates a new association triple.
		 * 
		 * @param associationSource the association source
		 * @param relationContainerName the relation container name
		 * @param associationPredicateName the association predicate name
		 */
		public AssociationTriple(AssociationSource associationSource,
				String relationContainerName, String associationPredicateName) {
			super();
			this.associationSource = associationSource;
			this.relationContainerName = relationContainerName;
			this.associationPredicateName = associationPredicateName;
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
		 * Gets the relation container name.
		 * 
		 * @return the relation container name
		 */
		public String getRelationContainerName() {
			return relationContainerName;
		}
		
		/**
		 * Sets the relation container name.
		 * 
		 * @param relationContainerName the new relation container name
		 */
		public void setRelationContainerName(String relationContainerName) {
			this.relationContainerName = relationContainerName;
		}
		
		/**
		 * Gets the association predicate name.
		 * 
		 * @return the association predicate name
		 */
		public String getAssociationPredicateName() {
			return associationPredicateName;
		}
		
		/**
		 * Sets the association predicate name.
		 * 
		 * @param associationPredicateName the new association predicate name
		 */
		public void setAssociationPredicateName(String associationPredicateName) {
			this.associationPredicateName = associationPredicateName;
		}
	}
}