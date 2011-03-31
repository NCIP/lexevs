/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.cts2.castor.printer;

import java.util.HashSet;
import java.util.Set;

import org.cts2.castor.printer.modifiers.AssociationModifier;
import org.cts2.castor.printer.modifiers.ChangeableResourceInterfaceAdder;
import org.cts2.castor.printer.modifiers.DirectorySubclassesModifier;
import org.cts2.castor.printer.modifiers.DirectoryURIMethodAddingModifier;
import org.cts2.castor.printer.modifiers.ResourceDescriptionModifier;
import org.cts2.castor.printer.modifiers.ValueSetResolutionRuleModifier;
import org.exolab.castor.builder.printing.WriterJClassPrinter;
import org.exolab.javasource.JClass;

/**
 * The Class JClassModifyingWriterJClassPrinter.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class JClassModifyingWriterJClassPrinter extends WriterJClassPrinter {

	/** The modifier map. */
	private Set<JClassModifier> modifierMap = new HashSet<JClassModifier>();
	{
		modifierMap.add(new DirectoryURIMethodAddingModifier());
		modifierMap.add(new DirectorySubclassesModifier());
		modifierMap.add(new ChangeableResourceInterfaceAdder());
		modifierMap.add(new ResourceDescriptionModifier());
		modifierMap.add(new ValueSetResolutionRuleModifier());
		modifierMap.add(new AssociationModifier());
	}
	
	/* (non-Javadoc)
	 * @see org.exolab.castor.builder.printing.WriterJClassPrinter#printClass(org.exolab.javasource.JClass, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void printClass(JClass jClass, String outputDir,
            String lineSeparator, String header) {
		
		for(JClassModifier modifier : this.modifierMap){
			if(modifier.isMatch(jClass)){
				modifier.prePrintModifyJClass(jClass, outputDir,
			            lineSeparator, header);
			}
		}
		
		super.printClass(jClass, outputDir, lineSeparator, header);
		
		for(JClassModifier modifier : this.modifierMap){
			if(modifier.isMatch(jClass)){
				modifier.postPrintModifyJClass(jClass, outputDir,
			            lineSeparator, header);
			}
		}
	}
}
