
package org.LexGrid.custom.relations;


import java.util.ArrayList;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;

public class RelationsUtil {

/**
	 * Returns the list of AssociationPredicates within the given coding scheme
	 * @param scheme
	 * @return List
	 */
public static List<AssociationPredicate> resolveAssociationPredicates(CodingScheme scheme) {
		List<AssociationPredicate> aps = new ArrayList<AssociationPredicate>();
		for (Relations rs : scheme.getRelations()) {
			for (AssociationPredicate ap : rs.getAssociationPredicate()) {
				aps.add(ap);
			}
		}
		return aps;
	} 
	
	/**
	 * Returns the list of AssociationPredicates within the given coding scheme that matches the association_name.
	 * @param scheme, association_name
	 * @return List
	 */
	public static List<AssociationPredicate> resolveAssociationPredicates(CodingScheme scheme, String association_name) {
		List<AssociationPredicate> aps = new ArrayList<AssociationPredicate>();
		for (Relations rs : scheme.getRelations()){
			for (AssociationPredicate ap : rs.getAssociationPredicate()) {
				if (ap.getAssociationName().equalsIgnoreCase(association_name))
					aps.add(ap);
			}
		}
		return aps;
	}
	
	/**
	 * Returns a source within a specific relation with the given code;
	 * null if not available.
	 * @param relation
	 * @param sourceCode
	 * @return AssociationSource
	 */
	public static AssociationSource resolveRelationSource(AssociationPredicate relation, String sourceCode) {
		if (sourceCode != null) {
			AssociationSource[] sources = relation.getSource();
			for (AssociationSource as : sources) {
				if (as.getSourceEntityCode().equals(sourceCode))
					return as;
			}
		}
		return null;
	}
	
	public static List<AssociationSource> resolveRelationSources(Entity entity, List<AssociationPredicate> association_list) {
		List<AssociationSource> aList = new ArrayList<AssociationSource>();
		for (AssociationPredicate ap : association_list) {
			for (AssociationSource as: ap.getSource()) {
				if (isEqual(entity.getEntityCode(), as.getSourceEntityCode()) && 
						isEqual(entity.getEntityCodeNamespace(), as.getSourceEntityCodeNamespace()))
					aList.add(as);
			}
		}
		
		return aList;
	}
	
	/**
	 * Returns a relation target within a specific source with the given code;
	 * null if not available.
	 * @param source
	 * @param targetCode
	 * @return AssociationTarget
	 */
	public static AssociationTarget resolveRelationTarget(AssociationSource source, String targetCode) {
		if (targetCode != null) {
			for (AssociationTarget at: source.getTarget()) {
				if (at.getTargetEntityCode().equals(targetCode))
					return at;
			}
		}
		return null;
	}
	public static List<AssociationSource>resolveRelationTargets(Entity entity, List<AssociationPredicate> association_list) {
		List<AssociationSource> aList = new ArrayList<AssociationSource>();
		for(AssociationPredicate ap : association_list) {
			for (AssociationSource as : ap.getSource()) {
				for (AssociationTarget at : as.getTarget()) {
					if ((entity.getEntityCode().equals(at.getTargetEntityCode())) &&
							entity.getEntityCodeNamespace().equals(at.getTargetEntityCodeNamespace())) {
						aList.add(as); // add association source
						break;
					}
				}
			}
		}
		return aList;
	}
	
	/**
	 * This method ensures that duplicate AssociationTarget's are not added to a
	 * Association Source
	 * 
	 * @param as
	 * @param at
	 * @return The AssociationTarget that is attached to the association source
	 */
	public static AssociationTarget subsume(AssociationSource as,
			AssociationTarget at) {
		if (as == null || at == null)
			return null;
		for (AssociationTarget next : as.getTarget()) {
			if (isEqual(next, at) == true) {
				return next;
			}
		}
		// No match found. We add at to as
		as.getTargetAsReference().add(at);
		return at;
	}

	/**
	 * This method ensures that duplicate AssociationSources are not added to AssociationPredicate
	 * @param ap
	 * @param as
	 * @return the AssociationSource that is added to the AssociationPredicate
	 */
	public static AssociationSource subsume(AssociationPredicate ap,
			AssociationSource as) {
		if (ap == null || as == null)
			return null;

		for (AssociationSource a : ap.getSource()) {
			if (isEqual(a, as) == true)
				return a;
		}

		// No matched found, add AS to AP.
		ap.addSource(as);
		return as;

	}
	
	/**
	 * This method ensures that duplicate AssociationPredicates is not added into Relations
	 * @param r
	 * @param ap
	 * @return the AssociationPredicate that is added to the Relations
	 */
	public static AssociationPredicate subsume(Relations r, AssociationPredicate ap) {
		if (r == null && ap == null)
			return null;
		
		for(AssociationPredicate a : r.getAssociationPredicate()) {
			if (isEqual(a, ap) == true)
				return  a;
		}
		// No matched found, add AP to relations
		r.addAssociationPredicate(ap);
		return ap;
	}
	
	/**
	 * The method ensure that duplicate Relations is not added to CodingSchem
	 * @param cs
	 * @param r
	 * @return the Relations that is added to the CodingScheme
	 */
	public static Relations subsume(CodingScheme cs, Relations r) {
		if (r == null && r == null)
			return null;
		
		for (Relations lr : cs.getRelations()) {
			if (isEqual(lr, r) == true)
				return lr;
		}
		
		// No matched found, add relations to CS
		cs.addRelations(r);
		return r;
	}

	/**
	 * This method is used to compare that two Strings (null included)
	 * @param st1
	 * @param st2
	 * @return
	 */
	private static boolean isEqual(String st1, String st2) {
		if (st1 != null)
			return st1.equals(st2);
		else if (st2 != null)
			return false;
		else
			return true;

	}

	/**
	 * This method is to compare two AssociationTargets, according to their codes and namespaces. 
	 * @param at1
	 * @param at2
	 * @return
	 */
	private static boolean isEqual(AssociationTarget at1, AssociationTarget at2) {
		if (isEqual(at1.getTargetEntityCode(), at2.getTargetEntityCode())
				&& isEqual(at1.getTargetEntityCodeNamespace(), at2
						.getTargetEntityCodeNamespace()))
			return true;
		return false;
	}

	/**
	 * This method is to compare two AssociationSources, according to the sources' codes and namespaces
	 * @param as1
	 * @param as2
	 * @return
	 */
	private static boolean isEqual(AssociationSource as1, AssociationSource as2) {
		if (isEqual(as1.getSourceEntityCode(), as2.getSourceEntityCode())
				&& isEqual(as1.getSourceEntityCodeNamespace(), as2
						.getSourceEntityCodeNamespace())) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method is to compare two AssociationPredicates, according to the AssociationNames
	 * @param ap1
	 * @param ap2
	 * @return
	 */
	private static boolean isEqual(AssociationPredicate ap1, AssociationPredicate ap2) {
		return isEqual(ap1.getAssociationName(), ap2.getAssociationName());
	}
	
	/**
	 * This method is to compare two Relations, according to their ContainerNames
	 * @param r1
	 * @param r2
	 * @return
	 */
	private static boolean isEqual(Relations r1, Relations r2) {
		return isEqual(r1.getContainerName(), r2.getContainerName());
	}
}