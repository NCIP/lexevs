
package org.LexGrid.LexBIG.Impl.Extensions.Sort;

import java.util.Comparator;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Entity description sort which implements the Sort interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a> 
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class EntityDescriptionSort extends AbstractSort {
  
    private static final long serialVersionUID = -8356411011570608432L;
    final static String description_ = "Sort the results according to the entityDescription";
    final static String name_ = "entityDescription";
    final static String provider_ = "Mayo Clinic";
    final static String version_ = "1.0";
    
    public EntityDescriptionSort() throws LBParameterException, LBException {
        super();
    }

    protected SortDescription buildSortDescription() {
        SortDescription sd = new SortDescription();
        sd.setExtensionBaseClass(Sort.class.getName());
        sd.setExtensionClass(EntityDescriptionSort.class.getName());
        sd.setDescription(description_);
        sd.setName(name_);
        sd.setVersion(version_);
        sd.setRestrictToContext(new SortContext[] { 
                SortContext.SET, 
                SortContext.SETLISTPRERESOLVE, 
                SortContext.SETITERATION });

        return sd;
    }
    
    @Override
    public void registerComparators(Map<Class, Comparator> classToComparatorsMap) {
        classToComparatorsMap.put(CodeToReturn.class, new EntityDescriptionSort.CodeToReturnEntityDescritpionSort());
    }


    @LgClientSideSafe
    protected class ResolvedConceptReferenceEntityDescritpionSort implements Comparator<ResolvedConceptReference>{
        @LgClientSideSafe
        public int compare(ResolvedConceptReference o1, ResolvedConceptReference o2) {
            return
                o1.getEntityDescription().getContent()
                    .compareToIgnoreCase(
                        o2.getEntityDescription().getContent());
        }
    }
    
    @LgClientSideSafe
    protected class CodeToReturnEntityDescritpionSort implements Comparator<CodeToReturn>{
        @LgClientSideSafe
        public int compare(CodeToReturn o1, CodeToReturn o2) {
            return
                o1.getEntityDescription()
                    .compareToIgnoreCase(
                        o2.getEntityDescription());
        }
    }
}