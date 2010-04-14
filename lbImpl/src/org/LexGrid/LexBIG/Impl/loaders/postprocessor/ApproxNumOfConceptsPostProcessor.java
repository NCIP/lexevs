package org.LexGrid.LexBIG.Impl.loaders.postprocessor;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.locator.LexEvsServiceLocator;

public class ApproxNumOfConceptsPostProcessor extends AbstractExtendable implements LoaderPostProcessor {

    private static final long serialVersionUID = 2828520523031693573L;
    
    public static String EXTENSION_NAME = "ApproxNumOfConceptsPostProcessor";

    public void register() throws LBParameterException, LBException {
        ExtensionRegistryImpl.instance().registerGenericExtension(
                super.getExtensionDescription());
    }
 
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("ApproxNumOfConceptsPostProcessor");
        ed.setName(EXTENSION_NAME);
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(this.getClass().getName());
        
        return ed;
    }

    public void runPostProcess(AbsoluteCodingSchemeVersionReference reference) {
        EntityService entityService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService();
        CodingSchemeService codingSchemeService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
        
        String uri = reference.getCodingSchemeURN();
        String version = reference.getCodingSchemeVersion();
        
        long entities = entityService.getEntityCount(uri, version);
        
        CodingScheme codingScheme = codingSchemeService.getCodingSchemeByUriAndVersion(uri, version);
        
        codingScheme.setApproxNumConcepts(entities);
        
        try {
            codingSchemeService.updateCodingScheme(codingScheme);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
    }
}
