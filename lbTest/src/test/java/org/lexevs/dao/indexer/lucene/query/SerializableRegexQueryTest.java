
package org.lexevs.dao.indexer.lucene.query;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.apache.lucene.index.Term;
import org.lexevs.dao.indexer.lucene.query.SerializableRegexQuery;

/**
 * The Class SerializableRegexQueryTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SerializableRegexQueryTest extends LexBIGServiceTestCase {

    /** The serializable regex query. */
    private SerializableRegexQuery serializableRegexQuery;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "SerializableRegexQuery Test";
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp(){
        serializableRegexQuery = new SerializableRegexQuery(new Term("test", "testValue"));
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
        out.writeObject(serializableRegexQuery);
        out.close();
    }
}