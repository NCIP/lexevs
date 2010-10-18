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
package org.LexGrid.LexBIG.Impl.exporters;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExportStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.exporters.ExporterMessageDirector;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.AnonymousOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedAssociationQualifier;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.relations.AssociationEntity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.exporter.owlrdf.LexRdf;
import org.lexgrid.exporter.owlrdf.LexRdfConstants;
import org.lexgrid.exporter.owlrdf.LexRdfMap;
import org.lexgrid.exporter.owlrdf.Skos;
import org.lexgrid.exporter.owlrdf.StringHelper;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.ComplementClass;
import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.ReifiedStatement;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.JDBC;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.LayoutType;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


public class LexGridToOwlRdfConverter {
	// HSQL config -- faster than MySQL, can be used when the test case is large
	// String className = "org.hsqldb.jdbcDriver"; // path of driver class
	// String DB_URL = "jdbc:hsqldb:file:C:/temp/owlrdf-export"; // URL of
	// database
	// String DB_USER = "sa"; // database user id
	// String DB_PASSWD = ""; // database password
	// String DB = "HSQL"; // database type

//	private String DB_URL = "jdbc:mysql://localhost:3306/lb60";
//	private String DB_USER = "root";
//	private String DB_PASSWD = "root";

	// private String NS_URI = "http://www.xfront.com/owl/ontologies/camera";
	// private String outputFile_ = "C:/temp/camera.owl";

//	private String NS_URI = "http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl";
//	private String outputFile_ = "C:/temp/pizza.owl";

//	 private String NS_URI = "urn:lsid:bioontology.org:cell";
//	 private String outputFile_ = "C:/temp/cell.owl";
	 
//	 private String NS_URI = "urn:oid:2.16.840.1.113883.6.110";
//	 private String outputFile_ = "C:/temp/umls.owl";
	
//	 private String NS_URI = "urn:lsid:bioontology.org:uberon";
//	 private String outputFile_ = "C:/temp/uberon.owl";

	private Store store_ = null;
	private String currentNamespace_;
	private OntModel model_;
	private CodingScheme cs_;
	private CodedNodeGraph cng_;
	private CodedNodeSet cns_;
	private OntologyFormat ontFormat_;
	private LgMessageDirectorIF messenger_;
	private Writer writer_;
	
	private int assnCounter = 0; // for dev only

	public LexGridToOwlRdfConverter() {
		
	}

