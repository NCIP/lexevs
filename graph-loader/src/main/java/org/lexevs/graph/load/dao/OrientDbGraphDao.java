package org.lexevs.graph.load.dao;

import java.util.List;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class OrientDbGraphDao {
	
	private static final String GET_ALL_EDGES_IN = "select flatten(in(\"Gene_Found_In_Organism\")) from ";

	
	private OrientGraph orientDb = null;

	public OrientDbGraphDao(String user, String password, String dbPath){
		this.orientDb = new OrientGraph(dbPath, user, password);
	}
	
	@SuppressWarnings("unchecked")
	public List<ODocument> getResultForSql(String sql){
		return (List<ODocument>)orientDb.getRawGraph().query(new OSQLSynchQuery<ODocument>(sql));
	}
	
	public void close(){
		orientDb.shutdown();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OrientDbGraphDao db = new OrientDbGraphDao("admin", "admin", "local:/Users/m029206/software/orientdb-graphed-1.4.0-SNAPSHOT/databases/testbpData");
		try{
		String sql = GET_ALL_EDGES_IN + "#12:39425";
		List<ODocument> docs = db.getResultForSql(sql);
		for(ODocument o: docs){
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
