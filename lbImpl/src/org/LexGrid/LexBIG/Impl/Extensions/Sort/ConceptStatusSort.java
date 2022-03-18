
package org.LexGrid.LexBIG.Impl.Extensions.Sort;

import java.util.Comparator;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;

/**
 * ConceptStatus sort which implements the Sort interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ConceptStatusSort extends AbstractSort {
    private static final long serialVersionUID = 2904818499120921606L;
    final static String description_ = "Sort the results according to the concept status";
    final static String name_ = SQLTableConstants.TBLCOL_CONCEPTSTATUS;
    final static String provider_ = "Mayo Clinic";
    final static String role_ = "author"; // 2006 model change
    final static Source providerSource = new Source(); // 2006 model change
    final static String version_ = "1.0";

    public ConceptStatusSort() throws LBParameterException, LBException {
        super();
    }
    
    public SortDescription buildSortDescription() {
        SortDescription sd = new SortDescription();
        sd.setExtensionBaseClass(Sort.class.getName());
        sd.setExtensionClass(ConceptStatusSort.class.getName());
        sd.setDescription(description_);
        sd.setName(name_);
        sd.setVersion(version_);
        // Update for 2006 Model
        providerSource.setContent(provider_); // 2006 model change
        providerSource.setRole(role_); // 2006 model change
        sd.setExtensionProvider(providerSource); // 2006 model change
        sd.setRestrictToContext(new SortContext[] { SortContext.SET });

       return sd;
    }

    @Override
    public void registerComparators(Map<Class, Comparator> classToComparatorsMap) {
        classToComparatorsMap.put(ResolvedConceptReference.class, new ConceptStatusSort.ResolvedConceptReferenceConceptStatusSort()); 
    }
    
    @LgClientSideSafe
    private class ResolvedConceptReferenceConceptStatusSort implements Comparator<ResolvedConceptReference>{
     
        @LgClientSideSafe
        public int compare(ResolvedConceptReference o1, ResolvedConceptReference o2) {
            return o1.getEntity().getStatus().compareToIgnoreCase(o2.getEntity().getStatus());
        }
    }
}