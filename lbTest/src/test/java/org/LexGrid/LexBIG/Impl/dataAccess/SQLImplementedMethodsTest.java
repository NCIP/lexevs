
package org.LexGrid.LexBIG.Impl.dataAccess;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.easymock.classextension.EasyMock;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.exceptions.MissingResourceException;

/**
 * The Class SQLImplementedMethodsTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SQLImplementedMethodsTest extends LexBIGServiceTestCase {

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "SQLImplementedMethods Tests";
    }
    
    /**
     * Test resolve concept reference.
     * 
     * @throws LBParameterException the LB parameter exception
     * @throws MissingResourceException the missing resource exception
     */
    public void testResolveConceptReference() throws LBParameterException, MissingResourceException {
        ResolvedConceptReference ref = SQLImplementedMethods.resolveConceptReference(
                createConceptReferenceCodingScheme("A0001", AUTO_SCHEME), AUTO_VERSION);
        assertTrue(ref.getCode().equals("A0001"));
        assertNotNull(ref.getEntity());
    }

    /**
     * Test resolve concept reference on ambigious code.
     */
    public void testResolveConceptReferenceOnAmbigiousCode() {
        try {
            SQLImplementedMethods.resolveConceptReference(createConceptReferenceCodingScheme("DifferentNamespaceConcept", AUTO_SCHEME), AUTO_VERSION);
        } catch (LBParameterException e) {
            return;
        } catch (MissingResourceException e) {
            fail(e.getLocalizedMessage());
        } 
        fail();
    }
    
    /**
     * Test resolve concept reference on non ambigious code.
     * 
     * @throws Exception the exception
     */
    public void testResolveConceptReferenceOnNonAmbigiousCode() throws Exception {
        ResolvedConceptReference ref = SQLImplementedMethods.resolveConceptReference(createConceptReferenceCodingSchemeAndNamespace("DifferentNamespaceConcept", AUTO_SCHEME, AUTO_SCHEME), AUTO_VERSION);
        assertTrue(ref.getCode().equals("DifferentNamespaceConcept"));
        assertTrue(ref.getCodeNamespace().equals(AUTO_SCHEME));
        assertNotNull(ref.getEntity());
    }
    
    public void testIsEntityAssnsToEQualsIndexPresent(){
        SQLInterface si = EasyMock.createMock(SQLInterface.class);
        SQLTableConstants constants = EasyMock.createMock(SQLTableConstants.class);
        EasyMock.expect(constants.getVersion()).andReturn("1.8");
        EasyMock.expect(si.getSQLTableConstants()).andReturn(constants);
        EasyMock.replay(si, constants);
        assertTrue(SQLImplementedMethods.isEntityAssnsToEQualsIndexPresent(si));
    }
    
    public void testIsEntryStateIdInAssociationTable(){
        SQLInterface si = EasyMock.createMock(SQLInterface.class);
        SQLTableConstants constants = EasyMock.createMock(SQLTableConstants.class);
        EasyMock.expect(constants.getVersion()).andReturn("1.8");
        EasyMock.expect(si.getSQLTableConstants()).andReturn(constants);
        EasyMock.replay(si, constants);
        assertTrue(SQLImplementedMethods.isEntryStateIdInAssociationTable(si));
    }
    
    public void testIsEntityAssnsToEQualsIndexPresentNotHighEnoughVersion(){
        SQLInterface si = EasyMock.createMock(SQLInterface.class);
        SQLTableConstants constants = EasyMock.createMock(SQLTableConstants.class);
        EasyMock.expect(constants.getVersion()).andReturn("1.7");
        EasyMock.expect(si.getSQLTableConstants()).andReturn(constants);
        EasyMock.replay(si, constants);
        assertFalse(SQLImplementedMethods.isEntityAssnsToEQualsIndexPresent(si));
    }
    
    public void testIsEntryStateIdInAssociationTableNotHighEnoughVersion(){
        SQLInterface si = EasyMock.createMock(SQLInterface.class);
        SQLTableConstants constants = EasyMock.createMock(SQLTableConstants.class);
        EasyMock.expect(constants.getVersion()).andReturn("1.7");
        EasyMock.expect(si.getSQLTableConstants()).andReturn(constants);
        EasyMock.replay(si, constants);
        assertFalse(SQLImplementedMethods.isEntryStateIdInAssociationTable(si));
    }
    
    public void testIsEntityAssnsToEQualsIndexPresentHigherVersion(){
        SQLInterface si = EasyMock.createMock(SQLInterface.class);
        SQLTableConstants constants = EasyMock.createMock(SQLTableConstants.class);
        EasyMock.expect(constants.getVersion()).andReturn("1.9");
        EasyMock.expect(si.getSQLTableConstants()).andReturn(constants);
        EasyMock.replay(si, constants);
        assertTrue(SQLImplementedMethods.isEntityAssnsToEQualsIndexPresent(si));
    }
    
    public void testIsEntryStateIdInAssociationTableHigherVersion(){
        SQLInterface si = EasyMock.createMock(SQLInterface.class);
        SQLTableConstants constants = EasyMock.createMock(SQLTableConstants.class);
        EasyMock.expect(constants.getVersion()).andReturn("1.9");
        EasyMock.expect(si.getSQLTableConstants()).andReturn(constants);
        EasyMock.replay(si, constants);
        assertTrue(SQLImplementedMethods.isEntryStateIdInAssociationTable(si));
    }
    
    public void testParseFloatFromTableVersion(){
        SQLInterface si = EasyMock.createMock(SQLInterface.class);
        SQLTableConstants constants = EasyMock.createMock(SQLTableConstants.class);
        EasyMock.expect(constants.getVersion()).andReturn("1.5");
        EasyMock.expect(si.getSQLTableConstants()).andReturn(constants);
        EasyMock.replay(si, constants);
        float version = SQLImplementedMethods.parseFloatFromTableVersion(si);
        assertTrue(version == 1.5f);
    }

    /**
     * Creates the concept reference coding scheme.
     * 
     * @param code the code
     * @param codingScheme the coding scheme
     * 
     * @return the concept reference
     */
    private ConceptReference createConceptReferenceCodingScheme(String code, String codingScheme){
        ConceptReference ref = new ConceptReference();
        ref.setCode(code);
        ref.setCodingSchemeName(codingScheme);
        return ref;
    }
    
    /**
     * Creates the concept reference coding scheme and namespace.
     * 
     * @param code the code
     * @param codingScheme the coding scheme
     * @param namespace the namespace
     * 
     * @return the concept reference
     */
    private ConceptReference createConceptReferenceCodingSchemeAndNamespace(String code, String codingScheme, String namespace){
        ConceptReference ref = new ConceptReference();
        ref.setCode(code);
        ref.setCodingSchemeName(codingScheme);
        ref.setCodeNamespace(namespace);
        return ref;
    }
    
    private void setTableVersion(SQLInterface si, String version) throws Exception {
        Field field = si.getSQLTableConstants().getClass().getDeclaredField("version_");
        field.setAccessible(true);
        field.set(si.getSQLTableConstants(), version);
    } 
}