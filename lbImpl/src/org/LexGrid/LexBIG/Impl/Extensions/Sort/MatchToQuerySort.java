
package org.LexGrid.LexBIG.Impl.Extensions.Sort;

import java.util.Comparator;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
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
public class MatchToQuerySort extends AbstractSort {
    private static final long serialVersionUID = 2904818499120921606L;
    public final static String description_ = "Sort the results according to the *best* match (according to the search engine)";
    public final static String name_ = "matchToQuery";
    public final static String provider_ = "Mayo Clinic";
    public final static String role_ = "author";
    public final static Source providerSource = new Source();
    public final static String version_ = "1.0";

    public MatchToQuerySort() throws LBParameterException, LBException {
        super();
    }
    
    public SortDescription buildSortDescription() {
        SortDescription sd = new SortDescription();
        sd.setExtensionBaseClass(Sort.class.getName());
        sd.setExtensionClass(MatchToQuerySort.class.getName());
        sd.setDescription(description_);
        sd.setName(name_);
        sd.setVersion(version_);
        providerSource.setContent(provider_);
        providerSource.setRole(role_);
        sd.setExtensionProvider(providerSource);
        sd.setRestrictToContext(new SortContext[] { 
                SortContext.SET, 
                SortContext.SETITERATION,
                SortContext.SETLISTPRERESOLVE });

       return sd;
    }

    @Override
    public void registerComparators(Map<Class, Comparator> classToComparatorsMap) {
        classToComparatorsMap.put(CodeToReturn.class, new MatchToQuerySort.CodeToReturnScoreSort()); 
    }
    
    @LgClientSideSafe
    private class CodeToReturnScoreSort implements Comparator<CodeToReturn>{
     
        @LgClientSideSafe
        public int compare(CodeToReturn o1, CodeToReturn o2) {
            return -(Float.compare(o1.getScore(),o2.getScore()));
        }
    }
}