package org.lexevs.graph.load.service;

import java.util.List;

import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;
import org.lexevs.graph.load.connect.OrientDbGraphDbConnect;
import org.lexevs.graph.load.connect.TriplePlus;
import org.lexevs.graph.load.service.LexEVSTripleService.GraphTripleIterator;

public class LoadGraphToOrientDb {
	OrientDbGraphDbConnect database ;
	LexEVSTripleService service ;
	String databasePath;

	String vertexTableName = "Nodes"; 
	String edgeTableName = "Edges";
	
	public String getDatabasePath() {
		return databasePath;
	}
	
	public LoadGraphToOrientDb(String codingSchemeUri, String version, String dbPath){
		service = new LexEVSTripleService(codingSchemeUri, version);
		this.databasePath = dbPath;
	}
	
	public void createDatabase(){
		database = new OrientDbGraphDbConnect("admin", "admin", "/Users/m029206/software/orientdb-1.3.0/databases/thesGraph");
		database.createEdgeTable(edgeTableName, database.getFieldNamesForEdge());
		database.createVertexTable(vertexTableName, database.getFieldNamesForVertex());
		database.initVerticesAndEdge();
	}
	
	public void runGraphLoad(String codingSchemeUri, String version){
		createDatabase();
		
		int countOut = 0;
		List<String> predicateIds = service.getAssociationPredicateIds(codingSchemeUri, version);
		for(String associationPredicateId : predicateIds){
		GraphTripleIterator tripleIterator = service.getGraphTripleIteratorforPredicate(codingSchemeUri, version, associationPredicateId);
		
		while(tripleIterator.hasNext()){
        GraphDbTriple triple =	processGraphTriple(tripleIterator.next(), codingSchemeUri, version);
		database.storeGraphTriple(triple, vertexTableName, edgeTableName);
		countOut++;
		if(countOut % 10000 == 0 )
		System.out.println("Count: " + countOut);
		}
		}
		System.out.println("Final count: " + countOut);
	}
	
	private GraphDbTriple processGraphTriple(GraphDbTriple tp, String uri, String version) {
		tp.setSourceSchemeUri(uri);
		tp.setSourceSchemeVersion(version);
		tp.setTargetSchemeUri(uri);
		tp.setTargetSchemeVersion(version);
		tp.setAnonymousStatus(getAnonStatusForPredicateId(uri, version, tp.getAssociationPredicateId()));
		return tp;
	}
	private boolean getAnonStatusForPredicateId(String uri, String version, String associationPredicateId) {
		String status = service.getPredicateToAnonStatusMap().get(associationPredicateId);
		if( status == null || status.endsWith("0")){
			return false;
		}
		else{ return true;}
	}
	
	public static void main(String[] args) {

		//String codingSchemeUri = "http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl";
		String codingSchemeUri = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl";
		//String version = "version 1.2";
		String version = "12.01f";
		LoadGraphToOrientDb load = new LoadGraphToOrientDb(codingSchemeUri, version, "/Users/m029206/software/orientdb-1.3.0/databases/thesGraph");
		try{
		load.runGraphLoad(codingSchemeUri, version);}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		finally{
		load.database.close();
		load.database.delete(load.databasePath);
		}

	}

}
