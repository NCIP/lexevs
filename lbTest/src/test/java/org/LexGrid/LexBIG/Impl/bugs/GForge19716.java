
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