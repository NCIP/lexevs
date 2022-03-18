
package org.LexGrid.LexBIG.Impl.helpers;

import java.util.Arrays;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.Impl.helpers.comparator.ResultComparator;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.lexevs.system.ResourceManager;

/**
 * JUnit Tests for the CodeToReturn sorter.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodeToReturnTest extends TestCase {
    
    public void setUp(){
        ResourceManager.instance();
    }

    public void testCodeToReturnSorter() throws Exception {
        CodeToReturn[] testData = new CodeToReturn[4];

        // basic test - sort by code.

        testData[0] = new CodeToReturn("a", "aED", "1", "1.0", (float) 0.9);
        testData[1] = new CodeToReturn("z", "zED", "1", "1.0", (float) 0.8);
        testData[2] = new CodeToReturn("bb", "bbED", "1", "1.0", (float) 0.7);
        testData[3] = new CodeToReturn("q", "qED", "1", "1.0", (float) 0.6);

        SortOption[] sortOrder = new SortOption[] { Constructors.createSortOption("code", null) };

        Arrays.sort(testData, new ResultComparator<CodeToReturn>(sortOrder, CodeToReturn.class));

        assertTrue(testData[0].getCode().equals("a"));
        assertTrue(testData[1].getCode().equals("bb"));
        assertTrue(testData[2].getCode().equals("q"));
        assertTrue(testData[3].getCode().equals("z"));

        // now try reverse
        sortOrder = new SortOption[] { Constructors.createSortOption("code", new Boolean(false)) };

        Arrays.sort(testData, new ResultComparator<CodeToReturn>(sortOrder, CodeToReturn.class));

        assertTrue(testData[3].getCode().equals("a"));
        assertTrue(testData[2].getCode().equals("bb"));
        assertTrue(testData[1].getCode().equals("q"));
        assertTrue(testData[0].getCode().equals("z"));

        // sort by code, break ties with score.

        testData[0] = new CodeToReturn("a", "aED", "1", "1.0", (float) 0.9);
        testData[1] = new CodeToReturn("a", "aED", "1", "1.0", (float) 1.0);
        testData[2] = new CodeToReturn("bb", "bbED", "1", "1.0", (float) 0.7);
        testData[3] = new CodeToReturn("q", "qED", "1", "1.0", (float) 0.6);

        sortOrder = new SortOption[] { Constructors.createSortOption("code", new Boolean(true)),
                Constructors.createSortOption("matchToQuery", null) };
       

        Arrays.sort(testData, new ResultComparator<CodeToReturn>(sortOrder, CodeToReturn.class));

        assertTrue(testData[0].getCode().equals("a"));
        assertTrue(testData[0].getScore() == (float) 1.0);
        assertTrue(testData[1].getCode().equals("a"));
        assertTrue(testData[1].getScore() == (float) 0.9);
        assertTrue(testData[2].getCode().equals("bb"));
        assertTrue(testData[3].getCode().equals("q"));

        // sort by score

        testData[0] = new CodeToReturn("a", "aED", "1", "1.0", (float) .1);
        testData[1] = new CodeToReturn("b", "bED", "1", "1.0", (float) .5);
        testData[2] = new CodeToReturn("c", "cED", "1", "1.0", (float) 0.7);
        testData[3] = new CodeToReturn("d", "dED", "1", "1.0", (float) 0.996);

        sortOrder = new SortOption[] { Constructors.createSortOption("matchToQuery", null) };

        Arrays.sort(testData, new ResultComparator<CodeToReturn>(sortOrder, CodeToReturn.class));

        assertTrue(testData[0].getCode().equals("d"));
        assertTrue(testData[1].getCode().equals("c"));
        assertTrue(testData[2].getCode().equals("b"));
        assertTrue(testData[3].getCode().equals("a"));

        // sort by score - break ties with code (reverse order on sort by score)

        testData[0] = new CodeToReturn("b", "bED", "1", "1.0", (float) .5);
        testData[1] = new CodeToReturn("a", "aED", "1", "1.0", (float) .5);
        testData[2] = new CodeToReturn("d", "dED", "1", "1.0", (float) 0.7);
        testData[3] = new CodeToReturn("c", "cED", "1", "1.0", (float) 0.7);

        sortOrder = new SortOption[] { Constructors.createSortOption("matchToQuery", new Boolean(false)),
                Constructors.createSortOption("code", null) };

        Arrays.sort(testData, new ResultComparator<CodeToReturn>(sortOrder, CodeToReturn.class));

        assertTrue(testData[0].getCode().equals("a"));
        assertTrue(testData[1].getCode().equals("b"));
        assertTrue(testData[2].getCode().equals("c"));
        assertTrue(testData[3].getCode().equals("d"));

        // sort by codesystem

        testData[0] = new CodeToReturn("a", "aED", "1.4", "1.0", (float) .5);
        testData[1] = new CodeToReturn("b", "bED", "1.3", "1.0", (float) .5);
        testData[2] = new CodeToReturn("c", "cED", "1.2", "1.0", (float) 0.7);
        testData[3] = new CodeToReturn("d", "dED", "1.1", "1.0", (float) 0.7);

        sortOrder = new SortOption[] { Constructors.createSortOption("codeSystem", null) };

        Arrays.sort(testData, new ResultComparator<CodeToReturn>(sortOrder, CodeToReturn.class));

        assertTrue(testData[0].getCode().equals("d"));
        assertTrue(testData[1].getCode().equals("c"));
        assertTrue(testData[2].getCode().equals("b"));
        assertTrue(testData[3].getCode().equals("a"));

        // group by code system, sort by score then code

        testData[0] = new CodeToReturn("a", "aED", "1.1", "1.0", (float) 0.9);
        testData[1] = new CodeToReturn("c", "cED", "1.1", "1.0", (float) 0.7);
        testData[2] = new CodeToReturn("b", "bED", "1.1", "1.0", (float) 0.7);
        testData[3] = new CodeToReturn("z", "zED", "1.0", "1.0", (float) 0.1);

        sortOrder = new SortOption[] { Constructors.createSortOption("codeSystem", null),
                Constructors.createSortOption("matchToQuery", null), Constructors.createSortOption("code", null) };

        Arrays.sort(testData, new ResultComparator<CodeToReturn>(sortOrder, CodeToReturn.class));

        assertTrue(testData[0].getCode().equals("z"));
        assertTrue(testData[1].getCode().equals("a"));
        assertTrue(testData[2].getCode().equals("b"));
        assertTrue(testData[3].getCode().equals("c"));

    }
}