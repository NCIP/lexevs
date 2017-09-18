/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.service.codednodegraph;

import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.service.codednodegraph.model.ColumnSortType;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;

/**
 * The Interface CodedNodeGraphService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodedNodeGraphService {
	
	/**
	 * The Enum Order.
	 */
	public enum Order {
		ASC, 
		DESC}
	
	/**
	 * The Class QualifierSort.
	 */
	public static class QualifierSort extends Sort {
		
		/** The qualifier name. */
		private String qualifierName;
		
		/** The table alias. */
		private String tableAlias;
		
		/**
		 * Instantiates a new qualifier sort.
		 * 
		 * @param columnSortType the column sort type
		 * @param order the order
		 * @param qualifierName the qualifier name
		 * @param tableAlias the table alias
		 */
		public QualifierSort(ColumnSortType columnSortType, Order order, String qualifierName, String tableAlias) {
			super(columnSortType, order);
			this.qualifierName = qualifierName;
			this.tableAlias = tableAlias;
		}
		
		/**
		 * Gets the qualifier name.
		 * 
		 * @return the qualifier name
		 */
		public String getQualifierName() {
			return qualifierName;
		}

		/**
		 * Gets the table alias.
		 * 
		 * @return the table alias
		 */
		protected String getTableAlias() {
			return tableAlias;
		}
	}
	
	/**
	 * The Class Sort.
	 */
	public static class Sort {
		
		/** The column sort type. */
		private ColumnSortType columnSortType;
		
		/** The order. */
		private Order order;
		
		/**
		 * Instantiates a new sort.
		 * 
		 * @param columnSortType the column sort type
		 * @param order the order
		 */
		public Sort(ColumnSortType columnSortType, Order order) {
			super();
			this.columnSortType = columnSortType;
			this.order = order;
		}

		/**
		 * Gets the column sort type.
		 * 
		 * @return the column sort type
		 */
		public ColumnSortType getColumnSortType() {
			return columnSortType;
		}

		/**
		 * Gets the order.
		 * 
		 * @return the order
		 */
		public Order getOrder() {
			return order;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
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

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
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
	
	/**
	 * List code relationships.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param relationsContainerName the relations container name
	 * @param sourceEntityCode the source entity code
	 * @param sourceEntityCodeNamespace the source entity code namespace
	 * @param targetEntityCode the target entity code
	 * @param targetEntityCodeNamespace the target entity code namespace
	 * @param query the query
	 * @param useTransitive the use transitive
	 * 
	 * @return the list< string>
	 */
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

	/**
	 * Gets the root concept references.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param relationsContainerName the relations container name
	 * @param associationPredicateNames the association predicate names
	 * @param qualifiers the qualifiers
	 * @param subjectEntityCodeNamespaces the subject entity code namespaces
	 * @param objectEntityCodeNamespaces the object entity code namespaces
	 * @param traverse the traverse
	 * @param sorts the sorts
	 * @param start the start
	 * @param pageSize the page size
	 * 
	 * @return the root concept references
	 */
	public List<ConceptReference> getRootConceptReferences(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			List<String> associationPredicateNames, 
			List<QualifierNameValuePair> qualifiers,
			List<String> subjectEntityCodeNamespaces, 
			List<String> objectEntityCodeNamespaces, 
			TraverseAssociations traverse,
			List<Sort> sorts,
			int start,
			int pageSize);
	
	/**
	 * Gets the tail concept references.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param relationsContainerName the relations container name
	 * @param associationPredicateNames the association predicate names
	 * @param qualifiers the qualifiers
	 * @param subjectEntityCodeNamespaces the subject entity code namespaces
	 * @param objectEntityCodeNamespaces the object entity code namespaces
	 * @param traverse the traverse
	 * @param sorts the sorts
	 * @param start the start
	 * @param pageSize the page size
	 * 
	 * @return the tail concept references
	 */
	public List<ConceptReference> getTailConceptReferences(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			List<String> associationPredicateNames,
			List<QualifierNameValuePair> qualifiers,
			List<String> subjectEntityCodeNamespaces,
			List<String> objectEntityCodeNamespaces,  
			TraverseAssociations traverse,
			List<Sort> sorts,
			int start,
			int pageSize);
	
	/**
	 * Gets the triple uids containing subject.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param relationsContainerName the relations container name
	 * @param associationPredicateName the association predicate name
	 * @param subjectEntityCode the subject entity code
	 * @param subjectEntityCodeNamespace the subject entity code namespace
	 * @param query the query
	 * @param sorts the sorts
	 * @param start the start
	 * @param pageSize the page size
	 * 
	 * @return the triple uids containing subject
	 */
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
	
	/**
	 * Gets the triple uids containing subject count.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param relationsContainerName the relations container name
	 * @param subjectEntityCode the subject entity code
	 * @param subjectEntityCodeNamespace the subject entity code namespace
	 * @param query the query
	 * 
	 * @return the triple uids containing subject count
	 */
	public Map<String, Integer> getTripleUidsContainingSubjectCount(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String subjectEntityCode,
			String subjectEntityCodeNamespace,
			GraphQuery query);
	
	/**
	 * Gets the triple uids containing object.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param relationsContainerName the relations container name
	 * @param associationPredicateName the association predicate name
	 * @param objectEntityCode the object entity code
	 * @param objectEntityCodeNamespace the object entity code namespace
	 * @param query the query
	 * @param sorts the sorts
	 * @param start the start
	 * @param pageSize the page size
	 * 
	 * @return the triple uids containing object
	 */
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
	
	/**
	 * Gets the triple uids containing object count.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param relationsContainerName the relations container name
	 * @param objectEntityCode the object entity code
	 * @param objectEntityCodeNamespace the object entity code namespace
	 * @param query the query
	 * 
	 * @return the triple uids containing object count
	 */
	public Map<String, Integer> getTripleUidsContainingObjectCount(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			String objectEntityCode,
			String objectEntityCodeNamespace,
			GraphQuery query);
	
	/**
	 * Gets the associated concept from uid source.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param resolve the resolve
	 * @param propertyNames the property names
	 * @param propertyTypes the property types
	 * @param tripleUid the triple uid
	 * 
	 * @return the associated concept from uid source
	 */
	public AssociatedConcept getAssociatedConceptFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			String tripleUid);
	
	/**
	 * Gets the associated concepts from uid source.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param resolve the resolve
	 * @param propertyNames the property names
	 * @param propertyTypes the property types
	 * @param list the list
	 * @param tripleUid the triple uid
	 * 
	 * @return the associated concepts from uid source
	 */
	public List<? extends AssociatedConcept> getAssociatedConceptsFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			List<Sort> list, 
			List<String> tripleUid);
	
	/**
	 * Gets the concept references from uid source.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param sorts the sorts
	 * @param tripleUid the triple uid
	 * 
	 * @return the concept references from uid source
	 */
	public List<ConceptReference> getConceptReferencesFromUidSource(
			String codingSchemeUri,
			String codingSchemeVersion,
			List<Sort> sorts,
			List<String> tripleUid);
	
	/**
	 * Gets the associated concept from uid target.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param resolve the resolve
	 * @param propertyNames the property names
	 * @param propertyTypes the property types
	 * @param tripleUid the triple uid
	 * 
	 * @return the associated concept from uid target
	 */
	public AssociatedConcept getAssociatedConceptFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			String tripleUid);
	
	/**
	 * Gets the associated concepts from uid target.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param resolve the resolve
	 * @param propertyNames the property names
	 * @param propertyTypes the property types
	 * @param sorts the sorts
	 * @param tripleUid the triple uid
	 * 
	 * @return the associated concepts from uid target
	 */
	public List<? extends AssociatedConcept> getAssociatedConceptsFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
	        List<Sort> sorts,
			List<String> tripleUid);
	
	/**
	 * Gets the concept references from uid target.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param sorts the sorts
	 * @param tripleUid the triple uid
	 * 
	 * @return the concept references from uid target
	 */
	public List<ConceptReference> getConceptReferencesFromUidTarget(
			String codingSchemeUri,
			String codingSchemeVersion,
			List<Sort> sorts,
			List<String> tripleUid);
	
	/**
	 * Gets the association predicate names for coding scheme.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param relationsContainerName the relations container name
	 * 
	 * @return the association predicate names for coding scheme
	 */
	public List<String> getAssociationPredicateNamesForCodingScheme(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName);
	
	/**
	 * Gets the association predicate uids for names.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param relationsContainerName the relations container name
	 * @param associationNames the association names
	 * 
	 * @return the association predicate uids for names
	 */
	public List<String> getAssociationPredicateUidsForNames(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName,
			List<String> associationNames);

	/**
	 * Gets the relation names for coding scheme.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * 
	 * @return the relation names for coding scheme
	 */
	public List<String> getRelationNamesForCodingScheme(String codingSchemeUri,
			String codingSchemeVersion);

	/**
	 * Gets the triple uids for mapping relations container.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param sourceCodingScheme the source coding scheme
	 * @param targetCodingScheme the target coding scheme
	 * @param relationsContainerName the relations container name
	 * @param sorts the sorts
	 * @param start the start
	 * @param pageSize the page size
	 * 
	 * @return the triple uids for mapping relations container
	 */
	public List<String> getTripleUidsForMappingRelationsContainer(
			String codingSchemeUri,
			String codingSchemeVersion, 
			AbsoluteCodingSchemeVersionReference sourceCodingScheme,
			AbsoluteCodingSchemeVersionReference targetCodingScheme,
			String relationsContainerName,
			List<Sort> sorts, 
			int start, 
			int pageSize);
	
	public List<String> getTripleUidsForMappingRelationsContainerForCodes(
			String codingSchemeUri,
			String codingSchemeVersion, 
			AbsoluteCodingSchemeVersionReference sourceCodingScheme,
			AbsoluteCodingSchemeVersionReference targetCodingScheme,
			String relationsContainerName,
			List<ConceptReference> sourceConceptReferences,
			List<ConceptReference> targetConceptReferences,
			List<ConceptReference> sourceOrTargetConceptReferences,
			List<Sort> sorts,
			int start, 
			int pageSize);
	
	public List<String> getTripleUidsForMappingRelationsContainerForCodes(
			String codingSchemeUri,
			String codingSchemeVersion, 
			String relationsContainerName,
			List<ConceptReference> sourceConceptReferences,
			List<ConceptReference> targetConceptReferences,
			List<ConceptReference> sourceOrTargetConceptReferences);

	/**
	 * Gets the mapping triples.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param sourceCodingScheme the source coding scheme
	 * @param targetCodingScheme the target coding scheme
	 * @param relationsContainerName the relations container name
	 * @param tripleUids the triple uids
	 * 
	 * @return the mapping triples
	 */
	public List<? extends ResolvedConceptReference> getMappingTriples(
			String codingSchemeUri,
			String codingSchemeVersion,
			AbsoluteCodingSchemeVersionReference sourceCodingScheme,
			AbsoluteCodingSchemeVersionReference targetCodingScheme,
			String relationsContainerName, 
			List<String> tripleUids);
	
	/**
	 * Gets the mapping triples count.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param relationsContainerName the relations container name
	 * 
	 * @return the mapping triples count
	 */
	public int getMappingTriplesCount(
			String codingSchemeUri,
			String codingSchemeVersion,
			String relationsContainerName);
	
	public int getMappingTriplesCountForCodes(
			String codingSchemeUri,
			String codingSchemeVersion, 
			String relationsContainerName,
			List<ConceptReference> sourceConceptReferences,
			List<ConceptReference> targetConceptReferences,
			List<ConceptReference> sourceOrTargetConceptReferences);

	List<Node> getDistinctTargetTriples(String codingSchemeUri, String version, String associationPredicateUid);
	
	List<Node> getTargetsFromSource(String codingSchemeUri, String version, String entityCode, String entityNamespace, String associationPredicateUid);

}