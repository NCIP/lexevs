/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.emfConversions.hl7;

import java.util.Hashtable;

/**
 * Hash for concepts
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * 
 */
public class HL7ConceptList {

    private Hashtable list;
    int x;

    HL7ConceptList() {
        super();
        list = new Hashtable();
        x = 0;
    }

    public void addConcept(HL7ConceptContainer concept) {
        list.put(new Integer(x), concept);
        x++;
    }

    public HL7ConceptContainer getConcept(int x) {
        return (HL7ConceptContainer) list.get(new Integer(x));
    }

    public int size() {
        return list.size();
    }

}