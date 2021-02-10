
package org.LexGrid.LexBIG.Impl.Extensions.Sort;

import java.util.Comparator;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.commonTypes.Source;

/**
 * Code sort which implements the Sort interface.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodePostSort extends CodeSort {
    private static final long serialVersionUID = 2904818499120921606L;
    final static String description_ = "Sort the results according to the concept code after resolving to a List.";
    final static String name_ = "codePost";
    final static String provider_ = "Mayo Clinic";
    final static String role_ = "author";
    final static Source providerSource = new Source();
    final static String version_ = "1.0";

    public CodePostSort() throws LBParameterException, LBException {
        super();
    }
    
    public SortDescription buildSortDescription() {
        SortDescription sd = new SortDescription();
        sd.setExtensionBaseClass(Sort.class.getName());
        sd.setExtensionClass(CodePostSort.class.getName());
        sd.setDescription(description_);
        sd.setName(name_);
        sd.setVersion(version_);
        providerSource.setContent(provider_);
        providerSource.setRole(role_); 
        sd.setExtensionProvider(providerSource); 
        sd.setRestrictToContext(new SortContext[] { SortContext.SET, 
                SortContext.SETLISTPOSTRESOLVE });

       return sd;
    }

    @Override
    public void registerComparators(Map<Class, Comparator> classToComparatorsMap) {
        classToComparatorsMap.put(ResolvedConceptReference.class, new CodeSort.ResolvedConceptReferenceConceptCodeSort());   
    }
}