
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import java.io.Reader;

import junit.framework.TestCase;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.ArrayUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;

/**
 * The Class LazyLoadableCodeToReturnTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LazyLoadableCodeToReturnTest extends TestCase {

    /** The code to return. */
    protected AbstractLazyLoadableCodeToReturn codeToReturn;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() {
        try {
            buildCodeToReturn();
            hydrate();
        } catch (Exception e) {
           fail(e.getMessage());
        }
    }
    
    /**
     * Hydrate.
     */
    protected void hydrate(){
        try {
            codeToReturn.hydrate();
        } catch (Exception e) {
           fail(e.getMessage());
        }
    }
    
    /**
     * Builds the code to return.
     * 
     * @throws Exception the exception
     */
    protected void buildCodeToReturn() throws Exception {
    	AbstractLazyLoadableCodeToReturn codeToReturn = new TestLazyLoadableCodeToReturn();
        codeToReturn.setDocumentId(1);
        this.codeToReturn = codeToReturn;
    }

    /**
     * Test uri.
     */
    public void testUri(){
        assertTrue(codeToReturn.getUri().equals("cs"));
    }
    
    /**
     * Test entity description.
     */
    public void testEntityDescription(){
        assertTrue(codeToReturn.getEntityDescription().equals("description"));
    }
    
    /**
     * Test namespace.
     */
    public void testNamespace(){
        assertTrue(codeToReturn.getNamespace().equals("namespace"));
    }
    
    /**
     * Test entity id.
     */
    public void testEntityId(){
        assertTrue(codeToReturn.getCode().equals("id"));
    }
    
    /**
     * Test entity type.
     */
    public void testEntityType(){
        assertTrue(codeToReturn.getEntityTypes().length == 2);
        assertTrue(ArrayUtils.contains(codeToReturn.getEntityTypes(), "type1"));
        assertTrue(ArrayUtils.contains(codeToReturn.getEntityTypes(), "type2"));
    }
    
    
    /**
     * The Class TestField.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class TestField extends FieldType {

        /** The value. */
        private String value;
        
        /**
         * Instantiates a new test field.
         * 
         * @param name the name
         * @param value the value
         */
        public TestField(String name, String value){
 //           this.name = name;
            this.value = value;
//            super. = name;
        }
        
        /* (non-Javadoc)
         * @see org.apache.lucene.document.Fieldable#binaryValue()
         */
        public byte[] binaryValue() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.apache.lucene.document.Fieldable#readerValue()
         */
        public Reader readerValue() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.apache.lucene.document.Fieldable#stringValue()
         */
        public String stringValue() {
            return value;
        }

        /* (non-Javadoc)
         * @see org.apache.lucene.document.Fieldable#tokenStreamValue()
         */
        public TokenStream tokenStreamValue() {
            return null;
        }
    }
    
    /**
     * The Class TestSQLTableConstants.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class TestSQLTableConstants extends SQLTableConstants {
 
        /**
         * Instantiates a new test sql table constants.
         * 
         * @param version the version
         * @param tablePrefix the table prefix
         */
        public TestSQLTableConstants(String version, String tablePrefix) {
            super(null, null);
        }
        
        /* (non-Javadoc)
         * @see org.LexGrid.util.sql.lgTables.SQLTableConstants#supports2009Model()
         */
        @Override
        public boolean supports2009Model(){
            return true;
        }    
    }   
    
    /**
     * The Class TestLazyLoadableCodeToReturn.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class TestLazyLoadableCodeToReturn extends AbstractLazyLoadableCodeToReturn {

        /* (non-Javadoc)
         * @see org.LexGrid.LexBIG.Impl.helpers.lazyloading.LazyLoadableCodeToReturn#getIndexReader()
         */
        @Override
        protected Document buildDocument(){
            Document doc = new Document();
            doc.add(new TextField(SQLTableConstants.TBLCOL_ENTITYCODE, "id", null));  
            doc.add(new TextField("codingSchemeUri","cs", null));  
            doc.add(new TextField(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION,"description", null));  
            doc.add(new TextField(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE,"namespace", null));  
            doc.add(new TextField("type","type1", null));
            doc.add(new TextField("type","type2", null));
            
            return doc;
        }

		@Override
		protected void doCompact() {
			//
		}  
        
        
    }
}