	public Store getStore() {
		if (store_ != null)
			return store_;
		
		String url = LexEvsServiceLocator.getInstance().
		    getSystemResourceService().getSystemVariables().getAutoLoadDBURL();
		
		String userName = LexEvsServiceLocator.getInstance().
            getSystemResourceService().getSystemVariables().getAutoLoadDBUsername();
		
		String password = LexEvsServiceLocator.getInstance().
            getSystemResourceService().getSystemVariables().getAutoLoadDBPassword();
		
		org.lexevs.dao.database.type.DatabaseType type = LexEvsServiceLocator.getInstance().
		    getLexEvsDatabaseOperations().getDatabaseType();
		
		StoreDesc storeDesc = null;
		if (type.getProductName().equals(DatabaseType.MySQL.getName())) {
		    JDBC.loadDriverMySQL();
		    storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesIndex,
	                DatabaseType.MySQL);
		}
		else if (type.getProductName().equals(DatabaseType.Oracle.getName())) {
            JDBC.loadDriverOracle();
            storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesIndex,
                    DatabaseType.Oracle);
        }
		else if (type.getProductName().equals(DatabaseType.PostgreSQL.getName())) {
            JDBC.loadDriverPGSQL();
            storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesIndex,
                    DatabaseType.PostgreSQL);
        }
		else if (type.getProductName().equals(DatabaseType.HSQLDB.getName())) {
            JDBC.loadDriverHSQL();
            storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesIndex,
                    DatabaseType.HSQLDB);
        }
		else if (type.getProductName().equals(DatabaseType.DB2.getName())) {
            JDBC.loadDriverDB2();
            storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesIndex,
                    DatabaseType.DB2);
        } 
		
		SDBConnection conn = new SDBConnection(url, userName, password);
		store_ = SDBFactory.connectStore(conn, storeDesc);

		// If SDB tables do not exist, it will throw an exception.
		try {
			store_.getSize();
		} catch (Exception e) {
			store_.getTableFormatter().create();
			System.out.println("create sdb tables");
		}
		return store_;
	}

	public Model getBaseModel(String iri) {
		return SDBFactory.connectNamedModel(this.getStore(), iri);
	}

	private void codingSchemeMapping() throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		// TODO find an ontology with sources to test.
		Ontology ontology = model_.createOntology(cs_.getCodingSchemeURI());
		ontology.addLabel(cs_.getCodingSchemeName(), LexRdfConstants.ENGLISH);
		ontology.addVersionInfo(cs_.getRepresentsVersion());
		if (cs_.getCopyright() != null)
			ontology.addProperty(DC.rights, cs_.getCopyright().getContent());
		if (cs_.getFormalName() != null)
			ontology.addProperty(DC.title, cs_.getFormalName());
		if (cs_.getDefaultLanguage() != null)
			ontology.addProperty(DC.language, cs_.getDefaultLanguage());
		for (Source source : cs_.getSource()) {
			if (source.getContent() != null)
				ontology.addProperty(DC.source, source.getContent());
		}
		// import namespaces
		this.initNamespace();
	}

	private void entityMapping() throws LBResourceUnavailableException, SecurityException, IllegalArgumentException, LBException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if (cns_ == null)
			return;
		List<Entity> instanceList = new ArrayList<Entity>();

		ResolvedConceptReferencesIterator iterator = cns_.resolve(null, null,
				null, null, true);

		int counter = 0;
		while (iterator.hasNext()) {
			ResolvedConceptReference conRef = iterator.next();
			Entity entity = conRef.getEntity();
			counter++;
			
			// handle the anonymous entity in association mapping, not here.
			if (entity.isIsAnonymous() != null && entity.isIsAnonymous() == true)
				continue; 

			if (entity.getEntityType() == null
					|| entity.getEntityType().length == 0
					|| entity.getEntityType().length > 1) {
				// map to skos:concept
				this.addSkosConcept(entity);
			} else if (entity.getEntityType()[0]
					.equalsIgnoreCase(EntityTypes.CONCEPT.value())) {
				// map to owl:class
				this.addOwlClass(entity);
			} else if (entity.getEntityType()[0]
					.equalsIgnoreCase(EntityTypes.ASSOCIATION.value())) {
				// map to owl:objectProperty/dataTypeProperty
				this.addOntProperty((AssociationEntity) entity);
			} else if (entity.getEntityType()[0]
					.equalsIgnoreCase(EntityTypes.INSTANCE.value())) {
				// save instance into a list, process the instances later
				instanceList.add(entity);
			}
		}

		// at last, process the instance, since the instance maybe an instance
		// of owl:class
		processInstance(instanceList);
		System.out.println("concepts total: " + Integer.toString(counter));
	}

	private void associationMapping() throws LBResourceUnavailableException, Exception{
		if (cns_ == null || cng_ == null) {
			return;
		}
		
		ResolvedConceptReferencesIterator conIterator = cns_.resolve(null, null, null, null, false);
		
		while (conIterator.hasNext()) {
			ResolvedConceptReference focus = conIterator.next();
			ResolvedConceptReferenceList localRcrl = cng_.resolveAsList(focus, true, false, 1, -1, null, null, null, null, -1);
			
			if (localRcrl == null)
				continue;
			Iterator<? extends ResolvedConceptReference> localIterator = localRcrl.iterateResolvedConceptReference();
			while (localIterator.hasNext()) {
				ResolvedConceptReference sourceConRef = localIterator.next();
				
				if (LexRdfMap.isMapped(sourceConRef, ontFormat_) == true)
					continue;
				this.processLgTargets(sourceConRef);
			}
		}
	}
	
	private SupportedAssociation getLgSupportedAssociation(String associationName)
			throws LBException {
		for (SupportedAssociation supportedAssn : cs_.getMappings()
				.getSupportedAssociation()) {
			if (supportedAssn.getContent().equalsIgnoreCase(associationName) || supportedAssn.getLocalId().equalsIgnoreCase(associationName)) {
				// get the code & namespace
//				String code = supportedAssn.getEntityCode();
//				String namespace = supportedAssn.getEntityCodeNamespace();

//				// get the entity
//				return (AssociationEntity) ServiceUtility.getEntity(cs_
//						.getCodingSchemeURI(), cs_.getRepresentsVersion(),
//						code, namespace);
				return supportedAssn;
			}
		}

		return null;
	}

	private void processLgTargets(ResolvedConceptReference sourceConRef)
			throws Exception {
		
		// for dev only
//		if (sourceConRef.getCode().equals("NonVegetarianPizza") == false)
//		if (sourceConRef.getCode().equals("AmericanHot") == false)
//		if (sourceConRef.getCode().equals("CheeseyPizza") == false)
//		if (sourceConRef.getCode().equals("VegetarianPizzaEquivalent1") == false)
//		if (sourceConRef.getCode().equals("American") == false)
//		if (sourceConRef.getCode().equals("SpicyPizza") == false)
//		if (sourceConRef.getCode().equals("VegetarianTopping") == false)
//		if (sourceConRef.getCode().equals("Country")) //loader issue
//		if (sourceConRef.getCode().equals("CL:0000015") == false)
//	    if (sourceConRef.getCode().equals("SlicedTomatoTopping") == false)
//			return;
		
		String sourceUri = this.resolveNamespace(sourceConRef.getCodeNamespace()) + sourceConRef.getCode();
		Resource source = null;
		
		// for dev only
		assnCounter++;
		
		//only consider the anonymous target
		if (sourceConRef.getCode().startsWith("@")) {
			return;
		}

		if (sourceConRef.getEntity().getEntityTypeCount() > 0 && sourceConRef.getEntity().getEntityType()[0].equalsIgnoreCase(EntityTypes.INSTANCE.value())) {
			// do nothing for instance
			return;
		}
		else if (sourceConRef.getEntity().getEntityTypeCount() > 0 && sourceConRef.getEntity().getEntityType()[0].equalsIgnoreCase(EntityTypes.ASSOCIATION.value())) {
			// for association
			source = model_.getOntProperty(this.getLgPropertyUri(sourceConRef.getCodeNamespace(), sourceConRef.getCode()));
		}
		else if (sourceConRef.getEntity().getEntityTypeCount() > 0 && sourceConRef.getEntity().getEntityType()[0].equalsIgnoreCase(EntityTypes.CONCEPT.value())) {
			// for concept
			source = model_.getOntClass(sourceUri);
		}
		
		AssociationList targetList = sourceConRef.getSourceOf();
		if ((targetList != null) && (targetList.getAssociationCount() > 0)) {
			Iterator<? extends Association> targetsIterator = targetList.iterateAssociation();
			while (targetsIterator.hasNext()) {
				Association targetAssociation = targetsIterator.next();
				// get association entity according to the association name
				SupportedAssociation supportedAssn = this
						.getLgSupportedAssociation(targetAssociation.getAssociationName());
				Property prop = null;
				if (supportedAssn == null) {
					throw new LBException("cannot find the supported association for " + targetAssociation.getAssociationName());
				} 
//				else if (supportedAssn.getEntityCodeNamespace() == null) {
////					prop = model_.getProperty(this.resolveNamespace(supportedAssn.getEntityCodeNamespace()) + supportedAssn.getEntityCode());
//					prop = model_.getProperty(this.getPropertyUri(supportedAssn.getEntityCodeNamespace(), supportedAssn.getEntityCode()));
//				}
				else {
					String propUri = null;
					if (supportedAssn.getEntityCode() == null){
						propUri = this.getLgPropertyUri(supportedAssn.getEntityCodeNamespace(), supportedAssn.getLocalId());
					} else {
						propUri = this.getLgPropertyUri(supportedAssn.getEntityCodeNamespace(), supportedAssn.getEntityCode());
					}
					
					if (propUri != null)
						prop = model_.getProperty(propUri);
					
					if (prop == null){
						// if it maps to owl/rdf properties, we don't need to add it
						if (LexRdfMap.isMapped(null, supportedAssn.getLocalId(), ontFormat_) == true) 
							return;
						propUri = this.resolveNamespace(currentNamespace_) + supportedAssn.getLocalId();
						prop = model_.createOntProperty(propUri);
					}
					
				}

				Iterator<? extends AssociatedConcept> associatedTargetsIterator = targetAssociation
						.getAssociatedConcepts().iterateAssociatedConcept();
				while (associatedTargetsIterator.hasNext()) {
					AssociatedConcept target = associatedTargetsIterator.next();
					Resource targetRsc = model_.getResource(this.resolveNamespace(target.getCodeNamespace()) + target.getCode());
					
					if (target == null || target.getCode().startsWith("@@"))
						continue;
					
					if (target.getCode().startsWith("@")) {
						Resource r = processLgAnonymousTarget(target);
						if (prop.getURI().equals(RDFS.subClassOf.getURI())) {
						    this.createTriple(r, prop, source);
						}
						else
						    this.createTriple(source, prop, r);
						continue;
					}
					
					// qualifier
					SupportedAssociationQualifier q = this.getLgQualifier(target);
					if (q != null && prop != null) {
						this.messenger_.info("in process target method there is a qualifier that is not null" );
					}
					
					if (this.ontFormat_.equals(OntologyFormat.UMLS) == false && prop.getURI().equalsIgnoreCase(RDFS.subClassOf.getURI())) {
						targetRsc = model_.getOntClass(targetRsc.getURI());
						this.createTriple(targetRsc, prop, source);
					}
					else if(this.ontFormat_.equals(OntologyFormat.UMLS) && prop.getURI().equalsIgnoreCase(RDFS.subClassOf.getURI())) {
						if (supportedAssn.getLocalId().equals("CHD")) {
							targetRsc = model_.getOntClass(targetRsc.getURI());
							this.createTriple(targetRsc, prop, source);
						}
						else if (supportedAssn.getLocalId().equals("PAR")) {
							this.createTriple(source, prop, targetRsc);
						}
					}
					else
//						System.out.println("process source: " + sourceConRef.getCode() + "-> " + target.getCode());
						this.createTriple(source, prop, targetRsc);

				}
			}
		}
	}
	
	/*
	 * Get Uri according to the name space and code. Apply the hash map if it isn't owl/rdf ontology
	 */
	private String getLgPropertyUri(String ns, String code) throws LBException {
		if (this.ontFormat_.equals(OntologyFormat.OWLRDF))
			return this.resolveNamespace(ns) + code;
		else {
			if (LexRdfMap.get(code, ontFormat_) != null)
				return LexRdfMap.get(code, ontFormat_).getURI();
			else
				return null;
		}
	}
	
	/**
	 * Create enumerated class -- oneOf. According to the owl loader if there is anonymous class does not have
	 * association target, it could be an enumerated class. This method checks its property which specify the enumerated type,
	 * and its entity description contains the list of enumerated things, for instance {instanceA, instanceB, classC}. 
	 * @param anonymousSource
	 * @return
	 * @throws LBException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	private Resource createEnumeratedClassFromEmptyAnonymousSource(AssociatedConcept anonymousSource) throws LBException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
	    Entity en = anonymousSource.getEntity();
	    boolean isOneOf = false;
	    
	    for (org.LexGrid.commonTypes.Property lgProp : en.getProperty()) {
//	        SupportedProperty lgSupProp = DaoUtility.getURIMap(cs_, SupportedProperty.class, lgProp.getPropertyName());
//	        if (!lgSupProp.getUri().equals(RDF.type.getURI()))
	        // TODO in supported property the URI of "type" is not correct
            if (!lgProp.getPropertyName().equalsIgnoreCase("type"))
	            continue;
	        StringHelper helper = new StringHelper(lgProp.getValue().getContent(), model_.getNsPrefixMap());
	        if (helper.getStrFormat().equals(StringHelper.StrFormat.PREFIX_TYPE)) {
	            if (helper.getType() == null)
	                System.out.println();
	            if (helper.getType()!= null && helper.getType().getURI().equals(OWL.oneOf.getURI())) {
	                isOneOf = true;
	                break;
	            }
	        }
	    }
	    if (isOneOf == false)
	        return null;
	    
	    if (en.getEntityDescription() != null && en.getEntityDescription().getContent().startsWith("{") && 
	            en.getEntityDescription().getContent().endsWith("}")) {
	        
	        String content = en.getEntityDescription().getContent();
	        content = content.substring(1, content.length()-1);
	        String[] elements = content.split(" ");
	        if (elements.length == 0)
	            return null;
	        List<RDFNode> rdfList = new ArrayList<RDFNode>();
	        for (String e : elements) {
	            String code = e.trim();
	            RDFNode rsc = model_.getResource(this.resolveNamespace(currentNamespace_) + code);
	            rdfList.add(rsc);
	        }
	        return model_.createEnumeratedClass(null, model_.createList(rdfList.iterator()));
	    }
	    return null;
	}
	
	private Resource processLgAnonymousTarget(AssociatedConcept anonymousSource) throws Exception{
		this.messenger_.info("processing anonymous " + anonymousSource.getCode());
		
//		AssociationList assnList = anonymousSource.getSourceOf();
		//anonymous class's getsourceof does not work correct, use cng instead for now
		ResolvedConceptReferenceList localRcrl = cng_.resolveAsList(anonymousSource, true, false, 1, -1, null, null, null, null, -1);
		if (localRcrl == null || localRcrl.getResolvedConceptReferenceCount() == 0)
			return null;
		AssociationList assnList = localRcrl.getResolvedConceptReference(0).getSourceOf();

		if (assnList == null) {
			return createEnumeratedClassFromEmptyAnonymousSource(anonymousSource);
		}
		
		Iterator<? extends Association> iteratorAssociations = assnList.iterateAssociation();

		//used for intersectionof or unionof
		List<RDFNode> rdfList = null;
		Class<?> classHolder = null;
		for (org.LexGrid.commonTypes.Property lgProp : anonymousSource.getEntity().getProperty()) {
			// if supported property change, it is not neccessary
			if (lgProp.getValue().getContent().equalsIgnoreCase(OWL.intersectionOf.getLocalName())) {
				rdfList = new ArrayList<RDFNode>();
				classHolder = IntersectionClass.class;
			}
			else if (lgProp.getValue().getContent().equalsIgnoreCase(OWL.unionOf.getLocalName())) {
				rdfList = new ArrayList<RDFNode>();
				classHolder = UnionClass.class;
			}
			else if (lgProp.getValue().getContent().equalsIgnoreCase(OWL.complementOf.getLocalName())) {
				classHolder = ComplementClass.class;
			}
		}
		
		while(iteratorAssociations.hasNext()) {
			Association assn = iteratorAssociations.next();
			SupportedAssociation suppAssn = this.getLgSupportedAssociation(assn.getAssociationName());
			Property localProp = null;
			if (suppAssn == null) {
				throw new LBException("no supported assocation found for " + assn.getAssociationName());
			}
			else {
				localProp = model_.getProperty(this.getLgPropertyUri(suppAssn.getEntityCodeNamespace(), suppAssn.getEntityCode()));
			}
				
			if (assn.getAssociatedConcepts() == null)
				continue;
			
			Iterator<? extends AssociatedConcept> iteratorConcepts = assn.getAssociatedConcepts().iterateAssociatedConcept();

			while(iteratorConcepts.hasNext()) {
				AssociatedConcept target = iteratorConcepts.next();
				Resource targetRsc = model_.getResource(this.resolveNamespace(target.getCodeNamespace()) + target.getCode());
				if (target.getCode().startsWith("@@"))
					continue;
				if (target.getCode().startsWith("@") && anonymousSource.getCode().startsWith("@")) {
					// target is anonymous class, recursively call itself
					targetRsc = processLgAnonymousTarget(target);
					if (localProp.getURI().equals(OWL.complementOf.getURI()))
						targetRsc = model_.createComplementClass(null, targetRsc);
					else {
						if (localProp.getURI().equals(RDFS.subClassOf.getURI()) == false && localProp.getNameSpace().equals(currentNamespace_))
							System.err.println("process anonymous targe error");
					}
						
				}
				
				SupportedAssociationQualifier qualifier = getLgQualifier(target);
				Restriction restriction = null;
				if (localProp != null && qualifier != null) {
					restriction = createRestriction(qualifier, localProp, targetRsc);
				}
				
				if (rdfList != null) {
					if (restriction != null)
						rdfList.add(restriction);
					else
						if (localProp != null &&localProp.getURI().equals(OWL.complementOf.getURI())) {
							targetRsc = model_.createComplementClass(null, targetRsc);
						}
						rdfList.add(targetRsc);
				}
				else {
					if (restriction != null)
						if  (classHolder != null && classHolder.equals(ComplementClass.class) == true) {
							return model_.createComplementClass(null, restriction);
						}
						else {
							return restriction;
						}
					else
						if  (classHolder != null && classHolder.equals(ComplementClass.class) == true) {
							return  model_.createComplementClass(null, targetRsc);
							
						}
						else {
							return targetRsc;
						}
				}
			}
		}

		if (rdfList != null ) {
			return this.createIntersectionOrUnionClass(classHolder, rdfList);
		}
		else 
			return null;
	}
	
	private void createTriple(Resource s, Property p, Resource o) {
		try {
			if (p.getURI().equals(OWL.equivalentClass.getURI())) {
				((OntClass)s).addEquivalentClass(o);
			}
			else if (p.getURI().equals(OWL.disjointWith.getURI())) {
				((OntClass)s).addDisjointWith(o);
			}
			else if (p.getURI().equals(OWL.differentFrom.getURI())) {
				((OntClass)s).addDifferentFrom(o);
			}
			else if (p.getURI().equals(OWL.sameAs.getURI())) {
				((OntClass)s).addSameAs(o);
			}
			else if (p.getURI().equals(RDFS.isDefinedBy.getURI())) {
				((OntClass)s).addIsDefinedBy(o);
			}
			else if (p.getURI().equals(RDFS.seeAlso.getURI())) {
				((OntClass)s).addSeeAlso(o);
			}
			else if (p.getURI().equals(RDFS.subClassOf.getURI())) {
				((OntClass)s).addSubClass(o);
			}
			else if(p.getURI().equals(RDFS.domain)) {
				((OntProperty)s).addDomain(o);
			}
			else if(p.getURI().equals(RDFS.range)) {
				((OntProperty)s).addRange(o);
			}
			else if(p.getURI().equals(OWL.complementOf.getURI())) {
				if (s.getURI() == null)
					s = model_.createComplementClass(null, o);
				else
					model_.createComplementClass(s.getURI(), o);
			}
			else{
				s.addProperty(p, o);
			}
		}
		catch (Exception e){
			messenger_.error(e.toString());
			System.out.println(e.toString());
		}
		
	}
	
	private Restriction createRestriction(SupportedAssociationQualifier q, Property p, Resource r) {
		if (q.getUri().equalsIgnoreCase(OWL.someValuesFrom.getURI())) {
			return model_.createSomeValuesFromRestriction(null, p, r);
		}
		else if (q.getUri().equalsIgnoreCase(OWL.allValuesFrom.getURI())) {
			return model_.createAllValuesFromRestriction(null, p, r);
		}
		else if (q.getUri().equalsIgnoreCase(OWL.hasValue.getURI())) {
			return model_.createHasValueRestriction(null, p, r);
		}
		else 
			return null;
	}
	
	
	// create intersection class or union class
	private Resource createIntersectionOrUnionClass(Class<?> cls, List<RDFNode> rdfList) throws Exception {
		final String prefix = "create";
		try {
			Method m = model_.getClass().getMethod(prefix + cls.getSimpleName(), String.class, RDFList.class);
			m.setAccessible(true);
			return (Resource)m.invoke(model_, null, model_.createList(rdfList.iterator()));
		}
		catch (Exception e) {
			System.err.println(e.toString());
			throw e;
		}
	}
	
	
	private SupportedAssociationQualifier getLgQualifier(AssociatedConcept concept) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		NameAndValueList nameValueList = concept.getAssociationQualifiers();
		if (nameValueList != null && nameValueList.getNameAndValueCount() > 0 ) {
			for (NameAndValue nameAndValue : nameValueList.getNameAndValue()) {
				// qualifier is saved in name field
				if (StringUtils.isEmpty(nameAndValue.getName()) == false) {
					return DaoUtility.getURIMap(cs_, SupportedAssociationQualifier.class, nameAndValue.getName());
				}
			}
		}
		return null;
	}
	
//	private boolean isAssociationSourceProcessed(ResolvedConceptReference rcr) {
//        return this.sourceCache.exists(rcr);
//    }
	
	private void addOntProperty(AssociationEntity entity) throws LBException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		// if it maps to owl/rdf properties, we don't need to add it
		if (LexRdfMap.isMapped(entity, ontFormat_) == true) 
			return;
			
		String namespace = resolveNamespace(entity.getEntityCodeNamespace());
		OntProperty ontProperty = model_.createOntProperty(namespace
				+ entity.getEntityCode());

		if (entity.isIsTransitive() != null && entity.isIsTransitive() == true) {
			ontProperty.addRDFType(OWL.TransitiveProperty);
		}

		for (org.LexGrid.commonTypes.Property property : entity.getProperty()) {
			processLgProperty(ontProperty, property);
		}
	}

	private void processLgProperty(OntProperty ontProperty,
			org.LexGrid.commonTypes.Property property) throws LBException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		// get supported property
		SupportedProperty supProp = DaoUtility.getURIMap(cs_, SupportedProperty.class, property.getPropertyName());
		if (supProp == null) {
			throw new LBException("no supported property found for " + property.getPropertyName() );
		}
		
		if (supProp.getUri().equalsIgnoreCase(
				RDFS.domain.getURI())) { 
			String id = resolveNamespace(currentNamespace_) + property.getValue().getContent();
			this.createTriple(ontProperty, model_.getProperty(supProp.getUri()), model_.getResource(id));
		} else if (supProp.getUri().equalsIgnoreCase(
				OWL.inverseOf.getURI())) {
			StringHelper sh = new StringHelper(
					property.getValue().getContent(), model_.getNsPrefixMap());
			if (sh.getStrFormat().equals(StringHelper.StrFormat.TYPE_VALUE)) {
				OntProperty localProperty = model_.createOntProperty(sh.getValue());
				localProperty.addRDFType(sh.getType());
				ontProperty.addInverseOf(localProperty);
			} else
				throw new LBException("invalid property value");
		} else if (supProp.getUri().equalsIgnoreCase(
				RDFS.range.getURI())) {
			String id = resolveNamespace(currentNamespace_) + property.getValue().getContent();
			this.createTriple(ontProperty, model_.getProperty(supProp.getUri()), model_.getResource(id));
		} else if (supProp.getUri().equalsIgnoreCase(
				RDF.type.getURI())) {
			StringHelper sh = new StringHelper(
					property.getValue().getContent(), model_.getNsPrefixMap());
			ontProperty.addRDFType(sh.getType());
		} else if (supProp.getUri().equalsIgnoreCase(
				RDFS.subPropertyOf.getURI())) {
			StringHelper sh = new StringHelper(
					property.getValue().getContent(), model_.getNsPrefixMap());
			OntProperty localProperty = model_.createOntProperty(sh.getValue());
			localProperty.addRDFType(sh.getType());
			ontProperty.addSubProperty(localProperty);
		} else {
			System.err.println("attention, not inlucded property: "
					+ property.getPropertyName() + ": "
					+ property.getValue().getContent());
		}
	}

	private void processInstance(List<Entity> instanceList) throws LBException {

		/*
		 * find the if this instance has defined type in the association table,
		 * if it does, create a that type of class, if the instance has no
		 * defined type, map it to owl:thing
		 */
		for (Entity entity : instanceList) {
			CodedNodeGraph localCng = this.restrictToCngAssociation(RDF.type
					.getLocalName());
			ConceptReference sourceConRef = new ConceptReference();
			sourceConRef.setCode(entity.getEntityCode());
			sourceConRef.setCodeNamespace(entity.getEntityCodeNamespace());
			sourceConRef.setCodingSchemeName(cs_.getCodingSchemeName());
			sourceConRef.setConceptCode(entity.getEntityCode());
			ResolvedConceptReferenceList resolvedConRefList = localCng
					.resolveAsList(sourceConRef, true, false, 0, 1, null, null,
							null, -1);

			// resolvedConRefList size is always one
			if (resolvedConRefList.getResolvedConceptReferenceCount() == 0
					|| resolvedConRefList.getResolvedConceptReferenceCount() > 1) {
				throw new LBException("CNG resolve as list failed");
			}

			AssociationList associationList = resolvedConRefList
					.getResolvedConceptReference(0).getSourceOf();

			if (associationList.getAssociation(0).getAssociatedConcepts()
					.getAssociatedConceptCount() == 0)
				// no specific type, treat it as owl:thing
				this.addOwlThing(model_, cs_, entity);
			else {
				// specific type
				for (AssociatedConcept target : associationList.getAssociation(
						0).getAssociatedConcepts().getAssociatedConcept()) {
					String namespace = this.resolveNamespace(entity
							.getEntityCodeNamespace());
					OntResource resource = model_.createOntResource(namespace
							+ entity.getEntityCode());
					OntResource typeResource = model_.getOntResource(this
							.resolveNamespace(target.getCodeNamespace())
							+ target.getCode());
					if (typeResource == null) {
						typeResource = model_.createOntResource(this
								.resolveNamespace(target.getCodeNamespace())
								+ target.getCode());
					}
					resource.addRDFType(typeResource);
				}
			}
		}
	}

	// if the instance has not defined type, map it to owl:thing
	private void addOwlThing(OntModel model, CodingScheme cs, Entity entity)
			throws LBException {
		String namespace = this.resolveNamespace(entity
				.getEntityCodeNamespace());
		OntResource resource = model.createOntResource(namespace
				+ entity.getEntityCode());
		resource.addRDFType(OWL.Thing);
	}

	private void addSkosConcept(Entity entity) throws LBException {
		System.out.println("SKOS!!!!!!"); // for dev
		String namespace = this.resolveNamespace(entity
				.getEntityCodeNamespace());
		OntResource resource = model_.createOntResource(namespace + entity.getEntityCode());
		resource.addRDFType(Skos.Concept);

		Map<String, ReifiedStatement> statementHash = new HashMap<String, ReifiedStatement>();
		// handle entity's comments
		for (Comment comment : entity.getComment()) {
			this.addLgCommentOrDefinitionOrPresentation(resource, Skos.note, comment, statementHash);
		}

		// handle entity's definition
		for (Definition def : entity.getDefinition()) {
			this.addLgCommentOrDefinitionOrPresentation(resource, Skos.definition, def, statementHash);
		}

		// handle entity's presentation
		for (Presentation presentation : entity.getPresentation()) {
			if (presentation.getIsPreferred() == true) {
				this.addLgCommentOrDefinitionOrPresentation(resource, Skos.prefLabel, presentation,
						statementHash);
			} else {
				this.addLgCommentOrDefinitionOrPresentation(resource, Skos.altLabel, presentation,
						statementHash);
			}
		}

		// handle property links
		// TODO: test
		for (PropertyLink propLink : entity.getPropertyLink()) {
			AnnotationProperty ap = model_.createAnnotationProperty(namespace
					+ propLink.getPropertyLink());

			String sourceProp = propLink.getSourceProperty();
			String targetProp = propLink.getTargetProperty();
			statementHash.get(sourceProp).addProperty(ap,
					statementHash.get(targetProp).getURI());

		}

	}

	private Property createPropertyLink() {
		OntProperty p = model_
				.getOntProperty(LexRdfConstants.LEXRDF_PROPERTY_LINK);
		if (p != null)
			return p;
		p = model_.createOntProperty(LexRdfConstants.LEXRDF_PROPERTY_LINK);
		p.setRDFType(OWL.AnnotationProperty);
		return p;
	}

	private void addOwlClass(Entity entity) throws LBException {
		String namespace = this.resolveNamespace(entity
				.getEntityCodeNamespace());

		// add owl class
		OntClass owlClass = model_.createClass(namespace
				+ entity.getEntityCode());

		// set rdf:isDefined
		if (entity.isIsDefined() != null)
			owlClass.addProperty(LexRdf.isDefined, entity.isIsDefined().toString());

		Map<String, ReifiedStatement> statementHash = new HashMap<String, ReifiedStatement>();
		// handle entity's comments
		for (Comment comment : entity.getComment()) {
			this.addLgCommentOrDefinitionOrPresentation(owlClass, Skos.note, comment, statementHash);
		}

		// handle entity's definition
		for (Definition def : entity.getDefinition()) {
			this.addLgCommentOrDefinitionOrPresentation(owlClass, Skos.definition, def, statementHash);
		}

		// handle entity's presentation
		for (Presentation presentation : entity.getPresentation()) {
			if (presentation.getIsPreferred() != null && presentation.getIsPreferred() == true) {
				this.addLgCommentOrDefinitionOrPresentation(owlClass, Skos.prefLabel, presentation,
						statementHash);
			} else {
				this.addLgCommentOrDefinitionOrPresentation(owlClass, Skos.altLabel, presentation,
						statementHash);
			}
		}

		// handle property links
		// TODO: test
		for (PropertyLink propLink : entity.getPropertyLink()) {
			AnnotationProperty ap = model_.createAnnotationProperty(namespace
					+ propLink.getPropertyLink());
			String sourceProp = propLink.getSourceProperty();
			String targetProp = propLink.getTargetProperty();
			statementHash.get(sourceProp).addProperty(ap,
					statementHash.get(targetProp).getURI());

		}
	}

	// only a regular mapping from lg:property's value to
	// comment/definition/presentation. not necessary to consider the property
	// name
	private void addLgCommentOrDefinitionOrPresentation(OntResource resource, Property prop,
			org.LexGrid.commonTypes.Property lgProp,
			Map<String, ReifiedStatement> statementHash) {
		Statement statement = model_.createStatement(resource, prop, lgProp
				.getValue().getContent());

		ReifiedStatement rs = model_.createReifiedStatement(statement);

		// general stuff for comment/definition/presentation
		if (StringUtils.isEmpty(lgProp.getLanguage()) == false)
			rs.addProperty(DC.language, lgProp.getLanguage());

		for (Source source : lgProp.getSource()) {
			if (StringUtils.isEmpty(source.getContent()) == false)
				rs.addProperty(DC.source, source.getContent());
		}

		if (lgProp instanceof Definition) {
			if (((Definition) lgProp).isIsPreferred() != null && ((Definition) lgProp).isIsPreferred() == true)
				rs.addProperty(LexRdf.isPreferred, "true");
		} else if (lgProp instanceof Presentation) {
			if (StringUtils.isEmpty(((Presentation) lgProp)
					.getDegreeOfFidelity()) == false)
				rs.addProperty(LexRdf.degreeOfFidelity,
						((Presentation) lgProp).getDegreeOfFidelity());
			if (((Presentation) lgProp).getMatchIfNoContext() != null
					&& ((Presentation) lgProp).getMatchIfNoContext() == true)
				rs.addProperty(LexRdf.matchIfNoContext,
						((Presentation) lgProp).getMatchIfNoContext()
								.toString());
			if (StringUtils.isEmpty(((Presentation) lgProp)
					.getRepresentationalForm()) == false)
				rs.addProperty(LexRdf.representationalForm,
						((Presentation) lgProp).getRepresentationalForm());
		}

		statementHash.put(lgProp.getPropertyId(), rs);

		// TODO property qualifier
	}

	private String resolveNamespace(String localId) throws LBException {
		Map<String, String> supportedNamespace = model_.getNsPrefixMap();
		String uri = supportedNamespace.get(localId);
		if (uri != null) {
			if (!uri.endsWith("#"))
				uri = uri + "#";
		}

		// for dev only
		if (uri == null)
			throw new LBException("ns " + localId
					+ " has not been imported to ontology yet!");

		return uri;
	}

	private void initNamespace() {
		Map<String, String> nsMap = new HashMap<String, String>();

		// skos
		nsMap.put(LexRdfConstants.SKOS_NS_PREFIX, LexRdfConstants.SKOS_NS_URI);

		// lexrdf
		nsMap.put(LexRdfConstants.LEXRDF_NS_PREFIX,
				LexRdfConstants.LEXRDF_NS_URI);

		// add codingSchemeURI
		currentNamespace_ = model_.getOntology(cs_.getCodingSchemeURI())
				.getLabel(null);
		nsMap.put("", cs_.getCodingSchemeURI());
		nsMap.put(currentNamespace_, cs_.getCodingSchemeURI() + "#");

		// check coding scheme mappings
		if (cs_.getMappings() == null) {
			// supported namespace
			SupportedNamespace[] supportedNss = cs_.getMappings()
					.getSupportedNamespace();
			for (SupportedNamespace ns : supportedNss) {
				nsMap.put(ns.getLocalId(), ns.getUri());
			}
			// supported associations
			SupportedAssociation[] supportedAssns = cs_.getMappings()
					.getSupportedAssociation();
			for (SupportedAssociation assn : supportedAssns)
				model_.createOntProperty(assn.getUri());
		}

		model_.setNsPrefixes(nsMap);
	}

	/*
	 * when we need to put restrictions on cng, we need to create a copy of it
	 * and run on it, the queries and restrictions can change the cng itself.
	 */
	private CodedNodeGraph restrictToCngAssociation(String restriction)
			throws LBException {
		LexBIGService lbsvc = LexBIGServiceImpl.defaultInstance();
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion(cs_.getRepresentsVersion());
		CodedNodeGraph localCng;

		localCng = lbsvc.getNodeGraph(cs_.getCodingSchemeURI(), versionOrTag,
				null);
		localCng.restrictToAssociations(Constructors
				.createNameAndValueList(restriction), null);

		return cng_.intersect(localCng);
	}
	
	private CodedNodeSet restrictToCnsAnonymous() throws LBException{
		LexBIGService lbsvc = LexBIGServiceImpl.defaultInstance();
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion(cs_.getRepresentsVersion());
		CodedNodeSet localCns;

		localCns = lbsvc.getNodeSet(cs_.getCodingSchemeURI(), versionOrTag, null);
		localCns.restrictToAnonymous(AnonymousOption.ANONYMOUS_ONLY);
		ResolvedConceptReferencesIterator iterator = localCns.resolve(null, null, null, null, true);
		iterator.numberRemaining();
		return cns_.intersect(localCns);
	}

	private OntologyFormat findOntFormat() {
	    for (org.LexGrid.commonTypes.Property p : cs_.getProperties().getProperty()) {
	        if (p.getPropertyName().equals(OntologyFormat.getMetaName())) {
	            return OntologyFormat.valueOf(p.getValue().getContent());
	        }
	    }
	    return null;
	}
	
	public void toTripleStore(CodingScheme cs, CodedNodeGraph baseCng,
			CodedNodeSet cns, Writer writer, LgMessageDirectorIF messenger, OntologyFormat ontFormat){
		Model baseModel = this.getBaseModel(cs.getCodingSchemeURI());

		// create or open the default model
		model_ = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM,
				baseModel);
		
		this.cs_ = cs;
		this.cns_ = cns;
		this.cng_ = baseCng;
		this.messenger_ = messenger;
		this.writer_ = writer;
		if (ontFormat != null)
		    this.ontFormat_ = ontFormat;
		else
		    ontFormat_ = this.findOntFormat();

		try {
			// coding schema mapping
			this.codingSchemeMapping();
		} catch (SecurityException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		}

		// entity mapping
		try {
			this.entityMapping();
		} catch (LBResourceUnavailableException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (SecurityException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (LBException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		}
		
		// association mapping
		try {
			this.associationMapping();
		} catch (SecurityException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (LBException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		}

		// model_.write(System.out); // for dev & test/

		// now write the model in XML form to a file
		try {
			this.toOwlOntology(cs_.getCodingSchemeURI());
		} catch (ClassNotFoundException e) {
			this.messenger_.error(e.toString());
			e.printStackTrace();
		}
		
		// Close the database connection
		model_.close();
		store_.getTableFormatter().truncate(); // for development only
		store_.getConnection().close();
		
		System.out.println("proccessed association: " + Integer.toString(assnCounter));
	}

	public void toOwlOntology(String iri) throws ClassNotFoundException {
		Model baseModel = this.getBaseModel(iri);

		OntModel localModel = ModelFactory.createOntologyModel(
				OntModelSpec.OWL_MEM, baseModel);

		RDFWriter rdfw = localModel
        		.getWriter(LexRdfConstants.RDFXML_ABBREV);
        rdfw.setProperty(LexRdfConstants.SHOW_XML_DECLARATION, true);
        rdfw.setProperty(LexRdfConstants.XML_BASE, cs_.getCodingSchemeURI());
        rdfw.setProperty(LexRdfConstants.RELATIVE_URIS, "");
        //FileOutputStream f = new FileOutputStream(outputFile_);
        rdfw.write(localModel, writer_, cs_.getCodingSchemeURI());
		baseModel.close();
	}

	public static void main(String[] args) {
		ExportStatus status = new ExportStatus();
        status.setState(ProcessState.PROCESSING);
        status.setStartTime(new Date(System.currentTimeMillis()));
		
		ExporterMessageDirector md = new ExporterMessageDirector("LexRdfExporter", status);
		
//		String codingSchemeUri = "http://www.xfront.com/owl/ontologies/camera", codingSchemeVersion = "UNASSIGNED";
		String codingSchemeUri = "http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl", 
		       codingSchemeVersion = "version 1.2",
		       output = "C:/temp/pizza.owl";
//		String codingSchemeUri = "urn:lsid:bioontology.org:cell", codingSchemeVersion = "UNASSIGNED";
//		String codingSchemeUri = "urn:lsid:bioontology.org:uberon", codingSchemeVersion = "UNASSIGNED";
//		String codingSchemeUri = "urn:oid:2.16.840.1.113883.6.110", codingSchemeVersion = "1993.bvt";

		LexGridToOwlRdfConverter converter = new LexGridToOwlRdfConverter();

		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion(codingSchemeVersion);

		LexBIGService lbsvc = LexBIGServiceImpl.defaultInstance();
		try {
			CodingScheme codingScheme = lbsvc
					.resolveCodingScheme(
							codingSchemeUri,
							Constructors
									.createCodingSchemeVersionOrTagFromVersion(codingSchemeVersion));
			CodedNodeSet cns = lbsvc.getNodeSet(codingSchemeUri, versionOrTag,
					null);

			CodedNodeGraph cng = lbsvc.getNodeGraph(codingSchemeUri,
					versionOrTag, null);

			FileOutputStream f = new FileOutputStream(output);
			Writer w = new OutputStreamWriter(f);
			converter.toTripleStore(codingScheme, cng, cns, w, md, null);

		}  catch (LBException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		}

	}

}