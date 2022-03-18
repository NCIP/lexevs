
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
import org.LexGrid.LexBIG.Impl.helpers.graph.GNode;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.commonTypes.Source;

/**
 * ConceptStatus sort which implements the Sort interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodeSort extends AbstractSort {
    private static final long serialVersionUID = 2904818499120921606L;
    final static String description_ = "Sort the results according to the concept code";
    final static String name_ = "code";
    final static String provider_ = "Mayo Clinic";
    final static String role_ = "author"; // 2006 model change
    final static Source providerSource = new Source(); // 2006 model change
    final static String version_ = "1.0";

    public CodeSort() throws LBParameterException, LBException {
        super();
    }
    
    public SortDescription buildSortDescription() {
        SortDescription sd = new SortDescription();
        sd.setExtensionBaseClass(Sort.class.getName());
        sd.setExtensionClass(CodeSort.class.getName());
        sd.setDescription(description_);
        sd.setName(name_);
        sd.setVersion(version_);
        providerSource.setContent(provider_);
        providerSource.setRole(role_);
        sd.setExtensionProvider(providerSource);
        sd.setRestrictToContext(new SortContext[] { SortContext.SET, 
                SortContext.SETLISTPRERESOLVE,
                SortContext.SETITERATION,  
                SortContext.GRAPH });

       return sd;
    }

    @Override
    public void registerComparators(Map<Class, Comparator> classToComparatorsMap) {
        classToComparatorsMap.put(CodeToReturn.class, new CodeSort.CodeToReturnConceptCodeSort()); 
        classToComparatorsMap.put(GNode.class, new CodeSort.GNodeConceptCodeSort());     
    }
    
    @LgClientSideSafe
    protected class CodeToReturnConceptCodeSort implements Comparator<CodeToReturn>{
     
        @LgClientSideSafe
        public int compare(CodeToReturn o1, CodeToReturn o2) {
            return o1.getCode().compareToIgnoreCase(o2.getCode());
        }
    }
    
    @LgClientSideSafe
    protected class GNodeConceptCodeSort implements Comparator<GNode>{
     
        @LgClientSideSafe
        public int compare(GNode o1, GNode o2) {
            return o1.getCode().compareToIgnoreCase(o2.getCode());
        }
    }
    
    @LgClientSideSafe
    protected class ResolvedConceptReferenceConceptCodeSort implements Comparator<ResolvedConceptReference>{
     
        @LgClientSideSafe
        public int compare(ResolvedConceptReference o1, ResolvedConceptReference o2) {
            return o1.getCode().compareToIgnoreCase(o2.getCode());
        }
    }
}