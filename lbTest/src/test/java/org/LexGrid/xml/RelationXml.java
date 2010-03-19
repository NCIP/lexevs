package org.LexGrid.xml;

public class RelationXml {
	private String relation = " <ns26:source sourceEntityCodeNamespace=\"colors\"" +
			"				sourceEntityCode=\"tobefilledsrccode\">				" +
			"<ns26:target targetEntityCode=\"tobefilledtgtcode1\"					" +
			"targetEntityCodeNamespace=\"colors\" />				" +
			"<ns26:target targetEntityCode=\"tobefilledtgtcode2\"					" +
			"targetEntityCodeNamespace=\"colors\" />			" +
			"</ns26:source>";
	
	public RelationXml(String sourceCode1, String targetCode1, String targetCode2){
		relation = relation.replaceFirst("tobefilledsrccode", sourceCode1);
		relation = relation.replaceFirst("tobefilledtgtcode1", targetCode1);
		relation = relation.replaceFirst("tobefilledtgtcode2", targetCode2);
		
	}
	
	public String toString(){
		return relation;
	}
	

}
