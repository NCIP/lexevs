
package org.LexGrid.LexBIG.Impl.dataAccess;

import java.util.ArrayList;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.function.codednodegraph.BaseCodedNodeGraphTest;
import org.LexGrid.LexBIG.Impl.helpers.graph.GHolder;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.system.ResourceManager;

/**
 * The Class SQLImplementedMethodsGraphVersionTests.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SQLImplementedMethodsGraphVersionTests extends LexBIGServiceTestCase {

    /** The si. */
    protected SQLInterface si;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
       return "SQLImplementedMethodsGraph Table Version 1.8 Tests";
    } 
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        si = ResourceManager.instance().getSQLInterface(AUTO_SCHEME, AUTO_VERSION);
        assertTrue(si.getSQLTableConstants().getVersion().equals("1.8"));
    }
    
    /**
     * Test helper.
     * 
     * @throws Exception the exception
     */
    public void testHelper() throws Exception {
        GHolder gh = new GHolder(AUTO_SCHEME, AUTO_VERSION, Constructors.createConceptReference("A0001", AUTO_SCHEME), true, false);
        SQLInterface si = ResourceManager.instance().getSQLInterface(AUTO_SCHEME, AUTO_VERSION);

        ConceptReferenceList crl = 
            SQLImplementedMethods.helper(si, gh, new ArrayList<Operation>(), true, AUTO_SCHEME, AUTO_VERSION, -1, "relations");
        assertTrue(crl.getConceptReferenceCount() > 0);       
    }

    /**
     * Test resolve relationships count.
     * 
     * @throws Exception the exception
     */
    public void testResolveRelationshipsCount() throws Exception {
        GHolder holder = SQLImplementedMethods.resolveRelationships(Constructors.createConceptReference("A0001", AUTO_SCHEME), 
                true, 
                false, 
                -1, 
                -1, 
                new ArrayList<Operation>(), 
                AUTO_SCHEME, 
                AUTO_VERSION, 
                "relations", 
                false);
        
        ResolvedConceptReferenceList rcrl = holder.getResolvedConceptReferenceList(-1, -1, null, null, null, null, false);
        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
    }
    
    /**
     * Test resolve relationships associations count.
     * 
     * @throws Exception the exception
     */
    public void testResolveRelationshipsAssociationsCount() throws Exception {
        GHolder holder = SQLImplementedMethods.resolveRelationships(Constructors.createConceptReference("A0001", AUTO_SCHEME), 
                true, 
                false, 
                -1, 
                -1, 
                new ArrayList<Operation>(), 
                AUTO_SCHEME, 
                AUTO_VERSION, 
                "relations", 
                false);
        
        ResolvedConceptReferenceList rcrl = holder.getResolvedConceptReferenceList(-1, -1, null, null, null, null, false);
        assertTrue(rcrl.getResolvedConceptReference(0).getSourceOf().getAssociationCount() == 2);
    }
    
    /**
     * Test resolve relationships associations names.
     * 
     * @throws Exception the exception
     */
    public void testResolveRelationshipsAssociationsNames() throws Exception {
        GHolder holder = SQLImplementedMethods.resolveRelationships(Constructors.createConceptReference("A0001", AUTO_SCHEME), 
                true, 
                false, 
                -1, 
                -1, 
                new ArrayList<Operation>(), 
                AUTO_SCHEME, 
                AUTO_VERSION, 
                "relations", 
                false);
        
        ResolvedConceptReferenceList rcrl = holder.getResolvedConceptReferenceList(-1, -1, null, null, null, null, false);
        Association[] assocs = rcrl.getResolvedConceptReference(0).getSourceOf().getAssociation();

        assertTrue(BaseCodedNodeGraphTest.associationListContains(assocs, "uses"));
        assertTrue(BaseCodedNodeGraphTest.associationListContains(assocs, "hasSubtype"));
    }
    
    /**
     * Test resolve relationships association qualifiers size.
     * 
     * @throws Exception the exception
     */
    public void testResolveRelationshipsAssociationQualifiersSize() throws Exception {
        GHolder holder = SQLImplementedMethods.resolveRelationships(Constructors.createConceptReference("Ford", AUTO_SCHEME), 
                true, 
                false, 
                -1, 
                -1, 
                new ArrayList<Operation>(), 
                AUTO_SCHEME, 
                AUTO_VERSION, 
                "relations", 
                false);

        ResolvedConceptReferenceList rcrl = holder.getResolvedConceptReferenceList(-1, -1, null, null, null, null, false);
        Association[] assocs = rcrl.getResolvedConceptReference(0).getSourceOf().getAssociation();

        Association hasSubtype = null;
        for(Association assoc : assocs){
            if(assoc.getAssociationName().equals("hasSubtype")){
                hasSubtype = assoc;
                break;
            }
        }
        assertTrue(hasSubtype != null);

        AssociatedConcept[] concepts = hasSubtype.getAssociatedConcepts().getAssociatedConcept();

        assertTrue(concepts.length == 1);

        AssociatedConcept jaguar = concepts[0];

        assertTrue(jaguar.getAssociationQualifiers().getNameAndValueCount() == 2);
    }
    
    /**
     * Test resolve relationships association qualifiers content.
     * 
     * @throws Exception the exception
     */
    public void testResolveRelationshipsAssociationQualifiersContent() throws Exception {
        GHolder holder = SQLImplementedMethods.resolveRelationships(Constructors.createConceptReference("Ford", AUTO_SCHEME), 
                true, 
                false, 
                -1, 
                -1, 
                new ArrayList<Operation>(), 
                AUTO_SCHEME, 
                AUTO_VERSION, 
                "relations", 
                false);

        ResolvedConceptReferenceList rcrl = holder.getResolvedConceptReferenceList(-1, -1, null, null, null, null, false);
        Association[] assocs = rcrl.getResolvedConceptReference(0).getSourceOf().getAssociation();

        Association hasSubtype = null;
        for(Association assoc : assocs){
            if(assoc.getAssociationName().equals("hasSubtype")){
                hasSubtype = assoc;
                break;
            }
        }

        AssociatedConcept[] concepts = hasSubtype.getAssociatedConcepts().getAssociatedConcept();
        
        AssociatedConcept jaguar = concepts[0];

        NameAndValue[] quals = jaguar.getAssociationQualifiers().getNameAndValue();
        
        assertTrue(nameAndValueContains(quals, "since", "1998"));
        assertTrue(nameAndValueContains(quals, "sold", "2009"));    
    }
    
    /**
     * Name and value contains.
     * 
     * @param nameAndValue the name and value
     * @param name the name
     * @param value the value
     * 
     * @return true, if successful
     */
    public boolean nameAndValueContains(NameAndValue[] nameAndValue, String name, String value){
        for(NameAndValue nv : nameAndValue){
            if(nv.getName().equals(name) &&
                    nv.getContent().equals(value)){
                return true;
            }
        }
        return false;
    }
}