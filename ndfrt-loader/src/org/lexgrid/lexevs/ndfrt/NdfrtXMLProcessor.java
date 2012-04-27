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

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexgrid.lexevs.ndfrt.data.AssociationDef;
import org.lexgrid.lexevs.ndfrt.data.KindDef;
import org.lexgrid.lexevs.ndfrt.data.PropertyDef;
import org.lexgrid.lexevs.ndfrt.data.QualifierDef;
import org.lexgrid.lexevs.ndfrt.data.RoleDef;

public class NdfrtXMLProcessor {

	public org.LexGrid.codingSchemes.CodingScheme getCodingScheme(URI uri,
			LgMessageDirectorIF messages, boolean validateXML,
			CodingSchemeManifest manifest)
			throws CodingSchemeAlreadyLoadedException {

		BufferedReader in = null;
		XMLStreamReader xmlStreamReader;
		CodingScheme cs = new CodingScheme();
		String codingSchemeName = new String();
        List<String> localNameList = new ArrayList<String>();
        String version = processPathForVersion(uri);
        cs.setRepresentsVersion(version);
		
		try {

			in = new BufferedReader(new InputStreamReader(uri.toURL()
					.openStream()));

			xmlStreamReader = XMLInputFactory.newInstance()
					.createXMLStreamReader(in);

			for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
					.next()) {

				if (event == XMLStreamConstants.START_ELEMENT
						&& xmlStreamReader.getLocalName().equals(
								NdfrtConstants.NAMESPACE_DEF)) {

				}

				if (event == XMLStreamConstants.START_ELEMENT
						&& xmlStreamReader.getLocalName().equals(
								NdfrtConstants.NAME)) {
					cs.setFormalName(xmlStreamReader.getElementText());
					System.out.println("Formal name text: "
							+ cs.getFormalName());
				}
				if (event == XMLStreamConstants.START_ELEMENT
						&& xmlStreamReader.getLocalName().equals(
								NdfrtConstants.CODE)) {
					codingSchemeName = xmlStreamReader.getElementText();
					cs.setCodingSchemeName(codingSchemeName);
					localNameList.add(codingSchemeName);
					System.out.println("Coding Scheme name text: "
							+ localNameList.get(0));
				}
				if (event == XMLStreamConstants.START_ELEMENT
						&& xmlStreamReader.getLocalName().equals(
								NdfrtConstants.ID)) {
					localNameList.add(xmlStreamReader.getElementText());
					System.out.println("Coding Scheme Property id text: "
							+ localNameList.get(1));
				}
				if (event == XMLStreamConstants.END_ELEMENT
						&& xmlStreamReader.getLocalName().equals(
								NdfrtConstants.NAMESPACE_DEF)) {
					break;
				}
			}
			xmlStreamReader.close();

			in.close();

		} catch (IOException e) {
			messages.error("Problem reading file at: "
					+ (uri == null ? "path appears to be null" : uri.toString()));
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cs.setLocalName(localNameList );
		return cs;

	}

	private String processPathForVersion(URI uri) {
		try{
		String path = uri.getPath();
		int index = path.indexOf("Public_2");
		path = path.substring(index + 7,  index + 17);
		return path;
		}
		catch(Exception e){
		return "VERSION_MISSING";}
	}

