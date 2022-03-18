
package org.LexGrid.LexBIG.Impl.helpers.comparator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.lexevs.system.ResourceManager;

/**
 * The Class ResultComparatorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResultComparatorTest extends LexBIGServiceTestCase {

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "ResultComparator Tests";
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp(){
        ResourceManager.instance();
    }
    
    /**
     * Test do compare.
     */
    public void testDoCompare(){
        SortOption sortOption = new SortOption();
        sortOption.setExtensionName("codeSystem");
        sortOption.setAscending(true);
        
        ResultComparator<CodeToReturn> comparator = new ResultComparator<CodeToReturn>();
        comparator.addSortOption(sortOption);
        comparator.setSortClazz(CodeToReturn.class);
        
        CodeToReturn c1 = new CodeToReturn();
        c1.setUri("A_URI");
        
        CodeToReturn c2 = new CodeToReturn();
        c2.setUri("Z_URI");
      
        assertTrue(comparator.doCompare(c1, c2, "codeSystem") <= -1);
    }
    
    /**
     * Test compare ascending.
     */
    public void testCompareAscending(){
        SortOption sortOption = new SortOption();
        sortOption.setExtensionName("codeSystem");
        sortOption.setAscending(true);
        
        ResultComparator<CodeToReturn> comparator = new ResultComparator<CodeToReturn>();
        comparator.addSortOption(sortOption);
        comparator.setSortClazz(CodeToReturn.class);
        
        CodeToReturn c1 = new CodeToReturn();
        c1.setUri("A_URI");
        
        CodeToReturn c2 = new CodeToReturn();
        c2.setUri("Z_URI");
      
        assertTrue(comparator.compare(c1, c2) <= -1);
    }
    
    /**
     * Test compare decending.
     */
    public void testCompareDecending(){
        SortOption sortOption = new SortOption();
        sortOption.setExtensionName("codeSystem");
        sortOption.setAscending(false);
        
        ResultComparator<CodeToReturn> comparator = new ResultComparator<CodeToReturn>();
        comparator.addSortOption(sortOption);
        comparator.setSortClazz(CodeToReturn.class);
        
        CodeToReturn c1 = new CodeToReturn();
        c1.setUri("A_URI");
        
        CodeToReturn c2 = new CodeToReturn();
        c2.setUri("Z_URI");
      
        assertTrue(comparator.compare(c1, c2) >= 1);
    }
    
    /**
     * Test compare different classes resolved concept reference.
     */
    public void testCompareDifferentClassesResolvedConceptReference(){
        SortOption sortOption = new SortOption();
        sortOption.setExtensionName("codeSystem");
        sortOption.setAscending(true);
        
        ResultComparator<ResolvedConceptReference> comparator = new ResultComparator<ResolvedConceptReference>();
        comparator.addSortOption(sortOption);
        comparator.setSortClazz(ResolvedConceptReference.class);
        
        ResolvedConceptReference ref1 = new ResolvedConceptReference();
        ref1.setCodingSchemeURI("A_URI");
        
        ResolvedConceptReference ref2 = new ResolvedConceptReference();
        ref2.setCodingSchemeURI("Z_URI");
      
        assertTrue(comparator.compare(ref1, ref2) <= -1);
    }
    
    /**
     * Test compare bad algorithm.
     */
    public void testCompareBadAlgorithm(){
        SortOption sortOption = new SortOption();
        sortOption.setExtensionName("bogusAlgorithm");
        sortOption.setAscending(true);
        
        ResultComparator<ResolvedConceptReference> comparator = new ResultComparator<ResolvedConceptReference>();
        comparator.addSortOption(sortOption);
        comparator.setSortClazz(ResolvedConceptReference.class);
        
        ResolvedConceptReference ref1 = new ResolvedConceptReference();
        ref1.setCodingSchemeURI("A_URI");
        
        ResolvedConceptReference ref2 = new ResolvedConceptReference();
        ref2.setCodingSchemeURI("Z_URI");
      
        try {
            assertTrue(comparator.compare(ref1, ref2) <= -1);
        } catch (Exception e) {
          return;
        }
        fail("Didn't throw an error when looking for a Comparator with a bogus algorithm.");
  
    }
    
    /**
     * Test compare different classes bogus class.
     */
    public void testCompareDifferentClassesBogusClass() throws Exception {
        SortOption sortOption = new SortOption();
        sortOption.setExtensionName("codeSystem");
        sortOption.setAscending(true);
        
        ResultComparator<Date> comparator = new ResultComparator<Date>();
        comparator.addSortOption(sortOption);
        comparator.setSortClazz(Date.class);
        
        Date ref1 = new Date();
        
        Date ref2 = new Date();
        
        assertFalse(comparator.validateSortOptionForClass(sortOption, Date.class));
      
        assertTrue(comparator.compare(ref1, ref2) == 0);
      }
    
    /**
     * Test get sort for extension name.
     * 
     * @throws Exception the exception
     */
    public void testGetSortForExtensionName() throws Exception{
        SortOption sortOption = new SortOption();
        sortOption.setExtensionName("codeSystem");
        sortOption.setAscending(true);
        
        ResultComparator<Date> comparator = new ResultComparator<Date>();
        comparator.addSortOption(sortOption);
        
        Sort sort = comparator.getSortForExtensionName("codeSystem");
        
        assertTrue(sort.getName().equals("codeSystem"));      
    }
    
    /**
     * Test validate sort option for class.
     * 
     * @throws Exception the exception
     */
    public void testValidateSortOptionForClass() throws Exception{
        SortOption sortOption = new SortOption();
        sortOption.setExtensionName("codeSystem");
        sortOption.setAscending(true);
        
        ResultComparator<Date> comparator = new ResultComparator<Date>();
        comparator.addSortOption(sortOption);
        
        assertTrue(
                comparator.validateSortOptionForClass(sortOption, CodeToReturn.class));
        
        assertTrue(
                comparator.validateSortOptionForClass(sortOption, ResolvedConceptReference.class));    
   
        assertFalse(
                comparator.validateSortOptionForClass(sortOption, Date.class));    
    }
    
    /**
     * Test validate sort option for classes.
     * 
     * @throws Exception the exception
     */
    public void testValidateSortOptionForClasses() throws Exception{
        SortOption sortOption = new SortOption();
        sortOption.setExtensionName("codeSystem");
        
        ResultComparator<Date> comparator = new ResultComparator<Date>();
        comparator.addSortOption(sortOption);
        
        Class[] goodClazzes1 = new Class[]{CodeToReturn.class};  
        Class[] goodClazzes2 = new Class[]{CodeToReturn.class, 
                ResolvedConceptReference.class};
        Class[] badClazzes1 = new Class[]{CodeToReturn.class, Date.class};
        Class[] badClazzes2 = new Class[]{CodeToReturn.class, 
                ResolvedConceptReference.class,
                Date.class};
          
        assertTrue(
                comparator.validateSortOptionForClasses(sortOption, goodClazzes1));
        assertTrue(
                comparator.validateSortOptionForClasses(sortOption, goodClazzes2));
        assertFalse(
                comparator.validateSortOptionForClasses(sortOption, badClazzes1));
        assertFalse(
                comparator.validateSortOptionForClasses(sortOption, badClazzes2));   
    }
    
    /**
     * Test validate sorts for classes.
     * 
     * @throws Exception the exception
     */
    public void testValidateSortsForClasses() throws Exception{
        SortOption sortOption1 = new SortOption();
        sortOption1.setExtensionName("codeSystem");
        
        SortOption sortOption2 = new SortOption();
        sortOption2.setExtensionName("matchToQuery");
        
        ResultComparator<Date> comparator = new ResultComparator<Date>();
        List<SortOption> sortOptions = new ArrayList<SortOption>();
        sortOptions.add(sortOption1);
        sortOptions.add(sortOption2);
        comparator.setSortOptions(sortOptions);
        
        Class[] goodClazzes = new Class[]{CodeToReturn.class};  
        Class[] badClazzes1 = new Class[]{CodeToReturn.class, Date.class};
        Class[] badClazzes2 = new Class[]{CodeToReturn.class, 
                ResolvedConceptReference.class};
          
        assertTrue(
                comparator.validateSortsForClasses(goodClazzes));
        assertFalse(
                comparator.validateSortsForClasses(badClazzes1));
        assertFalse(
                comparator.validateSortsForClasses(badClazzes2));   
    }
}