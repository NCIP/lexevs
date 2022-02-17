
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;

/**
 * The Class MappingTripleIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MappingTripleIterator extends AbstractMappingTripleIterator<Iterator<String>> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5709428653655124881L;

    private List<MappingSortOption> sortOptionList;
            
    /**
     * Instantiates a new mapping triple iterator.
     * 
     * @param uri the uri
     * @param version the version
     * @param relationsContainerName the relations container name
     * @param sortOptionList the sort option list
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public MappingTripleIterator(
            String uri, 
            String version,
            String relationsContainerName,
            List<MappingSortOption> sortOptionList) throws LBParameterException {
        super(uri,version, relationsContainerName);
        this.sortOptionList = sortOptionList;
        this.initializetMappingTripleIterator();
    }

    @Override
    protected Iterator<String> createTripleIterator() throws Exception {
        return new MappingTripleUidIterator(
                getUri(), 
                getVersion(), 
                getRelationsContainerName(),
                getRefs(), 
                sortOptionList);
    }
    
    

}