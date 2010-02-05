package org.lexevs.dao.database.utility;

import java.util.ArrayList;
import java.util.List;

public class DaoUtility {

	public static <T> List<T> createList(T item, Class<T> itemClazz){
		List<T> returnList = new ArrayList<T>();
		returnList.add(item);
		return returnList;
	}
}