	public List<KindDef> getKindDefList(URI uri, LgMessageDirectorIF message,
			boolean validate) throws MalformedURLException, IOException,
			XMLStreamException, FactoryConfigurationError {
		List<KindDef> list = new ArrayList<KindDef>();
		BufferedReader in = null;
		XMLStreamReader xmlStreamReader;


		in = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));

		xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(
				in);

		for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
				.next()) {
			KindDef kind = new KindDef();
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.KIND_DEF)) {
			}

			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {
				kind.name = xmlStreamReader.getElementText();
				System.out.println("Kind Property name text: "
						+ kind.name);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				kind.code = xmlStreamReader.getElementText();
				System.out.println("Kind Property id text: "
						+ kind.code);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ID)) {

						kind.id = xmlStreamReader.getElementText();
						System.out.println("Kind PropertyQualifier text: " + kind.id);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
				kind.namespace = xmlStreamReader.getElementText();
				System.out.println("Kind Property source: " + NdfrtConstants.NAMESPACE);
			}
			list.add(kind);
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ROLE_DEF)) {

				break;
			}
		}

		return list;
	}

	public List<RoleDef> getRoleDefList(URI uri, LgMessageDirectorIF message,
			boolean validate) throws MalformedURLException, IOException,
			XMLStreamException, FactoryConfigurationError {
        List<RoleDef> list = new ArrayList<RoleDef>();
		BufferedReader in = null;
		XMLStreamReader xmlStreamReader;


		in = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));

		xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(
				in);

		for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
				.next()) {
			RoleDef role = new RoleDef();
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ROLE_DEF)) {
			}

			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {
				role.name = xmlStreamReader.getElementText();
				System.out.println("Role name text: "
						+ role.name);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				role.code = xmlStreamReader.getElementText();
				System.out.println("Role code text: "
						+ role.code);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ID)) {
				role.id = xmlStreamReader.getElementText();
				System.out.println("Role id text: "
						+ role.id);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
				role.namespace = xmlStreamReader.getElementText();
				System.out.println("Role source: " + role.namespace);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.DOMAIN)) {
				role.domain = xmlStreamReader.getElementText();
				System.out.println("domain: " + role.domain);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.RANGE)) {
				role.range = xmlStreamReader.getElementText();
				System.out.println("range source: " + role.range);
			}
			list.add(role);
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.PROPERTY_DEF)) {

				break;
			}
		}

		return list;
	}
	

	
	public List<PropertyDef> getPropertyDefList(URI uri, LgMessageDirectorIF message,
			boolean validate) throws MalformedURLException, IOException,
			XMLStreamException, FactoryConfigurationError {
		List<PropertyDef> propList = new ArrayList<PropertyDef>();
		BufferedReader in = null;
		XMLStreamReader xmlStreamReader;


		in = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));

		xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(
				in);

		for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
				.next()) {
			PropertyDef property = new PropertyDef();
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.PROPERTY_DEF)) {
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {
				property.name = xmlStreamReader.getElementText();
				System.out.println("Property name text: "
						+ property.name);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				property.code = xmlStreamReader.getElementText();
				System.out.println("Property code text: "
						+ property.code);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ID)) {
				property.id = xmlStreamReader.getElementText();
				System.out.println("Property id text: "
						+ property.id);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
				property.namespace = xmlStreamReader.getElementText();
				System.out.println("Property source: " + property.namespace);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.RANGE)) {
				property.range = xmlStreamReader.getElementText();
				System.out.println("range source: " + property.range);
			}
			if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals(NdfrtConstants.PICKLIST)){
				String name = xmlStreamReader.getLocalName();
				List<String> list = getPickList(xmlStreamReader);
				for(String s : list){
					System.out.println("PicklistItem: " + s);
				}

				property.pickList = list;
			}
			propList.add(property);
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ASSOCIATION_DEF)) {

				break;
			}
		}

		return propList;
	}
	
	public List<AssociationDef> getAssociationDefList(URI uri, LgMessageDirectorIF message,
			boolean validate) throws MalformedURLException, IOException,
			XMLStreamException, FactoryConfigurationError {
		List<AssociationDef> associations = new ArrayList<AssociationDef>();
		BufferedReader in = null;
		XMLStreamReader xmlStreamReader;


		in = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));

		xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(
				in);

		for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
				.next()) {
			AssociationDef association = new AssociationDef();
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ASSOCIATION_DEF)) {
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {
				association.name = xmlStreamReader.getElementText();
				System.out.println("Association name text: "
						+ association.name);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				association.code = xmlStreamReader.getElementText();
				System.out.println("Association code text: "
						+ association.code);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ID)) {
				association.id = xmlStreamReader.getElementText();
				System.out.println("Association id text: "
						+ association.id);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
				association.namespace = xmlStreamReader.getElementText();
				System.out.println("Association source: " + association.namespace);
			}
			associations.add(association);
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.QUALIFIER_DEF)) {

				break;
			}
		}

		return null;
	}
	
	
	public List<QualifierDef> getQualifierDefList(URI uri, LgMessageDirectorIF message,
			boolean validate) throws MalformedURLException, IOException,
			XMLStreamException, FactoryConfigurationError {
		List<QualifierDef> list = new ArrayList<QualifierDef>();
		BufferedReader in = null;
		XMLStreamReader xmlStreamReader;


		in = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));

		xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(
				in);

		for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
				.next()) {
			QualifierDef qualifier = new QualifierDef();
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.QUALIFIER_DEF)) {
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {
				qualifier.name = xmlStreamReader.getElementText();
				System.out.println("Qualifier name text: "
						+ qualifier.name);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				qualifier.code = xmlStreamReader.getElementText();
				System.out.println("Qualifier code text: "
						+ qualifier.code);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ID)) {
				qualifier.id = xmlStreamReader.getElementText();
				System.out.println("Qualifier id text: "
						+ qualifier.id);
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
				qualifier.namespace = xmlStreamReader.getElementText();
				System.out.println("Qualifier source: " + qualifier.namespace);
			}
			if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals(NdfrtConstants.PICKLIST)){
					
				qualifier.pickList = getPickList(xmlStreamReader);
		    }
			list.add(qualifier);
			
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CONCEPT_DEF)) {

				break;
			}
		}
		return null;
		}
		
	private List<String> getPickList(XMLStreamReader xmlStreamReader) throws XMLStreamException {
		ArrayList<String> picklist = new ArrayList<String>();
		String localName = xmlStreamReader.getLocalName();
		int eventType = xmlStreamReader.getEventType();
		
		while(xmlStreamReader.hasNext()){
			int event = xmlStreamReader.next();

			if(event == XMLStreamConstants.START_ELEMENT){
				localName = xmlStreamReader.getLocalName();
				if(xmlStreamReader.getLocalName() == NdfrtConstants.PICKLIST_ITEM){
					picklist.add(xmlStreamReader.getElementText());
					
				}
			}
		   if(event == XMLStreamConstants.END_ELEMENT){
			   if(xmlStreamReader.getLocalName() == NdfrtConstants.PICKLIST){
				   return picklist;
			   }
		   }
		}
		
			
//		}
		

		return picklist;
	
		
	}

	public static void main(String[] args) {
		NdfrtXMLProcessor processor = new NdfrtXMLProcessor();
		try {

			processor.getCodingScheme(new File(args[0]).toURI(), null, true,
					null);
			try {
				processor.getKindDefList(new File(args[0]).toURI(), null, true);
				processor.getRoleDefList(new File(args[0]).toURI(), null, true);
				processor.getPropertyDefList(new File(args[0]).toURI(), null, true);
				processor.getAssociationDefList(new File(args[0]).toURI(), null, true);
				processor.getQualifierDefList(new File(args[0]).toURI(), null, true);
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

		} catch (CodingSchemeAlreadyLoadedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
