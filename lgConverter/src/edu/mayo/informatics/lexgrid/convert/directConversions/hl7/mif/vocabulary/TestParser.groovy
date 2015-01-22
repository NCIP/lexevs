package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import static org.junit.Assert.*;

import org.junit.Test;

class TestParser extends groovy.util.GroovyTestCase{

    @Test
    public void testGetEntities() {
       def vsp = new ValueSetParser()
       vsp.filename = "/Users/m029206/Desktop/rim0241c/rim0241d/DEFN=UV=VO=1189-20121121.coremif"
       assert 3 == vsp.getEntitiesForCodeSystemOid("2.16.840.1.113883.5.1").size()
    }
    
    @Test
    public void testGetConceptforOIDnCode(){
        def vsp = new ValueSetParser()
        vsp.filename = "/Users/m029206/Desktop/rim0241c/rim0241d/DEFN=UV=VO=1189-20121121.coremif"
        def concept = vsp.getEntityForOidandCode("2.16.840.1.113883.5.1", "F")
        assert "10174" == concept.code
        assert "Female" == concept.definition
        assert "Female" == concept.presentation
        assert "2.16.840.1.113883.5.1" == concept.csURN
        assert "AdministrativeGender" == concept.csName
        assert "F" == concept.MifCode
        }

}
