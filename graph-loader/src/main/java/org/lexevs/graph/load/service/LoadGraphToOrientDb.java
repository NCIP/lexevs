package org.lexevs.graph.load.service;

import java.util.List;

import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.graph.load.connect.OrientDbGraphDbConnect;
import org.lexevs.graph.load.connect.TriplePlus;
import org.lexevs.graph.load.service.LexEVSTripleService.TripleIterator;

public class LoadGraphToOrientDb {
	OrientDbGraphDbConnect database ;
	LexEVSTripleService service ;
	String databasePath = "/Users/m029206/software/orientdb-1.3.0/databases/testGraph";
	String vertexTableName = "Nodes"; 
	String edgeTableName = "Edges";
	
	public void createDatabase(){
		database = new OrientDbGraphDbConnect("admin", "admin", "/Users/m029206/software/orientdb-1.3.0/databases/testGraph");
		database.createEdgeTable(edgeTableName, database.getFieldNamesForEdge());
		database.createVertexTable(vertexTableName, database.getFieldNamesForVertex());
		database.initVerticesAndEdge();
	}

	public void run(){
		createDatabase();
		String codingSchemeUri = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl";
		String version = "12.01f";
		service = new LexEVSTripleService();
		int countOut = 0;
		List<String> predicateIds = service.getAssociationPredicateIds(codingSchemeUri, version);
		for(String associationPredicateId : predicateIds){
		TripleIterator tripleIterator = service.getTripleIteratorforPredicate(codingSchemeUri, version, associationPredicateId);
		
		while(tripleIterator.hasNext()){
        TriplePlus triple =	superSizeTriple(tripleIterator.next(), codingSchemeUri, version);
		database.storeTriple(triple, vertexTableName, edgeTableName);
		countOut++;
		if(countOut % 10000 == 0 )
		System.out.println("Count: " + countOut);
		}
		}
		System.out.println("Final count: " + countOut);
	}
	
	private TriplePlus superSizeTriple(Triple t, String uri, String version) {
		TriplePlus tp = new TriplePlus();
		tp.setAssociaitonName(uri);
		tp.setAssociationPredicateId(t.getAssociationPredicateId());
		tp.setSourceEntityCode(t.getSourceEntityCode());
		tp.setSourceEntityNamespace(t.getSourceEntityNamespace());
		tp.setSourceSchemeUri(uri);
		tp.setSourceSchemeVersion(version);
		tp.setTargetEntityCode(t.getTargetEntityCode());
		tp.setTargetEntityNamespace(t.getTargetEntityNamespace());
		tp.setTargetSchemeUri(uri);
		tp.setTargetSchemeVersion(version);
		return tp;
	}
	
	public static void main(String[] args) {
		LoadGraphToOrientDb load = new LoadGraphToOrientDb();
		try{
		load.run();}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		finally{
		load.database.close();
//		load.database.delete(load.databasePath);
		}

	}

}
