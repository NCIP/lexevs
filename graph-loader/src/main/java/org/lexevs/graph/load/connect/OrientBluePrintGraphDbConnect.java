package org.lexevs.graph.load.connect;

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
	private OGraphDatabase graphDb = null;
	Vertex source;
	Vertex target;
	Edge edge;
	
	public OrientBluePrintGraphDbConnect(String user, String password, String dbPath){
		graphDb = new OGraphDatabase("remote:" + dbPath);
		graphDb.open(user, password);
		//orientDB = new OrientGraph(dbPath, user, password);
		orientDB = new OrientGraph(graphDb);
	}

	public OGraphDatabase getUnderlyingGraphDb(){
		return graphDb;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OClass createEdgeTable(String table, List<String> fieldnames) {
		return null;
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
	
	private void sourceSet(GraphDbTriple triple){
		long start = System.currentTimeMillis();
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
		long end  = System.currentTimeMillis();
		System.out.println("Mills in sourceSet: " + (end-start));
	}
	
	private void targetSet(GraphDbTriple triple){
		long start = System.currentTimeMillis();
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
		long end  = System.currentTimeMillis();
		System.out.println("Mills in targetSet: " + (end-start));
	}
	

	public ODocument getVertexForCode(String code){
		long start = System.currentTimeMillis();
		String sql = "select from OGraphVertex" + 
	" where code = " + "\"" + code + "\"";
		@SuppressWarnings("unchecked")
		List<ODocument> docs= (List<ODocument>)graphDb
				.query(new OSQLSynchQuery<ODocument>(sql));
		if(docs.size() > 0){
			long end  = System.currentTimeMillis();
			System.out.println("Mills getting Vertex for code: " + (end-start));
			return docs.get(0);
		}
			else{
				long end  = System.currentTimeMillis();
				System.out.println("Mills gettting Vertex for code: " + (end-start));
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
		long start = System.currentTimeMillis();
			sourceSet(triple);
			targetSet(triple);
			edgeSet(triple);
			orientDB.getRawGraph().commit();
			long end  = System.currentTimeMillis();
			System.out.println("total Mills storing a single triple: " + (end-start));
	}
	
	private void edgeSet(GraphDbTriple triple) {
		long start = System.currentTimeMillis();
	 edge = orientDB.addEdge(null, source, target, triple.getAssociationName());
		long end  = System.currentTimeMillis();
		System.out.println("Mills adding edge code: " + (end-start));
	}

	public static void main(String[] args){
		OrientBluePrintGraphDbConnect db = new OrientBluePrintGraphDbConnect("admin", "admin", "localhost/testGraph");
		System.out.println(db.toString());
		db.close(db.getUnderlyingGraphDb());
		
		
	}

}
