
package org.LexGrid.LexBIG.Impl.Extensions.Sort;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * The Class AbstractSort.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSort extends AbstractExtendable implements Sort {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2344429406565871310L;
    
    /** The supported class comparators map. */
    private Map<Class,Comparator> supportedClassComparatorsMap;
    
    /** The sort description. */
    private SortDescription sortDescription;
    
    /**
     * Instantiates a new abstract sort.
     * 
     * @throws LBParameterException the LB parameter exception
     * @throws LBException the LB exception
     */
    public AbstractSort() throws LBParameterException, LBException{
        super();
        doRegisterComparators();
    }
    
    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerSortExtension(sortDescription);
    }
   
    /**
     * Builds the sort description.
     * 
     * @return the sort description
     */
    protected abstract SortDescription buildSortDescription();
    
    protected SortDescription buildExtensionDescription(){
       SortDescription sortDescription = this.buildSortDescription();
       this.sortDescription = sortDescription;
       return sortDescription;
    }

    /**
     * Register comparators.
     * 
     * @param classToComparatorsMap the class to comparators map
     */
    public abstract void registerComparators(Map<Class,Comparator> classToComparatorsMap);

    /**
     * Do register comparators.
     */
    private void doRegisterComparators(){
        supportedClassComparatorsMap = new HashMap<Class,Comparator>(); 
        registerComparators(supportedClassComparatorsMap);
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Sort#getComparatorForSearchClass(java.lang.Class)
     */
    @LgClientSideSafe
    public <T> Comparator<T> getComparatorForSearchClass(Class<T> searchClass) throws LBParameterException {
        if(supportedClassComparatorsMap.containsKey(searchClass)){
            return (Comparator<T>)supportedClassComparatorsMap.get(searchClass);
        } else {
            throw new LBParameterException("No Comparator available for: " + searchClass.getName());
        }
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Query.Sort#isSortValidForClass(java.lang.Class)
     */
    public boolean isSortValidForClass(Class clazz){
        return supportedClassComparatorsMap.containsKey(clazz);
    }
    
    /**
     * Gets the supported class comparators.
     * 
     * @return the supported class comparators
     */
    public Set<Class> getSupportedClassComparators(){
        return supportedClassComparatorsMap.keySet();
    }
    
    /**
     * Sets the sort description.
     */
    private void setSortDescription(){
        sortDescription = buildSortDescription();
    }
}