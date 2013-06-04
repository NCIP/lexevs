package org.lexevs.graph.load.connect;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientIndex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class OrientBluePrintGraphDbConnect implements GraphDataBaseConnect {
	
	private OrientGraph orientDB = null;
	OrientVertex source;
	OrientVertex target;
	OrientEdge edge;
	
	public OrientBluePrintGraphDbConnect(String user, String password, String dbPath){
		orientDB = new OrientGraph(dbPath, user, password);
	}

	@Override
	public OGraphDatabase openForWrite(String dbName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OGraphDatabase openForRead(String dbName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OGraphDatabase getGraphDbFromPool(String dbPath, String username,
			String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close(OGraphDatabase database) {
	orientDB.shutdown();

	}

	@Override
	public void delete(String dbPath) {
		File dir = new File(dbPath);
		if (dir.exists()) {
			System.out.println("exists,,,,, deleting");
			try {
				FileUtils.deleteDirectory(dir);
			} catch (Exception e) {
				System.out.println("Unable to delete old version of database: "	+ dbPath);
				throw new RuntimeException(e);
			}
		}
		else{
			System.out.println("No database to delete at: " + dbPath);
		}
	}

	@Override
	public OClass createVertexTable(String table, List<String> fieldnames) {
		OClass vertex = orientDB.createVertexType(table);
		OSchema schema = orientDB.getRawGraph().getMetadata().getSchema();

		createTablePropertiesAndIndexes(schema, table, fieldnames);
//		orientDB.commit();
		return  vertex;
	}

	private void createTablePropertiesAndIndexes(OSchema schema, String table, List<String> fieldnames){
 		OClass oClass = schema.getClass(table);
		for (int j = 0; j < fieldnames.size(); j++) {
			oClass.createProperty(fieldnames.get(j), OType.STRING);
		}
		oClass.createIndex("conceptCode",OClass.INDEX_TYPE.UNIQUE, "code");
		schema.save();	
//      createIndex("conceptCode", Vertex.class, new Parameter("code", "UNIQUE"), new Parameter("class", "Nodes"));
//		orientDB.commit();
		
	}

	@Override
	public OClass createEdgeTable(String table, List<String> fieldnames) {
		return  orientDB.createEdgeType(table);
	}

	@Override
	public Object storeVertex(String table, String entityCode,
			String entityNamespace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeGraphTriple(GraphDbTriple triple, String vertexTableName,
			String edgeTableName) {
		sourceSet(triple, vertexTableName);
		targetSet(triple, vertexTableName);
		edgeSet(triple);
//		orientDB.commit();
	}
	
	private void sourceSet(GraphDbTriple triple, String vertexTableName){
	    ODocument temp = getVertexForCode(triple.getSourceEntityCode());
		if(temp == null){
		source = orientDB.addVertex(vertexTableName, "clustertest");
		source.setProperty("code", triple.getSourceEntityCode());
		source.setProperty("namespace", triple.getSourceEntityNamespace());
		source.setProperty("uri", triple.getSourceSchemeUri());
		source.setProperty("version", triple.getSourceSchemeVersion());
		source.setProperty("description", triple.getSourceDescription());
		source.save();
		}
		else{
			source = orientDB.getVertex(temp.getIdentity())	;
		}
	}
	
	private void targetSet(GraphDbTriple triple, String vertexTableName){
		ODocument temp = getVertexForCode(triple.getTargetEntityCode());
		if(temp == null){
			target = orientDB.addVertex(vertexTableName, "clustertest");
			target.setProperty("code", triple.getTargetEntityCode());
			target.setProperty("namespace", triple.getTargetEntityNamespace());
			target.setProperty("uri", triple.getTargetSchemeUri());
			target.setProperty("version", triple.getTargetSchemeVersion());
			target.setProperty("description", triple.getTargetDescription());
			target.save();
			}
		else{
			target = orientDB.getVertex(temp.getIdentity());
		}
	}
	

	public ODocument getVertexForCode(String code){
		String sql = "select from Nodes" + 
				" where code = " + "\"" + code + "\"";
		@SuppressWarnings("unchecked")
		List<ODocument> docs= (List<ODocument>)orientDB.getRawGraph().query(new OSQLSynchQuery<ODocument>(sql));
		if(docs.size() > 0){
			return docs.get(0);
		}
			else{
				return null;
			}
	}
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public void createIndexFromRawGraph(String codingScheme){
//		String sql = "CREATE INDEX " + codingScheme+ ".code UNIQUE";
//		orientDB.getRawGraph().query(new OSQLSynchQuery(sql));
//	}
	
	@Override
	public ODocument getVertexForCode(String code, String vertexTableName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeGraphTriple(GraphDbTriple triple, String vertexTableName) {
		sourceSet(triple, vertexTableName);
		targetSet(triple, vertexTableName);
		edgeSet(triple);
	//	orientDB.getRawGraph().commit();
	}
	
	private void edgeSet(GraphDbTriple triple) {
		 edge = orientDB.addEdge("class:" + triple.getAssociationName(), source, target, triple.getAssociationName());
		 edge.save();
		}
		
		public List<String> getFieldNamesForVertex(){
			return  Arrays.asList("code", "namespace", "uri",
					"version", "predicateId", "description",
					"predicateName");
		}
		
		public void close(){
			orientDB.shutdown();

	}
		public void commit(){
			orientDB.commit();
		}
		

	public static void main(String[] args){
		OrientBluePrintGraphDbConnect db = null;
		try {
		 db = new OrientBluePrintGraphDbConnect(
				"admin",
				"admin",
				"local:/Users/m029206/git/releases/orientdb-graphed-1.4.0-SNAPSHOT/databases/testClgraph");
		
			db.createEdgeTable("edges", null);
			db.createVertexTable("nodes", db.getFieldNamesForVertex());
			System.out.println(db.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
			db.delete("/Users/m029206/git/releases/orientdb-graphed-1.4.0-SNAPSHOT/databases/testClgraph");
		}

		
		
	}

}
