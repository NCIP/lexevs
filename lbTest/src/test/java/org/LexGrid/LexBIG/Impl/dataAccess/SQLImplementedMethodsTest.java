/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.dataAccess;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.internalExceptions.MissingResourceException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.easymock.classextension.EasyMock;

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
     * Test are codes related coding scheme.
     * 
     * @throws Exception the exception
     */
    public void testAreCodesRelatedCodingScheme() throws Exception {
        assertTrue(SQLImplementedMethods.areCodesRelated(
                Constructors.createNameAndValue("hasSubtype", null), 
                this.createConceptReferenceCodingScheme("GM", AUTO_SCHEME), 
                this.createConceptReferenceCodingScheme("Chevy", AUTO_SCHEME), 
                true, 
                new ArrayList<Operation>(), 
                AUTO_SCHEME, 
                "1.0", 
                "relations"));
    }
    
    /**
     * Test are codes related coding scheme and namespace.
     * 
     * @throws Exception the exception
     */
    public void testAreCodesRelatedCodingSchemeAndNamespace() throws Exception {
        assertTrue(SQLImplementedMethods.areCodesRelated(
                Constructors.createNameAndValue("hasSubtype", null), 
                this.createConceptReferenceCodingSchemeAndNamespace("GM", AUTO_SCHEME, AUTO_SCHEME), 
                this.createConceptReferenceCodingSchemeAndNamespace("Chevy", AUTO_SCHEME, AUTO_SCHEME), 
                true, 
                new ArrayList<Operation>(), 
                AUTO_SCHEME, 
                "1.0", 
                "relations"));
    }
    
    /**
     * Test get coding scheme copyright.
     * 
     * @throws Exception the exception
     */
    public void testGetCodingSchemeCopyright() throws Exception {
        assertTrue(SQLImplementedMethods.getCodingSchemeCopyright(AUTO_SCHEME, "1.0").equals("Copyright by Mayo Clinic."));
    }
    
    /**
     * Test build coded entry.
     * 
     * @throws Exception the exception
     */
    public void testBuildCodedEntry() throws Exception {
        Entity entity = SQLImplementedMethods.buildCodedEntry(AUTO_SCHEME, "1.0", "Jaguar", AUTO_SCHEME, null, null);
        assertTrue(entity.getEntityCode().equals("Jaguar"));
        assertTrue(entity.getEntityCodeNamespace().equals(AUTO_SCHEME));
        assertTrue(entity.getEntityDescription().getContent().equals("Jaguar"));
        assertTrue(entity.getIsActive().equals(true));

        assertTrue(entity.getPresentation().length == 1);
        Presentation p1 = entity.getPresentation()[0];
        assertTrue(p1.getPropertyName().equals("textualPresentation"));
        assertTrue("Property Type is: " + p1.getPropertyType(), p1.getPropertyType().equalsIgnoreCase(CodedNodeSet.PropertyType.PRESENTATION.toString()));
        assertTrue(p1.getValue().getContent().equals("Jaguar"));
        assertTrue(p1.getIsPreferred().equals(false));
        assertTrue(p1.getPropertyId().equals("p1"));      
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
