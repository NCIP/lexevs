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
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.Extensions.Sort;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;

/**
 * The Class AbstractSortTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AbstractSortTest extends LexBIGServiceTestCase {
    
    /** The abstract sort. */
    private AbstractSort abstractSort;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "AbstractSort Test";
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp(){
        try {
            abstractSort = new TestAbstractSortTest();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Test set up.
     */
    public void testSetUp(){
        assertNotNull(abstractSort);
    }
    
    /**
     * Test sort description.
     * 
     * @throws LBParameterException the LB parameter exception
     * @throws LBException the LB exception
     */
    public void testSortDescription() throws LBParameterException, LBException{
        assertTrue(this.abstractSort.getName().equals("Test_Name"));
    }
    
    /**
     * Test get supported class comparators.
     */
    public void testGetSupportedClassComparators(){
       Set<Class> clazzSet = abstractSort.getSupportedClassComparators();
       assertTrue(clazzSet.size() == 2);
       
       assertTrue(clazzSet.contains(String.class));
       assertTrue(clazzSet.contains(Integer.class));
       assertFalse(clazzSet.contains(Float.class));
    }
    
    /**
     * Test is comparator registered for class.
     */
    public void testIsComparatorRegisteredForClass(){
        assertTrue(abstractSort.isSortValidForClass(String.class));
        assertTrue(abstractSort.isSortValidForClass(Integer.class));
        assertFalse(abstractSort.isSortValidForClass(Date.class));
    }
    
    /**
     * Test get comparator for search class string.
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public void testGetComparatorForSearchClassString() throws LBParameterException{
        Comparator<String> testString = abstractSort.getComparatorForSearchClass(String.class);
        assertNotNull(testString);
        
        assertTrue(testString.compare("short", "looooooong") <= -1);
    }
    
    /**
     * Test get comparator for search class integer.
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public void testGetComparatorForSearchClassInteger() throws LBParameterException{
        Comparator<Integer> testInteger = abstractSort.getComparatorForSearchClass(Integer.class);
        assertNotNull(testInteger);
        
        assertTrue(testInteger.compare(1,2) <= -1);
    }
    
    /**
     * Test get comparator for bogus class.
     */
    public void testGetComparatorForBogusClass() {
        try {
            Comparator<Date> testInteger = abstractSort.getComparatorForSearchClass(Date.class);
        } catch (LBParameterException e) {
            //this is good
            return;
        }
        
        fail("Returned a Comparator for a invalid class.");
    }
    
    /**
     * The Class TestAbstractSortTest.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class TestAbstractSortTest extends AbstractSort {

        /**
         * Instantiates a new test abstract sort test.
         * 
         * @throws LBParameterException the LB parameter exception
         * @throws LBException the LB exception
         */
        public TestAbstractSortTest() throws LBParameterException, LBException {
            super();
        }

        /* (non-Javadoc)
         * @see org.LexGrid.LexBIG.Impl.Extensions.Sort.AbstractSort#buildSortDescription()
         */
        @Override
        protected SortDescription buildSortDescription() {
           SortDescription sd = new SortDescription();
           sd.setName("Test_Name");
           return sd;
        }

        /* (non-Javadoc)
         * @see org.LexGrid.LexBIG.Impl.Extensions.Sort.AbstractSort#registerComparators(java.util.Map)
         */
        @Override
        public void registerComparators(Map<Class, Comparator> classToComparatorsMap) {
            classToComparatorsMap.put(String.class, new TestStringComparator());
            classToComparatorsMap.put(Integer.class, new TestIntegerComparator());      
        }
    }
    
    /**
     * The Class TestStringComparator.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class TestStringComparator implements Comparator<String> {
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(String arg0, String arg1) {
           return arg0.length() - arg1.length();
        }    
    }
    
    /**
     * The Class TestIntegerComparator.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class TestIntegerComparator implements Comparator<Integer> {
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Integer arg0, Integer arg1) {
           return arg0 - arg1;
        }    
    }

}
