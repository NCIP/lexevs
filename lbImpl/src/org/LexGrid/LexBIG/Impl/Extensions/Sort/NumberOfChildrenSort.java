
package org.LexGrid.LexBIG.Impl.Extensions.Sort;

import java.util.Comparator;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.LexBIG.Impl.helpers.graph.GAssociation;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * NumberOfChildren sort which implements the Sort interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class NumberOfChildrenSort extends AbstractSort {
    
    private static final long serialVersionUID = 6186186064325148661L;
    final static String description_ = "Sort the results according to the Associations's number of children.";
    final static String name_ = "associationChildCount";
    final static String provider_ = "Mayo Clinic";
    final static String version_ = "1.0";
    
    public NumberOfChildrenSort() throws LBParameterException, LBException {
        super();
    }

    public SortDescription buildSortDescription() {
        SortDescription sd = new SortDescription();
        sd.setExtensionBaseClass(Sort.class.getName());
        sd.setExtensionClass(NumberOfChildrenSort.class.getName());
        sd.setDescription(description_);
        sd.setName(name_);
        sd.setVersion(version_);
        sd.setRestrictToContext(new SortContext[] { SortContext.GRAPH });
        
        return sd;
    }
    
    @Override
    public void registerComparators(Map<Class, Comparator> classToComparatorsMap) {
        classToComparatorsMap.put(GAssociation.class, new NumberOfChildrenSort.GAssociationNumberOfChildrenSort());
        classToComparatorsMap.put(Association.class, new NumberOfChildrenSort.AssociationNumberOfChildrenSort());  
    }
 
    @LgClientSideSafe
    private class GAssociationNumberOfChildrenSort implements Comparator<GAssociation>{ 
       
        @LgClientSideSafe
        public int compare(GAssociation o1, GAssociation o2) {
            return o1.getChildCount() - o2.getChildCount();
        }
    }
    
    @LgClientSideSafe
    private class AssociationNumberOfChildrenSort implements Comparator<Association>{ 
       
        @LgClientSideSafe
        public int compare(Association o1, Association o2) {
            return o1.getAssociatedConcepts().getAssociatedConceptCount() - o2.getAssociatedConcepts().getAssociatedConceptCount();
        }
    }
}