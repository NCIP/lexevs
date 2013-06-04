package org.lexevs.graph.load.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;
import org.lexevs.graph.load.connect.OrientBluePrintGraphDbConnect;
import org.lexevs.graph.load.service.LexEVSTripleService.GraphIterator;

public class LoadGraphToBlueprintDb {
	OrientBluePrintGraphDbConnect  database ;
	LexEVSTripleService service ;
	String databasePath;
	List<String> associationIds;

	String vertexTableName = "Nodes"; 
	String edgeTableName = "Edges";
	
	public String getDatabasePath() {
		return databasePath;
	}
	
	public LoadGraphToBlueprintDb(String codingSchemeUri, String version, String dbPath){
		service = new LexEVSTripleService(codingSchemeUri, version);
		this.databasePath = dbPath;
	}
	
	public void createDatabase(){
		database = new OrientBluePrintGraphDbConnect ("admin", "admin", databasePath);
		database.createEdgeTable(edgeTableName, null);
		database.createVertexTable(vertexTableName, database.getFieldNamesForVertex());
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
		long nextTime = System.currentTimeMillis();
        GraphDbTriple triple =	processGraphTriple(tripleIterator.next(), codingSchemeUri, version);
        long nextEnd = System.currentTimeMillis();
        long next = nextEnd - nextTime;
//        if(next > 100)
//		System.out.println("Getting next: " + (next));
//		long storeTime = System.currentTimeMillis();
        database.storeGraphTriple(triple, vertexTableName, triple.getAssociationName());
        long storeEnd = System.currentTimeMillis();
//        long store = storeEnd - storeTime;
//        if(store > 100)
//        System.out.println("storing next: " + (store));
		countOut++;

		if(countOut % 10000 == 0 ){
//		database.commit();
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
//		String codingSchemeUri = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl";
//		String version = "12.01f";
		String codingSchemeUri = "http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl";
		String version = "version 1.2";
		LoadGraphToBlueprintDb load = new LoadGraphToBlueprintDb(codingSchemeUri, version, "local:/Users/m029206/git/releases/orientdb-graphed-1.4.0-SNAPSHOT/databases/testClgraph");
		try{
		load.runGraphLoad(codingSchemeUri, version);
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		finally{
		load.database.close();
		load.database.delete("/Users/m029206/git/releases/orientdb-graphed-1.4.0-SNAPSHOT/databases/testClgraph");
	}
	}

}
