package org.LexGrid.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class SchemeXml {
	private final String HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
			"<codingScheme xmlns=\"http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes\"" +
			"	codingSchemeName=\"colors\" codingSchemeURI=\"1.2.3\" formalName=\"colors coding " +
			"scheme\"	defaultLanguage=\"en\" representsVersion=\"1.0\">	" +
			"<ns1:entityDescription		xmlns:ns1=\"http://LexGrid.org/schema/2010/01/LexGrid/commonTypes\">" +
			"a simple example coding scheme using colors	</ns1:entityDescription>	" +
			"<source role=\"1.2.3\">Dans Head</source>	<copyright>This isn't worth copyrighting :)" +
			"</copyright>	<mappings />";
	
	private final String TAIL = "</codingScheme>";
	
	private final String ENTITIES_BEGIN = "<entities>";
	private final String ENTITIES_END = "</entities>";
	private final String RELATION_BEGIN = "<relations containerName=\"colorsRelation\">		" +
			"<ns26:associationPredicate			" +
			"xmlns:ns26=\"http://LexGrid.org/schema/2010/01/LexGrid/relations\"			" +
			"associationName=\"PAR\">";
	private final String RELATION_END = "</ns26:associationPredicate>	</relations>";
	
	private final int COUNT = 2000000;
	private final int PAGE = 1000;
	
	private File file;
	
	public SchemeXml() {
		
	}
	
	public void createXml(File f) throws IOException {
		file = f;
		save(HEAD+ ENTITIES_BEGIN); 
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
