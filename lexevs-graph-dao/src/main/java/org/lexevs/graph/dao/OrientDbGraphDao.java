package org.lexevs.graph.dao;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class OrientDbGraphDao {
	
	private static final String GET_ALL_EDGES_IN = "select flatten(in(";
	//private static final String GET_ALL_EDGES_IN = "select flatten(in(\"Gene_Found_In_Organism\")) from ";
	private static final String GET_VERTEX_FOR_CODE = "select from Nodes where code = ";
	private static final String GET_VERTEX_FOR_DESCRIPTION = "select from Nodes where description = ";
	
	private OGraphDatabase orientDb = null;

	public OrientDbGraphDao(String user, String password, String dbPath){
		this.orientDb = new OGraphDatabase(dbPath);
		orientDb.open(user, password);
	}
	
	@SuppressWarnings("unchecked")
	public List<OrientEdge> getResultForSql(String sql){
		return (List<OrientEdge>)orientDb.query(new OSQLSynchQuery<OrientEdge>(sql));
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
	
	public ConceptReference getConceptReferenceForJSON(ODocument vertex) {
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
		OrientDbGraphDao db = new OrientDbGraphDao("admin", "admin", "local:/Users/m029206/software/orientdb-graphed-1.4.0-SNAPSHOT/databases/testbpData");
		try{
			List<OrientEdge> docs = db.getResultForSql(db.getAllEdgesInForCode("C14225", "Gene_Found_In_Organism"));
			System.out.println("Number of edges: " + docs.size());
		for(OrientEdge o: docs){
		System.out.println("\nNew Edge: " + o.getLabel());
		ConceptReference ref = db.getConceptReferenceForJSON((ODocument) o.getOutVertex());
		System.out.println("ConceptReference code: " + ref.getCode());
		System.out.println("ConceptReference concept code: " + ref.getConceptCode());
		System.out.println("ConceptReference namespace: " + ref.getCodeNamespace());
		System.out.println("ConceptReference name: " + ref.getCodingSchemeName());
		System.out.println(o.toString());
		}
		}
		catch(Exception e){
		e.printStackTrace();
		}
		finally{
		db.close();
		}
	}


}
