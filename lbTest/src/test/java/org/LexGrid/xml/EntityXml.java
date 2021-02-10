
package org.LexGrid.xml;

public class EntityXml {
	private String entity = "<lgCon:entity" +
			"			entityCode=\"tobereplacedcode\" entityCodeNamespace=\"colors\" isAnonymous=\"false\"" +
			"			isDefined=\"true\">" +
			"			<lgCommon:entityDescription>Holder of colors</lgCommon:entityDescription>" +
			"			<lgCon:entityType>concept</lgCon:entityType>" +
			"			<lgCon:presentation propertyName=\"textPresentation\"" +
			"				propertyType=\"presentation\" isPreferred=\"true\">	" +
			"			<lgCommon:value>Holder of colors</lgCommon:value>" +
			"			</lgCon:presentation>" +
			"		</lgCon:entity>";
	public EntityXml(String code) {
		entity = entity.replaceFirst("tobereplacedcode", code);
	}
	public String toString() {
		return entity;
	}

}