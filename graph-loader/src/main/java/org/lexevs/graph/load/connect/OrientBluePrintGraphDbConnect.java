package org.lexevs.graph.load.connect;

import java.util.Arrays;
import java.util.List;

import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class OrientBluePrintGraphDbConnect implements GraphDataBaseConnect {
	
	private OrientGraph orientDB = null;
	Vertex source;
	Vertex target;
	Edge edge;
	
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
	public void delete(String dbName) {
		// TODO Auto-generated method stub

	}

	@Override
	public OClass createVertexTable(String table, List<String> fieldnames) {
		return orientDB.createVertexType(table);
	}

	@Override
	public OClass createEdgeTable(String table, List<String> fieldnames) {
		return orientDB.createEdgeType(table);
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
		sourceSet(triple);
		targetSet(triple);
		edgeSet(triple);
		orientDB.commit();
	}
	
	private void sourceSet(GraphDbTriple triple){
	    ODocument temp = getVertexForCode(triple.getSourceEntityCode());
		if(temp == null){
		source = orientDB.addVertex(null);
		source.setProperty("code", triple.getSourceEntityCode());
		source.setProperty("namespace", triple.getSourceEntityNamespace());
		source.setProperty("uri", triple.getSourceSchemeUri());
		source.setProperty("version", triple.getSourceSchemeVersion());
		source.setProperty("description", triple.getSourceDescription());
		}
		else{
			source = orientDB.getVertex(temp.getIdentity())	;
		}
	}
	
	private void targetSet(GraphDbTriple triple){
		ODocument temp = getVertexForCode(triple.getTargetEntityCode());
		if(temp == null){
			target = orientDB.addVertex(null);
			target.setProperty("code", triple.getTargetEntityCode());
			target.setProperty("namespace", triple.getTargetEntityNamespace());
			target.setProperty("uri", triple.getTargetSchemeUri());
			target.setProperty("version", triple.getTargetSchemeVersion());
			target.setProperty("description", triple.getTargetDescription());
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
	
	@Override
	public ODocument getVertexForCode(String code, String vertexTableName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeGraphTriple(GraphDbTriple triple, String vertexTableName) {
		sourceSet(triple);
		targetSet(triple);
		edgeSet(triple);
		orientDB.getRawGraph().commit();
	}
	
	private void edgeSet(GraphDbTriple triple) {
		 edge = orientDB.addEdge("class:" + triple.getAssociationName(), source, target, triple.getAssociationName());
		}
		
		public List<String> getFieldNamesForVertex(){
			return  Arrays.asList("code", "namespace", "uri",
					"version", "predicateId", "description",
					"predicateName");
		}
		
		public void close(){
			orientDB.shutdown();

	}

	public static void main(String[] args){
		OrientBluePrintGraphDbConnect db = null;
		try {
		 db = new OrientBluePrintGraphDbConnect(
				"admin",
				"admin",
				"local:/Users/m029206/software/orientdb-graphed-1.4.0-SNAPSHOT/databases/testbpData");
		
			db.createEdgeTable("edges", null);
			db.createVertexTable("nodes", db.getFieldNamesForVertex());
			System.out.println(db.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
			db.delete("/Users/m029206/software/orientdb-graphed-1.4.0-SNAPSHOT/databases/testbpData");
		}

		
		
	}

}
