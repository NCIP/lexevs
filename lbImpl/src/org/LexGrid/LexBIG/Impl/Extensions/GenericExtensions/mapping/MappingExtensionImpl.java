package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.helpers.IteratorBackedResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public class MappingExtensionImpl extends AbstractExtendable implements MappingExtension {

    private static final long serialVersionUID = 6439060328876806104L;

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("Mapping Extension for LexEVS.");
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(MappingExtensionImpl.class.getName());
        ed.setName("MappingExtension");
        ed.setVersion("1.0");
        
        return ed;
    }
    
    public void register() throws LBParameterException, LBException {
        ExtensionRegistryImpl.instance().registerGenericExtension(
                super.getExtensionDescription());
    }

    @Override
    public ResolvedConceptReferencesIterator resolveMapping(
            String codingScheme,
            CodingSchemeVersionOrTag codingSchemeVersionOrTag, 
            String relationsContainerName,
            List<MappingSortOption> sortOptionList,
            QualifierSortOption qualifierSortOption) throws LBParameterException {
        AbsoluteCodingSchemeVersionReference ref = 
            ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingScheme, codingSchemeVersionOrTag);
        
        String uri = ref.getCodingSchemeURN();
        String version = ref.getCodingSchemeVersion();
        
        Iterator<ResolvedConceptReference> iterator = 
            new MappingTripleIterator(
                    uri,
                    version,
                    relationsContainerName, 
                    sortOptionList, 
                    qualifierSortOption);
        
        return 
            new IteratorBackedResolvedConceptReferencesIterator(iterator);
    }
}
