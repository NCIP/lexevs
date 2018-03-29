package org.lexevs.dao.index.indexer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.lucene.document.Document;
import org.junit.Before;
import org.junit.Test;

public class SourceAssertedIndexerTest {
	AssertedValueSetEntityIndexer indexer;
	@Before
	public void setUp() throws Exception {
		 indexer = new AssertedValueSetEntityIndexer();
	}

	@Test
	public void test() {
		String codingSchemeUri = "urn:oid://this.test.uri";
		String codingSchemeName = "Onotology";
		String codingSchemeVersion = "33.3";
		Entity entity = new Entity();
		entity.setEntityCode("BR549");
		entity.setEntityCodeNamespace("ontology");
		List<String> typeList = new ArrayList<String>();
		typeList.add("concept");
		entity.setEntityType(typeList);
		entity.setEntityDescription(Constructors.createEntityDescription("telephone number"));
		String entityUid = "234234r3";
		entity.setIsActive(true);
		entity.setIsAnonymous(false);
		
		Properties props = new Properties();
		Property prop1 = new Property();
		prop1.setPropertyName("textualPresentation");
		prop1.setPropertyType("presentation");
		prop1.setValue(Constructors.createText("I'm a presentation property"));
		props.addProperty(prop1);
		Property prop2 = new Property();
		prop2.setPropertyName("definition");
		prop2.setPropertyType(PropertyType.DEFINITION.name());
		prop2.setValue(Constructors.createText("I'm a definition property"));
		props.addProperty(prop2);
		Property prop3 = new Property();
		prop3.setPropertyName("comment");
		prop3.setPropertyType(PropertyType.COMMENT.name());
		prop3.setValue(Constructors.createText("I'm a comment property"));
		props.addProperty(prop3);
		Property prop4 = new Property();
		prop4.setPropertyName("instruction");
		prop4.setValue(Constructors.createText("I'm an instruction property"));
		props.addProperty(prop4);
		Property prop5 = new Property();
		prop5.setPropertyName("generic");
		prop5.setPropertyType(PropertyType.GENERIC.name());
		prop5.setValue(Constructors.createText("I'm a definition property"));
		props.addProperty(prop5);
		
		Presentation pres = new Presentation();
		pres.setIsPreferred(true);
		pres.setPropertyName("presentation");
		pres.setPropertyType(PropertyType.PRESENTATION.name());
		pres.setValue(Constructors.createText("I'm a presentation property"));

		Presentation[] vPresentationList = {pres};
		entity.setPresentation(vPresentationList);
		entity.setProperty(props.getProperty());
		
		Source source1 = new Source();
		source1.setRole("PrimarySource");
		source1.setContent("NCI");
		Source source2 = new Source();
		source2.setRole("SecondarySource");
		source2.setContent("CDISC");
		
		PropertyQualifier qual1 = new PropertyQualifier();
		qual1.setPropertyQualifierName("modification");
		qual1.setValue(Constructors.createText("mod1"));
		PropertyQualifier qual2 = new PropertyQualifier();
		qual2.setPropertyQualifierName("qualification");
		qual2.setValue(Constructors.createText("qual1"));
		
		Source[] sources = {source1, source2};
		PropertyQualifier[] quals = {qual1, qual2};
		pres.setSource(sources);
		pres.setPropertyQualifier(quals);
		String vsURI = AssertedValueSetServices.createUri(AssertedValueSetServices.BASE,"CDISH", entity.getEntityCode());
		String vsName = entity.getEntityDescription().getContent();
		Document parentDoc = indexer.createParentDocument(codingSchemeName, codingSchemeUri, codingSchemeVersion, vsURI, vsName, entity, entityUid);
		assertNotNull(parentDoc);
		assertTrue(parentDoc.getFields().stream().anyMatch(x -> x.name().equals("isParentDoc")));
		assertTrue(parentDoc.getFields().stream().filter(x->x.name().equals("isParentDoc")).anyMatch(x->x.stringValue().equals("true")));

		Document property = indexer.addProperty(codingSchemeName, codingSchemeUri, codingSchemeVersion, vsURI, entity, prop1);
		assertNotNull(property);
		assertTrue(property.getFields().stream().anyMatch(x -> x.name().equals(SQLTableConstants.TBLCOL_PROPERTYNAME)));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(SQLTableConstants.TBLCOL_PROPERTYTYPE)).
		anyMatch(x->x.stringValue().equals("presentation")));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(org.lexevs.dao.indexer.lucene.Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD)).
		anyMatch(x->x.stringValue().equals(codingSchemeName + "-" + entity.getEntityCode())));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(AssertedValueSetEntityIndexer.UNIQUE_ID + "Tokenized")).
		anyMatch(x->x.stringValue().equals(entity.getEntityCode())));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(AssertedValueSetEntityIndexer.UNIQUE_ID)).
		anyMatch(x->x.stringValue().equals(entity.getEntityCode())));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(AssertedValueSetEntityIndexer.UNIQUE_ID + "LC")).
		anyMatch(x->x.stringValue().equals( entity.getEntityCode().toLowerCase())));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(org.lexevs.dao.indexer.lucene.Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD)).
		anyMatch(x->x.stringValue().equals(codingSchemeName + "-" + entity.getEntityCode())));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(AssertedValueSetEntityIndexer.CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD)).
		anyMatch(x->x.stringValue().equals(AssertedValueSetEntityIndexer.createCodingSchemeUriVersionCodeNamespaceKey(codingSchemeUri, codingSchemeVersion, 
				entity.getEntityCode(), entity.getEntityCodeNamespace()))));
		assertTrue(property.getFields().stream().filter(x->x.name().equals("propertyType")).
		anyMatch(x->x.stringValue().equals("presentation")));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE)).
		anyMatch(x->x.stringValue().equals(entity.getEntityCodeNamespace())));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(AssertedValueSetEntityIndexer.PROPERTY_VALUE_FIELD)).
		anyMatch(x->x.stringValue().equals(prop1.getValue().getContent())));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(AssertedValueSetEntityIndexer.REVERSE_PROPERTY_VALUE_FIELD)).
		anyMatch(x->x.stringValue().equals(AssertedValueSetEntityIndexer.reverseTermsInPropertyValue(prop1.getValue().getContent()))));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(AssertedValueSetEntityIndexer.LITERAL_PROPERTY_VALUE_FIELD)).
		anyMatch(x->x.stringValue().equals(prop1.getValue().getContent())));
		assertTrue(property.getFields().stream().filter(x->x.name().equals(AssertedValueSetEntityIndexer.LITERAL_AND_REVERSE_PROPERTY_VALUE_FIELD)).
				anyMatch(x->x.stringValue().equals(AssertedValueSetEntityIndexer.reverseTermsInPropertyValue(prop1.getValue().getContent()))));
	}

}
