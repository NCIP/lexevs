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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.commonTypes.types.EntityTypes;

//import com.hp.hpl.jena.vocabulary.OWL;
//import com.hp.hpl.jena.vocabulary.RDF;
//import com.hp.hpl.jena.rdf.model.Resource;

public class LexRdfConstants {
	// for owl ontology output
	public static final String ENGLISH 					= "EN";
	public static final String XML_BASE 				= "xmlBase";
	public static final String RELATIVE_URIS			= "relativeURIs";
	public static final String RDFXML_ABBREV			= "RDF/XML-ABBREV";
	public static final String RDFXML					= "RDF/XML";
	public static final String SHOW_XML_DECLARATION		= "showXmlDeclaration";
	
	// for skos	
	public static final String SKOS_NS_URI  			= "http://www.w3.org/2004/02/skos/core#";
	public static final String SKOS_NS_PREFIX			= "skos";
	public static final String SKOS_CONCEPT				= SKOS_NS_URI + "Concept";
	public static final String SKOS_NOTE				= SKOS_NS_URI + "note";
	public static final String SKOS_DEFINITION			= SKOS_NS_URI + "definition";
	public static final String SKOS_PREFLABEL			= SKOS_NS_URI + "prefLabel";
	public static final String SKOS_ALTLABEL			= SKOS_NS_URI + "altLabel";
	
	// for lexrdf
	public static final String LEXRDF_NS_URI			= "http://informatics.mayo.edu/lexrdf#";
	public static final String LEXRDF_NS_PREFIX			= "lexrdf";
	public static final String LEXRDF_IS_DEFINED			= LEXRDF_NS_URI + "isDefined";
	public static final String LEXRDF_COMMENT			= LEXRDF_NS_URI + "comment";
	public static final String LEXRDF_IS_PREFERRED		= LEXRDF_NS_URI + "isPreferred";
	public static final String LEXRDF_DEGREE_OF_FIDELITY = LEXRDF_NS_URI + "degreeOfFidelity";
	public static final String LEXRDF_MATCH_IF_NO_CONTEXT = LEXRDF_NS_URI + "matchIfNoContext";
	public static final String LEXRDF_REPRESENTATIONAL_FORM = LEXRDF_NS_URI + "representationalForm";
	public static final String LEXRDF_PROPERTY_LINK		= LEXRDF_NS_URI + "propertyLink";
	public static final String LEXRDF_ASSOCIATION_QUALIFICATION	= LEXRDF_NS_URI + "associationQualification";
	
	// loader settings
	public static final String LOADER_IS_DATA_TYPE_PROPERTY	= "isDatatypeProperty";
	public static final String LOADER_IS_OBJECT_PROPERTY	= "isObjectProperty";
	
	// for swrlb 
	public static final String SWRLB_PREFIX				= "swrlb";
	
	// for protege
	public static final String PROTEGE_PREFIX			= "protege";
	
	// imported namespace by default
	public static final List<String> IMPORTED_NS		= new ArrayList<String>();
	
	// not imported namespace, but they may appear since the loader use them, we don't need consider them in the exporter
	public static final List<String> NOT_IMPORT_NS		= new ArrayList<String>();
	static {NOT_IMPORT_NS.add(SWRLB_PREFIX);
	 		NOT_IMPORT_NS.add(PROTEGE_PREFIX);
			}

	
}