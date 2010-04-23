package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedProperty;
import org.easymock.EasyMock;
import org.junit.Test;

public class LexBIGServiceConvenienceMethodsImplTest {

    @Test
    public void getSupportedPropertiesOfTypeTest() throws LBException{
        LexBIGServiceConvenienceMethodsImpl lbscm = new LexBIGServiceConvenienceMethodsImpl();
        
        LexBIGService lbs = EasyMock.createMock(LexBIGService.class);
        
        CodingScheme cs = new CodingScheme();
        cs.setMappings(new Mappings());
        
        SupportedProperty prop1 = new SupportedProperty();
        prop1.setLocalId("prop1");
        prop1.setPropertyType(PropertyTypes.PRESENTATION);
        
        SupportedProperty prop2 = new SupportedProperty();
        prop2.setLocalId("prop2");
        prop2.setPropertyType(PropertyTypes.DEFINITION);
        
        cs.getMappings().addSupportedProperty(prop1);
        cs.getMappings().addSupportedProperty(prop2);
        
        EasyMock.expect(lbs.resolveCodingScheme("test", null)).andReturn(cs);
        
        EasyMock.replay(lbs);
        
        lbscm.setLexBIGService(lbs);
        
        SupportedProperty[] props = lbscm.getSupportedPropertiesOfType("test", null, PropertyTypes.PRESENTATION);
        
        assertEquals(1, props.length);
        assertEquals("prop1", props[0].getLocalId());
    }
}
