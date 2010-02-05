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
package edu.mayo.informatics.resourcereader.obo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OBOTest {

    // private static final String REGEX = "(\\w*):(\\w*)(\\s*\"?\\w*\"?)(,?)";
    private static final String REGEX = "((\\w*):(\\w*))";
    private static final String INPUT = "dog:doggie dog doggie dogg, GO:ma,GO:blah \"abba abbcd\" ";

    public static void main(String[] args) {
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(INPUT); // get a matcher object
        int count = 0;
        while (m.find()) {
            count++;
            System.out.println("Match number " + count);
            System.out.println("start(): " + m.start());
            System.out.println("end(): " + m.end());
            System.out.println("group(1): " + m.group(1));
            System.out.println("group(2): " + m.group(2));
            System.out.println("group(3): " + m.group(3));
        }
    }
}