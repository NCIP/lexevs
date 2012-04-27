package org.lexgrid.lexevs.ndfrt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.lexevs.ndfrt.data.AssociationDef;
import org.lexgrid.lexevs.ndfrt.data.KindDef;
import org.lexgrid.lexevs.ndfrt.data.PropertyDef;
import org.lexgrid.lexevs.ndfrt.data.QualifierDef;
import org.lexgrid.lexevs.ndfrt.data.RoleDef;



public class MapNDFRT2LexEVS {
	public NdfrtXMLProcessor processor;
	public List<RoleDef> roles;
	public List<KindDef> kinds;
	public List<PropertyDef> properties;
	public List<AssociationDef> associations;
	public List<QualifierDef> qualifiers;
	public CodingScheme scheme;
    LexEvsServiceLocator locator;
    DatabaseServiceManager dbManager;
    AuthoringService authoringService;
    
	private MapNDFRT2LexEVS() {
	}

	public MapNDFRT2LexEVS(URI uri, LgMessageDirectorIF message,
			boolean validate) {
		this();
		init(uri, message, validate);
	}

	private void init(URI uri, LgMessageDirectorIF message, boolean validate) {
		
		processor = new NdfrtXMLProcessor();
		
        locator = LexEvsServiceLocator.getInstance();
        dbManager = locator.getDatabaseServiceManager();
        authoringService = dbManager.getAuthoringService();
        
		try {
			roles = processor.getRoleDefList(uri, message, validate);
			kinds = processor.getKindDefList(uri, message, validate);
			properties = processor.getPropertyDefList(uri, message, validate);
			associations = processor.getAssociationDefList(uri, message,
					validate);
			qualifiers = processor.getQualifierDefList(uri, message, validate);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void buildCodingScheme(URI uri, LgMessageDirectorIF messages,
			boolean validateXML, CodingSchemeManifest manifest)
			throws CodingSchemeAlreadyLoadedException, LBRevisionException {
		// process coding scheme.
		CodingScheme scheme;
		scheme = processor
				.getCodingScheme(uri, messages, validateXML, manifest);
		scheme.setCodingSchemeURI("urn:oid:va.ndf-rt");
		scheme.setEntityDescription(processEnityDescription());
		scheme.setRelations(processRelations());
		processMappingsAndPredicates(scheme);
		//Store coding scheme
		 authoringService.loadRevision(scheme, null, null);
	}


	private Relations[] processRelations() {
		Relations relations = new Relations();
		relations.setContainerName("relations");
		Relations[] relList = new Relations[] { relations };
		return relList;
	}


	private EntityDescription processEnityDescription() {
		EntityDescription ed = new EntityDescription();
		ed.setContent("the Veterans Health Administration/n"
				+ "(VHA) National Drug File Reference Terminology (NDF-RT)./n"
				+ " NDF-RT is the terminology used by FDA and the FedMed/n"
				+ "collaboration to code these essential pharmacologic/n"
				+ "properties of medications:/n" + "Mechanism of Action/n"
				+ "Physiologic Effect/n" + "Structural Class");
		return ed;
	}

	private void processMappingsAndPredicates(CodingScheme scheme) {
		Mappings mappings = new Mappings();
		mappings.setSupportedCodingScheme(processSupportedCodingSchemes());
		List<SupportedAssociation> associations = processSupportedAssociations();
		Relations relations = scheme.getRelations(0);
		for(SupportedAssociation sa : associations){
	     AssociationPredicate predicate = new AssociationPredicate();
	     predicate.setAssociationName(sa.getContent());
	     //TODO do I need to add the association source here?
		relations.addAssociationPredicate(predicate);
		}
		mappings.setSupportedNamespace(processSupportedNamespace());
		mappings.setSupportedAssociation(associations);
		mappings.setSupportedProperty(processSupportedProperties());
	}

	private List<SupportedProperty> processSupportedProperties() {
		List<SupportedProperty> list = new ArrayList<SupportedProperty>();
		for (KindDef k : kinds) {
			SupportedProperty prop = new SupportedProperty();
			prop.setContent(k.name);
			prop.setLocalId(k.id);
			prop.setUri(scheme.getCodingSchemeURI());
			list.add(prop);
		}

		for (PropertyDef p : properties) {
			SupportedProperty prop = new SupportedProperty();
			prop.setContent(p.name);
			prop.setLocalId(p.id);
			prop.setUri(scheme.getCodingSchemeURI());
			list.add(prop);
		}
		return list;
	}

	private List<SupportedAssociation> processSupportedAssociations() {
		List<SupportedAssociation> list = new ArrayList<SupportedAssociation>();
		for (AssociationDef assoc : this.associations) {
			SupportedAssociation a = new SupportedAssociation();
			a.setContent(assoc.name);
			a.setEntityCode(assoc.code);
			a.setLocalId(assoc.id);
			a.setUri(scheme.getCodingSchemeURI());
			list.add(a);
		}
		for (RoleDef role : this.roles) {
			SupportedAssociation a = new SupportedAssociation();
			a.setCodingScheme(role.name);
			a.setEntityCode(role.code);
			a.setLocalId(role.id);
			a.setUri(scheme.getCodingSchemeURI());
			list.add(a);
		}
		//TODO populate association predicates here?
		return list;
	}

	private SupportedNamespace[] processSupportedNamespace() {
		SupportedNamespace namespace = new SupportedNamespace();
		namespace.setContent(scheme.getCodingSchemeName());
		namespace.setUri(scheme.getCodingSchemeURI());
		return new SupportedNamespace[] { namespace };
	}

	private SupportedCodingScheme[] processSupportedCodingSchemes() {
		SupportedCodingScheme supportedScheme = new SupportedCodingScheme();
		supportedScheme.setContent(scheme.getLocalName(0));
		supportedScheme.setUri(scheme.getCodingSchemeURI());
		return new SupportedCodingScheme[] { supportedScheme };
	}
	
	public void processEnitities(URI uri, LgMessageDirectorIF message,
			boolean validate) throws MalformedURLException, IOException,
			XMLStreamException, FactoryConfigurationError {

		BufferedReader in = null;
		XMLStreamReader xmlStreamReader;


		in = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));

		xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(
				in);

		for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
				.next()) {
			Entity entity = new Entity();
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CONCEPT_DEF)) {
			}

			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {

				Presentation[] presentations = processPresentation(xmlStreamReader);
				entity.setPresentation(presentations);
				EntityDescription entityDescription = new EntityDescription();
				entityDescription.setContent(presentations[0].getValue().getContent());
				entity.setEntityDescription(entityDescription );
				
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				entity.setEntityCode(xmlStreamReader.getElementText());

				System.out.println("entityCode: "
						+ entity.getEntityCode());
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ID)) {

//				entity.setProperty(new Property[]{property});

//				System.out.println("id property for entity: "
//						+ property.getValue());
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
			
//				 = xmlStreamReader.getElementText();
//				System.out.println("Role source: " + role.namespace);
			}

//			if (event == XMLStreamConstants.START_ELEMENT
//					&& xmlStreamReader.getLocalName().equals(
//							NdfrtConstants.PROPERTY_DEF)) {
//
//				break;
//			}
		}

	}

	private Property[] processProperty(XMLStreamReader xmlStreamReader) throws XMLStreamException {
		processProperty(xmlStreamReader);
		Property property = new Property();
		property.setPropertyId("P2");
		property.setPropertyName("id");
		property.setPropertyType("Property");
		Text text = new Text();
		String name = xmlStreamReader.getElementText();
		text.setContent(name);
		property.setValue(text);
		return new Property[]{property};
	}
	
	private void setProperty(){
		
	}

	private Presentation[] processPresentation(XMLStreamReader xmlStreamReader) throws XMLStreamException {
		processPresentation(xmlStreamReader);
		Presentation presentation = new Presentation();
		presentation.setPropertyId("P1");
		presentation.setIsPreferred(true);
		presentation.setPropertyName("PREFERRED_NAME");
		presentation.setPropertyType("presentation");
		Text text = new Text();
		String name = xmlStreamReader.getElementText();
		text.setContent(name);
		presentation.setValue(text);
		return new Presentation[] {presentation};
		
	}

}