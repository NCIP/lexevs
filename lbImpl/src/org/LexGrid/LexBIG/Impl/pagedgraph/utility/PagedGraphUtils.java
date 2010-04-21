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
package org.LexGrid.LexBIG.Impl.pagedgraph.utility;

import org.LexGrid.LexBIG.DataModel.Core.CodedNodeReference;
import org.apache.commons.lang.ArrayUtils;

/**
 * The Class PagedGraphUtils.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PagedGraphUtils {

    /**
     * Are coded node references equals.
     * 
     * @param ref1 the ref1
     * @param ref2 the ref2
     * 
     * @return true, if successful
     */
    public static boolean areCodedNodeReferencesEquals(CodedNodeReference ref1, CodedNodeReference ref2) {
        return 
        ref1.getCode().equals(ref2.getCode())
        &&
        ref1.getCodeNamespace().equals(ref2.getCodeNamespace())
        &&
        ArrayUtils.isEquals(ref1.getEntityType(), ref2.getEntityType());
    }
}
