package org.LexGrid.LexBIG.Impl.loaders.postprocessor;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedEntityType;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedProperty;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.DatabaseService.DaoCallback;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.locator.LexEvsServiceLocator;

public class SupportedAttributePostProcessor extends AbstractExtendable implements LoaderPostProcessor {

    private static final long serialVersionUID = 2828520523031693573L;
    
    public static String EXTENSION_NAME = "SupportedAttributePostProcessor";

    public void register() throws LBParameterException, LBException {
        ExtensionRegistryImpl.instance().registerGenericExtension(
                super.getExtensionDescription());
    }
 
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("SupportedAttributePostProcessor");
        ed.setName(EXTENSION_NAME);
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(this.getClass().getName());
        
        return ed;
    }

    public void runPostProcess(AbsoluteCodingSchemeVersionReference reference) {
        CodingSchemeService codingSchemeService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
        
        final String uri = reference.getCodingSchemeURN();
        final String version = reference.getCodingSchemeVersion();
        
        CodingScheme codingScheme = 
            codingSchemeService.
                getCodingSchemeByUriAndVersion(uri, version);
        
        SupportedCodingScheme scs = new SupportedCodingScheme();
        scs.setLocalId(codingScheme.getCodingSchemeName());
        scs.setUri(codingScheme.getCodingSchemeURI());
        
        codingSchemeService.insertURIMap(uri, version, scs);
        
        this.addSupportedFormats(uri, version, codingSchemeService);
        this.addSupportedLanguages(uri, version, codingSchemeService);
        this.addSupportedEntityTypes(uri, version, codingSchemeService);
        this.addSupportedProperties(uri, version, codingSchemeService); 
    }
    
    protected void addSupportedFormats(final String uri, final String version, CodingSchemeService codingSchemeService) {
        List<String> items = codingSchemeService.executeInDaoLayer(new DaoCallback<List<String>>(){

            public List<String> execute(DaoManager daoManager) {
               CodingSchemeDao csDao = daoManager.getCodingSchemeDao(uri, version);
               String csId = csDao.getCodingSchemeIdByUriAndVersion(uri, version);
               
               return daoManager.getCodingSchemeDao(uri, version).getDistinctFormatsOfCodingScheme(csId);
            }  
        });
        
        for(String item : items) {
            SupportedDataType supportedFormat = new SupportedDataType();
            supportedFormat.setContent(item);
            supportedFormat.setLocalId(item);
            codingSchemeService.insertURIMap(uri, version, supportedFormat);
        }
    }
    
    protected void addSupportedLanguages(final String uri, final String version, CodingSchemeService codingSchemeService) {
        List<String> items = codingSchemeService.executeInDaoLayer(new DaoCallback<List<String>>(){

            public List<String> execute(DaoManager daoManager) {
               CodingSchemeDao csDao = daoManager.getCodingSchemeDao(uri, version);
               String csId = csDao.getCodingSchemeIdByUriAndVersion(uri, version);
               
               return daoManager.getCodingSchemeDao(uri, version).getDistinctLanguagesOfCodingScheme(csId);
            }  
        });
        
        for(String item : items) {
            SupportedLanguage supportedLanguage = new SupportedLanguage();
            supportedLanguage.setContent(item);
            supportedLanguage.setLocalId(item);
            codingSchemeService.insertURIMap(uri, version, supportedLanguage);
        }
    }
    
    protected void addSupportedEntityTypes(final String uri, final String version, CodingSchemeService codingSchemeService) {
        List<String> items = codingSchemeService.executeInDaoLayer(new DaoCallback<List<String>>(){

            public List<String> execute(DaoManager daoManager) {
               CodingSchemeDao csDao = daoManager.getCodingSchemeDao(uri, version);
               String csId = csDao.getCodingSchemeIdByUriAndVersion(uri, version);
               
               return daoManager.getCodingSchemeDao(uri, version).getDistinctEntityTypesOfCodingScheme(csId);
            }  
        });
        
        for(String item : items) {
            SupportedEntityType supportedEntityType = new SupportedEntityType();
            supportedEntityType.setContent(item);
            supportedEntityType.setLocalId(item);
            codingSchemeService.insertURIMap(uri, version, supportedEntityType);
        }
    }
    
    protected void addSupportedProperties(final String uri, final String version, CodingSchemeService codingSchemeService) {
        List<String> items = codingSchemeService.executeInDaoLayer(new DaoCallback<List<String>>(){

            public List<String> execute(DaoManager daoManager) {
               CodingSchemeDao csDao = daoManager.getCodingSchemeDao(uri, version);
               String csId = csDao.getCodingSchemeIdByUriAndVersion(uri, version);
               
               return daoManager.getCodingSchemeDao(uri, version).getDistinctPropertyNamesOfCodingScheme(csId);
            }  
        });
        
        for(String item : items) {
            SupportedProperty uriMap = new SupportedProperty();
            uriMap.setContent(item);
            uriMap.setLocalId(item);
            codingSchemeService.insertURIMap(uri, version, uriMap);
        }
    }
}
