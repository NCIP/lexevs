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
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;

public class MappingExtensionImpl extends AbstractExtendable implements MappingExtension {

    private static final long serialVersionUID = 6439060328876806104L;
    
    protected static int PAGE_SIZE = 1000;
    
    public MappingExtensionImpl() {
        super();
    }
    
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
            List<MappingSortOption> sortOptionList) throws LBParameterException {
        AbsoluteCodingSchemeVersionReference ref = 
            ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingScheme, codingSchemeVersionOrTag, true);
        
        String uri = ref.getCodingSchemeURN();
        String version = ref.getCodingSchemeVersion();
        
        Iterator<ResolvedConceptReference> iterator = 
            new MappingTripleIterator(
                    uri,
                    version,
                    relationsContainerName, 
                    sortOptionList);
        
        int count = LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
                getCodedNodeGraphService().
                    getMappingTriplesCount(
                            uri, 
                            version, 
                            relationsContainerName);

        return 
            new IteratorBackedResolvedConceptReferencesIterator(iterator, count);
    }

    @Override
    public boolean isMappingCodingScheme(String codingScheme,
            CodingSchemeVersionOrTag codingSchemeVersionOrTag) throws LBParameterException {
        AbsoluteCodingSchemeVersionReference ref = 
            ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingScheme, codingSchemeVersionOrTag, true);

        final String uri = ref.getCodingSchemeURN();
        final String version = ref.getCodingSchemeVersion();

        boolean isMappingCodingScheme = 
            LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
            getDaoCallbackService().
            executeInDaoLayer(new DaoCallback<Boolean>() {

                @Override
                public Boolean execute(DaoManager daoManager) {
                    String codingSchemeUid = daoManager.getCodingSchemeDao(uri, version).getCodingSchemeUIdByUriAndVersion(uri, version);

                    AssociationDao associationDao = daoManager.getAssociationDao(uri, version);

                    List<String> relationContainerNames = 
                        associationDao.getRelationsNamesForCodingSchemeUId(codingSchemeUid);

                    for(String relationsContainerName : relationContainerNames) {
                        String relationsUid = daoManager.getAssociationDao(uri, version).getRelationUId(codingSchemeUid, relationsContainerName);

                        Relations relation = daoManager.getAssociationDao(uri, version).getRelationsByUId(
                                codingSchemeUid, 
                                relationsUid, 
                                false);

                        if(relation.getIsMapping() == null || relation.getIsMapping() == false) {
                            return false;
                        }
                    }
                    return true;
                }});
        
        return isMappingCodingScheme;
    }
}
