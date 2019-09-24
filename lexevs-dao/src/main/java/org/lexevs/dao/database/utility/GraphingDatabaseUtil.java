package org.lexevs.dao.database.utility;

public class GraphingDatabaseUtil {
	public static final String badChars = " !\"#$%&'()*+,-./:;<=>?@[\\]^`{|}~";

	public static String normalizeGraphandGraphDatabaseName(String graphName) {
		String result = graphName.trim();
		result = result.replaceAll("[^a-zA-Z0-9]", "_");
		if(result.startsWith("_")){
			result = result.substring(1);
		}
		return result;
		}


}
