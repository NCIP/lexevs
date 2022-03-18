
package org.lexevs.dao.indexer.lucene.query;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.lexevs.dao.indexer.lucene.query.SerializableRegexCapabilities;

/**
 * The Class SerializableRegexCapabilitiesTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SerializableRegexCapabilitiesTest extends LexBIGServiceTestCase {

    /** The serializable regex capabilities. */
    private SerializableRegexCapabilities serializableRegexCapabilities;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "SerializableRegexCapabilities Test";
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp(){
        serializableRegexCapabilities = new SerializableRegexCapabilities();
    }
    
    /**
     * Test is serializable.
     * 
     * @throws Exception the exception
     */
    public void testIsSerializable() throws Exception{
        ObjectOutputStream out = new ObjectOutputStream(new OutputStream(){

            @Override
            public void write(int b) throws IOException {
               //no-op
            }      
        });
        out.writeObject(serializableRegexCapabilities);
        out.close();
    }
}