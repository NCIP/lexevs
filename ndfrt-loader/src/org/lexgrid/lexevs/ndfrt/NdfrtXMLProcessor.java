
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

/**
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */
public class NdfrtXMLProcessor {

	/**
	 * @param uri
	 * @param messages
	 * @param validateXML
	 * @param manifest
	 * @return
	 * @throws CodingSchemeAlreadyLoadedException
	 */
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
		cs.setCodingSchemeURI(NdfrtConstants.SCHEME_URI);
		return cs;

	}

	public String processPathForVersion(URI uri) {
		try{
		String path = uri.getPath();
		int index = path.indexOf("Public_2");
		path = path.substring(index + 7,  index + 17);
		return path;
		}
		catch(Exception e){
		return "VERSION_MISSING";}
	}

	/**
	 * @param uri
	 * @param message
	 * @param validate
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 */
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

			while(xmlStreamReader.hasNext()){
				
			    event = xmlStreamReader.next();
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {
				kind.name = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				kind.code = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ID)) {

						kind.id = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
				kind.namespace = xmlStreamReader.getElementText();

			}

			if (event == XMLStreamConstants.END_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.KIND_DEF)) {
				list.add(kind);
				break;
			}
			}
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ROLE_DEF)) {

				break;
			}
		}
		xmlStreamReader.close();

		in.close();
		return list;
	}

	/**
	 * @param uri
	 * @param message
	 * @param validate
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 */
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
			
            while(xmlStreamReader.hasNext()){
            	event = xmlStreamReader.next();
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {
				role.name = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				role.code = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ID)) {
				role.id = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
				role.namespace = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.DOMAIN)) {
				role.domain = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.RANGE)) {
				role.range = xmlStreamReader.getElementText();

			}

			if (event == XMLStreamConstants.END_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ROLE_DEF)) {
				list.add(role);
				break;
			}
            }
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.PROPERTY_DEF)) {

				break;
			}
		}
		xmlStreamReader.close();

		in.close();
		return list;
	}
	

	
	/**
	 * @param uri
	 * @param message
	 * @param validate
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 */
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
			while(xmlStreamReader.hasNext()){
				
				event = xmlStreamReader.next();
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {
				property.name = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				property.code = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ID)) {
				property.id = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
				property.namespace = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.RANGE)) {
				property.range = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals(NdfrtConstants.PICKLIST)){
				List<String> list = getPickList(xmlStreamReader);

				property.pickList = list;
			}
			
			if (event == XMLStreamConstants.END_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.PROPERTY_DEF)) {
				propList.add(property);
				break;
			}
			}
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ASSOCIATION_DEF)) {

				break;
			}
		}
		xmlStreamReader.close();

		in.close();
		return propList;
	}
	
	/**
	 * @param uri
	 * @param message
	 * @param validate
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 */
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
			while(xmlStreamReader.hasNext()){
				event = xmlStreamReader.next();
		
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {
				association.name = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				association.code = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ID)) {
				association.id = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
				association.namespace = xmlStreamReader.getElementText();

			}
			
			if (event == XMLStreamConstants.END_ELEMENT
					
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ASSOCIATION_DEF)) {
				associations.add(association);
				break;
			}
			}
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.QUALIFIER_DEF)) {

				break;
			}
		}
		xmlStreamReader.close();

		in.close();
		return associations;
	}
	
	
	/**
	 * @param uri
	 * @param message
	 * @param validate
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 */
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
			while(xmlStreamReader.hasNext()){
			event = xmlStreamReader.next();
			
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAME)) {
				qualifier.name = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CODE)) {
				qualifier.code = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.ID)) {
				qualifier.id = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.NAMESPACE)) {
				qualifier.namespace = xmlStreamReader.getElementText();

			}
			if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals(NdfrtConstants.PICKLIST)){
					
				qualifier.pickList = getPickList(xmlStreamReader);
		    }
			
			
			if (event == XMLStreamConstants.END_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.QUALIFIER_DEF)) {
				list.add(qualifier);
				break;
			}
			}
			if (event == XMLStreamConstants.START_ELEMENT
					&& xmlStreamReader.getLocalName().equals(
							NdfrtConstants.CONCEPT_DEF)) {

				break;
			}
			}
		}
		
		xmlStreamReader.close();

		in.close();
		return list;
		}
		
	/**
	 * @param xmlStreamReader
	 * @return
	 * @throws XMLStreamException
	 */
	private List<String> getPickList(XMLStreamReader xmlStreamReader) throws XMLStreamException {
		ArrayList<String> picklist = new ArrayList<String>();
		while(xmlStreamReader.hasNext()){
			int event = xmlStreamReader.next();

			if(event == XMLStreamConstants.START_ELEMENT){
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
		
		return picklist;
	
		
	}

//	public static void main(String[] args) {
//		NdfrtXMLProcessor processor = new NdfrtXMLProcessor();
//		try {
//
//			processor.getCodingScheme(new File(args[0]).toURI(), null, true,
//					null);
//			try {
//				List<KindDef> kindList = processor.getKindDefList(new File(args[0]).toURI(), null, true);
//				for(KindDef kd: kindList){
//				   System.out.println("Kind property code: " + kd.code);
//				   System.out.println("Kind property id: "+ kd.id);
//				   System.out.println("Kind property name: "+ kd.name);
//				   System.out.println("Kind property namespace: "+ kd.namespace);
//
//				   
//				}
//				List<RoleDef> roleList = processor.getRoleDefList(new File(args[0]).toURI(), null, true);
//				for(RoleDef rd: roleList){
//					   System.out.println("Role code: " + rd.code);
//					   System.out.println("Role id: "+ rd.id);
//					   System.out.println("Role name: "+ rd.name);
//					   System.out.println("Role namespace: "+ rd.namespace);
//					   System.out.println("Role range: " + rd.range);		
//					   System.out.println("Role domain: " + rd.domain);
//				}
//
//				List<PropertyDef> propList = processor.getPropertyDefList(new File(args[0]).toURI(), null, true);
//				for(PropertyDef pd: propList){
//					   System.out.println("Prop code: " + pd.code);
//					   System.out.println("Prop id: "+ pd.id);
//					   System.out.println("Prop name: "+ pd.name);
//					   System.out.println("Prop namespace: "+ pd.namespace);
//					   System.out.println("Prop range: " + pd.range);	
//					   List<String> pickList = pd.pickList;
//					   if(pickList != null){
//					   for(String s: pickList){
//						   System.out.println("\tname: "+ s);
//					   }
//					   }
//				}
//				List<AssociationDef> assocList = processor.getAssociationDefList(new File(args[0]).toURI(), null, true);
//				for(AssociationDef pd: assocList){
//					   System.out.println("Assoc code: " + pd.code);
//					   System.out.println("Assoc id: "+ pd.id);
//					   System.out.println("Assoc name: "+ pd.name);
//					   System.out.println("Assoc namespace: "+ pd.namespace);
//
//				}
//				List<QualifierDef> qualList = processor.getQualifierDefList(new File(args[0]).toURI(), null, true);
//				for(QualifierDef pd: qualList){
//					   System.out.println("Qual code: " + pd.code);
//					   System.out.println("Qual id: "+ pd.id);
//					   System.out.println("Qual name: "+ pd.name);
//					   System.out.println("Qual namespace: "+ pd.namespace);
//
//					   List<String> pickList = pd.pickList;
//					   if(pickList != null){
//					   for(String s: pickList){
//						   System.out.println("\tname: "+ s);
//					   }
//					   }
//				}
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (XMLStreamException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (FactoryConfigurationError e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		} catch (CodingSchemeAlreadyLoadedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}