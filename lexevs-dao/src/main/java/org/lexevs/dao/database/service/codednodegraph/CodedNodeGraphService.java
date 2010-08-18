package org.lexevs.dao.database.service.codednodegraph;

import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.service.codednodegraph.model.ColumnSortType;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;

public interface CodedNodeGraphService {
	
	public enum Order {ASC, DESC}
	
	public static class Sort {
		private ColumnSortType columnSortType;
		private Order order;
		
		public Sort(ColumnSortType columnSortType, Order order) {
			super();
			this.columnSortType = columnSortType;
			this.order = order;
		}

		public ColumnSortType getColumnSortType() {
			return columnSortType;
		}

		public Order getOrder() {
			return order;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((columnSortType == null) ? 0 : columnSortType.hashCode());
			result = prime * result + ((order == null) ? 0 : order.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Sort other = (Sort) obj;
			if (columnSortType == null) {
				if (other.columnSortType != null)
					return false;
			} else if (!columnSortType.equals(other.columnSortType))
				return false;
			if (order == null) {
				if (other.order != null)
					return false;
			} else if (!order.equals(other.order))
				return false;
			return true;
		}
	}
	
	public List<String> listCodeRelationships(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace, 
			String targetEntityCode,
			String targetEntityCodeNamespace, 
			GraphQuery query,
			boolean useTransitive);

	public List<ConceptReference> getRootConceptReferences(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			List<String> associationPredicateNames, 
			List<QualifierNameValuePair> qualifiers,
			List<String> subjectEntityCodeNamespaces, 
			List<String> objectEntityCodeNamespaces, 
			TraverseAssociations traverse);
	
	public List<ConceptReference> getTailConceptReferences(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			List<String> associationPredicateNames,
			List<QualifierNameValuePair> qualifiers,
			List<String> subjectEntityCodeNamespaces,
			List<String> objectEntityCodeNamespaces,  
			TraverseAssociations traverse);
	
	public List<String> getTripleUidsContainingSubject(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String associationPredicateName,
			String subjectEntityCode,
			String subjectEntityCodeNamespace, 
			GraphQuery query,
			List<Sort> sorts,
			int start, 
			int pageSize);
	
	public Map<String, Integer> getTripleUidsContainingSubjectCount(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String subjectEntityCode,
			String subjectEntityCodeNamespace,
			GraphQuery query);
	
	public List<String> getTripleUidsContainingObject(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String associationPredicateName,
			String objectEntityCode,
			String objectEntityCodeNamespace, 
			GraphQuery query,
			List<Sort> sorts,
			int start, 
			int pageSize);
	
	public Map<String, Integer> getTripleUidsContainingObjectCount(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String objectEntityCode,
			String objectEntityCodeNamespace,
			GraphQuery query);
	
	public AssociatedConcept getAssociatedConceptFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			String tripleUid);
	
	public List<? extends AssociatedConcept> getAssociatedConceptsFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			List<Sort> list, 
			List<String> tripleUid);
	
	public List<ConceptReference> getConceptReferencesFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			List<String> tripleUid);
	
	public AssociatedConcept getAssociatedConceptFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			String tripleUid);
	
	public List<? extends AssociatedConcept> getAssociatedConceptsFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
	        List<Sort> sorts,
			List<String> tripleUid);
	
	public List<ConceptReference> getConceptReferencesFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			List<String> tripleUid);
	
	public List<String> getAssociationPredicateNamesForCodingScheme(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName);
	
	public List<String> getAssociationPredicateUidsForNames(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			List<String> associationNames);

	public List<String> getRelationNamesForCodingScheme(String codingSchemeUri,
			String codingSchemeVersion);
}



