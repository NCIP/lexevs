
package org.lexgrid.loader.data;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.relations.AssociationQualification;
import org.apache.commons.lang.StringUtils;
import org.exolab.castor.xml.Unmarshaller;
import org.lexgrid.loader.processor.support.PropertyQualifierResolver;
import org.lexgrid.loader.processor.support.QualifierResolver;
import org.lexgrid.loader.processor.support.SourceResolver;

/**
 * The Class DataUtils.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DataUtils {

	public static <T extends Property> T deepCloneProperty(T property) throws Exception {
		StringWriter writer = new StringWriter();
		property.marshal(writer);
		writer.flush();
		
		String stringProp = writer.toString();
		
		StringReader reader = new StringReader(stringProp);
		
		return  (T) Unmarshaller.unmarshal(property.getClass(), reader);
	}

	
	/**
	 * Makes a deep copy of an Object -- assumes the Object to be copied has a Constructor
	 * that accepts a String
	 * 
	 * Example: String stringCopy = new String(String originalString);.
	 * 
	 * @param value the value
	 * 
	 * @return the T
	 * 
	 * @throws Exception the exception
	 */
	public static <T> T deepCopy(T value) throws Exception {
		if(value == null){
			return null;
		}
		Constructor constructor = value.getClass().getConstructor(String.class);
		return (T)constructor.newInstance(value.toString());	
	}
	
	public static String adjustNonNullValue(String value){
		if(StringUtils.isEmpty(value)){
			return " ";
		} else {
			return value;
		}
	}
	
	public static <T> PropertyQualifier createPropertyQualifier(PropertyQualifierResolver<T> resolver, T item) {
		PropertyQualifier qual = new PropertyQualifier();
		qual.setPropertyQualifierName(resolver.getQualifierName(item));
		qual.setPropertyQualifierType(resolver.getPropertyQualifierType(item));
		qual.setValue(resolver.getQualifierValue(item));
		
		return qual;
	}
	
	public static <T> Source createSource(SourceResolver<T> resolver, T item) {
		Source source = new Source();
		source.setContent(resolver.getSource(item));
		source.setRole(resolver.getRole(item));
		source.setSubRef(resolver.getRole(item));
		
		return source;
	}
	
	public static <T> AssociationQualification createAssociationQualifier(QualifierResolver<T> resolver, T item) {
		AssociationQualification qual = new AssociationQualification();
		qual.setAssociationQualifier(resolver.getQualifierName(item));
		qual.setQualifierText(resolver.getQualifierValue(item));
		
		return qual;
	}
}