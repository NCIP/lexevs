
package org.lexevs.dao.indexer.lucene.analyzers;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;

/**
 * Test cases for the NormAnalyzer.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version 1.0 - cvs $Revision: 1.1 $ checked in on $Date: 2005/08/24 15:00:43
 *          $
 */
public class StringAnalyzerTest extends TestCase {
	
	private List<String> getList(){
	    List<String> list = new ArrayList<String>();
	    list.add("<:>");
	    return list;
		}
	
    public void testStringAnalyzer() throws Exception {
    	
    	 String input = new String("The<:>trees<:>have<:>Leaves!");
// Was    	 String[] output = {"The","trees", "have","Leaves!"};
// Changed to
    	 String[] output = {"the","trees", "have","leaves"};
    	 BaseTokenStreamTestCase.assertAnalyzesTo(new StandardAnalyzer(new CharArraySet(getList() , false)), input, output);
//        StringAnalyzer temp = new StringAnalyzer("<:>");

 //       String input = new String("The<:>trees<:>have<:>Leaves!");

 //       StringReader reader = new StringReader(input);
 //       TokenStream result = temp.tokenStream("test", reader);

//        Token token = result.next();
//        assertTrue(token.termText().equals("The"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 0);
//        assertTrue(token.endOffset() == 3);
//
//        token = result.next();
//        assertTrue(token.termText().equals("trees"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 6);
//        assertTrue(token.endOffset() == 11);
//
//        token = result.next();
//        assertTrue(token.termText().equals("have"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 14);
//        assertTrue(token.endOffset() == 18);
//
//        token = result.next();
//        assertTrue(token.termText().equals("Leaves!"));
//        assertTrue(token.getPositionIncrement() == 1);
//        assertTrue(token.startOffset() == 21);
//        assertTrue(token.endOffset() == 28);
//
//        token = result.next();
//
//        assertTrue(result.next() == null);
    }


}