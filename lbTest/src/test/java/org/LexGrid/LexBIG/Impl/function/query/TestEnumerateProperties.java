
package org.LexGrid.LexBIG.Impl.function.query;

// LexBIG Test ID: T1_FNC_29	TestEnumerateProperties

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

public class TestEnumerateProperties extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_29";

    @Override
    protected String getTestID() {
        return testID;
    }

    /**
     * Enumerate properties for the code.
     * 
     * @param code
     * @param ServiceHolder
     *            .instance().getLexBIGService()
     * @throws LBException
     */
    protected void enumerateProps(String code, String[] properties) throws LBException {
        // Perform the query ...
        ConceptReferenceList crefs = ConvenienceMethods.createConceptReferenceList(new String[] { code }, THES_SCHEME);

        ResolvedConceptReferenceList matches = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(
                THES_SCHEME, null).restrictToCodes(crefs).resolveToList(null, null, null, 1);

        // Analyze the result ...
        assertTrue(matches.getResolvedConceptReferenceCount() > 0);
        ResolvedConceptReference ref = (ResolvedConceptReference) matches.enumerateResolvedConceptReference()
                .nextElement();
        Entity node = ref.getEntity();

        // Print properties (definitions, comments, presentations
        // are not printed but would be similarly handled)
        Property[] props = node.getProperty();
        assertTrue(props.length > 0);
        assertTrue(contains(props, properties));

    }

    public boolean contains(Property[] props, String[] property) {
        if (props.length != property.length) {
            return false;
        }
        for (int i = 0; i < property.length; i++) {
            boolean found = false;
            for (int j = 0; j < props.length; j++) {
                if (props[j].getPropertyName().equals(property[i])) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }

    public void testT1_FNC_29a() throws LBException {
        enumerateProps("External_Lip", new String[] { "label", "Semantic_Type", "code" });
    }

    public void testT1_FNC_29b() throws LBException {
        enumerateProps("Bone_of_the_Extremity", new String[] { "label", "Semantic_Type", "code" });
    }

}