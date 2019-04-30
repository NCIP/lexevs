package org.LexGrid.LexBIG.Extensions.Generic;

import java.io.Serializable;



public class TerminologyServiceDesignation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6694550235166923110L;

	
	public TerminologyServiceDesignation(){
		//no arg constructor handy for spring applications
	}
	
	public TerminologyServiceDesignation(String designation){
		this.setDesignation(designation);
	}
	
	private String designation;


	public static final String REGULAR_CODING_SCHEME = "REGULAR_CODING_SCHEME";
	public static final String  RESOLVED_VALUESET_CODING_SCHEME = "RESOLVED_VALUESET_CODING_SCHEME";
	public static final String  MAPPING_CODING_SCHEME = "MAPPING_CODING_SCHEME";
	public static final String  UNIDENTIFIABLE   = "UNIDENTIFIABLE";
	public static final String  ASSERTED_VALUE_SET_SCHEME   = "ASSERTED_VALUE_SET_SCHEME ";


public String getDesignationForName(String name){
	return designation;
}

public String getDesignation() {
	return designation;
}

public void setDesignation(String designation) {
	if(designation.equals(REGULAR_CODING_SCHEME)
			|| designation.equals(RESOLVED_VALUESET_CODING_SCHEME)
			|| designation.equals(MAPPING_CODING_SCHEME) || 
			designation.equals(ASSERTED_VALUE_SET_SCHEME))
	{this.designation = designation;}
	else{ this.designation = UNIDENTIFIABLE;}
}

}
