package org.lexevs.graph.dao;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class OrientDbGraphDao {
	
	private static final String GET_ALL_EDGES_IN = "select expand(in(";
	private static final String GET_VERTEX_FOR_CODE = "select from Nodes where code = ";
	private static final String GET_VERTEX_FOR_DESCRIPTION = "select from Nodes where description = ";
	private static final String TRAVERSE_ASSOC = "traverse in(\"";
	
	
	private OGraphDatabase orientDb = null;

	public OrientDbGraphDao(String user, String password, String dbPath){
		this.orientDb = new OGraphDatabase(dbPath);
		orientDb.open(user, password);
	}
	
	@SuppressWarnings("unchecked") 
	public List<ODocument> getDocumentResultForSql(
			String sql) {
		return (List<ODocument>)orientDb.query(new OSQLSynchQuery<ODocument>(sql));
	}
	
	@SuppressWarnings("unchecked")
	public List<OrientVertex> getVertexResultForSql(String sql){
		return (List<OrientVertex>)orientDb.query(new OSQLSynchQuery<OrientVertex>(sql));
	}
	
	private String getAllEdgesInForAssociation(String associationName){
		return GET_ALL_EDGES_IN + "\"" + associationName + "\")) from ";
	}
	
	public String getAllEdgesInForCode(String code, String associationName){
		return getAllEdgesInForAssociation(associationName) + "("+ GET_VERTEX_FOR_CODE + "\"" + code + "\")";
	}
	
	public String getAllEdgesInForExactMatch(String description, String associationName){
		return getAllEdgesInForAssociation(associationName) + "("+ GET_VERTEX_FOR_DESCRIPTION + description + ")";
	}
	
	public String getAllNodesForFocusCodeAndAssoc(String code, String associationName){
		return createTraverseForAssociation(associationName) + "("+ GET_VERTEX_FOR_CODE + "\"" + code + "\")";
	}
	
	private String createTraverseForAssociation(String associationName) {
		return TRAVERSE_ASSOC + associationName + "\") from ";
	}

	public ConceptReference getConceptReferenceForVertex(ODocument vertex) {
		JsonObject o = (JsonObject) new JsonParser().parse(vertex.toJSON());
		ConceptReference ref =  new ConceptReference();
		ref.setCode(o.get("code").getAsString());
		ref.setCodeNamespace(o.get("namespace").getAsString());
		ref.setCodingSchemeName(o.get("uri").getAsString());
		ref.setConceptCode(o.get("code").getAsString());
		return ref;
	}

	public void close(){
		orientDb.close();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		OrientDbGraphDao db = new OrientDbGraphDao("admin", "admin", "local:/Users/m029206/software/orientdb-graphed-1.4.0-SNAPSHOT/databases/testbpData");
		OrientDbGraphDao db = new OrientDbGraphDao("admin", "admin", "local:/Users/m029206/git/releases/orientdb-graphed-1.4.0-SNAPSHOT/databases/testClgraph");
		try{
			List<OrientVertex> docs = db.getVertexResultForSql(db.getAllEdgesInForCode("C14225", "Gene_Found_In_Organism"));
			System.out.println("Number of edges: " + docs.size());
		for(OrientVertex o: docs){
		System.out.println("\nNew Edge: " + o.getClass());
		ConceptReference ref = db.getConceptReferenceForVertex((ODocument) o.getRecord());
		System.out.println("ConceptReference code: " + ref.getCode());
		System.out.println("ConceptReference concept code: " + ref.getConceptCode());
		System.out.println("ConceptReference namespace: " + ref.getCodeNamespace());
		System.out.println("ConceptReference name: " + ref.getCodingSchemeName());
		System.out.println(o.toString());
		}
		}
//		int counter = 0;
//		try{
//			List<ODocument> docs = db.getDocumentResultForSql(db.getAllNodesForFocusCodeAndAssoc("C3262", "subClassOf"));
//			for(ODocument o: docs){
//			ConceptReference ref = db.getConceptReferenceForVertex(o);
//			System.out.println("\n" + ref.getCode());
//			System.out.println(ref.getCodeNamespace());
//			System.out.println(ref.getCodingSchemeName());
//			System.out.println(ref.getConceptCode());
//			counter++;
//			}
//		}
		catch(Exception e){
		e.printStackTrace();
		}
		finally{
		db.close();
		}
//		System.out.println("count: " + counter);
	}




}
