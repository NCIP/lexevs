package org.lexevs.dao.database.utility;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Text;

public class DaoUtility {

	public static <T> List<T> createList(T item, Class<T> itemClazz){
		List<T> returnList = new ArrayList<T>();
		returnList.add(item);
		return returnList;
	}
	
	public static Text createText(String content){
		return createText(content, null);
	}
	
	public static Text createText(String content, String format){
		Text text = new Text();
		text.setContent(content);
		text.setDataType(format);
		return text;
	}
}
