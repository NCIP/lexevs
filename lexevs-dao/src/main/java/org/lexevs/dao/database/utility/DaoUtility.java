package org.lexevs.dao.database.utility;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.URIMap;
import org.compass.core.config.CompassEnvironment.Mapping;

public class DaoUtility {

	public static <T> List<T> createList(Class<T> itemClazz, T... items){
		List<T> returnList = new ArrayList<T>();
		for(T item : items) {
			returnList.add(item);
		}
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
	
	public static AbsoluteCodingSchemeVersionReference createAbsoluteCodingSchemeVersionReference(String urn,
			String version) {
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(urn);
		acsvr.setCodingSchemeVersion(version);
		return acsvr;
	}
	
	public static void insertIntoMappings(Mappings mappings, URIMap uriMap) {
		String addPrefix = "add";
		
		Class<? extends URIMap> uriMapClass = uriMap.getClass();
		String name = uriMapClass.getSimpleName();
		
		try {
			Method method = mappings.getClass().getMethod(addPrefix + name, uriMapClass);
			method.invoke(mappings, uriMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public static void main(String[] args) {
		for(Method method : Mappings.class.getMethods()) {
			if(method.getName().startsWith("addSupported")) {
				if(method.getParameterTypes().length == 1) {
					/*
					System.out.println("<subMap value=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("Supported", "") + "\" resultMap=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s") + "Result\" />");
					*/
			
					System.out.println("<resultMap id=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s") + "Result\" class=\"" + 
							method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s") + "\" extends=\"uriMapResult\">"
							+ "\n" + "</resultMap>"		
					);
					
					/*
					System.out.println("<typeAlias alias=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s")
					+ 	"\" type=\"" +	method.getParameterTypes()[0].getName() +"\"/>"
					);
					*/
				}
			}
		}
	}
}