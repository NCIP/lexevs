
package org.LexGrid.LexBIG.Impl.helpers.comparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgClientSideSafe;
import org.lexevs.logging.LoggerFactory;

/**
 * The Class ResultComparator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResultComparator<T> implements Comparator<T>{
   
    /** The sort options. */
    private List<SortOption> sortOptions = new ArrayList<SortOption>();
    
    /** The sort clazz. */
    private Class<T> sortClazz;
    
    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    /**
     * Instantiates a new result comparator.
     */
    public ResultComparator(){}
    
    /**
     * Instantiates a new result comparator.
     * 
     * @param sortOptions the sort options
     * @param sortClazz the sort clazz
     */
    public ResultComparator(List<SortOption> sortOptions, Class<T> sortClazz){
        this.sortOptions = sortOptions;  
        this.sortClazz = sortClazz;
    }
    
    public ResultComparator(SortOptionList sortOptions, Class<T> sortClazz){
        this(sortOptions.getEntry(), sortClazz);
    }
    
    /**
     * Instantiates a new result comparator.
     * 
     * @param sortOptions the sort options
     * @param sortClazz the sort clazz
     */
    public ResultComparator(SortOption[] sortOptions, Class<T> sortClazz){
        this(Arrays.asList(sortOptions), sortClazz);  
    }
    
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @LgClientSideSafe
    public int compare(T a, T b) {
        
        for(SortOption sortOption : sortOptions) {
            int result = doCompare(a, b, sortOption.getExtensionName());

            int reverse = 1;
            if (sortOption.getAscending() != null && !sortOption.getAscending().booleanValue()) {
                reverse = -1;
            }
            if (result != 0) {
                // items are not equal, don't need to step down to the
                // additional algorithms.
                return result * reverse;
            }
        }
        // If we get here, that means none of the algorithms found a difference
        // between two items.

        return 0;
    }
    
    /**
     * Do compare.
     * 
     * @param a the a
     * @param b the b
     * @param algorithm the algorithm
     * 
     * @return the int
     */
    protected int doCompare(T a, T b, String algorithm) {
        try {
            Sort sort = getSortForExtensionName(algorithm);
            if(sort.isSortValidForClass(sortClazz)){
                return sort.getComparatorForSearchClass(sortClazz).compare(a, b);
            } else {
                return 0;
            }
        } catch (LBParameterException e) {
            String errorMessage = "Invalid sort algorithm in the CodeToReturnComparator: "
                + algorithm + " - " + e.getMessage();
            getLogger().error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }
  
    /**
     * Gets the sort for extension name.
     * 
     * @param extensionName the extension name
     * 
     * @return the sort for extension name
     * 
     * @throws LBParameterException the LB parameter exception
     */
    protected Sort getSortForExtensionName(String extensionName) throws LBParameterException {
        return ExtensionRegistryImpl.instance().getSortAlgorithm(extensionName);
    }
    
    /**
     * Validate sorts for classes.
     * 
     * @param clazzes the clazzes
     * 
     * @return true, if successful
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public boolean validateSortsForClasses(Class[] clazzes) throws LBParameterException{
        boolean isValid = true;
        for(SortOption sortOption : sortOptions){
            isValid = isValid && validateSortOptionForClasses(sortOption, clazzes);
        }
        return isValid;
    }
    
    /**
     * Validate sort option for classes.
     * 
     * @param sortOption the sort option
     * @param clazzes the clazzes
     * 
     * @return true, if successful
     * 
     * @throws LBParameterException the LB parameter exception
     */
    protected boolean validateSortOptionForClasses(SortOption sortOption, Class[] clazzes) throws LBParameterException {
        boolean isValid = true;
        for(Class clazz : clazzes){
            isValid = isValid && validateSortOptionForClass(sortOption, clazz);
        }
        return isValid;
    }
    
    /**
     * Validate sort option for class.
     * 
     * @param sortOption the sort option
     * @param clazz the clazz
     * 
     * @return true, if successful
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public boolean validateSortOptionForClass(SortOption sortOption, Class clazz) throws LBParameterException{
        Sort sort = getSortForExtensionName(sortOption.getExtensionName());
        return sort.isSortValidForClass(clazz);
    }

    /**
     * Gets the sort options.
     * 
     * @return the sort options
     */
    public List<SortOption> getSortOptions() {
        return sortOptions;
    }
    
    /**
     * Adds the sort option.
     * 
     * @param sortOption the sort option
     */
    public void addSortOption(SortOption sortOption){
        sortOptions.add(sortOption);
    }

    /**
     * Sets the sort options.
     * 
     * @param sortOptions the new sort options
     */
    public void setSortOptions(List<SortOption> sortOptions) {
        this.sortOptions = sortOptions;
    }

    /**
     * Gets the sort clazz.
     * 
     * @return the sort clazz
     */
    public Class<T> getSortClazz() {
        return sortClazz;
    }

    /**
     * Sets the sort clazz.
     * 
     * @param sortClazz the new sort clazz
     */
    public void setSortClazz(Class<T> sortClazz) {
        this.sortClazz = sortClazz;
    }
    
    public static boolean isSortOptionListValid(SortOptionList sortOptionList){
        return sortOptionList != null 
            && sortOptionList.getEntry() != null
            && sortOptionList.getEntry().length > 0;
        
    }
}