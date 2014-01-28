/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Holder for concepts loaded from Text files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7190 $ checked in on $Date: 2008-02-15
 *          17:25:58 +0000 (Fri, 15 Feb 2008) $
 */
public class Concept {
    public String code;
    public String name;
    public String description;
    public Set<String> alternateDescriptions = new HashSet<String>();
    public int depth;

    public Concept(String code, String name, String description, int depth) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.depth = depth;
    }

    public Concept(String line, String token, CodingScheme codingScheme) {
        depth = 0;
        while (true) {
            if ((line.length() > depth + 1) && line.substring(depth, depth + 1).equals(token)) {
                depth++;
            } else {
                break;
            }
        }

        StringTokenizer tokenizer = new StringTokenizer(line, token);
        if (tokenizer.hasMoreElements()) {
            name = tokenizer.nextToken();
        }
        if (tokenizer.hasMoreElements()) {
            description = tokenizer.nextToken();
        }
        if (tokenizer.hasMoreElements()) {
            // if they provided 3, then the format is B.
            code = name;
            name = description;
            description = tokenizer.nextToken();
            codingScheme.isTypeB = true;
        }
        while( tokenizer.hasMoreElements()) {
            alternateDescriptions.add(tokenizer.nextToken());
        }
    }

    public String toString() {
        return "Code: " + code + "\nName: " + name + "\nDescription: " + description + "\nDepth: " + depth;
    }
    
    public boolean equals(Object o) {
        if (o instanceof Concept == true)
            return code.equals(((Concept)o).code);
        return false;
    }
}