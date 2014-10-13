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
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * The Class ResolveToListTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class FilterTest extends BaseCodedNodeGraphTest {

    /**
     * Test resolve to list associated concept count.
     * 
     * @throws Exception the exception
     */
    public void testFilter() throws Exception {

    	try {
    	ExtensionRegistryImpl.instance().registerFilterExtension(new AllButGMFilter().getExtensionDescription());
    	}catch(Exception e){
    		//May already be registered
    	}
    	
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("005", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    Constructors.createLocalNameList("allButGM"),
                    -1).getResolvedConceptReference();
        
        AssociatedConcept[] acons = 
        	rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();
        
        assertEquals(2,acons.length);
        
        assertTrue(contains(acons, "Ford"));
        assertTrue(contains(acons, "A"));
        assertFalse(contains(acons, "GM"));
    }
    
    public void testFilterFilterOutFocus() throws Exception {

    	try {
			ExtensionRegistryImpl.instance().registerFilterExtension(new AllButGMFilter().getExtensionDescription());
		} catch (Exception e) {
			// may be already registered.
		}
    	
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("GM", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    Constructors.createLocalNameList("allButGM"),
                    -1).getResolvedConceptReference();
        
        assertEquals(0,rcr.length);
    }
    
    public void testFilterNoFocus() throws Exception {

    	try {
			ExtensionRegistryImpl.instance().registerFilterExtension(new AllButFordFilter().getExtensionDescription());
		} catch (Exception e) {
			// may be already registered.
		}
		
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("uses"), null);
    	
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(
            		null, 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    null, 
                    Constructors.createLocalNameList("allButFord"),
                    -1).getResolvedConceptReference();
        
        assertEquals(1,rcr.length);
        assertEquals("A0001",rcr[0].getCode());
    }
    
    private boolean contains(AssociatedConcept[] assocCons, String code) {
    	for(AssociatedConcept con : assocCons) {
    		if(con.getCode().equals(code)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public static class AllButGMFilter extends AbstractExtendable implements Filter {

		@Override
		protected ExtensionDescription buildExtensionDescription() {
			ExtensionDescription ed = new ExtensionDescription();
			ed.setExtensionBaseClass(AllButGMFilter.class.getName());
			ed.setExtensionClass("org.LexGrid.LexBIG.Impl.function.codednodegraph.FilterTest$AllButGMFilter");
			ed.setDescription("allButGM");
			ed.setName("allButGM");
			
			return ed;

		}
		
	    @Override
	    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
	        registry.registerFilterExtension(description);
	    }

		@Override
		public boolean match(ResolvedConceptReference ref) {
			return ! ref.getCode().equalsIgnoreCase("GM");
		}
    }
    
    public static class AllButFordFilter extends AbstractExtendable implements Filter {

		@Override
		protected ExtensionDescription buildExtensionDescription() {
			ExtensionDescription ed = new ExtensionDescription();
			ed.setExtensionBaseClass(AllButFordFilter.class.getName());
			ed.setExtensionClass("org.LexGrid.LexBIG.Impl.function.codednodegraph.FilterTest$AllButFordFilter");
			ed.setDescription("allButFord");
			ed.setName("allButFord");
			
			return ed;

		}
		
	    @Override
	    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
	        registry.registerFilterExtension(description);
	    }

		@Override
		public boolean match(ResolvedConceptReference ref) {
			return ! ref.getCode().equalsIgnoreCase("Ford");
		}
    }
}