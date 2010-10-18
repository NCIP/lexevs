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
package org.LexGrid.LexBIG.Impl.pagedgraph.root;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.locator.LexEvsServiceLocator;

public abstract class AbstractEndNode extends ConceptReference {

    private static final long serialVersionUID = 4346299905579305877L;

    public static String ROOT = "@";
    public static String TAIL = "@@";
    
    protected AbstractEndNode(String uri, String version, String code) {
        try {
            this.setCode(code);
            this.setCodeNamespace(
            LexEvsServiceLocator.getInstance().
                getSystemResourceService().
                getInternalCodingSchemeNameForUserCodingSchemeName(uri, version)
            );
        } catch (LBParameterException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static class Root extends AbstractEndNode {
        private static final long serialVersionUID = 9146396838103778981L;

        protected Root(String uri, String version) {
            super(uri, version, ROOT);
        }  
    }
    
    public static class Tail extends AbstractEndNode {
        private static final long serialVersionUID = -7014931215754506298L;

        protected Tail(String uri, String version) {
            super(uri, version, TAIL);
        }  
    }
}