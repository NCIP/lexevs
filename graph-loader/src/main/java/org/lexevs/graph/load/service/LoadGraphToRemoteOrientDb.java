package org.lexevs.graph.load.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;
import org.lexevs.graph.load.connect.OrientBluePrintGraphDbConnect;
import org.lexevs.graph.load.connect.OrientDbGraphDbConnect;

public class LoadGraphToRemoteOrientDb {
	OrientDbGraphDbConnect database ;
	LexEVSTripleService service ;
	String databasePath;
	List<String> associationIds;

	String vertexTableName = "Nodes"; 
	String edgeTableName = "Edges";
	
	public String getDatabasePath() {
		return databasePath;
	}
	
	public LoadGraphToRemoteOrientDb(String codingSchemeUri, String version, String dbPath){
		service = new LexEVSTripleService(codingSchemeUri, version);
		this.databasePath = dbPath;
	}
	
	public void createDatabase(String codingSchemeUri, String version){
		database = new OrientDbGraphDbConnect("admin", "admin", databasePath);
		associationIds = service.getAssociationPredicateIds(codingSchemeUri, version);
		for(String id: associationIds){
			String name = service.getAssociationNameforPredicateId(codingSchemeUri, version, id).get(0);
			database.createEdgeTable(name, null);
		}
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
		createDatabase(codingSchemeUri, version);
		
		int countOut = 0;

		for(String associationPredicateId : associationIds){
		
//		GraphTripleIterator tripleIterator = service.getGraphTripleIteratorforPredicate(codingSchemeUri, version, associationPredicateId);
//		long start = System.currentTimeMillis();
//		while(tripleIterator.hasNext()){
//        GraphDbTriple triple =	processGraphTriple(tripleIterator.next(), codingSchemeUri, version);
//		database.storeGraphTriple(triple, vertexTableName);
//		countOut++;
//
//		if(countOut % 10000 == 0 ){
//		long current = System.currentTimeMillis();
//		System.out.println("Count: " + countOut);
////		database.commitRelationShips();
//		System.out.println("Time elapsed: " + durationAsString(current - start) );
//		}
//		}
		}
//		System.out.println("Final count: " + countOut);
	}
	
	public OrientBluePrintGraphDbConnect runBluePrintGraphLoad(String codingSchemeUri, String version){
		OrientBluePrintGraphDbConnect bpdatabase = new OrientBluePrintGraphDbConnect("admin", "admin", databasePath);
		associationIds = service.getAssociationPredicateIds(codingSchemeUri, version);
		
		int countOut = 0;

		for(String associationPredicateId : associationIds){
		
//		GraphTripleIterator tripleIterator = service.getGraphTripleIteratorforPredicate(codingSchemeUri, version, associationPredicateId);
//		long start = System.currentTimeMillis();
//		while(tripleIterator.hasNext()){
//        GraphDbTriple triple =	processGraphTriple(tripleIterator.next(), codingSchemeUri, version);
//		bpdatabase.storeGraphTriple(triple, vertexTableName);
//		countOut++;
//
//		if(countOut % 10000 == 0 ){
//		long current = System.currentTimeMillis();
//		System.out.println("Count: " + countOut);
//		System.out.println("Time elapsed: " + durationAsString(current - start) );
//		}
//		}
		}
//		System.out.println("Final count: " + countOut);
		
		return bpdatabase;
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
//		try {
//			new OServerAdmin("remote:testGraph").connect("root", "lexgrid").createDatabase("graph", "local");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		LoadGraphToOrientDb load = new LoadGraphToOrientDb(codingSchemeUri, version, "localhost/testGraph");
		try{
		long start = System.currentTimeMillis();

//		load.runGraphLoad(codingSchemeUri, version);
		load.runGraphLoad(codingSchemeUri, version);
		long finish = System.currentTimeMillis();
		System.out.println ("total load time: " + (finish - start ));
		//ODocument RID = load.database.getVertexForCode("VegetarianTopping", "Nodes");
		//System.out.println("RID: " + RID.field("rid"));
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		finally{
		load.database.close();
		load.database.delete(load.databasePath);
		}

	}

}
