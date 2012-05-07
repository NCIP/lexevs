package org.lexgrid.lexevs.ndfrt;

import java.io.BufferedReader;
import java.io.File;
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
import org.LexGrid.commonTypes.PropertyQualifier;
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
import org.lexgrid.lexevs.ndfrt.data.BaseDef;
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
		// Store coding scheme
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
		for (SupportedAssociation sa : associations) {
			AssociationPredicate predicate = new AssociationPredicate();
			predicate.setAssociationName(sa.getContent());
			// TODO do I need to add the association source here?
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
		// TODO populate association predicates here?
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
			List<Property> properties = new ArrayList<Property>();
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CONCEPT_DEF)) {
				
			while(xmlStreamReader.hasNext()){
             event = xmlStreamReader.next();
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {

				Presentation[] presentations = processPresentation(xmlStreamReader);
				entity.setPresentation(presentations);
				EntityDescription entityDescription = new EntityDescription();
				entityDescription.setContent(presentations[0].getValue()
						.getContent());
				entity.setEntityDescription(entityDescription);

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				entity.setEntityCode(xmlStreamReader.getElementText());
				// TODO Set this as a property as well?
//				System.out.println("entityCode: " + entity.getEntityCode());
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(NdfrtConstants.ID)) {

				// entity.setProperty(new Property[]{property});
				properties.add(processProperty(xmlStreamReader, "P2", null));
//				 System.out.println("id property for entity: "
//				 + properties.get(0).getPropertyName());
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
				properties.add(processProperty(xmlStreamReader, "P3", null));
				// = xmlStreamReader.getElementText();
				// System.out.println("Role source: " + role.namespace);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.KIND)) {
				properties.add(processKindsProperty(xmlStreamReader, "P4",
						kinds));
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.PROPERTIES)) {

				properties.addAll(processPropertyList(xmlStreamReader));

			}
			 if (event == XMLStreamConstants.END_ELEMENT
			 && xmlStreamReader.getLocalName().equals(
			 NdfrtConstants.CONCEPT_DEF)) {
			 entity.setProperty(properties);
			 System.out.println("Entity:" + entity.getPresentation(0).getValue().getContent() + " : "  + entity.getEntityCode());
			 Property[] prop = entity.getProperty();
			 for(Property p : prop){
				 System.out.println("Property id: " + p.getPropertyId());
				 System.out.println("Property name: " + p.getPropertyName());
				 System.out.println("Property value: " + p.getValue().getContent());
				 PropertyQualifier[] pq = p.getPropertyQualifier();
				 for(PropertyQualifier pqr : pq){
					System.out.println("Qualifier name: " + pqr.getPropertyQualifierName());
					System.out.println("Qualifier value " + pqr.getValue().getContent());
				 }
				
			 }
			 break;
			 }
			}
			}
		}
		xmlStreamReader.close();

		in.close();
	}

	private Property processKindsProperty(XMLStreamReader xmlStreamReader,
			String id, List<KindDef> kinds) throws XMLStreamException {
		Property property = new Property();
		String name = xmlStreamReader.getLocalName();
		String value = xmlStreamReader.getElementText();
		if (kinds != null) {
			for (KindDef k : kinds) {
				if (k.code.equals(value))
					property.setPropertyId(id);
				property.setPropertyName(name);
				property.setPropertyType("property");
				Text text = new Text();
				text.setContent(value);
				property.setValue(text);
				return property;
			}
		}
		return null;
	}

	private List<Property> processPropertyList(XMLStreamReader xmlStreamReader)
			throws XMLStreamException {
		int counter = 3;
		List<Property> properties = new ArrayList<Property>();
		while (xmlStreamReader.hasNext()) {
			int event = xmlStreamReader.next();
			if (event == XMLStreamConstants.START_ELEMENT) {
				if (xmlStreamReader.getLocalName() == NdfrtConstants.PROPERTY) {
					properties.add(processNDFRTProperty(xmlStreamReader,
							counter, this.properties));
				}

			}
			if (event == XMLStreamConstants.END_ELEMENT) {
				if (xmlStreamReader.getLocalName() == NdfrtConstants.PROPERTIES) {
					return properties;
				}
			}
		}
		return null;

	}

	private Property processNDFRTProperty(XMLStreamReader xmlStreamReader,
			int id, List<PropertyDef> properties) throws XMLStreamException {
		Property property = new Property();

		while (xmlStreamReader.hasNext()) {
			int event = xmlStreamReader.next();
			if (event == XMLStreamConstants.START_ELEMENT) {
				if (xmlStreamReader.getLocalName() == NdfrtConstants.NAME) {
					String name = xmlStreamReader.getElementText();
					for (PropertyDef p : properties) {
						if (p.code.equals(name)) {
							property.setPropertyId(String.valueOf(id++));
							property.setPropertyName(p.name);
							property.setPropertyType("property");
							break;
						}
					}
				}
				if (xmlStreamReader.getLocalName() == NdfrtConstants.VALUE) {

					property.setValue(setLexEVSText(xmlStreamReader
							.getElementText()));
					//break;
			}
			if(xmlStreamReader.getLocalName() == NdfrtConstants.QUALIFIERS){
				property.setPropertyQualifier(processPropertyQualifierList(xmlStreamReader));
			}
			}
			if(event == XMLStreamReader.END_ELEMENT && xmlStreamReader.getLocalName().equals(NdfrtConstants.PROPERTY)){
				break;
			}
		}
		return property;
	}

	private List<PropertyQualifier> processPropertyQualifierList(
			XMLStreamReader xmlStreamReader) throws XMLStreamException {
		List<PropertyQualifier> list = new ArrayList<PropertyQualifier>();
		while (xmlStreamReader.hasNext()) {
			int event = xmlStreamReader.next();
			if (event == XMLStreamConstants.START_ELEMENT) {
				if (xmlStreamReader.getLocalName() == NdfrtConstants.QUALIFIER) {
					list.add(processPropertyQualifier(xmlStreamReader, this.qualifiers));
				}

			}
			if (event == XMLStreamConstants.END_ELEMENT) {
				if (xmlStreamReader.getLocalName() == NdfrtConstants.QUALIFIERS) {
					return list;
				}
			}
		}		return null;
	}

	private PropertyQualifier processPropertyQualifier(
			XMLStreamReader xmlStreamReader, List<QualifierDef> qualifiers2) throws XMLStreamException {
		PropertyQualifier pqualifier = new PropertyQualifier();
		while (xmlStreamReader.hasNext()) {
			int event = xmlStreamReader.next();
			if (event == XMLStreamConstants.START_ELEMENT) {
				if (xmlStreamReader.getLocalName() == NdfrtConstants.NAME) {
					String name = xmlStreamReader.getElementText();
					for (QualifierDef q : qualifiers) {
						if (q.code.equals(name)) {
							pqualifier.setPropertyQualifierName(name);
							break;
						}
					}
				}
				if (xmlStreamReader.getLocalName() == NdfrtConstants.VALUE) {

				 pqualifier.setValue(setLexEVSText(xmlStreamReader
							.getElementText()));
			}
			}
			if(event == XMLStreamReader.END_ELEMENT && xmlStreamReader.getLocalName().equals(NdfrtConstants.QUALIFIER)){
				break;
			}
		}
		return pqualifier;
	}

	private Text setLexEVSText(String elementText) {
		Text text = new Text();
		text.setContent(elementText);
		return text;
	}

	private Property processProperty(XMLStreamReader xmlStreamReader,
			String id, List<? extends BaseDef> list) throws XMLStreamException {
		Property property = new Property();
		property.setPropertyId(id);
		String name;
		if (list != null) {
			name = getPropertyNameFromList(list);
		}
		property.setPropertyName(xmlStreamReader.getLocalName());
		property.setPropertyType("property");
		Text text = new Text();
		name = xmlStreamReader.getElementText();
		text.setContent(name);
		property.setValue(text);
		return property;
	}

	private String getPropertyNameFromList(List<? extends BaseDef> list) {
		for (BaseDef p : list) {
			// if(p.name.equals.)
		}
		return null;
	}

	private Presentation[] processPresentation(XMLStreamReader xmlStreamReader)
			throws XMLStreamException {

		Presentation presentation = new Presentation();
		presentation.setPropertyId("P1");
		presentation.setIsPreferred(true);
		presentation.setPropertyName("PREFERRED_NAME");
		presentation.setPropertyType("presentation");
		Text text = new Text();
		String name = xmlStreamReader.getElementText();
		text.setContent(name);
		presentation.setValue(text);
		return new Presentation[] { presentation };

	}
	
	public static void main(String[] args){
		MapNDFRT2LexEVS map = new MapNDFRT2LexEVS(new File(args[0]).toURI(), null, true);
		try {
			map.processEnitities(new File(args[0]).toURI(), null, true);
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

}