package org.lexevs.dao.database.utility;

import org.apache.commons.lang.StringUtils;

public class GraphingDatabaseUtil {
	

	public static String normalizeGraphandGraphDatabaseName(String graphName) {
		String result = graphName.trim();
		if(result.startsWith("_")){
			result = result.substring(1);
		}
		return StringUtils.replace(result, " ", "_");
	}


}
