
package org.LexGrid.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class SchemeXml {
	private final String HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"			<codingScheme" +
			"		    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
			"		    xsi:schemaLocation=\"http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes.xsd\"" +
			"			xmlns=\"http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes\"" +
			"			xmlns:lgBuiltin=\"http://LexGrid.org/schema/2010/01/LexGrid/builtins\"" +
			"		    xmlns:lgCommon=\"http://LexGrid.org/schema/2010/01/LexGrid/commonTypes\"" +
			"		    xmlns:lgCon=\"http://LexGrid.org/schema/2010/01/LexGrid/concepts\"" +
			"		    xmlns:lgRel=\"http://LexGrid.org/schema/2010/01/LexGrid/relations\"	" +
			"	        xmlns:lgCS=\"http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes\"" +
			"		    xmlns:lgLDAP=\"http://LexGrid.org/schema/2010/01/LexGrid/ldap\"" +
			"		    xmlns:lgNaming=\"http://LexGrid.org/schema/2010/01/LexGrid/naming\"" +
			"		    xmlns:lgService=\"http://LexGrid.org/schema/2010/01/LexGrid/service\"" +
			"		    xmlns:lgVD=\"http://LexGrid.org/schema/2010/01/LexGrid/valueSets\"" +
			"		    xmlns:lgVer=\"http://LexGrid.org/schema/2010/01/LexGrid/versions\"" +
			"		    xmlns:NCIHistory=\"http://LexGrid.org/schema/2010/01/LexGrid/NCIHistory\"" +
			"			codingSchemeName=\"colors\" codingSchemeURI=\"1.2.3\" formalName=\"colors coding scheme\"" +
			"			defaultLanguage=\"en\" representsVersion=\"1.0\">" +
			"			<ns1:entityDescription" +
			"				xmlns:ns1=\"http://LexGrid.org/schema/2010/01/LexGrid/commonTypes\">a simple example coding scheme using colors" +
			"			</ns1:entityDescription>" +
			"			<source role=\"1.2.3\">Dans Head</source>" +
			"			<copyright>This isn't worth copyrighting :)</copyright>";	
	
	private final String PROPERTIES = "<properties />";
	
	private final String MAPPINGS = "	<mappings>" +
			"		<lgNaming:supportedAssociation localId=\"PAR\" uri=\"http://some.uri.com\">PAR</lgNaming:supportedAssociation>" +
			"       <lgNaming:supportedCodingScheme localId=\"colors\" uri=\"http://some.uri.com\">colors</lgNaming:supportedCodingScheme>" +
			"       <lgNaming:supportedLanguage localId=\"en\" uri=\"http://some.uri.com\">en</lgNaming:supportedLanguage>" +
			"       <lgNaming:supportedNamespace localId=\"colors\" uri=\"http://some.uri.com\" equivalentCodingScheme=\"colors\">colors</lgNaming:supportedNamespace>" +
			"	</mappings>";	
	private final String TAIL = "</codingScheme>";
	
	private final String ENTITIES_BEGIN = "<entities>";
	private final String ENTITIES_END = "</entities>";
	private final String RELATION_BEGIN = "<relations containerName=\"colorsRelation\"> <lgRel:associationPredicate	associationName=\"PAR\">";
	private final String RELATION_END = "</lgRel:associationPredicate>	</relations>";
	
	private final int COUNT = 60000;
	private final int PAGE = 1000;
	
	private File file;
	
	public SchemeXml() {
		
	}
	
	public void createXml(File f) throws IOException {
		file = f;
		save(HEAD+ MAPPINGS + PROPERTIES + ENTITIES_BEGIN); 
		
		for (int i = 0; i < COUNT; i=i+PAGE) {
			save(getEntities(i, PAGE));
		}
		
		save(ENTITIES_END + RELATION_BEGIN);
		
		for (int i = 0; i < COUNT; i=i+PAGE) {
			save(getRelations(i, PAGE));
		}
		
		save(RELATION_END + TAIL);
	}
	
	private void save(String content) throws IOException {
		Writer w = new FileWriter(file, true);
		BufferedWriter out = new BufferedWriter(w);
		out.write(content);
		out.close();
	}
	
	private String getEntities(int init, int range ) {
		String entities = "";
		for (int i = init; i < init+range; i++) {
			EntityXml e = new EntityXml(Integer.toString(i));
			entities = entities + e.toString();
		}
		return entities;
	}
	
	private String getRelations(int init, int range) {
		String relations = "";
		for (int i = init; i < init+range-2; i++) {
			RelationXml r = new RelationXml(Integer.toString(i), Integer.toString(i+1), Integer.toString(i+2));
			relations = relations + r;
		}
		return relations;
	}

}