
package org.LexGrid.LexBIG.Impl.Extensions.Sort;

import java.util.Comparator;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Sort;

/**
 * Entity description sort which implements the Sort interface.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a> 
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class EntityDescriptionPostSort extends EntityDescriptionSort {
  
    private static final long serialVersionUID = -8356411011570608432L;
    final static String description_ = "Sort the results of a List Post-Resolve, according to the entityDescription";
    final static String name_ = "entityDescriptionPost";
    final static String provider_ = "Mayo Clinic";
    final static String version_ = "1.0";
    
    public EntityDescriptionPostSort() throws LBParameterException, LBException {
        super();
    }
    
    protected SortDescription buildSortDescription() {
        SortDescription sd = new SortDescription();
        sd.setExtensionBaseClass(Sort.class.getName());
        sd.setExtensionClass(EntityDescriptionPostSort.class.getName());
        sd.setDescription(description_);
        sd.setName(name_);
        sd.setVersion(version_);
        sd.setRestrictToContext(new SortContext[] { SortContext.SETLISTPOSTRESOLVE });

        return sd;
    }
    
    @Override
    public void registerComparators(Map<Class, Comparator> classToComparatorsMap) {
        classToComparatorsMap.put(ResolvedConceptReference.class, new EntityDescriptionPostSort.ResolvedConceptReferenceEntityDescritpionSort()); 
    }  
}