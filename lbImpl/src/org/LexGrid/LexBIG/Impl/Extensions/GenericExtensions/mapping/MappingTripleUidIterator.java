
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.AbstractMappingTripleIterator.MappingAbsoluteCodingSchemeVersionReferences;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractPageableIterator;

/**
 * The Class MappingTripleUidIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MappingTripleUidIterator extends AbstractPageableIterator<String> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5709428653655124881L;

    /** The uri. */
    private String uri;
    
    /** The version. */
    private String version;
    
    /** The relations container name. */
    private String relationsContainerName;
    
    /** The sort option list. */
    private List<MappingSortOption> sortOptionList;
    
    /** The refs. */
    private MappingAbsoluteCodingSchemeVersionReferences refs;
    
    /**
     * Instantiates a new mapping triple uid iterator.
     */
    public MappingTripleUidIterator() {
        super();
    }
    
    /**
     * Instantiates a new mapping triple uid iterator.
     * 
     * @param uri the uri
     * @param version the version
     * @param relationsContainerName the relations container name
     * @param refs the refs
     * @param sortOptionList the sort option list
     * 
     * @throws LBParameterException the LB parameter exception
     */
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
    
    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractPageableIterator#doPage(int, int)
     */
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