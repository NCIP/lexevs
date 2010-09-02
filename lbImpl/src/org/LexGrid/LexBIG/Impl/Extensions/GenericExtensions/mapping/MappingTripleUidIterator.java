package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.MappingTripleIterator.MappingAbsoluteCodingSchemeVersionReferences;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractPageableIterator;

public class MappingTripleUidIterator extends AbstractPageableIterator<String> {

    private static final long serialVersionUID = 5709428653655124881L;

    private String uri;
    private String version;
    private String relationsContainerName;
    private List<MappingSortOption> sortOptionList;
    private MappingAbsoluteCodingSchemeVersionReferences refs;
    
    public MappingTripleUidIterator(
            String uri, 
            String version,
            String relationsContainerName, 
            MappingAbsoluteCodingSchemeVersionReferences refs,
            List<MappingSortOption> sortOptionList) throws LBParameterException {
        super(MappingExtensionImpl.PAGE_SIZE);
        
        this.uri = uri;
        this.version = version;
        this.relationsContainerName = relationsContainerName;
        this.refs = refs;
        this.sortOptionList = sortOptionList;
    }
    
    @Override
    protected List<? extends String> doPage(int currentPosition, int pageSize) {
   
        
        return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService().
            getTripleUidsForMappingRelationsContainer(
                    uri, 
                    version, 
                    refs.getSourceCodingScheme(),
                    refs.getTargetCodingScheme(),
                    relationsContainerName, 
                    DaoUtility.mapMappingSortOptionListToSort(sortOptionList).getSorts(),
                    currentPosition, 
                    pageSize);
    } 
}
