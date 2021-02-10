
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
 * CodeSystem sort which implements the Sort interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodeSystemSort extends AbstractSort {
    
    private static final long serialVersionUID = 6186186064325148661L;
    final static String description_ = "Sort the results according to the code system";
    final static String name_ = "codeSystem";
    final static String provider_ = "Mayo Clinic";
    final static String version_ = "1.0";
    
    public CodeSystemSort() throws LBParameterException, LBException {
        super();
    }

    public SortDescription buildSortDescription() {
        SortDescription sd = new SortDescription();
        sd.setExtensionBaseClass(Sort.class.getName());
        sd.setExtensionClass(CodeSystemSort.class.getName());
        sd.setDescription(description_);
        sd.setName(name_);
        sd.setVersion(version_);
        sd.setRestrictToContext(new SortContext[] { SortContext.SET, SortContext.SETITERATION });
        
        return sd;
    }
    
    @Override
    public void registerComparators(Map<Class, Comparator> classToComparatorsMap) {
        classToComparatorsMap.put(CodeToReturn.class, new CodeSystemSort.CodeToReturnCodeSystemSort());  
        classToComparatorsMap.put(ResolvedConceptReference.class, new ResolvedConceptReferenceCodeSystemSort()); 
    }
 
    @LgClientSideSafe
    private class ResolvedConceptReferenceCodeSystemSort implements Comparator<ResolvedConceptReference>{ 
       
        @LgClientSideSafe
        public int compare(ResolvedConceptReference o1, ResolvedConceptReference o2) {
            return o1.getCodingSchemeURI().compareTo(o2.getCodingSchemeURI());
        }
    }

    @LgClientSideSafe
    private class CodeToReturnCodeSystemSort implements Comparator<CodeToReturn>{
        @LgClientSideSafe
        public int compare(CodeToReturn o1, CodeToReturn o2) {
            return o1.getUri().compareToIgnoreCase(o2.getUri());
        }
    }
}