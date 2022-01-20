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
