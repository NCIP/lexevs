
package org.lexgrid.exporter.owlrdf;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.concepts.Entity;

@Deprecated
public class LexRdfMap {



	public static boolean isMapped(Entity entity, OntologyFormat ontType) {
		throw new UnsupportedOperationException("We no longer support Jena based projects");
	}

	public static boolean isMapped(ResolvedConceptReference conRef,
			OntologyFormat ontType) {
		throw new UnsupportedOperationException("We no longer support Jena based projects");
	}

	public static boolean isMapped(String ns, String code, OntologyFormat ontType) {
		throw new UnsupportedOperationException("We no longer support Jena based projects");
	}
}