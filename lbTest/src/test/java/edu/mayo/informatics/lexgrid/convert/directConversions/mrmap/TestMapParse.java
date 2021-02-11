
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import junit.framework.TestCase;

public class TestMapParse extends TestCase {
 public void testParseTargetCode(){
	 String parseableTarget = "<Hyper> AND <tension>";
	 
	 String entityCode = getFirstParsableEntityCode(parseableTarget);
	assertEquals("Hyper", entityCode);
	
	String nonparseableTarget = "Hyper";
	 String entityCodeb = getFirstParsableEntityCode(nonparseableTarget);
		assertEquals("Hyper", entityCodeb);
		
	String parseableCode = "90.47 AND 41";
	String entityCodec = getFirstParsableEntityCode(parseableCode);
	assertEquals("90.47", entityCodec);
	String parseableCoded = "90.47";
	String entityCoded = getFirstParsableEntityCode(parseableCoded);
	assertEquals("90.47", entityCoded);
	
 }

private String getFirstParsableEntityCode(String parseTarget) {
	
    String s = "<";
    String and = " AND";
    if(parseTarget.contains(s)){
    int index = parseTarget.indexOf(">");
    parseTarget = parseTarget.substring(0, index);
    System.out.println(parseTarget);
    parseTarget = parseTarget.replace("<", "");
    System.out.println(parseTarget);}
    if (parseTarget.contains(and)){
      int index =  parseTarget.indexOf(and);
      parseTarget = parseTarget.substring(0, index);
      System.out.println(parseTarget);}
    
    return parseTarget;
}
}