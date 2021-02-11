
package org.lexgrid.exporter.owlrdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class Skos {
	private static Model model = ModelFactory.createDefaultModel();
	public static Resource Concept = model.createResource(LexRdfConstants.SKOS_CONCEPT);
	public static Property note =model.createProperty(LexRdfConstants.SKOS_NOTE);
	public static Property definition = model.createProperty(LexRdfConstants.SKOS_DEFINITION);
	public static Property prefLabel = model.createProperty(LexRdfConstants.SKOS_PREFLABEL);
	public static Property altLabel = model.createProperty(LexRdfConstants.SKOS_ALTLABEL);
}