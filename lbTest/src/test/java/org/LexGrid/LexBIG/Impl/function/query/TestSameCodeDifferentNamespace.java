
package org.LexGrid.LexBIG.Impl.function.query;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class TestSameCodeDifferentNamespace.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class TestSameCodeDifferentNamespace extends BaseCodedNodeSetTest{

    /**
     * Test no restrictions.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    @Test
    public void testNoRestrictions() throws LBInvocationException, LBParameterException{
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        assertTrue(contains(refs, "DifferentNamespaceConcept", AUTO_SCHEME, AUTO_SCHEME));
        assertTrue(contains(refs, "DifferentNamespaceConcept", "TestForSameCodeNamespace", AUTO_SCHEME));
    }
    
    /**
     * Test restrict to codes no namespace size.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    @Test
    public void testRestrictToCodesNoNamespaceSize() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept"));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        assertTrue("Length: " + refs.length, refs.length == 3);
    }
    
    /**
     * Test restrict to codes no namespace if namespace is correct.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    @Test
    public void testRestrictToCodesNoNamespaceIfNamespaceIsCorrect() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept"));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        assertTrue(contains(refs, "DifferentNamespaceConcept", AUTO_SCHEME, AUTO_SCHEME));
        assertTrue(contains(refs, "DifferentNamespaceConcept", "TestForSameCodeNamespace", AUTO_SCHEME));
    } 
    
    /**
     * Test restrict to codes no namespace if properties count are correct.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    @Test
    public void testRestrictToCodesNoNamespaceIfPropertiesCountAreCorrect() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept"));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        boolean found1 = false;
        boolean found2 = false;
        
        for(ResolvedConceptReference ref : refs){
            if(ref.getCodeNamespace().equals(AUTO_SCHEME)){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                found1 = true;
            }
            if(ref.getCodeNamespace().equals("TestForSameCodeNamespace")){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                found2 = true;
            }
        }
        
        assertTrue(found1);
        assertTrue(found2);
    } 
    
    /**
     * Test restrict to codes no namespace if properties types are correct.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    @Test
    public void testRestrictToCodesNoNamespaceIfPropertiesTypesAreCorrect() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept"));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        boolean found1 = false;
        boolean found2 = false;
        
        for(ResolvedConceptReference ref : refs){
            if(ref.getCodeNamespace().equals(AUTO_SCHEME)){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                Property prop = entity.getAllProperties()[0];
                assertTrue(prop instanceof Presentation);
                found1 = true;
            }
            if(ref.getCodeNamespace().equals("TestForSameCodeNamespace")){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                Property prop = entity.getAllProperties()[0];
                assertTrue(prop instanceof Presentation);
                found2 = true;
            }
        }
        
        assertTrue(found1);
        assertTrue(found2);
    } 
    
    /**
     * Test restrict to codes no namespace if properties values are correct.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    @Test
    public void testRestrictToCodesNoNamespaceIfPropertiesValuesAreCorrect() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept"));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        boolean found1 = false;
        boolean found2 = false;
        
        for(ResolvedConceptReference ref : refs){
            if(ref.getCodeNamespace().equals(AUTO_SCHEME)){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                Property prop = entity.getAllProperties()[0];
                assertTrue("Value: " + prop.getValue().getContent(), prop.getValue().getContent().equals("Concept for testing same code but different Namespace - 1"));
                found1 = true;
            }
            if(ref.getCodeNamespace().equals("TestForSameCodeNamespace")){
                Entity entity = ref.getEntity();
                assertTrue(entity.getAllProperties().length == 1);
                Property prop = entity.getAllProperties()[0];
                assertTrue("Value: " + prop.getValue().getContent(), prop.getValue().getContent().equals("Concept for testing same code but different Namespace - 2"));
                found2 = true;
            }
        }
        
        assertTrue(found1);
        assertTrue(found2);
    } 

    /**
     * Test restrict to codes with namespace1.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    @Test
    public void testRestrictToCodesWithNamespace1() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept",
                AUTO_SCHEME, AUTO_SCHEME));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        assertTrue("Length: " + refs.length, refs.length == 1);
        
        assertTrue(contains(refs, "DifferentNamespaceConcept", AUTO_SCHEME, AUTO_SCHEME));
    }
    
    /**
     * Test restrict to codes with namespace2.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    @Test
    public void testRestrictToCodesWithNamespace2() throws LBInvocationException, LBParameterException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("DifferentNamespaceConcept",
                "TestForSameCodeNamespace", AUTO_SCHEME));
        
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        
        assertTrue("Length: " + refs.length, refs.length == 1);
        
        assertTrue(contains(refs, "DifferentNamespaceConcept", "TestForSameCodeNamespace", AUTO_SCHEME));
    }
}