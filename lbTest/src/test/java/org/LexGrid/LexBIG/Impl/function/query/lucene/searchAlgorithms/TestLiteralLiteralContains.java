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
package org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms;

import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.experimental.categories.Category;

/**
 * The Class TestLiteralLiteralContains.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class TestLiteralLiteralContains extends TestLiteral {

    /** The algorithm. */
    private static String algorithm = "literalContains";

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestLiteral#getAlgorithm()
     */
    @Override
    protected String getAlgorithm() {
        return algorithm;
    }
}