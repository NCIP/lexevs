
package org.lexgrid.exporter.owlrdf;

import java.util.HashMap;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.concepts.Entity;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class LexRdfMap {

	private static final Map<String, Resource> map = new HashMap<String, Resource>();
	private final static String OBO = OntologyFormat.OBO.toString() + "#";
	private final static String UMLS = OntologyFormat.UMLS.toString() + "#";

	static {
		map.put(LexRdfConstants.LOADER_IS_DATA_TYPE_PROPERTY.toLowerCase(),
				OWL.DatatypeProperty);
		map.put(LexRdfConstants.LOADER_IS_OBJECT_PROPERTY.toLowerCase(),
				OWL.ObjectProperty);

		// OWL/RDFs
		map.put("type", RDF.type);
		map.put(EntityTypes.CONCEPT.value().toLowerCase(), OWL.Class);
		map.put("ObjectProperty".toLowerCase(), OWL.ObjectProperty);
		map.put("DatatypeProperty".toLowerCase(), OWL.DatatypeProperty);
		map.put("FunctionalProperty".toLowerCase(), OWL.FunctionalProperty);
		map.put("InverseFunctionalProperty".toLowerCase(),
				OWL.InverseFunctionalProperty);
		map.put("SymmetricProperty".toLowerCase(), OWL.SymmetricProperty);
		map.put("equivalentProperty", OWL.equivalentProperty);
		map.put("TransitiveProperty".toLowerCase(), OWL.TransitiveProperty);
		map.put("DefaultOWLObjectProperty".toLowerCase(), OWL.ObjectProperty);
		map.put("DefaultOWLDataTypeProperty".toLowerCase(),
				OWL.DatatypeProperty);
		map.put("subPropertyOf".toLowerCase(), RDFS.subPropertyOf);
		map.put("oneOf".toLowerCase(), OWL.oneOf);
		map.put("AnnotationProperty".toLowerCase(), OWL.AnnotationProperty);

		// OBO
		map.put(OBO + "is_a", RDFS.subClassOf);
		map.put(OBO + "union_of", OWL.unionOf);
		map.put(OBO + "disjoint_from", OWL.disjointWith);
		// map.put(OBO + "comemnt", null);
		// map.put(OBO + "subset", null);

		// UMLs
		map.put(UMLS + "chd", RDFS.subClassOf);
		map.put(UMLS + "par", RDFS.subClassOf);

	}

	public static Resource get(String key, OntologyFormat ontType) {
		if (ontType.equals(OntologyFormat.OWLRDF)) {
			return map.get(key.toLowerCase());
		}
		return map.get(ontType.toString() + "#" + key.toLowerCase());
	}

	public static boolean isMapped(Entity entity, OntologyFormat ontType) {
		if (ontType.equals(OntologyFormat.OWLRDF)) {
			// if it is just a owl/rdf/rdfs property, do nothing
			if (entity.getPropertyCount() == 0
					&& (entity.getEntityCodeNamespace().equalsIgnoreCase("owl")
							|| entity.getEntityCodeNamespace()
									.equalsIgnoreCase("rdf") || entity
							.getEntityCodeNamespace().equalsIgnoreCase("rdfs"))) {
				return true;
			}

			// neither swrlb nor protege
			if (LexRdfConstants.NOT_IMPORT_NS.contains(entity
					.getEntityCodeNamespace().toLowerCase()))
				return true;
		} else {
			// if (entity.getPropertyCount()==0 && map.containsKey(OBO +
			// entity.getEntityCode()))
			if (entity.getPropertyCount() == 0
					&& map.containsKey(ontType.toString() + "#"
							+ entity.getEntityCode()))
				return true;
			else
				return false;
		}
		return false;
	}

	public static boolean isMapped(ResolvedConceptReference conRef,
			OntologyFormat ontType) {
		if (ontType.equals(OntologyFormat.OWLRDF)) {
			// if it is just a owl/rdf/rdfs property, do nothing
			if (conRef.getCodeNamespace().equalsIgnoreCase("owl")
					|| conRef.getCodeNamespace().equalsIgnoreCase("rdf")
					|| conRef.getCodeNamespace().equalsIgnoreCase("rdfs")
					|| LexRdfConstants.NOT_IMPORT_NS.contains(conRef
							.getCodeNamespace()))
				return true;
		}
		// else if(ontType.equals(OntologyType.OBO)) {
		else {
			return map.containsKey(ontType.toString() + "#" + conRef.getCode());
		}
		return false;
	}

	public static boolean isMapped(String ns, String code, OntologyFormat ontType) {
		if (ontType.equals(OntologyFormat.OWLRDF)) {
			if (ns == null || code == null)
				return true;
			if (ns.equalsIgnoreCase("owl") || ns.equalsIgnoreCase("rdf")
					|| ns.equalsIgnoreCase("rdfs")
					|| LexRdfConstants.NOT_IMPORT_NS.contains(ns))
				return true;
		} else {
			return map.containsKey(ontType.toString() + "#" + code);
		}
		return false;
	}
}