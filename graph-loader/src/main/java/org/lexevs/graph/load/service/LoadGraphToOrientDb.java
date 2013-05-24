package org.lexevs.graph.load.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;
import org.lexevs.graph.load.connect.OrientDbGraphDbConnect;
import org.lexevs.graph.load.service.LexEVSTripleService.GraphIterator;

public class LoadGraphToOrientDb {
	OrientDbGraphDbConnect database ;
	LexEVSTripleService service ;
	String databasePath;
	List<String> associationIds;

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
		database = new OrientDbGraphDbConnect("admin", "admin", databasePath);
		database.createEdgeTable(edgeTableName, database.getFieldNamesForEdge());
		database.createVertexTable(vertexTableName, database.getFieldNamesForVertex());
		database.initVerticesAndEdge();
	}
	
	  public static String durationAsString(long duration) {
		    String res = "";
		    long days  = TimeUnit.MILLISECONDS.toDays(duration);
		    long hours = TimeUnit.MILLISECONDS.toHours(duration)
		                   - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
		    long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
		                     - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
		    long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
		                   - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
		    if (days == 0) {
		      res = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		    }
		    else {
		      res = String.format("%dd%02d:%02d:%02d", days, hours, minutes, seconds);
		    }
		    return res;
		  }
	  
	public void runGraphLoad(String codingSchemeUri, String version){
		createDatabase();
		
		int countOut = 0;
		associationIds = service.getAssociationPredicateIds(codingSchemeUri, version);
		for(String id : associationIds){
			String assnName = service.getPredicateName(codingSchemeUri, version, id);
			database.createEdgeTable(assnName, null);
		}
		GraphIterator tripleIterator = 	service.getGraphIterator(codingSchemeUri, version);
		long start = System.currentTimeMillis();
		while(tripleIterator.hasNext()){
        GraphDbTriple triple =	processGraphTriple(tripleIterator.next(), codingSchemeUri, version);
        database.storeGraphTriple(triple, vertexTableName, triple.getAssociationName());
		countOut++;

		if(countOut % 10000 == 0 ){
		long current = System.currentTimeMillis();
		System.out.println("Count: " + countOut);
		System.out.println("Time elapsed: " + durationAsString(current - start) );
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

//		String codingSchemeUri = "http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl";
		String codingSchemeUri = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl";
		
//		String version = "version 1.2";
		String version = "12.01f";
//		LoadGraphToOrientDb load = new LoadGraphToOrientDb(codingSchemeUri, version, "/Users/m029206/software/orientdb-1.4.0-SNAPSHOT/databases/perfTest");
//		LoadGraphToOrientDb load = new LoadGraphToOrientDb(codingSchemeUri, version, "/Users/m029206/software/orientdb-graphed-1.4.0-SNAPSHOT/databases/perfTest");
		LoadGraphToOrientDb load = new LoadGraphToOrientDb(codingSchemeUri, version, "/Users/m029206/software/orientdb-graphed-1.3.0/databases/testCLgraph");
		try{
		load.runGraphLoad(codingSchemeUri, version);
		//ODocument RID = load.database.getVertexForCode("VegetarianTopping", "Nodes");
		//System.out.println("RID: " + RID.field("rid"));
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		finally{
		load.database.close();
//		load.database.delete(load.databasePath);
		}

	}

}
