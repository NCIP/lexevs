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
package edu.mayo.informatics.indexer.api.generators;

import java.util.HashSet;

import junit.framework.TestCase;

/**
 * JUnit Test cases for indexer package.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version 1.0 - cvs $Revision: 1.1 $ checked in on $Date: 2005/08/24 15:00:43
 *          $
 */
public class QueryGeneratorTest extends TestCase {
    public void testEscapeUtility() throws Exception {
        HashSet temp = new HashSet();
        temp.add(new Character('-'));
        temp.add(new Character('&'));
        temp.add(new Character('#'));
        temp.add(new Character(':'));
        temp.add(new Character('('));
        temp.add(new Character(')'));

        // shouldn't touch the -, (, and ) because they aren't escaped.
        String in = "(+return -\"pink panther\")";
        String out = "(+return -\"pink panther\")";
        assertEquals(QueryGenerator.removeExtraWhiteSpaceCharacters(in, temp), out);

        // should remove this hyphen, it is a white space char, and it is
        // escaped.
        in = "heart\\-attack";
        out = "heart attack";
        assertEquals(QueryGenerator.removeExtraWhiteSpaceCharacters(in, temp), out);

        // shouldn't touch these.
        in = "+heart -attack";
        out = "+heart -attack";
        assertEquals(QueryGenerator.removeExtraWhiteSpaceCharacters(in, temp), out);

        // remove the #.
        in = "+heart -attack out#there";
        out = "+heart -attack out there";
        assertEquals(QueryGenerator.removeExtraWhiteSpaceCharacters(in, temp), out);
    }

}