
package org.LexGrid.LexBIG.Impl.loaders.postprocessor;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.custom.concepts.EntityFactory;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.relations.AssociationEntity;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;

/**
 * The Class HierarchyCheckingPostProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HierarchyCheckingPostProcessor extends AbstractExtendable implements LoaderPostProcessor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2828520523031693573L;

    /** The EXTENSION Name. */
    public static String EXTENSION_NAME = "HierarchyCheckingPostProcessor";

    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerGenericExtension(description);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("HierarchyCheckingPostProcessor");
        ed.setName(EXTENSION_NAME);
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(this.getClass().getName());

        return ed;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor#runPostProcess(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, org.LexGrid.LexBIG.Extensions.Load.OntologyFormat)
     */
    public void runPostProcess(AbsoluteCodingSchemeVersionReference reference, OntologyFormat ontFormat) {
        try {
            this.checkTransitivity(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
        } catch (LBException e) {
            LoggerFactory.getLogger().warn("Error applying " + EXTENSION_NAME + ". Skipping.", e);
        }
    }

    /**
     * Check the AssociationEntity defined by the SupportedHierarchy(s).
     * 
     * @param uri the uri
     * @param version the version
     * 
     * @throws LBException the LB exception
     */
    public void checkTransitivity(String uri, String version) throws LBException {
        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();

        CodingSchemeService codingSchemeService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
        EntityService entityService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService();

        if(ServiceUtility.isSupplement(uri, version)){
            LoggerFactory.getLogger().info("Skipping Coding Scheme Supplements.");
            return;
        }

        MappingExtension me = (MappingExtension) lbs.getGenericExtension("MappingExtension");
        if(me.isMappingCodingScheme(uri, Constructors.createCodingSchemeVersionOrTagFromVersion(version))){
            LoggerFactory.getLogger().info("Skipping Mapping Schemes.");
            return;
        }

        CodingScheme codingScheme = codingSchemeService.getCodingSchemeByUriAndVersion(uri, version);

        for(SupportedHierarchy sh : codingScheme.getMappings().getSupportedHierarchy()){
            String[] associationNames = sh.getAssociationNames();

            for(String associationName : associationNames){

                LoggerFactory.getLogger().info("Checking if Transitive AssociationEntity exists: " + associationName);

                String entityCode = associationName;
                String entityNamespace = codingScheme.getCodingSchemeName();

                for(SupportedAssociation sa : codingScheme.getMappings().getSupportedAssociation()){
                    if(sa.getLocalId().equals(associationName)){
                        if(StringUtils.isNotBlank(sa.getEntityCode())){
                            entityCode = sa.getEntityCode();
                        }
                        if(StringUtils.isNotBlank(sa.getEntityCodeNamespace())){
                            entityNamespace = sa.getEntityCodeNamespace();
                        }

                        if(StringUtils.isNotBlank(sa.getCodingScheme())){
                            if(! sa.getCodingScheme().equals(codingScheme.getCodingSchemeName())){
                                LoggerFactory.getLogger().info("WARNING: AssociationEntity defined elsewhere... will locally if necessary.");
                            }
                        }
                    }
                }
                
                AssociationEntity foundAssociationEntity = 
                    entityService.getAssociationEntity(uri, version, entityCode, entityNamespace);
                
                if(foundAssociationEntity == null){
                    LoggerFactory.getLogger().info(" * Auto Adding AssociationEntity Code: " + entityCode + " Namespace: " + entityNamespace);

                    AssociationEntity ae = EntityFactory.createAssociation();
                    ae.setEntityCode(entityCode);
                    ae.setEntityCodeNamespace(entityNamespace);
                    ae.setIsActive(true);
                    ae.setIsAnonymous(true);
                    ae.setIsNavigable(true);
                    ae.setIsTransitive(true);
    
                    entityService.insertEntity(uri, version, ae);
                } else {
                    if(! BooleanUtils.toBoolean(foundAssociationEntity.getIsTransitive())){
                        LoggerFactory.getLogger().info(" * AssociationEntity found, but is not set as Trasitive. Setting now...");
                        
                        foundAssociationEntity.setIsTransitive(true);
                        
                        entityService.updateEntity(uri, version, foundAssociationEntity);
                    }
                }
            }
        }
    }
}