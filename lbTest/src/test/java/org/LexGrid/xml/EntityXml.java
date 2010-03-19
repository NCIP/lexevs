package org.LexGrid.xml;

public class EntityXml {
	private String entity = " <ns2:entity xmlns:ns2=\"http://LexGrid.org/schema/2010/01" +
			"/LexGrid/concepts\"			entityCode=\"tobereplacedcode\" " +
			"entityCodeNamespace=\"colors\" isAnonymous=\"false\"			" +
			"isDefined=\"true\">			<ns3:entityDescription				" +
			"xmlns:ns3=\"http://LexGrid.org/schema/2010/01/LexGrid/commonTypes\">" +
			"Holder of colors</ns3:entityDescription>			" +
			"<ns2:entityType>concept</ns2:entityType>			" +
			"<ns2:presentation propertyName=\"textPresentation\"				" +
			"propertyType=\"presentation\" isPreferred=\"true\">				" +
			"<ns4:value xmlns:ns4=\"http://LexGrid.org/schema/2010/01/LexGrid/commonTypes\">" +
			"Holder of colors</ns4:value>			</ns2:presentation>		" +
			"</ns2:entity>";
	public EntityXml(String code) {
		entity = entity.replaceFirst("tobereplacedcode", code);
	}
	public String toString() {
		return entity;
	}

}
