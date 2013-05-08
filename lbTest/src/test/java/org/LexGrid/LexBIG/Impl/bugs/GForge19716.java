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
package org.LexGrid.LexBIG.Impl.bugs;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge19716 extends LexBIGServiceTestCase {
    final static String testID = "GForge19716";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testGetAllProperties() throws Exception {
        Entity entity = new Entity();
        
        Presentation pres = new Presentation();
        Definition def = new Definition();
        Comment com = new Comment();
        Property prop = new Property();
        
        entity.addPresentation(pres);
        entity.addDefinition(def);
        entity.addComment(com);
        entity.addProperty(prop);
        
        Property[] allProps = entity.getAllProperties();
        assertTrue(allProps.length == 4);
        
        boolean foundPres = false;
        boolean foundDef = false;
        boolean foundCom = false;
        boolean foundProp = false;
        
        for(Property individualProp : allProps){
            if(individualProp instanceof Presentation){
                foundPres = true;
            } else if (individualProp instanceof Definition) {
                 foundDef = true;
            } else if (individualProp instanceof Comment) {
                 foundCom = true;
            } else if (individualProp instanceof Property){
                foundProp = true;
            }
        }
    
        assertTrue(foundPres);
        assertTrue(foundDef);
        assertTrue(foundCom);
        assertTrue(foundProp);      
    }
    
    public void testAddAnyPropertySingle(){
        Entity entity = new Entity();
        
        Presentation pres = new Presentation();
        Definition def = new Definition();
        Comment com = new Comment();
        Property prop = new Property();
        
        entity.addAnyProperty(pres);
        assertTrue(entity.getPresentation().length == 1);
        
        entity.addAnyProperty(def);
        assertTrue(entity.getDefinition().length == 1);
        
        entity.addAnyProperty(com);
        assertTrue(entity.getComment().length == 1);
        
        entity.addAnyProperty(prop);
        assertTrue(entity.getProperty().length == 1);   
    }
    
    public void testAddAnyPropertyList(){
        Entity entity = new Entity();
        
        Presentation pres = new Presentation();
        Definition def = new Definition();
        Comment com = new Comment();
        Property prop = new Property();
        
        List<Property> allProps = new ArrayList<Property>();
        allProps.add(pres);
        allProps.add(def);
        allProps.add(com);
        allProps.add(prop);
        
        entity.addAnyProperties(allProps);
        assertTrue(entity.getPresentation().length == 1);     
        assertTrue(entity.getDefinition().length == 1);
        assertTrue(entity.getComment().length == 1);
        assertTrue(entity.getProperty().length == 1);   
    }
}