
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import org.LexGrid.LexBIG.Utility.ValidateConnection;
import org.lexevs.dao.database.graph.rest.client.LexEVSSpringRestClientImpl;

public class GraphDbValidateConnnection implements ValidateConnection {
	
	private LexEVSSpringRestClientImpl restClient;
	
	public GraphDbValidateConnnection(String uri){
		restClient = new LexEVSSpringRestClientImpl(uri);
	}

	@Override
	public boolean connect() {
		try{
		restClient.systemMetadata();
		}catch(Exception e){
			System.out.println("Not able to connect.  Skipping Test");
			return false;
		}
		return true;
	}
	
	public static void main(String ...strings){
		System.out.println(new GraphDbValidateConnnection("http://localhost:8080/graph-resolve/").connect());
		
	}

}